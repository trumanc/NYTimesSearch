package com.example.trumancranor.nytimessearch;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class SearchActivity extends AppCompatActivity
        implements QueryParamsFragment.OnSaveSettingsListener {

    //@BindView(R.id.etQuery) EditText etQuery;
    //@BindView(R.id.btnSearch) Button btnSearch;
    @BindView(R.id.gvResults) GridView gvResults;
    @BindView(R.id.startDate) TextView tvStartDate;
    @BindView(R.id.endDate) TextView tvEndDate;

    private ArrayList<Article> articles;
    private ArticleArrayAdapter adapter;

    private Calendar startDate;
    private Calendar endDate;

    private QueryParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        params = new QueryParams();
        articles = new ArrayList<Article>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SearchActivity.this, ArticleActivity.class);
                Article article = articles.get(position);
                i.putExtra("article", Parcels.wrap(article));
                startActivity(i);
            }
        });

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDate == null) {
                    startDate = Calendar.getInstance();
                }
                showDatePickerPickerDialog(tvStartDate, startDate);
            }
        });

        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endDate == null) {
                    endDate = Calendar.getInstance();
                }
                showDatePickerPickerDialog(tvEndDate, endDate);
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onArticleSearch(searchView.getQuery().toString());

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showFilterPreferencesFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(String query) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key", BuildConfig.NYTIMES_API_KEY);
        params.put("page", 0);
        params.put("q", query);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key), "");
        if (!sortOrder.isEmpty()) {
            Log.d("DEBUG", "Sort Order: " + sortOrder);
            params.put("sort", sortOrder);
        }

        Set<String> newsDesks = prefs.getStringSet(getString(R.string.pref_news_desks_key), new TreeSet<String>());
        if (!newsDesks.isEmpty()) {
            Iterator<String> iter = newsDesks.iterator();
            StringBuilder desksParam = new StringBuilder();
            desksParam.append("news_desk:\"" + iter.next() + "\"");

            while (iter.hasNext()) {
                desksParam.append(" OR news_desk:\"" + iter.next() + "\"");
            }

            Log.d("DEBUG", "fq string: " + desksParam.toString());
            params.put("fq", desksParam.toString());
        }

        if (startDate != null) {
            params.add("begin_date", getApiString(startDate));
        }

        if (endDate != null) {
            params.add("end_date", getApiString(endDate));
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());

                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("ERROR", "Failed to parse the json results", e);
                    Toast.makeText(SearchActivity.this, "Failed to parse Json results. Check the log.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                onFailure(statusCode, headers, errorResponse.toString(), throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                onFailure(statusCode, headers, errorResponse.toString(), throwable);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("API_REQ", "Failed api request. Response: " + responseString, throwable);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

        });
    }

    private void showDatePickerPickerDialog(final TextView textView, final Calendar date) {
        DatePickerDialog dialog = new DatePickerDialog(this);

        dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.set(year, month, dayOfMonth);
                textView.setText(humanReadableString(date));
            }
        });
        dialog.show();
    }

    private void showFilterPreferencesFragment() {
        Intent i = new Intent(this, FilterPreferencesActivity.class);
        startActivity(i);
        /*FragmentManager fm = getSupportFragmentManager();
        FilterPreferencesFragment f = new FilterPreferencesFragment();
        f.show(fm, "filter_prefs_frag"); */
    }

    private static String humanReadableString(Calendar date) {
        SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");
        return format.format(date.getTime());
    }

    private static String getApiString(Calendar date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date.getTime());
    }
}

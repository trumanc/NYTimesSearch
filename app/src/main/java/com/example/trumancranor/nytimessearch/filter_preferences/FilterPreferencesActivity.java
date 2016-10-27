package com.example.trumancranor.nytimessearch.filter_preferences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FilterPreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new FilterPreferencesFragment())
                .commit();
    }
}

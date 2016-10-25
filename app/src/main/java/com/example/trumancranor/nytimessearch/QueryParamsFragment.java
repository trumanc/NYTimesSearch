package com.example.trumancranor.nytimessearch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QueryParamsFragment.OnSaveSettingsListener} interface
 * to handle interaction events.
 * Use the {@link QueryParamsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QueryParamsFragment extends DialogFragment {

    @BindView(R.id.dpBeginDate)
    DatePicker beginDate;

    private Unbinder unbinder;
    private static final String ARG_PARAMS = "query_parameters";

    private QueryParams queryParams;
    public interface OnSaveSettingsListener {
        // TODO: Update argument type and name
        //void onSaveSettings();
    }

    private OnSaveSettingsListener mListener;

    public QueryParamsFragment() {
        // Required empty public constructor
    }

    public static QueryParamsFragment newInstance(QueryParams params) {
        QueryParamsFragment fragment = new QueryParamsFragment();
        Bundle args = new Bundle();
        if (params == null) {
            throw new RuntimeException("Null QueryParams passed in to QueryParamsFragment");
        }
        args.putSerializable(ARG_PARAMS, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG", "in onCreate");
        if (getArguments() != null) {
            queryParams = (QueryParams) getArguments().getSerializable(ARG_PARAMS);
        } else {
            // Fail fast for quicker debugging
            throw new RuntimeException("QueryParamsFragments did not receive a QueryParams argument");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DEBUG", "in onCreateView");
        View view = inflater.inflate(R.layout.fragment_query_params, container);
        unbinder = ButterKnife.bind(this, view);

        // Do a thing with the params
        beginDate.init(1993, 4, 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Toast.makeText(getContext(), "Set a new date!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSaveSettingsListener) {
            mListener = (OnSaveSettingsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement " + OnSaveSettingsListener.class.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}

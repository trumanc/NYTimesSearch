package com.example.trumancranor.nytimessearch;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * Created by truman_cranor on 10/25/16.
 */

public class DatePickerPreference extends DialogPreference {
    public DatePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.fragment_query_params);
    }
}

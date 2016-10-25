package com.example.trumancranor.nytimessearch;

import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QueryParams implements Serializable {
    @Nullable
    private Calendar beginDate;

    @Nullable
    private Calendar endDate;

    private Set<NewsDesk> newsDesks;

    @Nullable
    private SortOrder sortOrder;

    public QueryParams() {
        // Initialize this as an empty set
        newsDesks = EnumSet.noneOf(NewsDesk.class);

        // Everything else is null
    }

    public enum SortOrder {
        MOST_RELEVANT(""),
        NEWEST("newest"),
        OLDEST("oldest");

        private final String value;

        private SortOrder(String s) {
            value = s;
        }

        public String toString() {
            return value;
        }
    }

    public enum NewsDesk {
        ARTS_LEISURE("Arts&Leisure"),
        BUSINESS("Business"),
        CULTURE("Culture"),
        NATIONAL("National"),
        OP_ED("OpEd"),
        REAL_ESTATE("RealEstate"), // Intentionally omitted space in this string
        SPORTS("Sports"),
        STYLE("Style"),
        TECHNOLOGY("Technology"),
        WEEKEND("Weekend"),
        WORLD("World");

        private String value;

        private NewsDesk(String s) {
            value = s;
        }

        public String toString() {
            return value;
        }
    }
}

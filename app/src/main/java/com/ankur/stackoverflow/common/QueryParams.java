package com.ankur.stackoverflow.common;

import java.util.HashMap;

public class QueryParams extends HashMap {

    enum QueryKeys {
        ID, TEXT
    }

    private QueryParams() {

    }

    public static QueryParams getNewInstance() {
        return new QueryParams();
    }

    public QueryParams setId(Integer id) {
        put(QueryKeys.ID, id);
        return this;
    }

    public Integer getId() {
        return (Integer) get(QueryKeys.ID);
    }

    public QueryParams setText(String id) {
        put(QueryKeys.TEXT, id);
        return this;
    }

    public String getTEXT() {
        return (String) get(QueryKeys.TEXT);
    }

}

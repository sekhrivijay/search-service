package com.micro.services.search.config;


import java.util.Arrays;
import java.util.List;

public class GlobalConstants {

    public static final String REQUEST = "requests";
    public static final String ID = "id";
    public static final String TRUE = "true";
    public static final String FORWARD_SLASH = "/";
    public static final String SELECT_HANDLER = "select";
    public static final String QT = "qt";
    public static final String ASC = "asc";
    public static final List<String> KNOWN_PARAMETERS;
    public static final String INDEX = "index";
    public static final String CACHE = "cache";
    public static final String DEFAULT = "default";
    public static final String GROUP = "group";
    public static final String GROUP_LIMIT = "group.limit";
    public static final String GROUP_FIELD = "group.field";
    public static final String FQ = "fq";
    public static final String Q = "q";
    public static final String FACET_FIELDS = "facet.fields";
    public static final String FACET_SORT = "facet.sort";
    public static final String GROUP_FIELDS = "group.fields";
    public static final String ROWS = "rows";
    public static final String COUNT = "count";
    public static final String START = "start";
    public static final String SORT = "sort";
    public static final String SORT_ORDER = "sort.order";
    public static final String DEBUG = "debug";
    public static final String FROM = "from";
    public static final String COMMA = ",";

    static {
        KNOWN_PARAMETERS = Arrays.asList(
                Q,
                FQ,
                FACET_FIELDS,
                FACET_SORT,
                GROUP_FIELDS,
                ROWS,
                COUNT,
                START,
                DEBUG
        );
    }
}

package com.ftd.services.search.config;


import java.util.Arrays;
import java.util.List;


public class GlobalConstants {

    private static String applicationName = "";
    private static String environment = "";

    public static final String REQUEST = "requests";
    public static final String ID = "id";
    public static final String PID = "pid";
    public static final String TRUE = "true";
    public static final String FORWARD_SLASH = "/";
    public static final String SELECT_HANDLER = "select";
    public static final String SUGGEST_HANDLER = "suggest";
    public static final String DOLLAR = "$";
    public static final String FTD = "ftd";
    public static final String PROFLOWERS = "proflowers";
    public static final String QT = "qt";
    public static final String BF = "bf";
    public static final String ASC = "asc";
    public static final String INDEX = "index";
    public static final String DESKTOP = "desktop";
    public static final String MOBILE = "mobile";
    public static final String CACHE = "cache";
    public static final String DEFAULT = "default";
    public static final String GROUP = "group";
    public static final String GROUP_LIMIT = "group.limit";
    public static final String GROUP_FIELD = "group.field";
    public static final String FQ = "fq";
    public static final String AMPERSAND = "&";
    public static final String Q = "q";
    public static final String UTF_8 = "utf-8";
    public static final String EQUAL = "=";
    public static final String UNDERSCORE = "_";
    public static final String RIGHT_BRACKET = "[";
    public static final String LEFT_BRACKET = "]";
    public static final String TO = "TO";
    public static final String TILDE = "~";
    public static final String SPACE = " ";
    public static final String STAR = "*";
    public static final String FACET_FIELDS = "facet.fields";
    public static final String RANGE_FACET_FIELDS = "range.facet.fields";
    public static final String FACET_SORT = "facet.sort";
    public static final String GROUP_FIELDS = "group.fields";
    public static final String ROWS = "rows";
    public static final String COUNT = "count";
    public static final String START = "start";
    public static final String SORT = "sort";
    public static final String TYPE = "type";
    public static final String SITE_ID = "siteId";
    public static final String MEMBER_TYPE = "memberType";
    public static final String ZIPCODE = "zipCode";
    public static final String AVAIL_FROM = "availFrom";
    public static final String AVAIL_TO = "availTo";
    public static final String DOMAIN = "domain";
    public static final String SORT_ORDER = "sort.order";
    public static final String DEBUG = "debug";
    public static final String FROM = "from";
    public static final String COMMA = ",";
    public static final String COLON = ":";
    public static final String PERCENT = "%";
    public static final String MM = "mm";
    public static final String QUESTION_MARK = "?";
    public static final String DISTRIB = "distrib";
    public static final String LONG = "long";
    public static final String APPLICATION_JSON = "application/json";
    public static final String KEYWORD = "keyword";
    public static final String SEARCH_KEYWORDS = "searchKeywords";
    public static final String AUTOFILL_KEYWORDS = "autofillKeywords";
    public static final String AUTOFILL_KEYWORD = "autofillKeyword";
    public static final String AUTOFILL = "autofill";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String TITLE = "title";
    public static final String KEYWORDS = "keywords";
    public static final String CATEGORIES = "categories";
    public static final String IS_ACTIVE = "isActive";
    public static final String IMAGE = "image";
    public static final String SKU_ID = "skuId";
    public static final String CLIENT_ID = "clientId";
    public static final String SMALL = "small";
    public static final String SMALL_IMAGE = "smallImage";
    public static final String PRIMARY_IMAGE = "primaryImage";
    public static final String REGULAR_PRICE = "regularPrice";
    public static final String FINAL_PRICE = "finalPrice";
    public static final String PRIMARY = "primary";
    public static final String VERSION = "version";
    public static final String TYPE_COLON = GlobalConstants.TYPE + GlobalConstants.COLON;
    public static final String LOG_IDENTIFICATION = "url: {}, version: {}, enabled: {}";

    public static final String RELAXED_MUST_MATCH_FIFTY_PERCENT = "50" + PERCENT;
    public static final String RELAXED_MUST_MATCH_SEVENTY_FIVE_PERCENT = "75" + PERCENT;
    public static final String MUST_MATCH_HUNDRED_PERCENT = "100" + PERCENT;
    public static final int SPELL_CORRECT_LANGUAGE_TOOL_ROUND = 4;
    public static final int MUST_MATCH_ROUND_1 = 2;
    public static final int MUST_MATCH_ROUND_2 = 3;
    public static final int SPELL_CORRECT_SOLR_ROUND = 1;
    public static final String SITE_ID_PARAM =
            GlobalConstants.QUESTION_MARK +
                    GlobalConstants.SITE_ID +
                    GlobalConstants.EQUAL;
    public static final String MEMBER_TYPE_PARAM = GlobalConstants.QUESTION_MARK
            + AMPERSAND + MEMBER_TYPE + EQUAL;

    public static final String FQ_PREFIX = AMPERSAND +
            FQ +
            EQUAL;
    public static final String TYPE_PREFIX = AMPERSAND +
            TYPE +
            EQUAL;
    public static final String Q_PREFIX = AMPERSAND +
            Q +
            EQUAL;
    public static final String ID_FIELD_FILTER = GlobalConstants.FQ_PREFIX +
            ID +
            COLON;
    public static final String Q_STAR_FIELD = Q_PREFIX + STAR + COLON + STAR;
    public static final List<String> KNOWN_PARAMETERS;



    static {
        KNOWN_PARAMETERS = Arrays.asList(
                Q,
                FQ,
                BF,
                FACET_FIELDS,
                RANGE_FACET_FIELDS,
                FACET_SORT,
                GROUP_FIELDS,
                ROWS,
                COUNT,
                START,
                DEBUG
        );
    }

    public static void setApplicationName(String applicationName) {
        GlobalConstants.applicationName = applicationName;
    }

    public static void setEnvironment(String environment) {
        GlobalConstants.environment = environment;
    }

    public static String getApplicationName() {
        return applicationName;
    }

    public static String getEnvironment() {
        return environment;
    }
}

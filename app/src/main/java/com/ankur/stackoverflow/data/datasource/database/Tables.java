package com.ankur.stackoverflow.data.datasource.database;

import java.util.ArrayList;
import java.util.List;

public abstract class Tables {

    public static final String COMMA   = ", ";
    public static final String NULL    = " NULL ";
    public static final String INTEGER = " INTEGER ";
    public static final String REAL    = " REAL ";
    /**
     * use it to save text, varchar, char
     */
    public static final String TEXT    = " TEXT ";
    /**
     * use it to save fotos, videos, audio, data etc.
     */
    public static final String BLOB    = " BLOB ";

    /**
     * Media Table containing all the (first level) items.
     */
    public static class QuestionTable {
        static final String TABLE_NAME                = "QuestionItem";
        static final String COLUMN_OWNER_ID           = "owner_id";
        static final String COLUMN_OWNER_NAME         = "owner_name";
        static final String COLUMN_IS_ANSWERED        = "is_answered";
        static final String COLUMN_VIEW_COUNT         = "view_count";
        static final String COLUMN_ANSWER_COUNT       = "answer_count";
        static final String COLUMN_SCORE              = "score";
        static final String COLUMN_LAST_ACTIVITY_DATE = "last_activity_date";
        static final String COLUMN_CREATION_DATE      = "creation_date";
        static final String COLUMN_QUESTION_ID        = "question_id";
        static final String COLUMN_LINK               = "link";
        static final String COLUMN_TITLE              = "title";

        public static String[] getFullProjection() {
            return new String[] { COLUMN_OWNER_ID, COLUMN_OWNER_NAME, COLUMN_IS_ANSWERED, COLUMN_VIEW_COUNT, COLUMN_ANSWER_COUNT,
                    COLUMN_SCORE, COLUMN_LAST_ACTIVITY_DATE, COLUMN_CREATION_DATE, COLUMN_QUESTION_ID, COLUMN_LINK };
        }

        public static String getCreateQuery() {
            StringBuilder query = new StringBuilder("CREATE TABLE " + TABLE_NAME);
            query.append(" (");
            query.append(COLUMN_QUESTION_ID + " text, ");
            query.append(COLUMN_TITLE + " text, ");
            query.append(COLUMN_OWNER_ID + " integer DEFAULT 0, ");
            query.append(COLUMN_OWNER_NAME + " text, ");
            query.append(COLUMN_IS_ANSWERED + " text, ");
            query.append(COLUMN_VIEW_COUNT + " integer DEFAULT 0, ");
            query.append(COLUMN_ANSWER_COUNT + " integer DEFAULT 0, ");
            query.append(COLUMN_SCORE + " integer DEFAULT 0, ");
            query.append(COLUMN_LAST_ACTIVITY_DATE + " integer DEFAULT 0, ");
            query.append(COLUMN_CREATION_DATE + " integer DEFAULT 0, ");
            query.append(COLUMN_LINK + " text, ");
            query.append("primary key (");
            query.append(COLUMN_QUESTION_ID);
            query.append(")");
            query.append(")");
            return query.toString();
        }

        public static List<String> getCreateIndexQueries() {
            List<String> queries = new ArrayList<>();
            queries.add("CREATE INDEX IF NOT EXISTS ITEM_ID ON " + TABLE_NAME + " (" + COLUMN_QUESTION_ID + ")");
            return queries;
        }

        public static String getDropQuery() {
            return null;
        }
    }

    public static class CollectionTable {
        static final String TABLE_NAME           = "Collections";
        static final String COLUMN_ID            = "id";
        static final String COLUMN_COLLECTION_ID = "collection_id";
        static final String COLUMN_MAPPED_ID     = "mapped_id";
        static final String COLUMN_RANK          = "rank";

        public static String getCreateQuery() {
            StringBuilder query = new StringBuilder("CREATE TABLE " + CollectionTable.TABLE_NAME);
            query.append(" (");
            query.append(COLUMN_ID + " INTEGER NOT NULL,");
            query.append(COLUMN_COLLECTION_ID + " text, ");
            query.append(COLUMN_MAPPED_ID + " text, ");
            query.append(COLUMN_RANK + " INTEGER, ");
            query.append("PRIMARY KEY (");
            query.append(COLUMN_COLLECTION_ID + ", ");
            query.append(COLUMN_MAPPED_ID);
            query.append(")");
            query.append(")");
            return query.toString();
        }

        public static String getDropQuery() {
            return null;
        }
    }

}

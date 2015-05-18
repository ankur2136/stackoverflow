package com.ankur.stackoverflow.data.datasource.database;

import java.util.ArrayList;
import java.util.List;

public abstract class Tables {

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

        public static String getCreateQuery() {
            return "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_QUESTION_ID + " text, " + COLUMN_TITLE + " text, " + COLUMN_OWNER_ID + " integer DEFAULT 0, " + COLUMN_OWNER_NAME + " text, " + COLUMN_IS_ANSWERED + " text, " + COLUMN_VIEW_COUNT + " integer DEFAULT 0, " + COLUMN_ANSWER_COUNT + " integer DEFAULT 0, " + COLUMN_SCORE + " integer DEFAULT 0, " + COLUMN_LAST_ACTIVITY_DATE + " integer DEFAULT 0, " + COLUMN_CREATION_DATE + " integer DEFAULT 0, " + COLUMN_LINK + " text, " + "primary key (" + COLUMN_QUESTION_ID + ")" + ")";
        }

        public static List<String> getCreateIndexQueries() {
            List<String> queries = new ArrayList<>();
            queries.add("CREATE INDEX IF NOT EXISTS ITEM_ID ON " + TABLE_NAME + " (" + COLUMN_QUESTION_ID + ")");
            return queries;
        }
    }

    public static class AnswerTable {
        static final String TABLE_NAME                = "AnswerItems";
        static final String COLUMN_OWNER_ID           = "owner_id";
        static final String COLUMN_OWNER_NAME         = "owner_name";
        static final String COLUMN_IS_ACCEPTED        = "is_accepted";
        static final String COLUMN_UP_VOTES           = "up_votes";
        static final String COLUMN_DOWN_VOTES         = "down_votes";
        static final String COLUMN_SCORE              = "score";
        static final String COLUMN_LAST_ACTIVITY_DATE = "last_activity_date";
        static final String COLUMN_LAST_EDIT_DATE     = "last_edit_date";
        static final String COLUMN_CREATION_DATE      = "creation_date";
        static final String COLUMN_ANSWER_ID          = "answer_id";
        static final String COLUMN_QUESTION_ID        = "question_id";
        static final String COLUMN_LINK               = "link";
        static final String COLUMN_TITLE              = "title";
        static final String COLUMN_BODY               = "body";

        public static String[] getFullProjection() {
            return new String[] { COLUMN_OWNER_ID, COLUMN_OWNER_NAME, COLUMN_IS_ACCEPTED, COLUMN_UP_VOTES,
                    COLUMN_DOWN_VOTES, COLUMN_SCORE, COLUMN_LAST_ACTIVITY_DATE, COLUMN_LAST_EDIT_DATE,
                    COLUMN_CREATION_DATE, COLUMN_QUESTION_ID, COLUMN_QUESTION_ID, COLUMN_LINK, COLUMN_TITLE,
                    COLUMN_BODY };
        }

        public static String getCreateQuery() {
            return "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_QUESTION_ID + " text, " + COLUMN_ANSWER_ID + " text, " + COLUMN_TITLE + " text, " + COLUMN_IS_ACCEPTED + " integer DEFAULT 0, " + COLUMN_OWNER_ID + " integer DEFAULT 0, " + COLUMN_OWNER_NAME + " text, " + COLUMN_UP_VOTES + " integer DEFAULT 0, " + COLUMN_DOWN_VOTES + " integer DEFAULT 0, " + COLUMN_SCORE + " integer DEFAULT 0, " + COLUMN_LAST_ACTIVITY_DATE + " integer DEFAULT 0, " + COLUMN_LAST_EDIT_DATE + " integer DEFAULT 0, " + COLUMN_CREATION_DATE + " integer DEFAULT 0, " + COLUMN_LINK + " text, " + COLUMN_BODY + " text, " + "primary key (" + COLUMN_ANSWER_ID + ")" + ")";
        }
    }

    public static class CollectionTable {
        static final String TABLE_NAME           = "Collections";
        static final String COLUMN_ID            = "id";
        static final String COLUMN_COLLECTION_ID = "collection_id";
        static final String COLUMN_MAPPED_ID     = "mapped_id";
        static final String COLUMN_RANK          = "rank";

        public static String getCreateQuery() {
            return "CREATE TABLE " + CollectionTable.TABLE_NAME + " (" + COLUMN_ID + " INTEGER NOT NULL," + COLUMN_COLLECTION_ID + " text, " + COLUMN_MAPPED_ID + " text, " + COLUMN_RANK + " INTEGER, " + "PRIMARY KEY (" + COLUMN_COLLECTION_ID + ", " + COLUMN_MAPPED_ID + ")" + ")";
        }

    }

}

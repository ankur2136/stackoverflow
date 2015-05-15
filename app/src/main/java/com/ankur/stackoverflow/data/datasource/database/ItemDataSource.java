package com.ankur.stackoverflow.data.datasource.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.ankur.stackoverflow.domain.dto.QuestionItem;

public class ItemDataSource implements IItemDataSource {

    private static final String  LOG_TAG          = "ITEM_DATA_SOURCE";
    private final DatabaseHelper mDatabaseHelper;
    private final Context        mContext;
    private long                 RECENT_THRESHOLD = 1000 * 60 * 60 * 24;

    public ItemDataSource(Context context) {
        this.mContext = context.getApplicationContext();
        this.mDatabaseHelper = DatabaseHelper.getInstance(context.getApplicationContext());
    }

    public static List<String> getDropTableQueries() {
        List<String> queries = new ArrayList<>();
        queries.add("DROP TABLE IF EXISTS " + Tables.QuestionTable.TABLE_NAME);
        queries.add("DROP TABLE IF EXISTS " + Tables.CollectionTable.TABLE_NAME);
        return queries;
    }

    public static List<String> getCreateIndexQueries() {
        return Tables.QuestionTable.getCreateIndexQueries();
    }

    public static List<String> getCreateTableQueries() {
        List<String> queries = new ArrayList<>();
        queries.add(Tables.QuestionTable.getCreateQuery());
        queries.add(Tables.CollectionTable.getCreateQuery());
        return queries;
    }

    @Override
    public boolean putMediaItem(QuestionItem questionItem) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Tables.QuestionTable.COLUMN_QUESTION_ID, questionItem.mQuestionId);
        values.put(Tables.QuestionTable.COLUMN_TITLE, questionItem.mTitle);
        values.put(Tables.QuestionTable.COLUMN_OWNER_ID, questionItem.mOwnerInfo.mUserId);
        values.put(Tables.QuestionTable.COLUMN_IS_ANSWERED, questionItem.mIsAnswered);
        values.put(Tables.QuestionTable.COLUMN_VIEW_COUNT, questionItem.mViewCount);
        values.put(Tables.QuestionTable.COLUMN_ANSWER_COUNT, questionItem.mAnswerCount);
        values.put(Tables.QuestionTable.COLUMN_SCORE, questionItem.mScore);
        values.put(Tables.QuestionTable.COLUMN_LAST_ACTIVITY_DATE, questionItem.mLastActivityDate);
        values.put(Tables.QuestionTable.COLUMN_CREATION_DATE, questionItem.mCreationDate);
        values.put(Tables.QuestionTable.COLUMN_LINK, questionItem.mLink);

        long rowId = db.replace(Tables.QuestionTable.TABLE_NAME, null, values);

        return rowId >= 0;

    }

    @Override
    public List<QuestionItem> getSearchResults(String input) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        List<QuestionItem> searchResults = new ArrayList<>();

        return searchResults;
    }

    private String appendWildcard(String query) {
        if (TextUtils.isEmpty(query))
            return query;

        final StringBuilder builder = new StringBuilder();
        final String[] splits = TextUtils.split(query, " ");

        for (String split : splits)
            builder.append(split).append("*").append(" ");

        return builder.toString().trim();
    }

}

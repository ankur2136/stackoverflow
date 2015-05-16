package com.ankur.stackoverflow.data.datasource.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.ankur.stackoverflow.domain.dto.AnswerItem;
import com.ankur.stackoverflow.domain.dto.QuestionItem;
import com.ankur.stackoverflow.utils.LogUtils;

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
    public boolean putQuestionItem(QuestionItem questionItem, String query) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Tables.QuestionTable.COLUMN_QUESTION_ID, questionItem.mQuestionId);
        values.put(Tables.QuestionTable.COLUMN_TITLE, questionItem.mTitle);
        values.put(Tables.QuestionTable.COLUMN_OWNER_ID, questionItem.mOwnerInfo.mUserId);
        values.put(Tables.QuestionTable.COLUMN_OWNER_NAME, questionItem.mOwnerInfo.mDisplayName);
        values.put(Tables.QuestionTable.COLUMN_IS_ANSWERED, questionItem.mIsAnswered);
        values.put(Tables.QuestionTable.COLUMN_VIEW_COUNT, questionItem.mViewCount);
        values.put(Tables.QuestionTable.COLUMN_ANSWER_COUNT, questionItem.mAnswerCount);
        values.put(Tables.QuestionTable.COLUMN_SCORE, questionItem.mScore);
        values.put(Tables.QuestionTable.COLUMN_LAST_ACTIVITY_DATE, questionItem.mLastActivityDate);
        values.put(Tables.QuestionTable.COLUMN_CREATION_DATE, questionItem.mCreationDate);
        values.put(Tables.QuestionTable.COLUMN_LINK, questionItem.mLink);

        updateMapping(questionItem, query);

        long rowId = db.replace(Tables.QuestionTable.TABLE_NAME, null, values);

        return rowId >= 0;
    }

    private void updateMapping(QuestionItem questionItem, String query) {

        String searchKey = getSearchKey(query);

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Tables.CollectionTable.COLUMN_ID,
                getCollectionTableKey(Integer.toString(questionItem.mQuestionId), query));
        values.put(Tables.CollectionTable.COLUMN_COLLECTION_ID, getSearchKey(query));
        values.put(Tables.CollectionTable.COLUMN_MAPPED_ID, questionItem.mQuestionId);

        // TODO:: Fix rank
        values.put(Tables.CollectionTable.COLUMN_RANK, 0);

        db.beginTransaction();

        Cursor childItemMapping = getMapping(query, questionItem.mQuestionId);

        if ((childItemMapping == null) || !(childItemMapping.moveToFirst())) {
            db.insert(Tables.CollectionTable.TABLE_NAME, null, values);
        } else {
            String where = Tables.CollectionTable.COLUMN_COLLECTION_ID + "=? AND "
                    + Tables.CollectionTable.COLUMN_MAPPED_ID + "=?";
            String[] whereParams = { Integer.toString(questionItem.mQuestionId), searchKey };
            db.update(Tables.CollectionTable.TABLE_NAME, values, where, whereParams);
        }

        if (childItemMapping != null) {
            childItemMapping.close();
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private Cursor getMapping(String query, int questionID) {
        if (TextUtils.isEmpty(query)) {
            LogUtils.errorLog(LOG_TAG, "Item id is null or empty");
            return null;
        }
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        String where = Tables.CollectionTable.COLUMN_COLLECTION_ID + "=? AND "
                + Tables.CollectionTable.COLUMN_MAPPED_ID + "=?";
        String whereArgs[] = { getSearchKey(query), Integer.toString(questionID) };
        return db.query(Tables.CollectionTable.TABLE_NAME, null, where, whereArgs, null, null, null);
    }

    private String getSearchKey(String query) {
        query = query.trim().toLowerCase();
        final StringBuilder builder = new StringBuilder();
        final String[] splits = TextUtils.split(query, " ");

        builder.append("search_");
        for (String split : splits)
            builder.append(split).append("_");

        return builder.toString().trim();
    }

    private int getCollectionTableKey(String collectionId, String itemId) {
        int result = 17;
        int seed = 31;
        result = seed * result + collectionId.hashCode();
        result = seed * result + itemId.hashCode();
        return result;
    }

    @Override
    public List<QuestionItem> getSearchResults(String input) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        List<QuestionItem> searchResults = new ArrayList<>();

        return searchResults;
    }

    @Override
    public List<AnswerItem> getAnswersForQuestion(Integer questionID) {
        return null;
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

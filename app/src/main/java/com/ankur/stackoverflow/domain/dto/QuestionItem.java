package com.ankur.stackoverflow.domain.dto;

import com.ankur.stackoverflow.constants.ApiConstants;
import com.ankur.stackoverflow.domain.ParsingObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionItem implements Serializable, ParsingObject {

    private final String LOG_TAG = "CONTENT_ITEM";

    public List<String>  mTags;
    public UserInfo      mOwnerInfo;
    public boolean       mIsAnswered;
    public Integer       mViewCount;
    public Integer       mAnswerCount;
    public Integer       mScore;
    public Long          mLastActivityDate;
    public Long          mCreationDate;
    public int           mQuestionId;
    public String        mLink;
    public String        mTitle;

    @Override
    public QuestionItem fromJsonObject(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }

        JSONArray tags = obj.getJSONArray(ApiConstants.QuestionItem.TAGS);
        mTags = new ArrayList<>();
        if (tags != null) {
            for (int i = 0; i < tags.length(); i++) {
                String temp = tags.optString(i);
                mTags.add(temp);
            }
        }

        JSONObject ownerInfo = obj.optJSONObject(ApiConstants.QuestionItem.OWNER);
        mOwnerInfo = new UserInfo();
        if (ownerInfo != null) {
            mOwnerInfo = (new UserInfo()).fromJsonObject(ownerInfo);
        }

        mIsAnswered = obj.optBoolean(ApiConstants.QuestionItem.IS_ANSWERED, false);
        mViewCount = obj.optInt(ApiConstants.QuestionItem.VIEW_COUNT, 0);
        mAnswerCount = obj.optInt(ApiConstants.QuestionItem.ANSWER_COUNT, 0);
        mScore = obj.optInt(ApiConstants.QuestionItem.SCORE, 0);
        mLastActivityDate = obj.optLong(ApiConstants.QuestionItem.LAST_ACTIVITY_DATE, 0);
        mCreationDate = obj.optLong(ApiConstants.QuestionItem.CREATION_DATE, 0);
        mQuestionId = obj.getInt(ApiConstants.QuestionItem.QUESTION_ID);
        mLink = obj.optString(ApiConstants.QuestionItem.LINK, "");
        mTitle = obj.optString(ApiConstants.QuestionItem.TITLE, "");

        return this;
    }
}

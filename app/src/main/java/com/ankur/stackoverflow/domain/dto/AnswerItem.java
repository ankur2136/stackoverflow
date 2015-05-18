package com.ankur.stackoverflow.domain.dto;

import com.ankur.stackoverflow.constants.ApiConstants;
import com.ankur.stackoverflow.domain.ParsingObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AnswerItem implements Serializable, ParsingObject {

    public UserInfo      mOwnerInfo;
    public Integer       mDownVote;
    public Integer       mUpVote;
    public boolean       mIsAccepted;
    public Integer       mScore;
    public Long          mLastActivityDate;
    public Long          mLastEditDate;
    public Long          mCreationDate;
    public int           mAnswerId;
    public int           mQuestionId;
    public String        mLink;
    public String        mTitle;
    public String        mBody;

    @Override
    public AnswerItem fromJsonObject(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }

        JSONObject ownerInfo = obj.optJSONObject(ApiConstants.AnswerItem.OWNER);
        mOwnerInfo = new UserInfo();
        if (ownerInfo != null) {
            mOwnerInfo = (new UserInfo()).fromJsonObject(ownerInfo);
        }

        mDownVote = obj.optInt(ApiConstants.AnswerItem.DOWN_VOTE_COUNT, 0);
        mUpVote = obj.optInt(ApiConstants.AnswerItem.UP_VOTE_COUNT, 0);
        mIsAccepted = obj.optBoolean(ApiConstants.AnswerItem.IS_ACCEPTED, false);
        mScore = obj.optInt(ApiConstants.AnswerItem.SCORE, 0);
        mLastActivityDate = obj.optLong(ApiConstants.AnswerItem.LAST_ACTIVITY_DATE, 0);
        mLastEditDate = obj.optLong(ApiConstants.AnswerItem.LAST_EDIT_DATE, 0);
        mCreationDate = obj.optLong(ApiConstants.AnswerItem.CREATION_DATE, 0);
        mAnswerId = obj.getInt(ApiConstants.AnswerItem.ANSWER_ID);
        mQuestionId = obj.getInt(ApiConstants.AnswerItem.QUESTION_ID);
        mLink = obj.optString(ApiConstants.AnswerItem.LINK, "");
        mTitle = obj.optString(ApiConstants.AnswerItem.TITLE, "");
        mBody = obj.optString(ApiConstants.AnswerItem.BODY, "");

        return this;
    }
}

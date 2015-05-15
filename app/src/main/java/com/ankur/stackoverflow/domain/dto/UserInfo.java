package com.ankur.stackoverflow.domain.dto;

import com.ankur.stackoverflow.constants.ApiConstants;
import com.ankur.stackoverflow.domain.ParsingObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ankurjain on 5/15/15.
 */
public class UserInfo implements Serializable, ParsingObject {

    public int    mReputation;
    public int    mUserId;
    public String mUserType;
    public int    mAcceptRate;
    public String mProfilePicUrl;
    public String mDisplayName;
    public String mLink;

    @Override
    public UserInfo fromJsonObject(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }

        mReputation = obj.optInt(ApiConstants.UserInfo.REPUTATION, 0);
        mUserId = obj.optInt(ApiConstants.UserInfo.USER_ID, 0);
        mAcceptRate = obj.optInt(ApiConstants.UserInfo.ACCEPT_RATE, 0);
        mUserType = obj.optString(ApiConstants.UserInfo.USER_TYPE, "");
        mProfilePicUrl = obj.optString(ApiConstants.UserInfo.PROFILE_IMAGE, "");
        mDisplayName = obj.optString(ApiConstants.UserInfo.DISPLAY_NAME, "");
        mLink = obj.optString(ApiConstants.UserInfo.LINK, "");

        return this;
    }
}

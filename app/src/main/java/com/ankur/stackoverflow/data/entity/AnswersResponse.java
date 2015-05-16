package com.ankur.stackoverflow.data.entity;

import com.ankur.stackoverflow.constants.ApiConstants;
import com.ankur.stackoverflow.domain.ParsingObject;
import com.ankur.stackoverflow.domain.dto.AnswerItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnswersResponse implements ParsingObject, Serializable {
    private List<AnswerItem> mAnswersItemList;

    public List<AnswerItem> getResult() {
        return mAnswersItemList;
    }
    @Override
    public AnswersResponse fromJsonObject(JSONObject obj) throws JSONException {
        if (null == obj) {
            throw new JSONException("JSON is null");
        }
        this.mAnswersItemList = new ArrayList<>();
        JSONArray itemsArray = obj.optJSONArray(ApiConstants.AnswerItem.ITEMS);
        if (itemsArray == null) {
            return this;
        }

        JSONObject itemObj;
        for (int i = 0; i < itemsArray.length(); i++) {
            itemObj = itemsArray.getJSONObject(i);
            AnswerItem answerItem = new AnswerItem();
            answerItem.fromJsonObject(itemObj);
            this.mAnswersItemList.add(answerItem);
        }
        return this;
    }
}
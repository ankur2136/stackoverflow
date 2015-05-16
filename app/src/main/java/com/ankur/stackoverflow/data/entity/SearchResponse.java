package com.ankur.stackoverflow.data.entity;

import com.ankur.stackoverflow.constants.ApiConstants;
import com.ankur.stackoverflow.domain.ParsingObject;
import com.ankur.stackoverflow.domain.dto.QuestionItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchResponse implements ParsingObject, Serializable {
    private List<QuestionItem> mQuestionItemList;

    public List<QuestionItem> getSearchResults() {
        return mQuestionItemList;
    }
    @Override
    public SearchResponse fromJsonObject(JSONObject obj) throws JSONException {
        if (null == obj) {
            throw new JSONException("JSON is null");
        }
        this.mQuestionItemList = new ArrayList<>();
        JSONArray itemsArray = obj.optJSONArray(ApiConstants.Item.ITEMS);
        if (itemsArray == null) {
            return this;
        }

        JSONObject itemObj;
        for (int i = 0; i < itemsArray.length(); i++) {
            itemObj = itemsArray.getJSONObject(i);
            QuestionItem questionItem = new QuestionItem();
            questionItem.fromJsonObject(itemObj);
            this.mQuestionItemList.add(questionItem);
        }
        return this;
    }
}
package com.ankur.stackoverflow.constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ApiConstants {

    public interface QuestionItem {
        String ITEMS              = "items";
        String TAGS               = "tags";
        String OWNER              = "owner";
        String IS_ANSWERED        = "is_answered";
        String VIEW_COUNT         = "view_count";
        String ANSWER_COUNT       = "answer_count";
        String SCORE              = "score";
        String LAST_ACTIVITY_DATE = "last_activity_date";
        String CREATION_DATE      = "creation_date";
        String QUESTION_ID        = "question_id";
        String LINK               = "link";
        String TITLE              = "title";
    }

    public interface AnswerItem {
        String ITEMS              = "items";
        String OWNER              = "owner";
        String DOWN_VOTE_COUNT    = "down_vote_count";
        String UP_VOTE_COUNT      = "up_vote_count";
        String IS_ACCEPTED        = "is_accepted";
        String SCORE              = "score";
        String LAST_ACTIVITY_DATE = "last_activity_date";
        String LAST_EDIT_DATE     = "last_edit_date";
        String CREATION_DATE      = "creation_date";
        String ANSWER_ID          = "answer_id";
        String QUESTION_ID        = "question_id";
        String LINK               = "link";
        String TITLE              = "title";
        String BODY               = "body";
    }

    public interface UserInfo {
        String REPUTATION    = "reputation";
        String USER_ID       = "user_id";
        String ACCEPT_RATE   = "accept_rate";
        String USER_TYPE     = "user_type";
        String PROFILE_IMAGE = "profile_image";
        String DISPLAY_NAME  = "display_name";
        String LINK          = "link";
    }

    public enum UserType {
        unregistered(0), registered(1), moderator(2), does_not_exist(3);

        private final int mValue;

        UserType(final int newValue) {
            mValue = newValue;
        }

        public int getValue() {
            return mValue;
        }
    }

    public static String getSearchUrl(String query) {
        return getSearchUrl(query, 1, 20);
    }

    public static String getSearchUrl(String query, int page, int pageSize) {

        try {
            query = query == null ? null : URLEncoder.encode(query, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // do nothing
        }
        String url = "https://api.stackexchange.com/2.2/search?page=" + page + "&pagesize=" + pageSize
                + "&order=desc&sort=activity&intitle=" + query + "&site=stackoverflow";
        return url;
    }

}

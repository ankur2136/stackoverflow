package com.ankur.stackoverflow.constants;

public class ApiConstants {

    public interface Item {
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
        UNREGISTERED(0), REGISTERED(1), MODERATOR(2), DOES_NOT_EXIST(3);

        private final int mValue;

        UserType(final int newValue) {
            mValue = newValue;
        }

        public int getValue() {
            return mValue;
        }
    }

}

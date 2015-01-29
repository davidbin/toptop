

package com.happygarden.core;

/**
 * Bootstrap constants
 */
public final class Constants {
    private Constants() {}
    public static final String Token = "com.happygarden.token";
    public static final class Http {
        private Http(){}

        //public static final String SERVER = "http://10.0.2.2:85/Paypal/android";
        public static final String SERVER = "http://happygardenseafood.com/android";
        public static final String URL_CATEGORY = "/category.php";
        public static final String URL_MENUITEM = "/menu.php";
        public static final String URL_FOODIMAGE = "http://happygardenseafood.com/images/food/";
        public static final String URL_COMMENT = "/comment.php";
    }

    public static final class Intent {
        private Intent() {}

        /**
         * Action prefix for all intents created
         */
        public static final String INTENT_PREFIX = "com.happygarden.";

    }

    public static class Notification {
        private Notification() {
        }

        public static final int TIMER_NOTIFICATION_ID = 1000; // Why 1000? Why not? :)
    }

}



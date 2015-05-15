package com.ankur.stackoverflow.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtils {
    private static final String             LOG_TAG               = "FONT_UTILS";

    private static final String             ENGLISH_FONT_NAME     = "English/Roboto";

    private static final String             REGULAR_SUFFIX        = "-Regular.ttf";

    private static final String             BOLD_SUFFIX           = "-Bold.ttf";

    private static final String             CONDENSED_SUFFIX      = "-Condensed.ttf";

    private static final String             CONDENSED_BOLD_SUFFIX = "-CondensedBold.ttf";

    private static final String             LIGHT_SUFFIX          = "-Light.ttf";

    private static final String             FONT_PATH_PREFIX      = "fonts/";

    private static Map<FontStyle, Typeface> fontMap               = new ConcurrentHashMap<FontStyle, Typeface>();

    public enum FontStyle {
        REGULAR(0), BOLD(1), CONDENSED(2), CONDENSED_BOLD(3), LIGHT(4);

        private final int                      id;

        private static Map<Integer, FontStyle> idToFontStyleMap = new HashMap<Integer, FontStyle>();

        static {
            for (FontStyle style : FontStyle.values()) {
                idToFontStyleMap.put(style.getId(), style);
            }
        }

        private FontStyle(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public static FontStyle getFontStyleById(int id) {
            if (idToFontStyleMap.containsKey(id)) {
                return idToFontStyleMap.get(id);
            }

            return REGULAR;
        }
    }

    public static Typeface getFont(Context context, int fontStyleId) {
        return getFont(context, fontStyleId, null);
    }

    public static Typeface getFont(Context context, int fontStyleId, String appLangCode) {
        FontStyle fontStyle = FontStyle.getFontStyleById(fontStyleId);
        return getFont(context, fontStyle, appLangCode);
    }

    public static Typeface getFont(Context context, FontStyle fontStyle) {
        return getFont(context, fontStyle, null);
    }

    public static Typeface getFont(Context context, FontStyle fontStyle, String appLangCode) {

        if (appLangCode == null) {
            appLangCode = "en";
        }

        String fontNamePrefix = null;

        if (appLangCode.equalsIgnoreCase("en")) {
            fontNamePrefix = ENGLISH_FONT_NAME;
        } else {
            LogUtils.warnLog(LOG_TAG, "Language not supported. Setting to 'en'");
            fontNamePrefix = ENGLISH_FONT_NAME;
        }

        String fontNameSuffix = null;

        switch (fontStyle) {
        case REGULAR:
            fontNameSuffix = REGULAR_SUFFIX;
            break;
        case BOLD:
            fontNameSuffix = BOLD_SUFFIX;
            break;
        case CONDENSED:
            fontNameSuffix = CONDENSED_SUFFIX;
            break;
        case CONDENSED_BOLD:
            fontNameSuffix = CONDENSED_BOLD_SUFFIX;
            break;
        case LIGHT:
            fontNameSuffix = LIGHT_SUFFIX;
            break;
        default:
            LogUtils.warnLog(LOG_TAG, "Style not supported. Setting to 'Regular'");
            fontNameSuffix = REGULAR_SUFFIX;
        }

        Typeface typeface = null;
        String fontName = fontNamePrefix + fontNameSuffix;

        try {
            typeface = Typeface.createFromAsset(context.getAssets(), FONT_PATH_PREFIX + fontName);
        } catch (Exception e) {
            LogUtils.warnLog(LOG_TAG, fontName + " font not found", e);
            fontName = fontNamePrefix + REGULAR_SUFFIX;

            try {
                typeface = Typeface.createFromAsset(context.getAssets(), FONT_PATH_PREFIX + fontName);
            } catch (Exception ex) {
                LogUtils.warnLog(LOG_TAG, fontName + " font not found", ex);
                fontName = ENGLISH_FONT_NAME + fontNameSuffix;

                try {
                    typeface = Typeface.createFromAsset(context.getAssets(), FONT_PATH_PREFIX + fontName);
                } catch (Exception exc) {
                    LogUtils.warnLog(LOG_TAG, fontName + " font not found", exc);
                }
            }
        }

        if (appLangCode.equalsIgnoreCase("en")) {
            fontMap.put(fontStyle, typeface);
        }
        return typeface;
    }

    public static void reset() {
        fontMap.clear();
    }

}

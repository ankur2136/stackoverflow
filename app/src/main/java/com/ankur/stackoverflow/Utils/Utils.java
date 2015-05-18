package com.ankur.stackoverflow.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import org.json.JSONObject;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.ankur.stackoverflow.data.datasource.database.DatabaseHelper;
import com.ankur.stackoverflow.domain.ParsingObject;

public class Utils {

    private static String LOG_TAG = "UTILS";

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static void dbDump(Context context) throws Exception {
        File sd = Environment.getExternalStorageDirectory();
        String dbPath;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            dbPath = context.getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator;
        } else {
            dbPath = context.getFilesDir().getPath() + context.getPackageName() + "/databases/";
        }

        if (sd.canWrite()) {
            String currentDBPath = DatabaseHelper.DATABASE_NAME;
            String backupDBPath = "stackoverflow.sqlite";
            File currentDB = new File(dbPath, currentDBPath);
            File backupDB = new File(sd, backupDBPath);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        }
    }

    public static int dpToPixels(Context context, float dps) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    public static <T> T fromJsonObject(JSONObject jsonObject, Object object) throws Exception {
        T obj = null;
        if (object instanceof JSONObject) {
            obj = (T) jsonObject;
        } else if (object instanceof ParsingObject) {
            try {
                obj = ((ParsingObject) object).fromJsonObject(jsonObject);
            } catch (Exception e) {
                LogUtils.errorLog(LOG_TAG, "Exception Parsing: ", e);
            }
        } else {
            throw new Exception("The object should implement ParsingObject");
        }
        return obj;
    }

}

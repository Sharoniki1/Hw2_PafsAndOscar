package com.example.hw1application;

import android.content.Context;
import android.content.SharedPreferences;

public class SPV3 {

    private static final String DB_FILE= "DB_FILE";
    private static SPV3 spv3 = null;
    private SharedPreferences sharedPreferences;

    private SPV3(Context context){
            sharedPreferences = context.getSharedPreferences(DB_FILE, context.MODE_PRIVATE);
        }


        public static SPV3 getInstance() {
            return spv3;
        }

        public static void init(Context context){
            if( spv3 == null)
                spv3 = new SPV3(context);
        }

        public void putString(String key, String value){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key,value);
            editor.apply();
        }

        public String getString(String key, String def){
            return sharedPreferences.getString(key,def);
        }

}

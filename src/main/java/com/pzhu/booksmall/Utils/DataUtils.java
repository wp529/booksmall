package com.pzhu.booksmall.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class DataUtils {
    public static SharedPreferences getSharedPreferences(Context ctx){
        return ctx.getSharedPreferences("user",Context.MODE_PRIVATE);
    }
}

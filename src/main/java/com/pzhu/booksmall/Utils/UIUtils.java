package com.pzhu.booksmall.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/5/17.
 */
public class UIUtils {
    public static void makeToast(Context ctx,String content){
        Toast.makeText(ctx,content,Toast.LENGTH_SHORT).show();
    }
}

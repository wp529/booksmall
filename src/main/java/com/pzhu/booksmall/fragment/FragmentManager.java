package com.pzhu.booksmall.fragment;

import android.support.v4.app.Fragment;

public class FragmentManager {
    private static ShoppingFragment fragment = new ShoppingFragment();
    public static Fragment getShoppingFragment() {
        return fragment;
    }
}

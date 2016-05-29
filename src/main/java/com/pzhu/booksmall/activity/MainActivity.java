package com.pzhu.booksmall.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pzhu.booksmall.R;
import com.pzhu.booksmall.Utils.UIUtils;
import com.pzhu.booksmall.database.MyDatabaseHelper;
import com.pzhu.booksmall.domain.Book;
import com.pzhu.booksmall.fragment.HomeFragment;
import com.pzhu.booksmall.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity";
    private ViewPager mViewPager;
    private RadioGroup mGroup;
    private RadioButton home, shop, mine;
    private MyDatabaseHelper mHelper;
    private SQLiteDatabase bookDB;
    private List<Book> mBooks;
    private int pageSize = 5;
    private int mBackPressCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initData();
        mViewPager.setCurrentItem(0);
        home.setBackgroundColor(Color.RED);
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_home:
                        home.setBackgroundColor(Color.RED);
                        shop.setBackgroundColor(Color.WHITE);
                        mine.setBackgroundColor(Color.WHITE);
                        mViewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_shop:
                        home.setBackgroundColor(Color.WHITE);
                        mine.setBackgroundColor(Color.WHITE);
                        shop.setBackgroundColor(Color.RED);
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_mine:
                        mViewPager.setCurrentItem(2, false);
                        mine.setBackgroundColor(Color.RED);
                        home.setBackgroundColor(Color.WHITE);
                        shop.setBackgroundColor(Color.WHITE);
                        break;
                }
            }
        });
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
    }

    private void initData() {
        mHelper = new MyDatabaseHelper(this, "BooksMall.db", null, 3);
        bookDB = mHelper.getWritableDatabase();
//        mBooks = new ArrayList<>();
//        Cursor books = bookDB.query("Book", null, null, null, null, null, null);
//        if (books.moveToFirst()) {
//            do {
//                String name = books.getString(books.getColumnIndex("name"));
//                String author = books.getString(books.getColumnIndex("author"));
//                int pages = books.getInt(books.getColumnIndex("pages"));
//                double price = books.getDouble(books.getColumnIndex("price"));
//                Book book = new Book(name, author, pages, price);
//                mBooks.add(book);
//            } while (books.moveToNext());
//            books.close();
//        }
    }

    public ArrayList<Book> loadDatabase(int pageID){
        mBooks = new ArrayList<>();
        String sql= "select * from Book"+" Limit "+String.valueOf(pageSize)+ " Offset " +String.valueOf(pageID*pageSize);
        Cursor books = bookDB.rawQuery(sql, null);
//      Cursor books = bookDB.query("Book", null, null, null, null, null, null);
        if (books.moveToFirst()) {
            do {
                String name = books.getString(books.getColumnIndex("name"));
                String author = books.getString(books.getColumnIndex("author"));
                int pages = books.getInt(books.getColumnIndex("pages"));
                double price = books.getDouble(books.getColumnIndex("price"));
                Book book = new Book(name, author, pages, price);
                mBooks.add(book);
            } while (books.moveToNext());
            books.close();
        }

        return (ArrayList<Book>) mBooks;
    }

    private void initUI() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);
        mViewPager = (ViewPager) findViewById(R.id.vp);
        home = (RadioButton) findViewById(R.id.rb_home);
        shop = (RadioButton) findViewById(R.id.rb_shop);
        mine = (RadioButton) findViewById(R.id.rb_mine);
        mGroup = (RadioGroup) findViewById(R.id.rg_group);
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
//                    return new HomeFragment(mBooks);
                    return new HomeFragment(loadDatabase(0));
                case 1:
                    return com.pzhu.booksmall.fragment.FragmentManager.getShoppingFragment();
                case 2:
                    return new MineFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public void onBackPressed() {
        if(mBackPressCount != 1){
            UIUtils.makeToast(this,"再次点击退出程序");
        }else{
            super.onBackPressed();
        }
        mBackPressCount++;
    }

    public void setBackPressCount(int mBackPressCount) {
        this.mBackPressCount = mBackPressCount;
    }
}

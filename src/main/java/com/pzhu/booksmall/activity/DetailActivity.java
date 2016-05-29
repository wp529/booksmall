package com.pzhu.booksmall.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pzhu.booksmall.R;
import com.pzhu.booksmall.Utils.UIUtils;
import com.pzhu.booksmall.database.MyDatabaseHelper;
import com.pzhu.booksmall.fragment.FragmentManager;
import com.pzhu.booksmall.fragment.ShoppingFragment;

public class DetailActivity extends Activity {
    private static final String TAG = "DetailActivity";
    private TextView tvName, tvAuthor, tvPrice, tvPages;
    private MyDatabaseHelper helper;
    private Button mAddShop;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mode = getIntent().getStringExtra("Mode");
        initUI();
        initData();
    }

    private void initUI() {
        tvName = (TextView) findViewById(R.id.tv_detail_name);
        tvAuthor = (TextView) findViewById(R.id.tv_detail_author);
        tvPrice = (TextView) findViewById(R.id.tv_detail_price);
        tvPages = (TextView) findViewById(R.id.tv_detail_pages);
        mAddShop = (Button) findViewById(R.id.btn_add_shopcar);
        if("tourist".equals(mode)){
            mAddShop.setEnabled(false);
            mAddShop.setTextColor(Color.GRAY);
        }else{
            mAddShop.setEnabled(true);
            mAddShop.setTextColor(Color.BLACK);
        }
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        final String name = data.getString("name");
        final String author = data.getString("author");
        final double price = data.getDouble("price");
        final int pages = data.getInt("pages");
        tvName.setText("书名:" + name);
        tvAuthor.setText("作者:" + author);
        tvPrice.setText("价格:" + price);
        tvPages.setText("页数:" + pages);
        mAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper = new MyDatabaseHelper(DetailActivity.this, "BooksMall.db", null, 3);
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name",name);
                values.put("author",author);
                values.put("price",price);
                values.put("pages",pages);
                Cursor books = db.query("Shopping", null, null, null, null, null, null);
                if (books.moveToFirst()) {
                    do {
                        String queryName = books.getString(books.getColumnIndex("name"));
                        if(name.equals(queryName)){
                            UIUtils.makeToast(DetailActivity.this,"已经添加到购物车了");
                            return;
                        }
                    } while (books.moveToNext());
                    books.close();
                }
                db.insert("Shopping",null,values);
                Fragment shoppingFragment = FragmentManager.getShoppingFragment();
                ShoppingFragment fragment = (ShoppingFragment) shoppingFragment;
                fragment.initData();
                UIUtils.makeToast(DetailActivity.this,"添加成功!");
            }
        });
    }
}

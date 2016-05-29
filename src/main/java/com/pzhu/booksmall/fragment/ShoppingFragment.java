package com.pzhu.booksmall.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.pzhu.booksmall.R;
import com.pzhu.booksmall.Utils.UIUtils;
import com.pzhu.booksmall.activity.LoginActivity;
import com.pzhu.booksmall.activity.MainActivity;
import com.pzhu.booksmall.database.MyDatabaseHelper;
import com.pzhu.booksmall.domain.Book;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment {
    private static final String TAG = "ShoppingFragment";
    private double total = 0;
    DecimalFormat format;
    private ListView mList;
    private ShopAdapter mAdapter;
    private MyDatabaseHelper helper;
    private Button mPay;
    private List<Book> mBooks;
    private SQLiteDatabase db;
    private TextView mTotal;
    private MainActivity activity;
    private String loginMode;

    public ShoppingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        loginMode = activity.getIntent().getStringExtra("LoginMode");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        if("tourist".equals(loginMode)){
            view = inflater.inflate(R.layout.fragment_tourist_shopping, container, false);
            Button btn = (Button) view.findViewById(R.id.btn_tourist_login);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    activity.finish();
                }
            });
            return view;
        }
        view = inflater.inflate(R.layout.fragment_shopping, container, false);
        format = new DecimalFormat("0.00");
        mList = (ListView) view.findViewById(R.id.lv_shop);
        mTotal = (TextView) view.findViewById(R.id.tv_total);
        mPay = (Button) view.findViewById(R.id.btn_pay);
        mPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.makeToast(getContext(),"调用接口支付:" + format.format(total) + "元");
            }
        });
        mAdapter = new ShopAdapter();
        initData();
        mList.setAdapter(mAdapter);
        return view;
    }

    public void initData() {
        mBooks = new ArrayList<>();
        helper = new MyDatabaseHelper(getContext(), "BooksMall.db", null, 3);
        db = helper.getWritableDatabase();
        Cursor books = db.query("Shopping", null, null, null, null, null, null);
        if (books.moveToFirst()) {
            do {
                String name = books.getString(books.getColumnIndex("name"));
                double price = books.getDouble(books.getColumnIndex("price"));
                Book book = new Book(name,price);
                mBooks.add(book);
            } while (books.moveToNext());
        }
        mAdapter.notifyDataSetChanged();
        books.close();
    }

    class ShopAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mBooks.size();
        }

        @Override
        public Book getItem(int i) {
            return mBooks.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final ViewHolder holder;
            if(view == null){
                holder = new ViewHolder();
                view = LayoutInflater.from(getActivity()).inflate(R.layout.shop_list_item, null);
                holder.tvName = (TextView) view.findViewById(R.id.tv_shop_name);
                holder.mButton = (Button) view.findViewById(R.id.btn_delete);
                holder.tvPrice = (TextView) view.findViewById(R.id.tv_shop_price);
                holder.mAdd = (CheckBox) view.findViewById(R.id.cb_add);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            final Book book = getItem(i);
            holder.mAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        total += book.getPrice();
                    }else{
                        total -= book.getPrice();
                    }
                    total = Math.abs(total);
                    mTotal.setText("总计:" + format.format(total) + "元");
                }
            });

            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBooks.remove(i);
                    db.delete("Shopping","name=?",new String[]{book.getName()});
                    if(holder.mAdd.isChecked()){
                        total -= book.getPrice();
                        total = Math.abs(total);
                        mTotal.setText("总计:" + format.format(total) + "元");
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
            holder.tvName.setText("书名:" + book.getName());
            holder.tvPrice.setText(String.valueOf(book.getPrice()) + "元");
            return view;
        }
    }

    static class ViewHolder{
        TextView tvName,tvPrice;
        Button mButton;
        CheckBox mAdd;
    }
}

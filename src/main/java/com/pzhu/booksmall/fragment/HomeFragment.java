package com.pzhu.booksmall.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pzhu.booksmall.R;
import com.pzhu.booksmall.activity.DetailActivity;
import com.pzhu.booksmall.activity.MainActivity;
import com.pzhu.booksmall.domain.Book;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "HomeFragment";
    private List<Book> mBooks;
    private ListView mList;
    private BookAdapter mAdapter;
    private ViewPager mViewPager;
    private int lastItem;
    private int pageID = 1;

    private int[] imageID = new int[]{R.drawable.ad1,R.drawable.ad2,R.drawable.ad3,R.drawable.ad4};
    private List<ImageView> images;
    private MainActivity activity;

    public HomeFragment(List<Book> mBooks) {
        this.mBooks = mBooks;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mList = (ListView) view.findViewById(R.id.lv_book);

        View footer = inflater.inflate(R.layout.list_footer,null);
        final LinearLayout llMore = (LinearLayout) footer.findViewById(R.id.ll_more);
        final TextView tvNo = (TextView) footer.findViewById(R.id.ll_nomore);
        mList.addFooterView(footer);
        mList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (lastItem == mBooks.size() && i == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    ArrayList<Book> books = activity.loadDatabase(pageID);
                    if(books.size() == 0){
                        llMore.setVisibility(View.INVISIBLE);
                        tvNo.setVisibility(View.VISIBLE);
                    }else{
                        llMore.setVisibility(View.VISIBLE);
                        tvNo.setVisibility(View.INVISIBLE);
                        mBooks.addAll(mBooks.size(),books);
//                        if(books.size() == 5){
//                            mBooks.addAll(pageID * 5,books);
//
////                            llMore.setVisibility(View.INVISIBLE);
////                            tvNo.setVisibility(View.VISIBLE);
//                        }else{
//                            mBooks.addAll(mBooks.size(),books);
//                        }
                    }

                    mList.setSelection(mBooks.size());
                    //加载下一页数据
                    pageID++;
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                lastItem = i + i1 - 1 ;
            }
        });

        mViewPager = (ViewPager) view.findViewById(R.id.vp_ad);
        mAdapter = new BookAdapter();
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 10000;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(images.get(position % 4));
                return images.get(position % 4);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        mViewPager.setCurrentItem(5000);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentItem = mViewPager.getCurrentItem();
                currentItem++;
                mViewPager.setCurrentItem(currentItem);
                handler.postDelayed(this,2000);
            }
        },2000);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);
        return view;
    }

    private void initData() {
        images = new ArrayList<>();
        for(int i=0;i<imageID.length;i++){
            ImageView image = new ImageView(getContext());
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageID[i]);
            image.setImageBitmap(bitmap);
            images.add(image);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i != mBooks.size()){
            Book book = mBooks.get(i);
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("name",book.getName());
            bundle.putString("author",book.getAuthor());
            bundle.putDouble("price",book.getPrice());
            bundle.putInt("pages",book.getPages());
            intent.putExtras(bundle);
            MainActivity activity = (MainActivity) getActivity();
            activity.setBackPressCount(0);
            String loginMode = activity.getIntent().getStringExtra("LoginMode");
            intent.putExtra("Mode",loginMode);
            startActivity(intent);
        }
    }

    class BookAdapter extends BaseAdapter{

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
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view == null){
                holder = new ViewHolder();
                view = LayoutInflater.from(getActivity()).inflate(R.layout.book_list_item, null);
                holder.tvName = (TextView) view.findViewById(R.id.tv_name);
                holder.tvAuthor = (TextView) view.findViewById(R.id.tv_author);
                holder.tvPrice = (TextView) view.findViewById(R.id.tv_price);
                holder.tvPages = (TextView) view.findViewById(R.id.tv_pages);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            Book book = getItem(i);
            holder.tvName.setText("书名:" + book.getName());
            holder.tvAuthor.setText("作者:" + book.getAuthor());
            holder.tvPrice.setText("价格:" + String.valueOf(book.getPrice()));
            holder.tvPages.setText("页数:" + String.valueOf(book.getPages()));
            return view;
        }
    }
    static class ViewHolder{
        TextView tvName,tvAuthor,tvPrice,tvPages;
    }
}

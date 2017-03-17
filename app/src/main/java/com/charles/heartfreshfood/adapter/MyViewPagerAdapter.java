package com.charles.heartfreshfood.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * myviewpager 的适配器
 */
public class MyViewPagerAdapter extends PagerAdapter{


    private Context mContext;

    private ArrayList<View> mListViews = null;

    private HashMap<Integer,View> mViews = null;

    /**
     * Instantiates a new ab view pager adapter.
     * @param context the context
     * @param mListViews the m list views
     */
    public MyViewPagerAdapter(Context context,ArrayList<View> mListViews) {
        this.mContext = context;
        this.mListViews = mListViews;
        this.mViews = new HashMap <Integer,View>();
    }

    /**
     * 获取数量
     *
     * @return the count
     * @see PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        return mListViews.size();
    }

    /**
     * 必须实现的方法，object是否对应这个view
     *
     * @param arg0 the arg0
     * @param arg1 the arg1
     * @return true, if is view from object
     * @see PagerAdapter#isViewFromObject(View, Object)
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == (arg1);
    }

    /**
     * 添加、显示view,important
     *
     * @param container the container
     * @param position the position
     * @return the object
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = mListViews.get(position);
        container.addView(v);
        return v;
    }

    /**
     * 移除view
     *
     * @param container the container
     * @param position the position
     * @param object the object
     * @see PagerAdapter#destroyItem(View, int, Object)
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    /**
     * 数据集改变notifyDataSetChanged.
     *
     * @param object the object
     * @return the item position
     * @see PagerAdapter#getItemPosition(Object)
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

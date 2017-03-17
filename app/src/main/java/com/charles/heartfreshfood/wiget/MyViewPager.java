package com.charles.heartfreshfood.wiget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * 使用viewpager 来作为广告条图片的容器，并且解决scrollview和listview造成的滚动冲突
 * 说明下冲突原因就是因为用户的滑动事件会被捕获导致viewpager失灵
 */
public class MyViewPager extends ViewPager{
    /** The parent scroll view. */
    private ScrollView parentScrollView;

    /** The parent list view. */
    private ListView parentListView;

    private GestureDetector mGestureDetector;

    /**
     * 初始化ViewPager.
     *
     * @param context the context
     */

    public MyViewPager(Context context) {
        super(context);
        mGestureDetector = new GestureDetector(context,new YScrollDetector());
        setFadingEdgeLength(0);
    }

    /**
     * 初始化ViewPager.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context,new YScrollDetector());
        setFadingEdgeLength(0);
    }

    /**
     * 用户触摸广告条后的中断事件
     *
     * @param ev the ev
     * @return true, if successful
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev)
              && mGestureDetector.onTouchEvent(ev);
    }

    /**
     *  外层为scrollview时需要设置
     *
     * @param parentScrollView the new parent scroll view
     */
    public void setParentScrollView(ScrollView parentScrollView) {
        this.parentScrollView = parentScrollView;
    }

    /**
     * 父级为listview时需要设置
     *
     * @param parentListView the new parent scroll view
     */
    public void setParentListView(ListView parentListView) {
        this.parentListView = parentListView;
    }

    /**
     * 设置父级View.
     *
     * @param flag 父级view滚动开关
     */
    private void setParentScrollAble(boolean flag) {
        if(parentScrollView!=null){
            parentScrollView.requestDisallowInterceptTouchEvent(!flag);
        }

        if(parentListView!=null){
            parentListView.requestDisallowInterceptTouchEvent(!flag);
        }

    }



    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            //判断手势偏向
            if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                //父级层次不滑动
                setParentScrollAble(false);
                return true;
            }else{
                setParentScrollAble(true);
            }
            return false;
        }
    }
}

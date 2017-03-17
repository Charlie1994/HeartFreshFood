package com.charles.heartfreshfood.wiget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.adapter.MyViewPagerAdapter;
import com.charles.heartfreshfood.util.HttpUrls;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *  这个布局不仅用于首页的广告条还用于商品详细的图片播放器，因此需要做到足够的flexibility
 */
public class PictureRecycleView extends LinearLayout {

    private final String TAG = "BannerView";
    private int showType = 0;
    private Context context;
    private MyViewPager mViewPager;

    /** The page line layout. */
    private LinearLayout pageLineLayout;

    /** The layout params pageLine. */
    public LayoutParams pageLineLayoutParams = null;

    private int count, position;

    private Bitmap displayImage, hideImage;

    private BannerOnItemClickListener mOnItemClickListener;
    private BannerOnChangeListener mAbChangeListener;
    private BannerOnScrollListener mAbScrolledListener;
    private BannerOnTouchListener mBannerOnTouchListener;

    public LayoutParams layoutParamsWF,layoutParamsFW,
          layoutParamsFF,layoutParamsWW;

    private ArrayList<String> imgUrlList = null;
    private ArrayList<View> mListViews = null;

    private MyViewPagerAdapter mViewPagerAdapter = null;

    /** 导航的点父View */
    private LinearLayout mPageLineLayoutParent = null;

    /** The page line horizontal gravity. */
    private int pageLineHorizontalGravity = Gravity.RIGHT;

    /** 播放的方向 */
    private int playingDirection = 0;
    /** 播放的开关 */
    private boolean play = false;
    /** 播放的间隔时间 */
    private int sleepTime = 5000;
    /** 播放方向方式（1顺序播放和0来回播放） */
    private int playType = 1;

    public PictureRecycleView(Context context) {
        super(context);
    }

    public PictureRecycleView(Context context, AttributeSet attrs){
        super(context, attrs);
        //LayoutInflater.from(context).inflate(R.layout.adbanner_layout, this);
        initView(context);
    }
    /*
     * 设置用于首页还是商品详细页面
     *  0=用于首页广告条,1=用于商品详细页面
     */
    public void setShowType(int showType){
        this.showType = showType;
    }

    /*
     * 设置添加的图片url列表
     */
    public void setImgUrlList(ArrayList<String> imgUrlList){
        this.imgUrlList = imgUrlList;
    }
    /*
     * 启动加载图片
     */
    public void launch(){
        addViewsToList(imgUrlList);
    }
    /**
     *
     * 描述：初始化这个View
     *
     */
    public void initView(Context context) {
        this.context = context;
        layoutParamsFF = new LayoutParams(LayoutParams.MATCH_PARENT,
              LayoutParams.MATCH_PARENT);
        layoutParamsFW = new LayoutParams(LayoutParams.MATCH_PARENT,
              LayoutParams.WRAP_CONTENT);
        layoutParamsWF = new LayoutParams(LayoutParams.WRAP_CONTENT,
              LayoutParams.MATCH_PARENT);
        layoutParamsWW = new LayoutParams(LayoutParams.WRAP_CONTENT,
              LayoutParams.WRAP_CONTENT);
        pageLineLayoutParams = new LayoutParams(layoutParamsWW);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.rgb(255, 255, 255));

        RelativeLayout mRelativeLayout = new RelativeLayout(context);

        mViewPager = new MyViewPager(context);
        // 手动创建的ViewPager,如果用fragment必须调用setId()方法设置一个id
        mViewPager.setId(1985);
        // 导航的点
        mPageLineLayoutParent = new LinearLayout(context);
        mPageLineLayoutParent.setPadding(0, 5, 0, 5);
        mPageLineLayoutParent.setGravity(Gravity.CENTER);
        pageLineLayout = new LinearLayout(context);
        pageLineLayout.setPadding(15, 1, 15, 1);
        pageLineLayout.setVisibility(View.INVISIBLE);
        mPageLineLayoutParent.addView(pageLineLayout, layoutParamsWW);

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
              ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        lp1.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        lp1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        mRelativeLayout.addView(mViewPager, lp1);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mRelativeLayout.addView(mPageLineLayoutParent, lp2);
        addView(mRelativeLayout, layoutParamsFW);

        //得到导航点的图片资源文件
        displayImage =getBitmapFormSrc(R.drawable.icon_point_pre);
        hideImage =getBitmapFormSrc(R.drawable.icon_point);

        mListViews = new ArrayList<>();
        mViewPagerAdapter = new MyViewPagerAdapter(context, mListViews);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setFadingEdgeLength(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                makesurePosition();
                onPageSelectedCallBack(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                onPageScrolledCallBack(position);
            }
        });

    }

    /**
     * 得到本地导航点图片
     * @return bitmap
     */
    private Bitmap getBitmapFormSrc(int resid){
        Bitmap bitmap=null;
        bitmap= BitmapFactory.decodeResource(getResources(), resid);
        return bitmap;
    }

    /**
     * 创建导航点.
     */
    public void creatIndex() {
        // 显示下面的点
        pageLineLayout.removeAllViews();
        mPageLineLayoutParent.setHorizontalGravity(pageLineHorizontalGravity);
        pageLineLayout.setGravity(Gravity.CENTER);
        pageLineLayout.setVisibility(View.VISIBLE);
        count = imgUrlList.size();
        for (int j = 0; j < count; j++) {
            ImageView imageView = new ImageView(context);
            pageLineLayoutParams.setMargins(5, 5, 5, 5);
            imageView.setLayoutParams(pageLineLayoutParams);
            if (j == 0) {
                imageView.setImageBitmap(displayImage);
            } else {
                imageView.setImageBitmap(hideImage);
            }
            pageLineLayout.addView(imageView, j);
        }
    }

    /**
     * 定位点的位置.
     */
    public void makesurePosition() {
        position = mViewPager.getCurrentItem();
        for (int j = 0; j < count; j++) {
            if (position == j) {
                ((ImageView) pageLineLayout.getChildAt(position)).setImageBitmap(displayImage);
            } else {
                ((ImageView) pageLineLayout.getChildAt(j)).setImageBitmap(hideImage);
            }
        }
    }
    /*
    *   使用Glide将传入的url集合变成imgview并且导入到mListViews中
    */
    public void addViewsToList(ArrayList<String> imgUrlList){
        creatIndex();
        for(String url:imgUrlList) {
            ImageView img = new ImageView(context);
            Glide
                  .with(context)
                  .load(HttpUrls.LOCAL_URL+url)
                  .centerCrop()
                  .into(img);
            mListViews.add(img);
            mViewPagerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 描述：删除可播放视图.
     *
     */
    @Override
    public void removeAllViews() {
        mListViews.clear();
        mViewPagerAdapter.notifyDataSetChanged();
        creatIndex();
    }

    /**
     * 描述：设置页面切换事件.
     *
     * @param position
     *            the position
     */
    private void onPageScrolledCallBack(int position) {
        if (mAbScrolledListener != null) {
            mAbScrolledListener.onScroll(position);
        }

    }

    /**
     * 描述：设置页面切换事件.
     *
     * @param position
     *            the position
     */
    private void onPageSelectedCallBack(int position) {
        if (mAbChangeListener != null) {
            mAbChangeListener.onChange(position);
        }

    }

    /** 用与轮换的 handler. */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                //显示播放的页面
                ShowPlay();
                if (play) {
                    handler.postDelayed(runnable, sleepTime);
                }
            }
        }

    };

    /** 用于轮播的线程. */
    private Runnable runnable = new Runnable() {
        public void run() {
            if (mViewPager != null) {
                handler.sendEmptyMessage(0);
            }
        }
    };

    /**
     * 描述：自动轮播. sleepTime 播放的间隔时间
     */
    public void startPlay() {
        if (handler != null) {
            play = true;
            handler.postDelayed(runnable, sleepTime);
        }
    }

    /**
     * 描述：自动轮播.
     */
    public void stopPlay() {
        if (handler != null) {
            play = false;
            handler.removeCallbacks(runnable);
        }
    }

    /**
     * 设置点击事件监听.
     *
     * @param onItemClickListener
     *            the new on item click listener
     */
    public void setOnItemClickListener(BannerOnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 描述：设置页面切换的监听器.
     *
     * @param abChangeListener
     *            the new on page change listener
     */
    public void setOnPageChangeListener(BannerOnChangeListener abChangeListener) {
        mAbChangeListener = abChangeListener;
    }

    /**
     * 描述：设置页面滑动的监听器.
     *
     * @param abScrolledListener
     *            the new on page scrolled listener
     */
    public void setOnPageScrolledListener(BannerOnScrollListener abScrolledListener) {
        mAbScrolledListener = abScrolledListener;
    }

    /**
     *
     * 描述：设置页面Touch的监听器.
     *
     * @param abOnTouchListener
     * @throws
     */
    public void setOnTouchListener(BannerOnTouchListener abOnTouchListener) {
        mBannerOnTouchListener = abOnTouchListener;
    }

    /**
     * Sets the page line image.
     *
     * @param displayImage
     *            the display image
     * @param hideImage
     *            the hide image
     */
    public void setPageLineImage(Bitmap displayImage, Bitmap hideImage) {
        this.displayImage = displayImage;
        this.hideImage = hideImage;
        creatIndex();

    }

    /**
     * 描述：获取这个滑动的ViewPager类.
     *
     * @return the view pager
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * 描述：获取当前的View的数量.
     *
     * @return the count
     */
    public int getCount() {
        return mListViews.size();
    }

    /**
     * 描述：设置页显示条的位置,在AddView前设置.
     *
     * @param horizontalGravity
     *            the new page line horizontal gravity
     */
    public void setPageLineHorizontalGravity(int horizontalGravity) {
        pageLineHorizontalGravity = horizontalGravity;
    }

    /**
     * 如果外层有ScrollView需要设置.
     *
     * @param parentScrollView
     *            the new parent scroll view
     */
    public void setParentScrollView(ScrollView parentScrollView) {
        this.mViewPager.setParentScrollView(parentScrollView);
    }

    /**
     * 如果外层有ListView需要设置.
     *
     * @param parentListView
     *            the new parent list view
     */
    public void setParentListView(ListView parentListView) {
        this.mViewPager.setParentListView(parentListView);
    }

    /**
     *
     * 描述：设置导航点的背景
     *
     * @param resid
     * @throws
     */
    public void setPageLineLayoutBackground(int resid) {
        pageLineLayout.setBackgroundResource(resid);
    }

    /**
     * 描述：设置播放的间隔时间
     * @param sleepTime  间隔时间单位是毫秒
     */
    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    /**
     *  描述：设置播放方向的方式（1顺序播放和0来回播放） playType
     * @param playType    为0表示来回播放，为1表示顺序播放
     */
    public void setPlayType(int playType) {
        this.playType = playType;
    }


    /**
     * 描述：播放显示界面（1顺序播放和0来回播放） playType 为0表示来回播放，为1表示顺序播放
     */
    public void ShowPlay() {
        //总页数
        int count = mListViews.size();
        // 当前显示的页数
        int i = mViewPager.getCurrentItem();
        switch (playType) {
            case 0:
                // 来回播放
                if (playingDirection == 0) {
                    if (i == count - 1) {
                        playingDirection = -1;
                        i--;
                    } else {
                        i++;
                    }
                } else {
                    if (i == 0) {
                        playingDirection = 0;
                        i++;
                    } else {
                        i--;
                    }
                }
                break;
            case 1:
                // 顺序播放
                if (i == count - 1) {
                    i = 0;
                } else {
                    i++;
                }

                break;

            default:
                break;
        }
        // 设置显示第几页
        mViewPager.setCurrentItem(i, true);
    }

    public interface BannerOnChangeListener {

        /**
         * On change.
         *
         * @param position the position
         */
        public void onChange(int position);

    }
    public interface BannerOnItemClickListener {

        /**
         * 条目点击接口
         * @param position 点击位置
         */
        public void onClick(int position);
    }
    public interface BannerOnScrollListener {

        /**
         * 滚动监听
         * @param arg1
         */
        public void onScroll(int arg1);

        /**
         *滚动停止
         */
        public void onScrollStoped();

        /**
         * 滚到最左边
         */
        public void onScrollToLeft();

        /**
         * 滚到最右边
         */
        public void onScrollToRight();

    }
    public interface BannerOnTouchListener {
        /**
         * 屏幕触摸监听.
         *
         * @param event 触摸的手势
         */
        public void onTouch(MotionEvent event);
    }

}

package com.charles.heartfreshfood.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.charles.heartfreshfood.model.Comment;
import com.charles.heartfreshfood.model.Review;

import java.util.List;

/**
 *  评论和评价的适配器
 */
public class CommentListAdapter extends BaseAdapter {
    boolean iscomment = true;//判断是否为评论，以确定要不要放RatingBar
    public CommentListAdapter(List<Comment> comments){

    }
    public Com
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}

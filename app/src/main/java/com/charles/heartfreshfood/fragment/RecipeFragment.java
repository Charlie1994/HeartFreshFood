package com.charles.heartfreshfood.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.activity.CreateRecipeActivity;
import com.charles.heartfreshfood.activity.RecipeDetailShowActivity;
import com.charles.heartfreshfood.api.NetworkApi;
import com.charles.heartfreshfood.model.RecipeBrief;
import com.charles.heartfreshfood.wiget.RecipeItemView;

import java.util.ArrayList;

import medusa.theone.waterdroplistview.view.WaterDropListView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * 菜谱界面
 */
public class RecipeFragment extends Fragment implements WaterDropListView.OnItemClickListener,WaterDropListView.IWaterDropListViewListener{
    View view;
    FloatingActionButton btn;
    WaterDropListView listView;
    ArrayList<RecipeBrief> list = new ArrayList<>();
    MyAdapter adapter;
    int status = 0;
    final int STATUS_NORMAL = 0;
    final int STATUS_LOADING = 1;
    final int STATUS_REFRESH = 2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recipe,null,false);
        btn = (FloatingActionButton)view.findViewById(R.id.fragment_recipe_btn);
        initViews();
        return view;
    }
    private void initViews(){
        adapter = new MyAdapter();
        listView = (WaterDropListView)view.findViewById(R.id.fragment_recipe_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        getRecipes();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateRecipeActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
    }
    private void getRecipes(){
        NetworkApi service = NetworkApi.retrofit.create(NetworkApi.class);
        Call<ArrayList<RecipeBrief>> call = service.getRecipelist();
        call.enqueue(new Callback<ArrayList<RecipeBrief>>() {
            ProgressDialog dialog = ProgressDialog.show(getActivity(),"爱心鲜","努力加载中……");
            @Override
            public void onResponse(Response<ArrayList<RecipeBrief>> response, Retrofit retrofit) {
                dialog.dismiss();
                if(response.isSuccess()){
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                switch (status){
                    case STATUS_REFRESH:
                        status = STATUS_NORMAL;
                        listView.stopRefresh();
                        break;
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position<=list.size()){
            Intent intent = new Intent(getActivity(), RecipeDetailShowActivity.class);
            intent.putExtra("recipebrief",list.get(position-1));
            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
        }
    }

    @Override
    public void onRefresh() {
        status = STATUS_REFRESH;
        list.clear();
        getRecipes();
    }

    @Override
    public void onLoadMore() {

    }

    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View layout = LayoutInflater.from(getActivity()).inflate(R.layout.view_list_recipe,null);
            RecipeItemView item = (RecipeItemView)layout.findViewById(R.id.view_list_recipe_item);
            item.setViews(list.get(position));
            return layout;
        }
    }
}

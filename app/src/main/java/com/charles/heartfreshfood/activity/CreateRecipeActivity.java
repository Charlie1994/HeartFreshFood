package com.charles.heartfreshfood.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.model.Material;
import com.charles.heartfreshfood.model.Recipe;
import com.charles.heartfreshfood.wiget.MaterialItemView;

import java.util.ArrayList;

public class CreateRecipeActivity extends AppCompatActivity {


    EditText mName;
    LinearLayout createRecipeMaterial;
    EditText mIntroduction;
    TextView addmaterial;
    Context context = CreateRecipeActivity.this;
    Spinner mDifficulty;
    Spinner mTime;
    MaterialAdapter materialAdapter;
    ListView listView;
    private String time;
    private String difficulty;
    private ArrayList<Material> materials;

    public static int GET_MATERIAL = 1;//取得用材
    public static final int TYPE_MODIFY = 1;
    public static final int TYPE_NEW = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        time = "1";difficulty="low";
        mIntroduction = (EditText)findViewById(R.id.create_recipe_introduction);
        mName = (EditText)findViewById(R.id.create_recipe_name);
        initHead();
        initSpinner();
        initMaterial();
    }
    private void initHead(){
        TextView rightbtn = (TextView)findViewById(R.id.view_banner_head_right);

        rightbtn.setText("下一步");
        rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recipe r = new Recipe(mName.getText().toString(), materials, mIntroduction.getText().toString(), time, difficulty);
                Intent intent = new Intent(context, RecipeAddstepActivity.class);
                intent.putExtra("recipe", r);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
        TextView leftbtn = (TextView)findViewById(R.id.view_banner_head_left);
        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            }
        });
    }

    private void initSpinner(){
        mDifficulty = (Spinner)findViewById(R.id.create_recipe_difficulty);
        mTime = (Spinner)findViewById(R.id.create_recipe_time);
        ArrayList<String> difficults = new ArrayList<>();
        difficults.add("初级");
        difficults.add("中级");
        difficults.add("高级");
        ArrayList<String> times = new ArrayList<>();
        times.add("10分钟左右");
        times.add("10-30分钟");
        times.add("30-60分钟");
        times.add("1小时以上");
        ArrayAdapter<String> diffAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficults);
        diffAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDifficulty.setAdapter(diffAdapter);
        mDifficulty.setPrompt("请选择难度");
        mTime.setAdapter(timeAdapter);
        mTime.setPrompt("请选择制作时间");
        mDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :
                        difficulty = "low";
                        break;
                    case 1 :
                        difficulty = "medium";
                        break;
                    case 2 :
                        difficulty = "hard";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :
                        time = "1";
                        break;
                    case 1 :
                        time = "2";
                        break;
                    case 2 :
                        time = "3";
                        break;
                    case 3 :
                        time = "4";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void initMaterial(){
        materials = new ArrayList<>();
        materialAdapter = new MaterialAdapter();
        listView = (ListView)findViewById(R.id.create_recipe_materialist);
        listView.setAdapter(materialAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CreateRecipeActivity.this,MaterialActivity.class);
                intent.putExtra("type",TYPE_MODIFY);
                intent.putExtra("material",materials.get(position));
                intent.putExtra("position",position);
                startActivityForResult(intent,GET_MATERIAL);
            }
        });
        //设置添加用料的点击事件
        addmaterial = (TextView)findViewById(R.id.create_recipe_addmaterial);
        addmaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateRecipeActivity.this,MaterialActivity.class);
                intent.putExtra("type",TYPE_NEW);
                startActivityForResult(intent, GET_MATERIAL);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
    }
    //每次添加的时候listview的高度需要改变
    private void setListviewHeight(ListView listView){
        int height=0;
        View view = materialAdapter.getView(0,null,null);
        view.measure(0,0);
        height = (view.getMeasuredHeight()+listView.getDividerHeight())*materials.size();
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height;
        listView.setLayoutParams(layoutParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case TYPE_NEW:
                Material material1 = (Material)data.getSerializableExtra("material");
                materials.add(material1);
                materialAdapter.notifyDataSetChanged();
                setListviewHeight(listView);
                break;
            default:
                break;
        }
    }

    private class MaterialAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return materials.size();
        }

        @Override
        public Object getItem(int position) {
            return materials.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MaterialItemView view = new MaterialItemView(CreateRecipeActivity.this,null);
            Material material = (Material)getItem(position);
            view.setName(material.getName());
            view.setNum(material.getNum());
            return view;
        }
    }
}

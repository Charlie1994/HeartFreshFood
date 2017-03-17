package com.charles.heartfreshfood.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.charles.heartfreshfood.R;
import com.charles.heartfreshfood.model.Recipe;
import com.charles.heartfreshfood.model.RecipeStep;
import com.charles.heartfreshfood.wiget.SmallRecipeStepView;

import java.util.HashMap;

public class RecipeAddstepActivity extends AppCompatActivity {
    public HashMap<Integer,RecipeStep> steps;
    private Recipe recipe;
    private GridView grid;
    private AddstepAddapter adapter;
    private Button btn;
    SmallRecipeStepView recipestep;
    public final static int RECIPE_DETAIL = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_addstep);
        recipe  = (Recipe) getIntent().getExtras().getSerializable("recipe");
        steps = new HashMap<>();
        grid = (GridView) findViewById(R.id.recipe_addstep_grid);
        adapter = new AddstepAddapter(this);
        grid.setAdapter(adapter);
        btn = (Button)findViewById(R.id.recipe_addstep_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeAddstepActivity.this,RecipeDetailActivity.class);
                intent.putExtra("num", steps.size()+1);
                startActivityForResult(intent, RECIPE_DETAIL);
                overridePendingTransition(R.anim.pop_up_in,R.anim.anim_static);
            }
        });
        initHead();
    }
    private void initHead(){
        TextView leftbtn = (TextView)findViewById(R.id.view_banner_head_left);
        TextView title = (TextView)findViewById(R.id.view_banner_head_title);
        title.setText("制作步骤");
        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            }
        });
        TextView rightbtn = (TextView)findViewById(R.id.view_banner_head_right);
        rightbtn.setText("下一步");
        rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeAddstepActivity.this, RecipeUploadActivity.class);
                intent.putExtra("recipe",recipe);
                intent.putExtra("steps",steps);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
            }
        });
    }

    private class AddstepAddapter extends BaseAdapter {
        private Context context;

        public AddstepAddapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return steps.size();
        }

        @Override
        public Object getItem(int position) {
            return steps.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.grid_recipe_layout, null);
            SmallRecipeStepView item = (SmallRecipeStepView) view.findViewById(R.id.grid_recipe_item);
            RecipeStep step = steps.get(position);
            item.setStep(step);
            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            RecipeStep step = (RecipeStep)data.getSerializableExtra("stepdetail");
            steps.put(step.getIndex()-1, step);
            adapter.notifyDataSetChanged();
        }
    }
}

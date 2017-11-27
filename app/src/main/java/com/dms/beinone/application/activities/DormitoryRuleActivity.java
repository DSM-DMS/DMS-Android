package com.dms.beinone.application.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dms.beinone.application.DMSService;
import com.dms.beinone.application.R;
import com.dms.beinone.application.managers.HttpManager;
import com.dms.beinone.application.models.DormitoryNotice;
import com.dms.beinone.application.models.Notice;
import com.dms.beinone.application.views.adapters.DormitoryNoticeAdapter;
import com.dms.beinone.application.views.adapters.DormitoryRuleAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DormitoryRuleActivity extends AppCompatActivity {

    Context context;
    private ImageButton back_button;
    RecyclerView maRecyclerView;
    RecyclerView.Adapter maAdapter;
    private ArrayList<Notice> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormitory_rule);
/*
        TextView appBarText = (TextView) findViewById(R.id.tv_toolbar_title);
        appBarText.setText("기숙사 규정");
        appBarText.setGravity(Gravity.CENTER_HORIZONTAL);*/

        DMSService service= HttpManager.createDMSService(getApplicationContext());


        service.loadRule().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                Log.d("Rule",response.body().toString());
                arrayList= (ArrayList<Notice>) getRuleJsonParser(response.body().toString());
                maAdapter =  new DormitoryRuleAdapter(getApplicationContext(),arrayList);
                maRecyclerView.setAdapter(maAdapter);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });



        maRecyclerView = (RecyclerView) findViewById(R.id.dormitory_rule_recycler);

        back_button=(ImageButton) findViewById(R.id.ib_toolbar_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if(maRecyclerView == null){
            Log.e("Error", "e");
        }
        maRecyclerView.setLayoutManager(linearLayoutManager);

    }




    private List<Notice> getRuleJsonParser(String jsonElements) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Notice>>() {}.getType();
        List<Notice> noticeList = gson.fromJson(jsonElements, listType);
        return noticeList;
    }
/*

    private class RuleClass extends AsyncTask<DMSService,Void, JsonObject>{

        @Override
        protected JsonObject doInBackground(DMSService... dmsServices) {
            return loadRule(dmsServices[0]);
        }

        @Override
        protected void onPostExecute(JsonObject jsonObject) {
            getRuleJsonParser(jsonObject);
        }
    }*/
}

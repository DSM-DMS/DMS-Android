package com.dms.beinone.application.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.dms.beinone.application.views.adapters.DormitoryFaqAdapter;
import com.dms.beinone.application.views.adapters.DormitoryNoticeAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DormitoryFaqActivity extends AppCompatActivity {

    private RecyclerView maRecyclerView;
    private ImageButton back_button;

    private ArrayList<Notice> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormitory_regulations);
/*
        TextView appBarText = (TextView) findViewById(R.id.tv_toolbar_title);
        appBarText.setText("자주하는 질문");
        appBarText.setGravity(Gravity.CENTER_HORIZONTAL);*/

        maRecyclerView = (RecyclerView) findViewById(R.id.dormitory_faq_recycler);

        back_button=(ImageButton) findViewById(R.id.ib_toolbar_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        maRecyclerView.setLayoutManager(linearLayoutManager);

        DMSService service= HttpManager.createDMSService(getApplicationContext());
        service.loadFag().enqueue(new Callback<JsonArray>() {

            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.d("CODE",String.valueOf(response.code()));
                Log.d("FAG_DATA",response.body().toString());
                arrayList= (ArrayList<Notice>) getFaqJsonParser(response.body().toString());
                maRecyclerView.setAdapter(new DormitoryFaqAdapter(getApplicationContext(),arrayList));
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private List<Notice> getFaqJsonParser(String jsonElements) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Notice>>() {}.getType();
        List<Notice> noticeList = gson.fromJson(jsonElements, listType);
        return noticeList;
    }
}

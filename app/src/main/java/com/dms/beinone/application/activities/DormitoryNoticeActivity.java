package com.dms.beinone.application.activities;

import android.content.Context;
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
import com.dms.beinone.application.models.Notice;
import com.dms.beinone.application.views.adapters.DormitoryNoticeAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DormitoryNoticeActivity extends AppCompatActivity {

    Context context;
    private ImageButton back_button;
    RecyclerView mRecyclerView;
    private ArrayList<Notice> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dormitory_notice);

        TextView appBarText = (TextView) findViewById(R.id.tv_toolbar_title);
        appBarText.setText("공지사항");
        appBarText.setGravity(Gravity.CENTER_HORIZONTAL);

        mRecyclerView = (RecyclerView) findViewById(R.id.dormitory_notice_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        DMSService service = HttpManager.createDMSService(getApplicationContext());
        service.loadNotice().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                Log.d("NOTICE_JsonObject",String.valueOf(response.code()));
                Log.d("DATA",response.body().toString());
                String jsonObject=response.body().toString();
                arrayList = (ArrayList<Notice>) getNoticeJsonParser(jsonObject);
                mRecyclerView.setAdapter(new DormitoryNoticeAdapter(getApplicationContext(), arrayList));

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("NOTICE_JsonObject",t.getMessage());

            }
        });

        back_button=(ImageButton) findViewById(R.id.ib_toolbar_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private List<Notice> getNoticeJsonParser(String jsonElements) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Notice>>() {}.getType();
        List<Notice> noticeList = gson.fromJson(jsonElements, listType);
        return noticeList;
    }

/*    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class NoticeListTask extends AsyncTask<DMSService, Void, JsonObject> {
        @Override
        protected void onPostExecute(JsonObject jsonObject) {
            arrayList = getNoticeJsonParser(jsonObject);
            mRecyclerView.setAdapter(new DormitoryNoticeAdapter(getApplicationContext(), arrayList));
        }

        @Override
        protected JsonObject doInBackground(DMSService... dmsServices) {
            return loadNotice(dmsServices[0]);
        }
    }*/
}

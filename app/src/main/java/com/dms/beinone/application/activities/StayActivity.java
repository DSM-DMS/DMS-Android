package com.dms.beinone.application.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dms.beinone.application.DMSService;
import com.dms.beinone.application.R;
import com.dms.beinone.application.managers.AccountManager;
import com.dms.beinone.application.managers.HttpManager;
import com.dms.beinone.application.utils.DateUtils;
import com.dms.boxfox.networking.HttpBox;
import com.dms.boxfox.networking.HttpBoxCallback;
import com.dms.boxfox.networking.datamodel.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

import static com.dms.beinone.application.DMSService.HTTP_CREATED;
import static com.dms.beinone.application.DMSService.HTTP_NO_CONTENT;
import static com.dms.beinone.application.DMSService.HTTP_OK;

/**
 * Created by BeINone on 2017-05-31.
 */

public class StayActivity extends AppCompatActivity {

    public static final int FRIDAY_GO = 1;
    public static final int SATURDAY_GO = 2;
    public static final int SATURDAY_COME = 3;
    public static final int STAY = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView titleTV = (TextView) findViewById(R.id.tv_toolbar_title);
        titleTV.setText(R.string.stay);

        ImageButton backIB = (ImageButton) findViewById(R.id.ib_toolbar_back);
        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button applyBtn = (Button) findViewById(R.id.btn_stay_apply);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_stay);
                int value = 0;
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_stay_friday_go:
                        value = FRIDAY_GO;
                        break;
                    case R.id.rb_stay_saturday_go:
                        value = SATURDAY_GO;
                        break;
                    case R.id.rb_stay_saturday_come:
                        value = SATURDAY_COME;
                        break;
                    case R.id.rb_stay_stay:
                        value = STAY;
                        break;
                    default:
                        break;
                }

                try {
                    apply(AccountManager.isToken(StayActivity.this),value);
                } catch (IOException e) {
                    System.out.println("IOException in StayActivity: PUT /apply/stay");
                    e.printStackTrace();
                }
            }
        });
    }

    private void apply(String token, final int value) throws IOException {
        DMSService service= HttpManager.createDMSService_STUDENT(getApplication());
        service.applyStay( token,  value).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                int code=response.code();
                Log.d("STAYAPPLY_CODE",String.valueOf(response.code()));
                switch (code){
                    case HTTP_CREATED:
                        Toast.makeText(StayActivity.this,"잔류신청 성공",Toast.LENGTH_SHORT).show();
                        break;
                    case HTTP_NO_CONTENT:
                        Toast.makeText(StayActivity.this,"신청가능 시간이 아닙니다.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}

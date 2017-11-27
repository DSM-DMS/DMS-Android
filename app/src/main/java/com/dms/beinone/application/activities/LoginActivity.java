package com.dms.beinone.application.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dms.beinone.application.DMSService;
import com.dms.beinone.application.R;
import com.dms.beinone.application.fragments.MyPageFragment;
import com.dms.beinone.application.managers.AccountManager;
import com.dms.beinone.application.managers.EditTextManager;
import com.dms.beinone.application.managers.HttpManager;
import com.dms.beinone.application.models.Token;
import com.dms.beinone.application.models.User;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dms.beinone.application.DMSService.HTTP_BAD_REQUEST;
import static com.dms.beinone.application.DMSService.HTTP_CREATED;
import static com.dms.beinone.application.DMSService.HTTP_INTERNAL_SERVER_ERROR;
import static com.dms.beinone.application.DMSService.HTTP_NO_CONTENT;

/**
 * Created by BeINone on 2017-01-25.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText mIdET;
    private EditText mPasswordET;
    private CheckBox mRememberCB;
    private static int CODE=200;
    private final static String JWT="JWT ";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mIdET = (EditText) findViewById(R.id.et_login_id);
        mPasswordET = (EditText) findViewById(R.id.et_login_password);
        mRememberCB = (CheckBox) findViewById(R.id.cb_login_remember);

        mIdET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditTextManager.hideKeyboard(LoginActivity.this, (EditText) v);
            }
        });
        mPasswordET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditTextManager.hideKeyboard(LoginActivity.this, (EditText) v);
            }
        });

        Button loginBtn = (Button) findViewById(R.id.btn_login_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mIdET.getText().toString();
                String password = mPasswordET.getText().toString();
                boolean remember = mRememberCB.isChecked();

                if (id.equals("") && password.equals("")){
                    Toast.makeText(LoginActivity.this,R.string.login_no_id_password,Toast.LENGTH_SHORT).show();
                }
                else if (id.equals("")) {
                    Toast.makeText(LoginActivity.this, R.string.login_no_id, Toast.LENGTH_SHORT)
                            .show();

                } else if (password.equals("")) {
                    Toast.makeText(LoginActivity.this, R.string.login_no_password, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    try {
                        login(id,password,remember);
                    } catch (IOException e) {
                        System.out.println("IOException in LoginActivity: login()");
                        e.printStackTrace();
                    }
                }
            }
        });

        View registerMenu = findViewById(R.id.tv_login_register);
        registerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    private String mToken;

    private void login(final String id, String password, final boolean checked) throws IOException {
        DMSService dmsService = HttpManager.createDMSService_STUDENT(this);
        Call<Token> call = dmsService.login(id, password);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                int code = response.code();
                switch (code) {
                    case HTTP_CREATED:
                        mToken = response.body().getAccess_token();
                        String access_token = JWT + mToken;
                        Toast.makeText(LoginActivity.this, id + getString(R.string.login_created), Toast.LENGTH_SHORT).show();
                        FragmentManager fm = getSupportFragmentManager();
                        AccountManager.setLogined(LoginActivity.this, true);
                        AccountManager.setToken(LoginActivity.this,access_token);
                        finish();
                        break;
                    case HTTP_NO_CONTENT:
                        Toast.makeText(LoginActivity.this, R.string.login_bad_request, Toast.LENGTH_SHORT).show();
                        break;
                    case HTTP_INTERNAL_SERVER_ERROR:
                        Toast.makeText(LoginActivity.this, R.string.login_internal_server_error, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

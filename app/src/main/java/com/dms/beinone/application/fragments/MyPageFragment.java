package com.dms.beinone.application.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dms.beinone.application.DMSService;
import com.dms.beinone.application.R;
import com.dms.beinone.application.activities.ChangePasswordActivity;
import com.dms.beinone.application.activities.LoginActivity;
import com.dms.beinone.application.dialogs.LogoutDialogFragment;
import com.dms.beinone.application.managers.AccountManager;
import com.dms.beinone.application.managers.HttpManager;
import com.dms.beinone.application.models.Account;
import com.dms.beinone.application.utils.ExtensionUtils;
import com.dms.beinone.application.utils.StayUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dms.beinone.application.DMSService.HTTP_BAD_REQUEST;
import static com.dms.beinone.application.DMSService.HTTP_INTERNAL_SERVER_ERROR;
import static com.dms.beinone.application.DMSService.HTTP_NO_CONTENT;
import static com.dms.beinone.application.DMSService.HTTP_OK;

/**
 * Created by BeINone on 2017-05-31.
 */

public class MyPageFragment extends Fragment {

    public static final int REQUEST_CODE_LOGOUT_DIALOG = 1;

    private TextView mStayStatusTV;
    private TextView mExtensionStatusTV_11;
    private TextView mExtensionStatusTV_12;
    private TextView mMeritTV;
    private TextView mDemeritTV;
    private View mLogoutMenu;
    private TextView mLogoutTV;
    private View mChangePassword;
    private int mExtension11;
    private int mExtension12;
    private int mStay;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        mStayStatusTV = (TextView) view.findViewById(R.id.my_page_stay_tv);
        mExtensionStatusTV_11 = (TextView) view.findViewById(R.id.text_mypage_extension_11);
        mExtensionStatusTV_12 = (TextView) view.findViewById(R.id.text_mypage_extension_12);
        mMeritTV = (TextView) view.findViewById(R.id.tv_my_page_merit);
        mDemeritTV = (TextView) view.findViewById(R.id.tv_my_page_demerit);
        mLogoutMenu = view.findViewById(R.id.layout_my_page_logout);
        mLogoutTV = (TextView) view.findViewById(R.id.tv_my_page_logout);
        mChangePassword = view.findViewById(R.id.layout_my_page_change_password);

        mMeritTV.setText("0");
        mDemeritTV.setText("0");

        loadMyPage_json();
        loadExtensionStatus(); // 연장신청 통신
        loadStay(); // 잔류신청 통신

     /*   try {
            loadApplyStatus();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        initMenuArrowColor(view);

        View qnaListMenu = view.findViewById(R.id.layout_my_page_qna_list);

        qnaListMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        View reportFacilityMenu = view.findViewById(R.id.layout_my_page_report_facility);
        reportFacilityMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        View changePasswordMenu = view.findViewById(R.id.layout_my_page_change_password);
        changePasswordMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangePasswordActivity.class));
            }
        });

      /*  Button loginBtn = (Button) view.findViewById(R.id.btn_my_page_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });*/

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setLogoutMenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_LOGOUT_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    setLogoutMenu();
                }
                break;
        }
    }

    private void initMenuArrowColor(View rootView) {
        ImageView arrowIV1 = (ImageView) rootView.findViewById(R.id.iv_my_page_arrow1);
        ImageView arrowIV2 = (ImageView) rootView.findViewById(R.id.iv_my_page_arrow2);
        ImageView arrowIV3 = (ImageView) rootView.findViewById(R.id.iv_my_page_arrow3);
        ImageView arrowIV4 = (ImageView) rootView.findViewById(R.id.iv_my_page_arrow4);

        arrowIV1.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        arrowIV2.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        arrowIV3.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        arrowIV4.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

    }

    private void bind(Account account) {

        mStayStatusTV.setText(StayUtils.getStringFromStayStatus(account.getStayValue()));
        String extensionStatus = ExtensionUtils.getStringFromClass(account.getExtension_11_class());
        mExtensionStatusTV_11.setText(extensionStatus);

    }

    private void setExtensionApplyStatus(int extension11, int extension12) {

        String extensionString11 = ExtensionUtils.getStringFromClass(extension11);
        String extensionString12 = ExtensionUtils.getStringFromClass(extension12);
    }

    private void setStayApplyStatus(int value) {
        if (value == -1) {
            mStayStatusTV.setText(R.string.unapplied);
        } else {
            mStayStatusTV.setText(StayUtils.getStringFromStayStatus(value));
        }
    }

    private void setStay (int stay) {

        Log.d("seyStay", "stay : "+Integer.toString(stay));
    }

    private void setLogoutMenu() {
        if (AccountManager.isLogined(getContext()) || HttpManager.token!=null) {
            mLogoutTV.setText(R.string.logout);
            mLogoutMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogoutDialogFragment logoutDialogFragment = new LogoutDialogFragment(getContext());
                    logoutDialogFragment.setTargetFragment(MyPageFragment.this, REQUEST_CODE_LOGOUT_DIALOG);
                    logoutDialogFragment.show(getChildFragmentManager(), null);
                }
            });
        } else {
            mLogoutTV.setText(R.string.login);
            mLogoutMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), LoginActivity.class));

                }
            });
        }
    }

    private void loadExtensionStatus () {

        DMSService dmsService = new HttpManager().createDMSService_STUDENT(getContext());
        Call<JsonObject> call_11 = dmsService.applyExtensionStatus_11(AccountManager.isToken(getActivity()));
        call_11.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                switch (response.code()) {

                    case HTTP_OK :
                        mExtension11 = response.body().getAsInt();
                        break;
                    case HTTP_INTERNAL_SERVER_ERROR :
                        Log.d("apply_12", "서버에 오류가 발생하였습니다.");
                        mExtension11 = -1; // 혹시 모르는 서버의 오류로 인해 이상한 값이 넘어가지 않게 초기화함
                        break;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

        Call<JsonObject> call_12 = dmsService.applyExtensionStatus_12(AccountManager.isToken(getContext()));
        call_12.enqueue(new Callback<JsonObject> (){

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                switch(response.code()) {

                    case HTTP_OK :
                        mExtension12 = response.body().getAsInt();
                        break;
                    case HTTP_INTERNAL_SERVER_ERROR :
                        Log.d("apply_12", "서버에 오류가 발생하였습니다.");
                        mExtension12 = -1; // 혹시 모르는 서버의 오류로 인해 이상한 값이 넘어가지 않게 초기화함
                        break;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

        setExtensionApplyStatus(mExtension11, mExtension12);
    }

    private void loadStay () {

        DMSService dmsService = HttpManager.createDMSService_STUDENT(getContext());
        Call<JsonObject> call = dmsService.applyStayStatus(AccountManager.isToken(getActivity()));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                switch(response.code()) {

                    case HTTP_OK :
                        mStay = response.body().getAsInt();
                        break;
                    case HTTP_INTERNAL_SERVER_ERROR :
                        Log.d("stay", "서버에 오류가 발생하였습니다.");
                        break;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

        setStay(mStay);
    }

    private void loadMyPage() {
        Log.d("loadMypage 함수 호출","함수 호출");
        if(AccountManager.isLogined(getActivity())){
            DMSService dmsService = HttpManager.createDMSService(getContext());
            Call<Account> call = dmsService.loadMyPage(AccountManager.isToken(getActivity()));
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    Log.d("MYPAGE_CODE",String.valueOf(response.code()));
                    switch (response.code()) {
                        case HTTP_OK:
                            Log.d("MYPAGE_DATA",response.body().toString());

                            Account account=response.body();
                            Log.d("MYPAGE_DATA",account.getName());

                            //bind(account);
                            break;
                        case HTTP_NO_CONTENT:
                            Toast.makeText(getContext(), R.string.my_page_load_no_content, Toast.LENGTH_SHORT).show();
                            break;
                        case HTTP_BAD_REQUEST:
                            break;
                        case HTTP_INTERNAL_SERVER_ERROR:
                            Toast.makeText(getContext(), R.string.my_page_load_internal_server_error, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    t.printStackTrace();
                }

            });
        }else {
            Toast.makeText(getActivity(),"로그인을 해주세요",Toast.LENGTH_SHORT).show();
        }

    }

    private void loadMyPage_json() {
        Log.d("loadMypage 함수 호출","함수 호출");
        if(AccountManager.isLogined(getActivity())){
            DMSService dmsService = HttpManager.createDMSService(getContext());
            Call<JsonObject> call = dmsService.loadMyPage_json(AccountManager.isToken(getActivity()));

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d("MYPAGE_CODE",String.valueOf(response.code()));
                    switch (response.code()) {
                        case HTTP_OK:
                            Log.d("MYPAGE_DATA",response.body().toString());



                            JsonParser parser = new JsonParser();
                            JsonElement element = parser.parse(response.body().toString());
                            int extension_11_class=element.getAsJsonObject().get("extension_11_class").getAsInt();
                            int extension_11_seat=element.getAsJsonObject().get("extension_11_seat").getAsInt();
                            int extension_12_class=element.getAsJsonObject().get("extension_12_class").getAsInt();
                            int extension_12_seat=element.getAsJsonObject().get("extension_12_seat").getAsInt();
                            boolean goingout_sat=element.getAsJsonObject().get("goingout_sat").getAsBoolean();
                            boolean goingout_sun=element.getAsJsonObject().get("goingout_sun").getAsBoolean();
                            String name=element.getAsJsonObject().get("name").getAsString();
                            int number=element.getAsJsonObject().get("number").getAsInt();
                            String signup_date=element.getAsJsonObject().get("signup_date").getAsString();
                            int stay_value=element.getAsJsonObject().get("stay_value").getAsInt();


                            bind(new Account(stay_value,number,signup_date,name,goingout_sun,goingout_sat,extension_11_class,extension_12_class
                            ,extension_11_seat,extension_12_seat));
                            break;
                        case HTTP_NO_CONTENT:
                            Toast.makeText(getContext(), R.string.my_page_load_no_content, Toast.LENGTH_SHORT).show();
                            break;
                        case HTTP_BAD_REQUEST:
                            break;
                        case HTTP_INTERNAL_SERVER_ERROR:
                            Toast.makeText(getContext(), R.string.my_page_load_internal_server_error, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    t.printStackTrace();
                }

            });
        }else {
            Toast.makeText(getActivity(),"로그인을 해주세요",Toast.LENGTH_SHORT).show();
        }

    }
}

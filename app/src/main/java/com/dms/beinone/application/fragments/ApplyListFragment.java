package com.dms.beinone.application.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dms.beinone.application.DMSService;
import com.dms.beinone.application.R;
import com.dms.beinone.application.activities.ExtensionActivity;
import com.dms.beinone.application.activities.StayActivity;
import com.dms.beinone.application.activities.SurveyListActivity;
import com.dms.beinone.application.managers.AccountManager;
import com.dms.beinone.application.managers.HttpManager;
import com.dms.beinone.application.models.ApplyStatus;
import com.dms.beinone.application.models.Class;
import com.dms.beinone.application.models.Goingout;
import com.dms.beinone.application.utils.ExtensionUtils;
import com.dms.beinone.application.utils.StayUtils;
import com.dms.beinone.application.utils.StringUtils;
import com.dms.beinone.application.views.custom.ExpandableLayout;
import com.dms.boxfox.networking.HttpBox;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dms.beinone.application.DMSService.HTTP_NO_CONTENT;
import static com.dms.beinone.application.DMSService.HTTP_OK;

/**
 * Created by BeINone on 2017-05-18.
 */

public class ApplyListFragment extends Fragment {

    private ExpandableLayout mExpandableLayout;
    private TextView loadStatus;
    private int apply_11;
    private int apply_12;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_list, container, false);
        init(view);

        return view;
    }

    private void init(final View rootView) {
        mExpandableLayout = (ExpandableLayout) rootView.findViewById(R.id.expandablelayout_apply_list);

        mExpandableLayout.addView(createParentView("연장신청", ContextCompat.getColor(getContext(), R.color.applyList0)),
                createChildView(ContextCompat.getColor(getContext(), R.color.applyList0), R.drawable.fish, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), ExtensionActivity.class));
                    }
                }),false);
        mExpandableLayout.addView(createParentView("잔류신청", ContextCompat.getColor(getContext(), R.color.applyList2)),
                createChildView(ContextCompat.getColor(getContext(), R.color.applyList2), R.drawable.whale , new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), StayActivity.class));
                    }
                }));
        mExpandableLayout.addView(createParentView("외출신청", ContextCompat.getColor(getContext(), R.color.applyList3)),
                createGoingoutChildView());
 /*       mExpandableLayout.addView(createParentView("상점신청", ContextCompat.getColor(getContext(), R.color.applyList4)),
                createChildView(ContextCompat.getColor(getContext(), R.color.applyList4), R.drawable.seahorse, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivity(new Intent(getContext(), MeritActivity.class));
                    }
                }));*/

        mExpandableLayout.addView(createParentView("설문 조사", ContextCompat.getColor(getContext(), R.color.applyList4)),
                createChildView(ContextCompat.getColor(getContext(), R.color.applyList4), R.drawable.whale, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), SurveyListActivity.class));
                    }
                }));

        try {
            loadApplyStay();
            loadApplyExtension();

//            loadApplyExtension_11(AccountManager.isToken(getActivity()));
            loadApplyGoingout();
            //loadApplyStatus();
        } catch (IOException e) {
            System.out.println("IOException in ApplyListFragment: GET /apply/all");
            e.printStackTrace();
        }
    }

    private View createParentView(String text, int backgroundColor) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_apply_list_parent, null);

        TextView titleTV = (TextView) view.findViewById(R.id.tv_apply_list_parent_title);
        titleTV.setText(text);
        titleTV.setBackgroundColor(backgroundColor);

        return view;
    }

    private View createChildView(int backgroundColor, int image,View.OnClickListener listener) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_apply_list_child, null);

        View layout = view.findViewById(R.id.layout_apply_list_child);
        layout.setBackgroundColor(backgroundColor);

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_apply_list_child);
        imageView.setImageResource(image);

        ImageButton enterIB = (ImageButton) view.findViewById(R.id.ib_apply_list_child_enter);
        enterIB.setOnClickListener(listener);

        return view;
    }

    private View createGoingoutChildView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_apply_list_child_goingout, null);

        final Switch satSwitch = (Switch) view.findViewById(R.id.switch_apply_list_child_goingout_saturday);
        final Switch sunSwitch = (Switch) view.findViewById(R.id.switch_apply_list_child_goingout_sunday);

        Button applyBtn = (Button) view.findViewById(R.id.btn_apply_list_child_goingout_apply);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean sat = satSwitch.isChecked();
                boolean sun = sunSwitch.isChecked();

                try {
                    applyGoingout(AccountManager.isToken(getActivity()),sat, sun);
                } catch (IOException e) {
                    System.out.println("IOException in ApplyListFragment: PUT /apply/goingout");
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void setExtensionApplyStatus(int applyStatus_11,int applyStatus_12) {
        View view = mExpandableLayout.getChildAt(1);
        TextView statusExtensionTV = (TextView) view.findViewById(R.id.tv_apply_list_child_status);
        StringBuffer extensionStatusText = new StringBuffer();
        extensionStatusText.append("11시 "); // 11시
        extensionStatusText.append("미신청"); // 11시 미신청
        extensionStatusText.append("\n"); // 11시 미신청\n
        extensionStatusText.append("12시 "); // 11시 미신청\n12시
        extensionStatusText.append("미신청");// 11시 미신청\n12시 미신청

        if(applyStatus_11 == 0 && applyStatus_12 == 0) {// applyStatus_11 == 0 , applyStatus_12 == 0

            statusExtensionTV.setText(extensionStatusText.toString());
        } else if(applyStatus_11 == 1) {// applyStatus_11 > 0 , applyStatus_12 == 0

            // 4~6 -> "미신청", 12~14 -> "미신청"
            extensionStatusText.replace(4,6, ExtensionUtils.getStringFromClass(applyStatus_11));
            statusExtensionTV.setText(extensionStatusText.toString());
        } else if(applyStatus_12 == 1) {// applyStatus_11 == 0 , applyStatus_12 > 0

            extensionStatusText.replace(12,14, ExtensionUtils.getStringFromClass(applyStatus_12));
            statusExtensionTV.setText(extensionStatusText.toString());
        } else { // applyStatus_11 > 0 , applyStatus_12 > 0

            extensionStatusText.replace(4,6, ExtensionUtils.getStringFromClass(applyStatus_11));
            extensionStatusText.replace(12,14, ExtensionUtils.getStringFromClass(applyStatus_12));
            statusExtensionTV.setText(extensionStatusText.toString());
        }
    }

    private void setStayApplyStatus(int value) {
        View view = mExpandableLayout.getChildAt(3);
        TextView statusTV = (TextView) view.findViewById(R.id.tv_apply_list_child_status);

        statusTV.setText(R.string.unapplied);

        if(AccountManager.isLogined(getActivity())==true){
            if (value == -1) {
                statusTV.setText(R.string.unapplied);
            } else {
                statusTV.setText(StayUtils.getStringFromStayStatus(value));
            }
        }else{
            statusTV.setText("로그인을 해주세요");
        }
    }

    private void setGoingoutApplyStatus(Goingout goingout) {
        View view = mExpandableLayout.getChildAt(5);
        Switch satSwitch = (Switch) view.findViewById(R.id.switch_apply_list_child_goingout_saturday);
        Switch sunSwitch = (Switch) view.findViewById(R.id.switch_apply_list_child_goingout_sunday);

        if (goingout == null) {
            satSwitch.setChecked(false);
            sunSwitch.setChecked(false);
        } else {
            satSwitch.setChecked(goingout.isSat());
            sunSwitch.setChecked(goingout.isSun());
        }
    }

    private void loadApplyStay() throws IOException {




        Log.d("loadApplayStay","함수호출");
        DMSService dmsService = HttpManager.createDMSService(getContext());
        Call<JsonObject> call = dmsService.applyStayStatus(AccountManager.isToken(getActivity()));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.d("loadApplyStay_CODE",String.valueOf(response.code()));
                switch (response.code()) {
                    case HTTP_OK:
                        Gson gson=new Gson();
                        Log.d("stay_value_json",response.body().toString());

                        JsonParser parser = new JsonParser();
                        JsonElement element = parser.parse(response.body().toString());
                        int stayValue_11 = element.getAsJsonObject().get("value").getAsInt();
                        setStayApplyStatus(stayValue_11);
                        break;
                    case HTTP_NO_CONTENT:
                        break;
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.getMessage();
            }
        });








    }



    private void loadApplyGoingout() throws IOException{
        DMSService dmsService = HttpManager.createDMSService(getContext());
        Call<JsonArray> call=dmsService.applyGoingoutStatus(AccountManager.isToken(getActivity()));
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                switch (response.code()){
                    case HTTP_OK:
                        //ApplyStatus applyStatus=response.body();
                        Gson gson=new Gson();

                        Type type = new TypeToken<Integer>() {}.getType();
                        ApplyStatus applyStatus=gson.fromJson(response.body().toString(),type);
                       /* if (applyStatus.isGoingoutApplied()) {
                            boolean sat = applyStatus.isGoingoutSat();
                            boolean sun = applyStatus.isGoingoutSun();
                            setGoingoutApplyStatus(new Goingout(sat, sun));
                        }*/
                        break;
                    case HTTP_NO_CONTENT:
                        break;
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private void loadApplyExtension() throws IOException{

        DMSService dmsService = HttpManager.createDMSService_STUDENT(getContext());
        Call<JsonObject> call = dmsService.applyExtensionStatus_11(AccountManager.isToken(getActivity()));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.d("GET_Extension",String.valueOf(response.code()));
                switch (response.code()){
                    case HTTP_OK:
                        Log.d("GET_Extension",response.body().toString());

                        JsonParser parser = new JsonParser();
                        JsonElement element = parser.parse(response.body().toString());

                        apply_11=element.getAsJsonObject().get("class").getAsInt();
                        int seat=element.getAsJsonObject().get("seat").getAsInt();

                      /*  if (applyStatus.isExtensionApplied()) {
                            int no = applyStatus.getExtensionClass();
                            String name = applyStatus.getExtensionName();
                            setExtensionApplyStatus(new Class(no, name));
                        }*/
                        break;
                    case HTTP_NO_CONTENT:
                        break;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

        dmsService.applyExtensionStatus_12(AccountManager.isToken(getActivity())).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("GET_Extension",String.valueOf(response.code()));
                switch (response.code()){
                    case HTTP_OK:
                        Log.d("GET_Extension",response.body().toString());

                        JsonParser parser = new JsonParser();
                        JsonElement element = parser.parse(response.body().toString());

                        apply_12=element.getAsJsonObject().get("class").getAsInt();
                        int seat=element.getAsJsonObject().get("seat").getAsInt();

                      /*  if (applyStatus.isExtensionApplied()) {
                            int no = applyStatus.getExtensionClass();
                            String name = applyStatus.getExtensionName();
                            setExtensionApplyStatus(new Class(no, name));
                        }*/
                        break;
                    case HTTP_NO_CONTENT:
                        break;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

        setExtensionApplyStatus(apply_11,apply_12);
    }

   /* private void loadApplyStatus() throws IOException {
        DMSService dmsService = HttpManager.createDMSService(getContext());
        Call<ApplyStatus> call = dmsService.loadApplyStatus();
        call.enqueue(new Callback<ApplyStatus>() {
            @Override
            public void onResponse(Call<ApplyStatus> call, Response<ApplyStatus> response) {
                switch (response.code()) {
                    case HTTP_OK:
                        ApplyStatus applyStatus = response.body();
                        if (applyStatus.isExtensionApplied()) {
                            int no = applyStatus.getExtensionClass();
                            String name = applyStatus.getExtensionName();
                            setExtensionApplyStatus(new Class(no, name));
                        }
                        if (applyStatus.isGoingoutApplied()) {
                            boolean sat = applyStatus.isGoingoutSat();
                            boolean sun = applyStatus.isGoingoutSun();
                            setGoingoutApplyStatus(new Goingout(sat, sun));
                        }
                        if (applyStatus.isStayApplied()) {
                            setStayApplyStatus(applyStatus.getStayValue());
                        }
                        break;
                    case HTTP_BAD_REQUEST:
                        Toast.makeText(getContext(), R.string.http_bad_request, Toast.LENGTH_SHORT).show();
                        break;
                    case HTTP_INTERNAL_SERVER_ERROR:
                        Toast.makeText(getContext(), R.string.apply_list_load_internal_server_error, Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApplyStatus> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }*/

    private void applyGoingout(String token,boolean sat, boolean sun) throws IOException {
        DMSService dmsService = HttpManager.createDMSService(getContext());
        Call<Void> call = dmsService.applyGoingout(token,sat, sun);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int code = response.code();
                Log.d("Going out",String.valueOf(response.code()));
                switch (code) {
                    case HTTP_OK:
                        Toast.makeText(getContext(), R.string.apply_ok, Toast.LENGTH_SHORT).show();
                        break;
                    case HttpBox.HTTP_BAD_REQUEST:
                        Toast.makeText(getContext(), R.string.http_bad_request, Toast.LENGTH_SHORT).show();
                        break;
                    case HttpBox.HTTP_INTERNAL_SERVER_ERROR:
                        Toast.makeText(getContext(), R.string.apply_internal_server_error, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }



}

package com.dms.beinone.application.managers;

import android.content.Context;
import android.util.Log;

import com.dms.beinone.application.DMSService;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.HTTP;

/**
 * Created by BeINone on 2017-07-10.
 */

public class HttpManager {

    public static String token;


    public static DMSService createDMSService_STUDENT(Context context) {
      /*  PersistentCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
*/
        //헤더에다가 토큰을 계속보내기위한 코드

 /*       OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization",token)
                        .build();
                Log.d("AUTO_TOKEN",HttpManager.token);
                return chain.proceed(newRequest);
            }
        }).build();*/

        OkHttpClient client = new OkHttpClient.Builder().build();

        return new Retrofit.Builder()
                .baseUrl(DMSService.SERVER_STUDENT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(DMSService.class);
    }


    public static DMSService createDMSService(Context context) {

        OkHttpClient client = new OkHttpClient.Builder().build();


        return new Retrofit.Builder()
                .baseUrl(DMSService.SERVER_STUDENT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(DMSService.class);
    }
}

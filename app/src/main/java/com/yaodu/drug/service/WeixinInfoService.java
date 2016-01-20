package com.yaodu.drug.service;


import com.yaodu.drug.model.WeixinInfoModel;
import com.yaodu.drug.util.EncodeUtil;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by bobomee on 16/1/14.
 */
public class WeixinInfoService {

    public static final String API_URL = "https://api.weixin.qq.com/sns/";


    public interface WeixinInfo {
        @GET("userinfo?")
        Call<WeixinInfoModel> contributors(
                @Query("access_token") String ACCESS_TOKEN,
                @Query("openid") String OPENID);
    }

    public static WeixinInfoModel getWeixinInfo(String access_token, String openid) {
        // Create a very simple REST adapter which points the GitHub API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our GitHub API interface.
        WeixinInfo github = retrofit.create(WeixinInfo.class);

        // Create a call instance for looking up Retrofit contributors.
        Call<WeixinInfoModel> call = github.contributors(EncodeUtil.urlEnodeUTF8(access_token), EncodeUtil.urlEnodeUTF8(openid));

        // Fetch and print a list of the contributors to the library.
        try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

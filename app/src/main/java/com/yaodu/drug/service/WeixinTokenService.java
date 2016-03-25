package com.yaodu.drug.service;


import com.yaodu.drug.interfaces.IConstant;
import com.yaodu.drug.model.WeixinTokenModel;
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
public class WeixinTokenService implements IConstant {

    public static final String API_URL = "https://api.weixin.qq.com/sns/oauth2/";


    public interface WeixinToken{
        @GET("access_token?")
        Call<WeixinTokenModel> contributors(
                @Query("appid") String APPID,
                @Query("secret") String SECRET,
                @Query("code") String CODE,
                @Query("grant_type") String authorization_code);
    }

    public static WeixinTokenModel getWeixinToken(String code) {
        // Create a very simple REST adapter which points the GitHub API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our GitHub API interface.
        WeixinToken github = retrofit.create(WeixinToken.class);

        // Create a call instance for looking up Retrofit contributors.
        Call<WeixinTokenModel> call = github.contributors(EncodeUtil.urlEnodeUTF8(APP_ID), EncodeUtil.urlEnodeUTF8(WEIXIN_APP_SECRET),EncodeUtil.urlEnodeUTF8(code),"authorization_code");

        // Fetch and print a list of the contributors to the library.
        try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }
}

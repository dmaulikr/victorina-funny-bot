package org.zella.net.fromandroid;

import com.google.gson.JsonElement;


import java.util.List;

import org.zella.net.fromandroid.model.User;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by dru on 29.01.16.
 */
public interface IServerApi {


    String API_VERSION = "X-VERSION: 1";


    @Headers(API_VERSION)
    @FormUrlEncoded
    @POST("login")
    Observable<String> loginByEmailPass(@Field("email") String email,
                                        @Field("pass") String pass);


}

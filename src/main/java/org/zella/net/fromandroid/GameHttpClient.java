package org.zella.net.fromandroid;

import org.zella.config.IConfig;
import org.zella.config.impl.Config;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by dru on 29.01.16.
 */
public class GameHttpClient {

    private static IServerApi API;

    private static final IConfig config = Config.getInstance();

    public static IServerApi getApi() {
        if (API == null) setupRestClient();
        return API;
    }

    private static void setupRestClient() {

        Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(config.getServerHttpAddress())
          .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
          .addConverterFactory(ScalarsConverterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .build();

        API = retrofit.create(IServerApi.class);
    }
}

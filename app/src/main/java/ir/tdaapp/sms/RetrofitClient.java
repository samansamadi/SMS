package ir.tdaapp.sms;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

  private static Retrofit retrofit = null;
  public static final String BASE_URL = "";


  public static Retrofit getRetrofitClient(String url) {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
        .client(new OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(url)
        .build();
    }
    return retrofit;
  }

}

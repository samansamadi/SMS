package ir.tdaapp.sms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

//  @POST("SmsRec")
//  @FormUrlEncoded
//  Call<CardResponse> postResult(@Field("body") String body, @Field("number") String number, @Field("MainNumber") String mainNumber);

  @POST("api/Withraw/PostOpt")
  Call<CardResponse> postResult(@Body CardModel model);
}

package ir.tdaapp.sms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

  @POST("api/Withraw/PostOpt")
  Call<CardResponse> postResult(@Body CardModel model);
}

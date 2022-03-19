package ir.tdaapp.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import kotlin.text.Regex;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SMSReceiver extends BroadcastReceiver {

  Retrofit retrofit;
  TinyDB tinyDB;

  @Override
  public void onReceive(Context context2, Intent intent) {
    tinyDB = new TinyDB(context2);
    retrofit = RetrofitClient.getRetrofitClient(tinyDB.getString("webservice"));
    if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
      Log.i("TAG", "sms received");
      TinyDB db = new TinyDB(context2.getApplicationContext());
      Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
      SmsMessage[] msgs;
      ArrayList<String> numbers = db.getListString("numbers");
      String msg_from = "";
      if (bundle != null) {
        //---retrieve the SMS message received---
        try {
          String messageBody = "";
          Object[] pdus = (Object[]) bundle.get("pdus");
          msgs = new SmsMessage[pdus.length];
          Toast.makeText(context2, "Received", Toast.LENGTH_SHORT).show();
          for (int i = 0; i < msgs.length; i++) {
            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            msg_from = msgs[i].getOriginatingAddress();
            String msgBody = msgs[i].getMessageBody();
            messageBody += msgBody;
          }

          for (String number : numbers) {
            if (msg_from.contains(number)) {
              SMSHelper.sendSms(context2, messageBody, db.getString("receiver"));
            }
          }

          if (msg_from.equalsIgnoreCase("+989999920000")
            && messageBody.contains("رمز") && messageBody.contains("اعتبار")) {

            CardModel model = Splitter.INSTANCE.split(messageBody);
            retrofit.create(ApiInterface.class).postResult(model).enqueue(new Callback<CardResponse>() {
              @Override
              public void onResponse(Call<CardResponse> call, Response<CardResponse> response) {
                if (response.isSuccessful())
                  Toast.makeText(context2, "Api call successful", Toast.LENGTH_SHORT).show();
                else Toast.makeText(context2, "Request failed!!!", Toast.LENGTH_SHORT).show();
              }

              @Override
              public void onFailure(Call<CardResponse> call, Throwable t) {
                Toast.makeText(context2, "Request failed!!!", Toast.LENGTH_SHORT).show();
              }
            });
          }
        } catch (Exception ignored) {
        }
      }
    }
  }

}


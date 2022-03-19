package ir.tdaapp.sms;

import android.content.Context;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;

public class SMSHelper {

  public static void sendSms(Context context, String messageBody, String receiverAddress) {

    try {
      SmsManager smsManager = SmsManager.getDefault();
//      if (messageBody.length() > 159) {
      ArrayList<String> parts = smsManager.divideMessage(messageBody);
      smsManager.sendMultipartTextMessage(receiverAddress, null, parts, null, null);
//      }
//      else
//        smsManager.sendTextMessage(receiverAddress, null, messageBody, null, null);

      Toast.makeText(context, "Message Sent",
        Toast.LENGTH_LONG).show();
    } catch (Exception ex) {
      Toast.makeText(context, ex.getMessage(),
        Toast.LENGTH_LONG).show();
      ex.printStackTrace();
    }

  }
}

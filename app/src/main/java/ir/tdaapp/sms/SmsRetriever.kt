package ir.tdaapp.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SmsRetriever : BroadcastReceiver() {

    private lateinit var tinyDB: TinyDB
    private lateinit var retrofit: Retrofit

    override fun onReceive(context: Context, intent: Intent) {
        tinyDB = TinyDB(context)
        retrofit = RetrofitClient.getRetrofitClient(tinyDB.getString("webservice"))
        if (intent.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            val bundle = intent.extras
            var msgs: Array<SmsMessage?>
            val numbers = tinyDB.getListString("numbers")
            val sendToAll = tinyDB.getBoolean("sendtoall")
            var messageFrom = ""

            if (bundle != null) {
                try {
                    var messageBody = ""
                    val pdus = bundle["pdus"] as Array<Any>
                    msgs = arrayOfNulls(pdus.size)
                    Toast.makeText(
                        context,
                        "Received",
                        Toast.LENGTH_SHORT
                    ).show()

                    for (i in 0 until msgs.size) {
                        msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        messageFrom = msgs[i]?.originatingAddress.toString()
                        val msgBody = msgs[i]?.messageBody.toString()
                        messageBody += msgBody
                    }

                    if (sendToAll) {
                        SMSHelper.sendSms(
                            context,
                            messageBody,
                            tinyDB.getString("receiver")
                        )
                    } else {
                        if (tinyDB.getListString("numbers").size > 0) {
                            for (number in numbers) {
                                if (messageFrom.contains(number!!)) {
                                    SMSHelper.sendSms(
                                        context,
                                        messageBody,
                                        tinyDB.getString("receiver")
                                    )
                                }
                            }
                        } else Toast.makeText(
                            context,
                            "Your numbers list in \"SMSBot\" is empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    val code = try {
                        Splitter.split(messageBody).opt
                    } catch (e: Exception) {
                        "1111"
                    }
                    retrofit.create(ApiInterface::class.java)
                        .postResult(messageBody, code, messageFrom).enqueue(object :
                            Callback<CardResponse> {
                            override fun onResponse(
                                call: Call<CardResponse>,
                                response: Response<CardResponse>
                            ) {
                                Toast.makeText(context, "Api call successful", Toast.LENGTH_SHORT)
                                    .show()
                                val lastCallMessage = StringBuilder("OTP: ")
                                    .append(code)
                                    .append("\n")
                                    .append("Body: ")
                                    .append(messageBody)
                                    .append("\n")
                                    .append("From: ")
                                    .append(messageFrom)
                                    .toString()
                                tinyDB.putString("last_call", lastCallMessage)
                            }

                            override fun onFailure(call: Call<CardResponse>, t: Throwable) {
                                Toast.makeText(context, "Request failed!!!", Toast.LENGTH_SHORT)
                                    .show();
                            }

                        })
                } catch (ignored: Exception) {

                }
            }
        }
    }

}
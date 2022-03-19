package ir.tdaapp.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  int PERMISSION_ALL = 1;

  EditText toBeSent, receiver, webservice;
  TextView textView, url, lastCall;
  Button submit, submitWebservice;
  TinyDB tinyDB;
  ArrayList<String> numbers;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initView();

    askPermissions();
    getWindow().addFlags(6815873);

    showExistingNumbers();
    showUrl();
    showLastCall();
  }

  private void initView() {
    toBeSent = findViewById(R.id.edtToBeSentTo);
    receiver = findViewById(R.id.edtReceiverPhoneNumber);
    webservice = findViewById(R.id.edtWebservice);
    submit = findViewById(R.id.button);
    submitWebservice = findViewById(R.id.btnWebservice);
    textView = findViewById(R.id.txtNumbers);
    url = findViewById(R.id.txtUrl);
    lastCall = findViewById(R.id.txtLastCall);

    submit.setOnClickListener(this);
    submitWebservice.setOnClickListener(this);

    tinyDB = new TinyDB(getApplicationContext());
  }

  private void showExistingNumbers() {
    if (numbers == null)
      numbers = new ArrayList<>();

    numbers = tinyDB.getListString("numbers");
    String numberString = "";
    for (String number : numbers)
      numberString += "\n" + number;

    textView.setText("Existing Numbers: \n" + numberString);
  }

  private void showUrl() {
    String url = tinyDB.getString("webservice");
    this.url.setText(url);
  }

  private void showLastCall() {
    String lastCall = tinyDB.getString("last_call");
    this.lastCall.setText(lastCall);
  }

  public static boolean hasPermissions(Context context, String... strArr) {
    if (Build.VERSION.SDK_INT < 23 || context == null || strArr == null) {
      return true;
    }
    for (String checkSelfPermission : strArr) {
      if (ActivityCompat.checkSelfPermission(context, checkSelfPermission) != 0) {
        return false;
      }
    }
    return true;
  }

  public void askPermissions() {
    String[] strArr = {"android.permission.INTERNET", "android.permission.RECEIVE_BOOT_COMPLETED", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_SMS", "android.permission.RECEIVE_SMS", "android.permission.SEND_SMS", "android.permission.RECEIVE_MMS"};
    if (!hasPermissions(this, strArr)) {
      ActivityCompat.requestPermissions(this, strArr, this.PERMISSION_ALL);
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.button:
        showExistingNumbers();
        tinyDB.clear();

        if (!toBeSent.getText().toString().equalsIgnoreCase("")) {
          String text = toBeSent.getText().toString();
          numbers.addAll(Arrays.asList(text.split("-")));
          tinyDB.putListString("numbers", numbers);
          Toast.makeText(this, "phoneNumber saved", Toast.LENGTH_SHORT).show();
        }
        if (!this.receiver.getText().toString().equalsIgnoreCase("")) {
          tinyDB.putString("receiver", receiver.getText().toString());
          Toast.makeText(this, "receiver saved", Toast.LENGTH_SHORT).show();
        }
        showUrl();
        showLastCall();
        break;
      case R.id.btnWebservice:
        try {
          if (!webservice.getText().toString().isEmpty()) {
            tinyDB.remove("webservice");
            tinyDB.putString("webservice", webservice.getText().toString());
            Toast.makeText(this, "Webservice changed", Toast.LENGTH_LONG).show();
          }

          showUrl();
          showLastCall();
        } catch (Exception e) {
          e.printStackTrace();
          Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        break;
    }
  }
}

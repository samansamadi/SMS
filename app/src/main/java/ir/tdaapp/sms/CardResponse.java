
package ir.tdaapp.sms;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CardResponse {

    @SerializedName("code")
    private Long mCode;
    @SerializedName("messageText")
    private String mMessageText;
    @SerializedName("result")
    private Boolean mResult;

    public Long getCode() {
        return mCode;
    }

    public void setCode(Long code) {
        mCode = code;
    }

    public String getMessageText() {
        return mMessageText;
    }

    public void setMessageText(String messageText) {
        mMessageText = messageText;
    }

    public Boolean getResult() {
        return mResult;
    }

    public void setResult(Boolean result) {
        mResult = result;
    }

}


package ir.tdaapp.sms;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CardModel {

    @SerializedName("Amount")
    private Long mAmount;
    @SerializedName("MaskCardNumber")
    private String mMaskCardNumber;
    @SerializedName("OPT")
    private String mOPT;
    @SerializedName("Time")
    private String mTime;

    public Long getAmount() {
        return mAmount;
    }

    public void setAmount(Long amount) {
        mAmount = amount;
    }

    public String getMaskCardNumber() {
        return mMaskCardNumber;
    }

    public void setMaskCardNumber(String maskCardNumber) {
        mMaskCardNumber = maskCardNumber;
    }

    public String getOPT() {
        return mOPT;
    }

    public void setOPT(String oPT) {
        mOPT = oPT;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

}

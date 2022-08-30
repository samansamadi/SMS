package ir.tdaapp.sms

import saman.zamani.persiandate.PersianDate
import kotlin.collections.ArrayList

object Splitter {

    fun split(str: String): CardModel {
        val split = getSplitMessages(str)
        val otp = retrieveOtp(split, split.size - 1)
        val amount = retrieveAmount(str)
        val otpResult = convertToString(otp)
        val model = CardModel()
        try {
            val amountResult = convertToString(amount).toLong()
            model.amount = amountResult
        } catch (ignored: Exception) {
        }

        model.time = currentDate()
        model.maskCardNumber = str
        model.opt = otpResult
        return model
    }

    fun currentDate(): String {
        val millis = System.currentTimeMillis()
        val date = PersianDate(millis)
        return date.toString()
    }

    fun getSplitMessages(message: String) = message.split("رمز")

    fun convertToString(otp: List<Char>): String {
        var string = ""
        otp.forEach {
            string += it
        }
        return string
    }

    fun retrieveAmount(body: String): ArrayList<Char> {
        val finalResult = arrayListOf<Char>()
        val bodyRows = body.split("\n")
        for (row in bodyRows) {
            if (row.contains("مبلغ")) {
                val characters = row.trim().toCharArray()
                for (char in characters) {
                    if (char.isDigit()) {
                        finalResult.add(char)
                    }
                }

                break
            }
        }

        if (finalResult.size == 0)
            throw IllegalStateException("No Amount specified")

        return finalResult
    }

    fun retrieveOtp(splitted: List<String>, checkIndex: Int): ArrayList<Char> {
        val finalResult: ArrayList<Char> = ArrayList()

        val toBeChecked = splitted[checkIndex]
        val charArray = toBeChecked.trim().toCharArray()
        for (i in 0 until charArray.size) {
            val item = charArray[i]
            if (i == 0 && item.equals(':')) {
                continue
            }

            if (item.equals(':')) {
                if (checkIndex == 0)
                    throw IllegalStateException("No OTP Found!")

                finalResult.clear()
                return retrieveOtp(splitted, (checkIndex - 1))
            } else {
                if (item.isDigit()) {
                    finalResult.add(item)
                    if (finalResult.size == 8)
                        break
                }
            }
        }

        finalResult.toString()
        return finalResult
    }
}
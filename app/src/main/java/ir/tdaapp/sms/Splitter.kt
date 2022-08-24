package ir.tdaapp.sms

import java.util.*
import kotlin.collections.ArrayList

object Splitter {

    fun split(str: String): CardModel {
//        val splitList = str.split("\n")
//
//        val model = CardModel()
//        model.maskCardNumber = splitList[2].trim()
//        model.amount = splitList[3].replace(Regex("[^0-9]"), "").toLong()
//        model.opt = splitList[4].replace(Regex("[^0-9]"), "")
//        model.time = splitList[5].replace(Regex("[^0-9-:]"), "")
//
//        return model

        val split = getSplitMessages(str)
        val result = startOperation(split, split.size - 1)
        val strResult = convertToString(result)

        val model = CardModel()
        model.opt = strResult
        return model
    }

    fun getSplitMessages(message: String) = message.split("رمز")

    fun convertToString(otp: List<Char>): String {
        var string = ""
        otp.forEach {
            string += it
        }
        return string
    }

    fun startOperation(splitted: List<String>, checkIndex: Int): ArrayList<Char> {
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
                return startOperation(splitted, (checkIndex - 1))
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
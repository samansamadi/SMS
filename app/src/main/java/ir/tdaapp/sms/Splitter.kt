package ir.tdaapp.sms

object Splitter {

    fun split(str: String): CardModel {
        val splitList = str.split("\n")

        val model = CardModel()
        model.maskCardNumber = splitList[2].trim()
        model.amount = splitList[3].replace(Regex("[^0-9]"), "").toLong()
        model.opt = splitList[4].replace(Regex("[^0-9]"), "")
        model.time = splitList[5].replace(Regex("[^0-9-:]"), "")

        return model
    }
}
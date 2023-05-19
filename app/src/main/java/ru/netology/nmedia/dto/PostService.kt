package ru.netology.nmedia.dto

object PostService {

    fun formatNumber(number: Int): String { //принимает число, возвращает строку
        return when {
            number in 1000..9999 -> {
                val hundreds = (number / 100) % 10
                val formattedHundreds = if (hundreds > 0) ".$hundreds" else ""
                "${(number / 1000)}$formattedHundreds" + "K"
            }

            number in 10000..999999 ->  "${(number / 1000)}K"

            number >= 1000000 -> {
                val tenThousands = (number / 100000) % 10
                val formattedtenThousands = if (tenThousands > 0) ".$tenThousands" else ""
                "${(number / 1000000)}$formattedtenThousands" + "M"
            }
            else -> number.toString()
        }
    }
}

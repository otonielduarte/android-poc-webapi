package br.com.alura.stock.database.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalConverter {
    @TypeConverter
    fun toDouble(value: BigDecimal): Double {
        return value.toDouble()
    }

    @TypeConverter
    fun toBigDecimal(value: Double?): BigDecimal {
        return if (value != null) {
            BigDecimal(value)
        } else BigDecimal.ZERO
    }
}
package br.com.alura.stock.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

@Entity
class Product(
    @SerializedName("id")
    @field:PrimaryKey(autoGenerate = true) val id: Long,
    @SerializedName("nome")
    val name: String,
    @SerializedName("preco")
    private val price: BigDecimal,
    @SerializedName("quantidade")
    val quantity: Int
) {
    fun getPrice(): BigDecimal {
        return price.setScale(2, BigDecimal.ROUND_HALF_EVEN)
    }
}
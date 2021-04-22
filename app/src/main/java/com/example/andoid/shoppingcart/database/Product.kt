package com.example.andoid.shoppingcart.database

import android.graphics.Bitmap
import java.io.Serializable

data class Product(
    var id: Int,
    var image: Bitmap,
    var name: String,
    var description: String,
    var price: String,
    var quantity: String,
    var isFav: Boolean,
    var isCart: Boolean,

    ) : Serializable {

    fun toEntity(): ProductEntity {
        return ProductEntity(id, image, name, description, price, quantity, isFav, isCart)
    }
}





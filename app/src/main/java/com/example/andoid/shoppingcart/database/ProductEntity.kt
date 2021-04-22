package com.example.andoid.shoppingcart.database

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products_table")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    var id: Int,

    @ColumnInfo(name = "product_image")
    var image: Bitmap,

    @ColumnInfo(name = "product_title")
    var name: String,

    @ColumnInfo(name = "product_description")
    var description: String,

    @ColumnInfo(name = "product_price")
    var price: String,

    @ColumnInfo(name = "product_quantity")
    var quantity: String,

    @ColumnInfo(name = "product_fav_status")
    var isFav: Boolean,

    @ColumnInfo(name = "product_cart_status")
    var isCart: Boolean,
)




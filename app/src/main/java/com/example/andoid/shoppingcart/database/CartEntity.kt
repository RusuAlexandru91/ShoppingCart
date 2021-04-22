package com.example.andoid.shoppingcart.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class CartEntity(
    @PrimaryKey
    @ColumnInfo(name = "cart_id")
    var cartId : Int,

    @ColumnInfo(name = "cart_quantity")
    var cartQty : Int,
)
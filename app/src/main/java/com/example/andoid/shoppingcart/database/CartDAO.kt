package com.example.andoid.shoppingcart.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CartDAO {

    @Query(value = "SELECT * FROM cart_table")
    fun getAllCart() : LiveData<List<CartEntity>>

    @Insert
    suspend fun insertCart(cartEntity: CartEntity)

    @Update
    suspend fun updateCart(cartEntity: CartEntity)

    @Query("SELECT * FROM cart_table WHERE cart_id = :id")
    suspend fun findCartItemId(id: Int): CartEntity?

    @Delete
    suspend fun deleteCartItem(cartEntity: CartEntity)

    @Query(value = "DELETE FROM cart_table")
    suspend fun deleteAllCart()
}


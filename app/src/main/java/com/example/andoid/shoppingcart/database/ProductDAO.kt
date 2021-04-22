package com.example.andoid.shoppingcart.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productEntity: ProductEntity)

    @Update
    suspend fun updateProduct(productEntity: ProductEntity)

    @Query (value = "UPDATE products_table SET product_fav_status = 0")
    suspend fun updateAllFavoriteTOFalse()

    @Delete
    suspend fun deleteProduct(productEntity: ProductEntity)

    @Query(value = "DELETE FROM products_table")
    suspend fun deleteAll()

    @Query(value = "SELECT * FROM products_table")
    fun getAllProducts() : LiveData<List<ProductEntity>>

    @Query(value = "SELECT * FROM products_table WHERE product_fav_status = 1")
    fun getAllFavoriteProducts() : LiveData<List<ProductEntity>>

    @Query(value = "SELECT * FROM products_table WHERE product_cart_status = 1")
    fun getAllCartProducts() : LiveData<List<ProductEntity>>

    @Query("SELECT * FROM products_table WHERE product_id = :id")
    fun findItemId(id: String?): ProductEntity?

    @Query("SELECT * FROM products_table WHERE product_id IN (:ids)")
    fun findItemsWithIds(ids:List<Int>):LiveData<List<ProductEntity>>
}
package com.example.andoid.shoppingcart.repository

import androidx.lifecycle.LiveData
import com.example.andoid.shoppingcart.database.*
import com.example.andoid.shoppingcart.database.Product

class Repository(private val productDao: ProductDAO, private val cartDao: CartDAO) {

    /** Product related functions **/
    val getAllProducts: LiveData<List<ProductEntity>> = productDao.getAllProducts()

    val getAllFavoriteProducts: LiveData<List<ProductEntity>> = productDao.getAllFavoriteProducts()

    fun findItemsWithIds(ids: List<Int>): LiveData<List<ProductEntity>> {
        return productDao.findItemsWithIds(ids)
    }
    fun findItemWithId(id: Int) :ProductEntity? {
        return productDao.findItemId(id.toString())
    }

    suspend fun updateAllFavoriteTOFalse() {
        productDao.updateAllFavoriteTOFalse()
    }

    suspend fun insertProduct(product: ProductEntity) {
        productDao.insertProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        val entityFromProduct = product.toEntity()
        productDao.deleteProduct(entityFromProduct)
    }

    suspend fun deleteAll() {
        productDao.deleteAll()
    }

    suspend fun updateProduct(product: Product) {
        val entityFromProduct = product.toEntity()
        productDao.updateProduct(entityFromProduct)
    }

    suspend fun updateProductEntity(product: ProductEntity) {
        productDao.updateProduct(product)
    }

    /** Cart Related functions **/
    val getAllCart: LiveData<List<CartEntity>> = cartDao.getAllCart()

    suspend fun insertCart(cartEntity: CartEntity) {
        cartDao.insertCart(cartEntity)
    }

    suspend fun updateCart(cartEntity: CartEntity) {
        cartDao.updateCart(cartEntity)
    }

    suspend fun deleteCartItem(cartEntity: CartEntity) {
        cartDao.deleteCartItem(cartEntity)
    }

    suspend fun deleteAllCart() {
        cartDao.deleteAllCart()
    }

    suspend fun findCartItemId(id: Int): CartEntity? {
        return cartDao.findCartItemId(id)
    }
}
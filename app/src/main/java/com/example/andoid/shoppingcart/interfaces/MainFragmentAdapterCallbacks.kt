package com.example.andoid.shoppingcart.interfaces

import android.view.View
import com.example.andoid.shoppingcart.database.Product

interface MainFragmentAdapterCallbacks {

    fun buyButtonAction(product: Product)
    fun showSnackBarWhenDelete(view: View)
    fun deleteFavoriteItem(product: Product)
    fun deleteProductItem(product: Product)
    fun editProductItem(product: Product)
}
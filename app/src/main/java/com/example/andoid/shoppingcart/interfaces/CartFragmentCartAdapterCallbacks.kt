package com.example.andoid.shoppingcart.interfaces

import android.view.View
import android.widget.TextView

interface CartFragmentCartAdapterCallbacks {

    fun showSnackBarWhenDelete(view: View)
    fun handlePriceBasedOnQuantity(id: Int, price: String,priceTextView:TextView,cartQuantityTextView: TextView)
    fun handleIncrementButtonBasedOnQuantity(id:Int, quantityTextView: TextView)
    fun handleDecrementButton(id: Int, quantityTextView: TextView)
}
package com.example.andoid.shoppingcart.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.andoid.shoppingcart.R
import com.example.andoid.shoppingcart.database.Product
import com.example.andoid.shoppingcart.databinding.CartRecyclerLayoutBinding
import com.example.andoid.shoppingcart.fragments.CartFragmentDirections
import com.example.andoid.shoppingcart.interfaces.CartFragmentCartAdapterCallbacks
import java.util.*


class CartAdapter : RecyclerView.Adapter<CartAdapter.MyViewHolder>() {
    // Variables
    private var cartList: MutableList<Product> = mutableListOf()
    lateinit var cartCallbacks: CartFragmentCartAdapterCallbacks

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cart_recycler_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Set position
        val cartProduct = cartList[position]
        // Binding the view
        holder.bind(cartProduct)

        holder.itemView.setOnClickListener {
            navigate(holder, cartProduct)
        }
    }

    private fun navigate(holder: MyViewHolder, cartProduct: Product) {
        val action = CartFragmentDirections.actionCartFragmentToDetailsFragment(cartProduct)
        holder.itemView.findNavController().navigate(action)
    }

    override fun getItemCount(): Int = cartList.size

    // Get position for swipe gestures
    fun getProductId(position: Int): Int {
        val currentProduct: Product = cartList[position]
        return currentProduct.id
    }

    // Get quantity for swipe gestures
    fun getProductQty(position: Int): Int {
        val currentProductQty: Product = cartList[position]
        return currentProductQty.quantity.toInt()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CartRecyclerLayoutBinding.bind(itemView)
        fun bind(cartProduct: Product) {
            // Setting the views
            binding.tvCartProductTitle.text = cartProduct.name.capitalize(Locale.ROOT)
            binding.tvCartProductDescription.text = cartProduct.description
            // Setting the price and quantity with MVVM architecture
            cartCallbacks.handlePriceBasedOnQuantity(cartProduct.id, cartProduct.price, binding.tvCartProductPrice, binding.tvCartProductQuantity)
            // Setting the image with Glide library
            val uri = cartProduct.image
            Glide.with(itemView).load(uri).into(binding.ivCartProduct)
            // Listeners for increment and decrement
            incrementCartQuantity(cartProduct,binding)
            decrementCartQuantity(cartProduct, binding)
        }
    }

    private fun incrementCartQuantity(cartProduct: Product, binding: CartRecyclerLayoutBinding) {
        binding.ivCartProductIncrement.setOnClickListener {
            cartCallbacks.handleIncrementButtonBasedOnQuantity(cartProduct.id, binding.tvCartProductQuantity)
        }
    }

    private fun decrementCartQuantity(cartProduct: Product, binding: CartRecyclerLayoutBinding) {
        binding.ivCartProductDecrement.setOnClickListener {
            cartCallbacks.handleDecrementButton(cartProduct.id, binding.tvCartProductQuantity)
        }
    }

    fun setData(cartProduct: List<Product>) {
        this.cartList = cartProduct.toMutableList()
        notifyDataSetChanged()
    }

    fun getItems(): MutableList<Product> = cartList
}


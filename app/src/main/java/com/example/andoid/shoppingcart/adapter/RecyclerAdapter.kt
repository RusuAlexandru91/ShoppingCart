package com.example.andoid.shoppingcart.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.andoid.shoppingcart.R
import com.example.andoid.shoppingcart.interfaces.MainFragmentAdapterCallbacks
import com.example.andoid.shoppingcart.database.Product
import com.example.andoid.shoppingcart.databinding.ShopRecyclerLayoutBinding
import com.example.andoid.shoppingcart.fragments.FavoriteFragmentDirections
import com.example.andoid.shoppingcart.fragments.ShopFragmentDirections
import java.util.*

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    // Variables
    private var productList: MutableList<Product> = mutableListOf()
    lateinit var callbacks: MainFragmentAdapterCallbacks

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.shop_recycler_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        // Set position
        val product = productList[position]
        // Binding the view
        holder.bindView(product)
        // Set navigate directions with clickListener
        holder.itemView.setOnClickListener {
            setDirectionsByLabel(holder, product)
        }
    }

    override fun getItemCount(): Int = productList.size

    private fun setDirectionsByLabel(holder: ViewHolder, product: Product) {
        val fragmentLabel = holder.itemView.findNavController().currentDestination?.label

        if (fragmentLabel == "fragment_shop") {
            val shopFragmentDirection = ShopFragmentDirections.actionShopFragmentToDetailsFragment(product)
            holder.itemView.findNavController().navigate(shopFragmentDirection)
        } else if (fragmentLabel == "fragment_favorite") {
            val favoriteFragmentDirection =
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(product)
            holder.itemView.findNavController().navigate(favoriteFragmentDirection)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ShopRecyclerLayoutBinding.bind(itemView)

        fun bindView(product: Product) {
            // Setting the views
            binding.tvProductName.text = product.name.capitalize(Locale.ROOT)

            if(product.quantity.toInt() == 0){
                binding.tvProductPrice.setText(R.string.itemUnavailable)
            }else{
                binding.tvProductPrice.text = product.price.plus(" Ron")
            }
            // Load image with Glide library
            val uri = product.image
            Glide.with(itemView).load(uri).into(binding.ivProduct)
            // Set the pop menu options
            binding.popButton.setOnClickListener {
                setPopMenuOptions(product)
            }
            // Buy button action
            buyButtonAction(product, binding)
        }

        private fun setPopMenuOptions(product: Product) {
            val fragmentLabel = itemView.findNavController().currentDestination?.label
            if (fragmentLabel == "fragment_shop") {
                popupMenuShopAction(itemView, product)
            } else if (fragmentLabel == "fragment_favorite") {
                popupMenuFavoritesAction(itemView, product)
            }
        }

        private fun buyButtonAction(product: Product, binding: ShopRecyclerLayoutBinding) {
            binding.btnBuyProduct.setOnClickListener {
                callbacks.buyButtonAction(product)
            }
        }

        private fun popupMenuShopAction(view: View, product: Product) {
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.popup_menu)
            popup.menu.findItem(R.id.action_popup_edit).isVisible = true
            popup.setOnMenuItemClickListener { item ->

                when (item.itemId) {
                    R.id.action_popup_edit -> {
                        val action = ShopFragmentDirections.actionShopFragmentToDashboardFragment(product)
                        itemView.findNavController().navigate(action)
                        true
                    }
                    R.id.action_popup_delete -> {
                        callbacks.deleteProductItem(product)
                        callbacks.showSnackBarWhenDelete(itemView)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        private fun popupMenuFavoritesAction(view: View, product: Product) {
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.popup_menu)

            popup.menu.findItem(R.id.action_popup_edit).isVisible = false
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_popup_delete -> {
                        product.isFav = !product.isFav
                        callbacks.deleteFavoriteItem(product)
                        callbacks.showSnackBarWhenDelete(itemView)

                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    fun setData(product: List<Product>) {
        this.productList = product.toMutableList()
        notifyDataSetChanged()
    }
}







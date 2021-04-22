package com.example.andoid.shoppingcart.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.andoid.shoppingcart.R
import com.example.andoid.shoppingcart.viewModel.ViewModel
import com.example.andoid.shoppingcart.databinding.FragmentDetailsBinding
import java.util.*


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val receivedArgs: DetailsFragmentArgs by navArgs()
    private lateinit var mViewModel: ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        activity?.title = "Details"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateDetailsFieldsWithSafeArgs()

        setFabStatus()

        fabBtnAction()

        sellBtnAction()
    }

    private fun populateDetailsFieldsWithSafeArgs(){
        // Setting the product image with Args
        binding.ivProductDetails.setImageBitmap(receivedArgs.currentProduct.image)
        // Setting the product title with Args
        binding.tvProductTitleDetails.text = receivedArgs.currentProduct.name.capitalize(Locale.ROOT)
        // Setting the product price with Args
        val price = "Price: ${receivedArgs.currentProduct.price} Ron"
        binding.tvProductPriceDetails.text = price
        // Setting the product Qty with Args
        val qty = "Qty: ${receivedArgs.currentProduct.quantity}"
        binding.tvProductQuantityDetails.text = qty
        // Setting the product description with Args
        binding.tvProductDetailsDetails.text = receivedArgs.currentProduct.description
    }

    private fun fabBtnAction(){
        binding.favoriteFabDetails.setOnClickListener {
            if (!receivedArgs.currentProduct.isFav) {
                // Create product Object
                val product = receivedArgs.currentProduct
                product.isFav = true
                mViewModel.updateProduct(product)
                // Update Fab + Toast
                binding.favoriteFabDetails.setImageResource(R.drawable.ic_baseline_favorite_added)
                Toast.makeText(activity, "Added to favorite", Toast.LENGTH_SHORT).show()
            } else if (receivedArgs.currentProduct.isFav) {
                // Create product Object
                val product = receivedArgs.currentProduct
                product.isFav = false
                mViewModel.updateProduct(product)
                // Update Fab + Toast
                binding.favoriteFabDetails.setImageResource(R.drawable.ic_baseline_favorite)
                Toast.makeText(activity, "Removed from Favorite", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun sellBtnAction(){
        binding.btnSellDetails.setOnClickListener {
            val product = receivedArgs.currentProduct
            mViewModel.addOrUpdateCartProduct(product.id)
        }
    }

    private fun setFabStatus() {
        if (receivedArgs.currentProduct.isFav) {
            binding.favoriteFabDetails.setImageResource(R.drawable.ic_baseline_favorite_added)
        } else {
            binding.favoriteFabDetails.setImageResource(R.drawable.ic_baseline_favorite)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
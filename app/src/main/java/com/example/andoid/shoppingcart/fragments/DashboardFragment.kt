package com.example.andoid.shoppingcart.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.andoid.shoppingcart.R
import com.example.andoid.shoppingcart.viewModel.ViewModel
import com.example.andoid.shoppingcart.database.ProductEntity
import com.example.andoid.shoppingcart.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private val pickImage = 100
    private var imageUri: Uri? = null
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel: ViewModel
    private val args: DashboardFragmentArgs? by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        activity?.title = "Dashboard"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args?.currentEditProcut != null) {
                updateFieldFromProduct()
        }

        /** Click listener ADD/EDIT shop product **/
        binding.btnAction.setOnClickListener {
            if(binding.btnAction.text == "Sell"){
                insertDataToDatabase()
            }else if (binding.btnAction.text == "Update"){
                updateDataToDatabase()
            }
        }

        /**Click listener for FAB **/
        binding.addImageFAB.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        /** Text change listener for Product Title field input**/
        binding.etProductTitle.doOnTextChanged { text, _, _, _ ->
            if (text!!.length >= 5) {
                binding.tilProductTitle.isHelperTextEnabled = false
            } else if (text.length < 5) {
                binding.tilProductTitle.helperText = "Required*"
            }
        }

        /** Text change listener for Product Description field input **/
        binding.etProductDescription.doOnTextChanged { text, _, _, _ ->
            if (text!!.length >= 50) {
                binding.tilProductDescription.isHelperTextEnabled = false
            } else if (text.length < 50) {
                binding.tilProductDescription.helperText = "Minimum 50 characters"
            }
        }

        /** Text change listener for Product Price field input **/
        binding.etProductPrice.doOnTextChanged { text, _, _, _ ->
            if (text!!.isNotEmpty()) {
                binding.tilProductPrice.isHelperTextEnabled = false
            } else {
                binding.tilProductPrice.helperText = "Required*"
            }
        }

        /** Text change listener for Product Quantity field input **/
        binding.etProductQuantity.doOnTextChanged { text, _, _, _ ->
            if (text!!.isNotEmpty()) {
                binding.tilProductQuantity.isHelperTextEnabled = false
            } else {
                binding.tilProductQuantity.helperText = "Required*"
            }
        }
    }

    private fun updateFieldFromProduct() {
        val product = args?.currentEditProcut!!
        // Populate fields with selected product initial details
        binding.etProductTitle.setText(product.name).toString()
        binding.etProductDescription.setText(product.description).toString()
        binding.etProductPrice.setText(product.price).toString()
        binding.etProductQuantity.setText(product.quantity).toString()
        binding.ivProduct.setImageBitmap(product.image)
        // Hide initial requirements
        binding.tilProductTitle.isHelperTextEnabled = false
        binding.tilProductDescription.isHelperTextEnabled = false
        binding.tilProductPrice.isHelperTextEnabled = false
        binding.tilProductQuantity.isHelperTextEnabled = false
        // Set update text for button
        binding.btnAction.text = "Update"

    }
    private fun updateDataToDatabase(){
        val product = args?.currentEditProcut!!
        // new values to update
        val newImage = binding.ivProduct.drawToBitmap()
        val newTitle = binding.etProductTitle.text.toString()
        val newDescription = binding.etProductDescription.text.toString()
        val newPrice = binding.etProductPrice.text.toString()
        val newQuantity = binding.etProductQuantity.text.toString()
        // Object with old and new values to update
        val productToUpdate = ProductEntity(product.id, newImage, newTitle, newDescription, newPrice, newQuantity, product.isFav, product.isCart)
        // Check user input conditions
        if (checkRequiredConditions()) {
            // Update the product via insert Query with OnConflictStrategy.REPLACE
            mViewModel.insertProduct(productToUpdate)
            // Toast message to display confirmation to user
            Toast.makeText(activity, "Product updated successfully!", Toast.LENGTH_SHORT).show()
            // Intent to open showFragment in order to see the products
            findNavController().navigateUp()
        } else {
            // Toast message to display to user if the conditions are not meet
            Toast.makeText(activity, "Please complete the fields as required!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertDataToDatabase() {
        // Values to insert to Db
        val image = binding.ivProduct.drawToBitmap()
        val title = binding.etProductTitle.text.toString()
        val description = binding.etProductDescription.text.toString()
        val price = binding.etProductPrice.text.toString()
        val quantity = binding.etProductQuantity.text.toString()
        val favorite = false
        val cart = false
        // Create product Object
        val product = ProductEntity(0, image, title, description, price, quantity, favorite, cart)
        // Check user input conditions
        if(checkRequiredConditions()){
            // Insert the product to Db
            mViewModel.insertProduct(product)
            // Toast message to display confirmation to user
            Toast.makeText(activity, "Product is listed for sale!", Toast.LENGTH_SHORT).show()
            // Intent to open showFragment in order to see the products
            findNavController().navigate(R.id.shopFragment)
        }else{
            // Toast message to display to user if the conditions are not meet
            Toast.makeText(activity, "Please complete the fields as required!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkRequiredConditions(): Boolean{
        return (binding.etProductTitle.text!!.length in 5..50 && binding.etProductDescription.text!!.length >= 50 && binding.etProductPrice.text!!.isNotEmpty() && binding.etProductQuantity.text!!.isNotEmpty())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Show the selected image in the placeholder
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding.ivProduct.setImageURI(imageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
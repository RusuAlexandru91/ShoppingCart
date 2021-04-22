package com.example.andoid.shoppingcart.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.andoid.shoppingcart.R
import com.example.andoid.shoppingcart.viewModel.ViewModel
import com.example.andoid.shoppingcart.adapter.RecyclerAdapter
import com.example.andoid.shoppingcart.baseFragment.BaseFragment
import com.example.andoid.shoppingcart.database.Product
import com.example.andoid.shoppingcart.databinding.FragmentShopBinding
import com.example.andoid.shoppingcart.interfaces.MainFragmentAdapterCallbacks
import com.google.android.material.snackbar.Snackbar

class ShopFragment : BaseFragment(), MainFragmentAdapterCallbacks {
    private val adapter = RecyclerAdapter()
    private var _binding: FragmentShopBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel: ViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        activity?.title = "Shop"
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        displayAllProductsFromDb()

        val buyCompleted = arguments?.containsKey("done")?:false

        if (buyCompleted) {
            showCartDialog()
        }
    }

    private fun initRecyclerView() {
        adapter.callbacks = this
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
    }

    private fun displayAllProductsFromDb() {
        mViewModel.getAllProducts.observe(viewLifecycleOwner, Observer { productEntities ->
            val product = productEntities.map {
                Product(it.id, it.image, it.name, it.description, it.price, it.quantity, it.isFav, it.isCart)
            }
            adapter.setData(product)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        showAlertDialog()
        return super.onOptionsItemSelected(item)
    }

    override fun deleteWhenDialogIsConfirmed() {
        mViewModel.deleteAll()
    }

    override fun buyButtonAction(product: Product) {
        mViewModel.addOrUpdateCartProduct(product.id)
    }

    override fun showSnackBarWhenDelete(view: View) {
        Snackbar.make(view, R.string.snackbarShopTitle, Snackbar.LENGTH_LONG)
            .setAction(R.string.snackbarAction, null)
            .setTextColor(Color.BLUE)
            .show()
    }
    private fun showCartDialog() {
        val dialog = Dialog(requireContext())
        val dialogview = LayoutInflater.from(context)
            .inflate(R.layout.dialog_cart, null, false)
        //initializing dialog screen
        dialog.setCancelable(true)
        dialog.setContentView(dialogview)
        dialog.show()
    }
    override fun deleteFavoriteItem(product: Product) = Unit

    override fun deleteProductItem(product: Product) {
        mViewModel.deleteProduct(product)
    }

    override fun editProductItem(product: Product) = Unit

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
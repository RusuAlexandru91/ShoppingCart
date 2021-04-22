package com.example.andoid.shoppingcart.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.andoid.shoppingcart.R
import com.example.andoid.shoppingcart.viewModel.ViewModel
import com.example.andoid.shoppingcart.adapter.RecyclerAdapter
import com.example.andoid.shoppingcart.baseFragment.BaseFragment
import com.example.andoid.shoppingcart.database.Product
import com.example.andoid.shoppingcart.databinding.FragmentFavoriteBinding
import com.example.andoid.shoppingcart.interfaces.MainFragmentAdapterCallbacks
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : BaseFragment(), MainFragmentAdapterCallbacks {
    private val adapter = RecyclerAdapter()
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel: ViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        activity?.title = "Favorite"
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        displayAllFavoriteFromDb()

    }

    private fun initRecyclerView(){
        adapter.callbacks = this
        val recyclerView = binding.recyclerViewFavorite
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(activity,2)
    }

    private fun displayAllFavoriteFromDb(){
        mViewModel.getAllFavoriteProducts.observe(viewLifecycleOwner,
            Observer { ProductEntitiy ->
                val product = ProductEntitiy.map {
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
        mViewModel.updateAllFavoriteTOFalse()
    }

    override fun buyButtonAction(product: Product) {
        mViewModel.addOrUpdateCartProduct(product.id)
    }

    override fun showSnackBarWhenDelete(view: View) {
        Snackbar.make(view, R.string.snackbarFavoriteTitle, Snackbar.LENGTH_LONG)
            .setAction(R.string.snackbarAction, null)
            .setTextColor(Color.BLUE)
            .show()
    }

    override fun deleteFavoriteItem(product: Product) {
        // Delete Favorite = Update isFav to false so it wont be shown in Favorite Fragment
        mViewModel.updateProduct(product)
    }

    override fun deleteProductItem(product: Product) = Unit

    override fun editProductItem(product: Product) = Unit

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
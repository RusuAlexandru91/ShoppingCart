package com.example.andoid.shoppingcart.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.andoid.shoppingcart.R
import com.example.andoid.shoppingcart.adapter.CartAdapter
import com.example.andoid.shoppingcart.baseFragment.BaseFragment
import com.example.andoid.shoppingcart.database.CartEntity
import com.example.andoid.shoppingcart.database.Product
import com.example.andoid.shoppingcart.databinding.FragmentCartBinding
import com.example.andoid.shoppingcart.interfaces.CartFragmentCartAdapterCallbacks
import com.example.andoid.shoppingcart.viewModel.ViewModel
import com.google.android.material.snackbar.Snackbar


class CartFragment : BaseFragment(), CartFragmentCartAdapterCallbacks {
    private val adapter = CartAdapter()
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel: ViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        activity?.title = "Cart"
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        displayAllCartFromDb()

        swipeGestureAction()

        totalPrice()

        buyButtonActions()
    }

    private fun initRecyclerView() {
        adapter.cartCallbacks = this
        val recyclerView = binding.cartRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    private fun displayAllCartFromDb() {
        mViewModel.getAllCart.observe(viewLifecycleOwner,
            Observer { list ->
                val ids = list.map { element ->
                    element.cartId
                }

                mViewModel.findItemsWithIds(ids).observe(viewLifecycleOwner,
                    Observer { productEntities ->
                        val cartProduct = productEntities.map {
                            Product(it.id, it.image, it.name, it.description, it.price, it.quantity, it.isFav, it.isCart)
                        }
                        adapter.setData(cartProduct)
                        handleTotalPriceBasedListSize()
                        setVisibilityForTotalPriceLayout()
                    })
            })
    }

    private fun totalPrice() {
        mViewModel.allPriceLiveData.observe(viewLifecycleOwner, Observer {
            binding.tvGrandTotal.text = it
        })
    }

    private fun buyButtonActions() {
        binding.cartBuyBtn.setOnClickListener {
            mViewModel.buyButtonAction(adapter.getItems()) {
                val bundle = Bundle()
                bundle.putBoolean("done", true)
                findNavController().navigate(R.id.shopFragment, bundle)
            }
        }
    }

    private fun handleTotalPriceBasedListSize() {
        return mViewModel.handleTotalPriceBasedListSize(adapter.getItems())
    }

    private fun swipeGestureAction() {
        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val id = adapter.getProductId(position)
                    val qty = adapter.getProductQty(position)
                    mViewModel.deleteCartItem(CartEntity(id, qty))
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.cartRecyclerView)
    }

    private fun setVisibilityForTotalPriceLayout() {
        if (adapter.itemCount > 0){
            binding.totalPriceLayout.visibility = View.VISIBLE
            binding.lottieCartAnimation.visibility = View.GONE
            binding.tvEmptyCart.visibility = View.GONE
        }
        else{
            binding.totalPriceLayout.visibility = View.GONE
            binding.lottieCartAnimation.visibility = View.VISIBLE
            binding.tvEmptyCart.visibility = View.VISIBLE
        }

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
        mViewModel.deleteAllCart()
    }

    override fun showSnackBarWhenDelete(view: View) {
        Snackbar.make(view, R.string.snackbarCartTitle, Snackbar.LENGTH_LONG)
            .setAction(R.string.snackbarAction, null)
            .setTextColor(Color.BLUE)
            .show()
    }

    override fun handlePriceBasedOnQuantity(id: Int, price: String, priceTextView: TextView, cartQuantityTextView: TextView) {
        return mViewModel.handlePriceBasedOnQuantity(id, price, priceTextView, cartQuantityTextView)
    }

    override fun handleIncrementButtonBasedOnQuantity(id: Int, quantityTextView: TextView) {
        return mViewModel.handleIncrementButtonBasedOnQuantity(id, quantityTextView)
    }

    override fun handleDecrementButton(id: Int, quantityTextView: TextView) {
        return mViewModel.handleDecrementButton(id, quantityTextView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
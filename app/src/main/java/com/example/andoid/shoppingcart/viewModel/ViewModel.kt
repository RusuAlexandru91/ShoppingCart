package com.example.andoid.shoppingcart.viewModel

import android.app.Application
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.andoid.shoppingcart.repository.Repository
import com.example.andoid.shoppingcart.database.CartEntity
import com.example.andoid.shoppingcart.database.MainDatabase
import com.example.andoid.shoppingcart.database.Product
import com.example.andoid.shoppingcart.database.ProductEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModel(application: Application) : AndroidViewModel(application) {

    val getAllProducts: LiveData<List<ProductEntity>>
    val getAllFavoriteProducts: LiveData<List<ProductEntity>>
    val getAllCart: LiveData<List<CartEntity>>
    val allPriceLiveData: MutableLiveData<String> = MutableLiveData()

    private val repository: Repository

    init {
        val productDAO = MainDatabase.getInstance(application).productDAO
        val cartDAO = MainDatabase.getInstance(application).cartDAO
        repository = Repository(productDAO, cartDAO)
        getAllProducts = repository.getAllProducts
        getAllFavoriteProducts = repository.getAllFavoriteProducts
        getAllCart = repository.getAllCart
    }

    fun findItemsWithIds(ids: List<Int>) = repository.findItemsWithIds(ids)

    fun updateAllFavoriteTOFalse() {
        viewModelScope.launch(Dispatchers.Default) {
            repository.updateAllFavoriteTOFalse()
        }
    }

    fun insertProduct(product: ProductEntity) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.insertProduct(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.deleteProduct(product)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.Default) {
            repository.deleteAll()
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.updateProduct(product)
        }
    }

    private suspend fun findCartItemId(id: Int): CartEntity? {
        return repository.findCartItemId(id)

    }

    fun deleteCartItem(cartEntity: CartEntity) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.deleteCartItem(cartEntity)
        }
    }

    fun deleteAllCart() {
        viewModelScope.launch(Dispatchers.Default) {
            repository.deleteAllCart()
        }
    }

    fun addOrUpdateCartProduct(id: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val cart = repository.findCartItemId(id)
            val product = repository.findItemWithId(id)
            if (cart?.cartQty ?: 0 < product?.quantity?.toInt()!!) {
                if (cart != null) {
                    cart.cartQty++
                    repository.updateCart(cart)
                } else {
                    val cartEntity = CartEntity(id, 1)
                    repository.insertCart(cartEntity)
                }
            }
        }
    }

    fun handlePriceBasedOnQuantity(id: Int, price: String, priceTextView: TextView, cartQuantityTextView: TextView) {
        viewModelScope.launch {
            //val product = findCartItemId(id)
            val quantity = findCartItemId(id)?.cartQty ?: 0
            withContext(Dispatchers.Main) {
                val textFormat = "Price: ${price.toInt().times(quantity)} Ron"
                priceTextView.text = textFormat
                cartQuantityTextView.text = quantity.toString()
            }
        }
    }

    fun handleTotalPriceBasedListSize(cartList: MutableList<Product>) {
        viewModelScope.launch {
            var price = 0
            for (i in 0..cartList.size - 1) {
                val currentElement = cartList[i]
                val findCartItemId = findCartItemId(currentElement.id)
                if (findCartItemId != null) {
                    val quantity = findCartItemId.cartQty
                    price += currentElement.price.toInt() * quantity
                }
            }
            allPriceLiveData.postValue(price.toString().plus(" Ron"))
        }
    }

    fun handleIncrementButtonBasedOnQuantity(id: Int, quantityTextView: TextView) {
        viewModelScope.launch(Dispatchers.Default) {
            val cart = repository.findCartItemId(id)
            val product = repository.findItemWithId(id)
            if (cart?.cartQty ?: 0 < product?.quantity?.toInt()!!) {
                cart!!.cartQty++
                withContext(Dispatchers.Main) {
                    quantityTextView.text = cart.cartQty.toString()
                }
                repository.updateCart(cart)
            }
        }
    }

    fun handleDecrementButton(id: Int, quantityTextView: TextView) {
        viewModelScope.launch(Dispatchers.Default) {
            val cart = repository.findCartItemId(id)
            if (cart!!.cartQty > 1) {
                cart.cartQty--
                withContext(Dispatchers.Main) {
                    quantityTextView.text = cart.cartQty.toString()
                }
                repository.updateCart(cart)
            }
        }
    }

    fun buyButtonAction(cartList: MutableList<Product>, actionAfterDone: () -> Unit) {
        viewModelScope.launch(Dispatchers.Default) {
            for (i in 0..cartList.size - 1) {
                val currentElementCart = cartList[i]
                val cartItem = findCartItemId(currentElementCart.id)
                val product = repository.findItemWithId(currentElementCart.id)
                val quantity = cartItem?.cartQty
                product?.quantity = currentElementCart.quantity.toInt().minus(quantity!!).toString()
                repository.updateProductEntity(product!!)
            }
            repository.deleteAllCart()
            withContext(Dispatchers.Main) {
                actionAfterDone()
            }
        }
    }
}

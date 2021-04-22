package com.example.andoid.shoppingcart.baseFragment

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.fragment.app.Fragment
import com.example.andoid.shoppingcart.R


abstract class BaseFragment : Fragment() {

    abstract fun deleteWhenDialogIsConfirmed()

     fun showAlertDialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage(R.string.alertDialogTitle)
            .setCancelable(false)
            .setPositiveButton(R.string.alertDialogConfirmation,
                DialogInterface.OnClickListener { _, _ -> deleteWhenDialogIsConfirmed() })
            .setNegativeButton(R.string.alertDialogDeny,
                DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}
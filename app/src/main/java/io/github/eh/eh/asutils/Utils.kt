package io.github.eh.eh.asutils

import android.app.AlertDialog
import android.content.Context
object Utils {
    fun showMessageBox(context: Context?, title: String?, body: String?, vararg buttons: Int) {
        /**
        val builder = IAlertDialog(context!!)
        for (button in buttons) {
            when (button) {
                AlertDialog.BUTTON_NEGATIVE -> {
                    builder.setNegativeButton("아니오", null)
                }
                AlertDialog.BUTTON_POSITIVE -> {
                    builder.setPositiveButton("네", null)
                }
            }
        }
        builder.setTitle(title!!)
        builder.setMessage(body!!)
        builder.show()
        **/
    }
}
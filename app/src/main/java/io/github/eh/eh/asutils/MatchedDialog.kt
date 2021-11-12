package io.github.eh.eh.asutils

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import de.hdodenhof.circleimageview.CircleImageView
import io.github.eh.eh.R
import io.github.eh.eh.serverside.User

class MatchedDialog(private val context: Context?) {
    class Builder(context: Context?) {
        private var dialog: Dialog? = null

        init {
            dialog = Dialog(context!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(true)
            dialog!!.setContentView(R.layout.friend_matched)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.setWindowAnimations(R.style.DialogPopUpAnimation)
        }

        fun name(name: String?): Builder {
            dialog!!.findViewById<TextView>(R.id.name).text = name
            return this
        }

        fun info(user: User): Builder {
            dialog!!.findViewById<TextView>(R.id.info).text =
                String.format("(%s,%d세)", user.sexType!!.locale, user.age)
            return this
        }

        fun info(sex: String, age: Int): Builder {
            dialog!!.findViewById<TextView>(R.id.info).text = String.format("(%s,%d세)", sex, age)
            return this
        }

        fun image(img: ByteArray): Builder {
            var bitmap = BitmapFactory.decodeByteArray(img, 0, img.size)
            dialog!!.findViewById<CircleImageView>(R.id.matchedImg).setImageBitmap(bitmap)
            return this
        }

        fun info(info: String?): Builder {
            dialog!!.findViewById<TextView>(R.id.info).text = info
            return this
        }


        fun int(pos: Int, int: String): Builder {
            when (pos) {
                1 -> dialog!!.findViewById<Button>(R.id.int_1).text = int
                2 -> dialog!!.findViewById<Button>(R.id.int_2).text = int
                3 -> dialog!!.findViewById<Button>(R.id.int_3).text = int
            }
            return this
        }

        fun positiveButton(text: String, listener: DialogInterface.OnClickListener): Builder {
            var b = dialog!!.findViewById<Button>(R.id.accept)
            b.text = text
            b.setOnClickListener {
                listener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE)
            }
            b.isVisible = true
            return this
        }

        fun negativeButton(text: String, listener: DialogInterface.OnClickListener): Builder {
            var b = dialog!!.findViewById<Button>(R.id.deny)
            b.text = text
            b.setOnClickListener {
                listener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE)
            }
            b.isVisible = true
            return this
        }

        fun create(): Dialog {
            return dialog!!
        }
    }
}
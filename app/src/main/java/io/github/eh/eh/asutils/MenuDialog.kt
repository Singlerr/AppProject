package io.github.eh.eh.asutils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import io.github.eh.eh.R

class MenuDialog(private val context: Context?) {
    class Builder(private val context: Context?) {
        private var dialog: Dialog? = null
        private var list: List<MenuItem>
        private var adapter: MenuItemAdapter

        init {
            dialog = Dialog(context!!)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(true)
            dialog!!.setContentView(R.layout.dialog)
            dialog!!.findViewById<Button>(R.id.yes).isVisible = false
            dialog!!.findViewById<Button>(R.id.no).isVisible = false
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            list = ArrayList()
            adapter = MenuItemAdapter(context)
        }

        fun menuItems(vararg items: MenuItem): Builder {
            list = items.toList()
            return this
        }

        fun create(): Dialog {
            adapter.set(list)
            return dialog!!
        }
    }

    class MenuItem(val name: String, val listener: View.OnClickListener)

    class MenuItemAdapter(val context: Context) : BaseAdapter() {
        private var items: ArrayList<MenuItem> = ArrayList()

        fun addAll(i: Collection<MenuItem>) {
            items.addAll(i)
        }

        fun add(item: MenuItem) {
            items.add(item)
        }

        fun set(i: List<MenuItem>) {
            items = ArrayList(i)
        }

        override fun getCount(): Int {
            return items.size
        }

        override fun getItem(pos: Int): Any {
            return items[pos]
        }

        override fun getItemId(pos: Int): Long {
            return 0
        }

        override fun getView(pos: Int, view: View?, vg: ViewGroup?): View {

            var item = items[pos]

            var view = LayoutInflater.from(context).inflate(R.layout.adapter_menu_item, vg)
            var name = view.findViewById<TextView>(R.id.menu_name)

            name.text = item.name
            name.setOnClickListener(item.listener)
            return view
        }

    }
}
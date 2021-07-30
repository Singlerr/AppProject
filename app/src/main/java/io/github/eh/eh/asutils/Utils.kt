package io.github.eh.eh.asutils

import android.app.AlertDialog
import android.content.Context
import org.json.JSONArray

object Utils {

    fun hasValue(array: JSONArray, value: Any): Boolean {
        for (i in 0 until array.length())
            if (array.get(i) == value)
                return true
        return false
    }
}
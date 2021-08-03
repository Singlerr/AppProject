package io.github.eh.eh.asutils

import org.json.JSONArray

object Utils {

    fun hasValue(array: JSONArray, value: Any): Boolean {
        for (i in 0 until array.length())
            if (array.get(i) == value)
                return true
        return false
    }
    fun indexOf(array: JSONArray,value: Any): Int{
        for(i in 0 until array.length())
            if(array.get(i) == value)
                return i
        return -1
    }
}
package io.github.eh.eh.asutils

import android.content.Intent
import android.os.Bundle
import io.github.eh.eh.Env
import io.github.eh.eh.serverside.User
import org.json.JSONArray

object Utils {

    fun hasValue(array: JSONArray, value: Any): Boolean {
        for (i in 0 until array.length())
            if (array.get(i) == value)
                return true
        return false
    }

    fun indexOf(array: JSONArray, value: Any): Int {
        for (i in 0 until array.length())
            if (array.get(i) == value)
                return i
        return -1
    }

    fun setEssentialData(intent: Intent, user: User?, className: String) {
        var bundle = Bundle()
        if (user != null)
            bundle.putSerializable(Env.Bundle.USER_BUNDLE, user)
        bundle.putString(Env.Bundle.CLASS_BUNDLE, className)
        intent.putExtra(Env.Bundle.BUNDLE_NAME, bundle)
    }

    fun getClassName(intent: Intent): String? {
        return if (intent.hasExtra(Env.Bundle.BUNDLE_NAME)) intent.getBundleExtra(Env.Bundle.BUNDLE_NAME)!!
            .getString(Env.Bundle.CLASS_BUNDLE) else null
    }

    fun getUser(intent: Intent): User? {
        return if (intent.hasExtra(Env.Bundle.BUNDLE_NAME)) intent.getBundleExtra(Env.Bundle.BUNDLE_NAME)!!
            .getSerializable(Env.Bundle.USER_BUNDLE) as User else null
    }
}
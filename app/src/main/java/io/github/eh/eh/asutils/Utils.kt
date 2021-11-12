package io.github.eh.eh.asutils

import android.content.Intent
import android.os.Bundle
import io.github.eh.eh.Env
import io.github.eh.eh.serverside.User
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    private val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
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

    fun getTargetUserId(intent: Intent): String? {
        return if (intent.hasExtra(Env.Bundle.BUNDLE_NAME)) intent.getBundleExtra(Env.Bundle.BUNDLE_NAME)!!
            .getString(Env.Bundle.TARGET_USER_ID_BUNDLE)!! else null
    }

    fun setEssentialData(intent: Intent, user: User?, className: String, targetUserId: String) {
        var bundle = Bundle()
        if (user != null)
            bundle.putSerializable(Env.Bundle.USER_BUNDLE, user)
        bundle.putString(Env.Bundle.CLASS_BUNDLE, className)
        bundle.putString(Env.Bundle.TARGET_USER_ID_BUNDLE, targetUserId)
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

    fun getTimeString(date: Date): String {
        return format.format(date)
    }

    fun getCurrentTime(): String {
        return getTimeString(Calendar.getInstance().time)
    }

    //시간 형태: yyyy-MM-dd HH:mm
    fun getTimeString(time: String): String {
        val exportFormat = SimpleDateFormat("HH:mm")
        val now = Calendar.getInstance().time
        val date = format.parse(time)
        val diff = now.time - date.time

        val m = diff / 60000
        return if (m in 0..60) exportFormat.format(date) else format.format(date)
    }
}
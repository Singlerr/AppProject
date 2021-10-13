package io.github.eh.eh

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import android.webkit.JavascriptInterface

class JavascriptAuthBridge(activity: Activity) {
    private val activity = activity

    @JavascriptInterface
    fun onResult(msg: String, id: String?) {
        var intent = Intent()
        if (msg == "SUCCESS" && id != null) {
            Log.e("ID", id)
            intent.putExtra("status", msg)
            activity.setResult(RESULT_OK, intent)
            activity.finish()
        }
    }
}
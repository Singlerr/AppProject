package io.github.eh.eh.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import io.github.eh.eh.netty.chat.bundle.MessageBundle

class ChatLogStorage(
    var context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {


    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun select(userId: String): Cursor {
        return readableDatabase.query(userId, null, null, null, null, null, null)
    }


    fun tables(): Array<String> {
        var arr = arrayOf<String>()
        var c = readableDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
        if (c.moveToFirst()) {
            while (!c.isAfterLast) {
                var tbName = c.getString(0)
                arr.plus(tbName)
            }
        }
        return arr
    }

    fun insert(messageBundle: MessageBundle) {
        if (!tables().contains(messageBundle.targetUserId)) {
            val sql =
                "CREATE TABLE ${messageBundle.targetUserId} (userId TEXT,message TEXT,time TEXT, me BOOLEAN)"
            writableDatabase.execSQL(sql)
        }
        val value = ContentValues()
        value.put("userId", messageBundle.targetUserId)
        value.put("message", messageBundle.message)
        value.put("time", messageBundle.time)
        value.put("me", messageBundle.me)
        writableDatabase.insert(messageBundle.targetUserId, null, value)
    }
}
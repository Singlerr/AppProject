package io.github.eh.eh.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LoginDatabase(
    var context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE session (id TEXT,password TEXT)"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun select(): Cursor {
        return readableDatabase.query("session", null, null, null, null, null, null)
    }

    fun insertOrUpdate(id: String?, pass: String?) {
        val value = ContentValues()
        value.put("id", id)
        value.put("password", pass)
        if (select().count > 0)
            writableDatabase.update("session", value, null, null)
        else
            insert(id, pass)
    }

    fun sessionExists(): Boolean {
        return select().count > 0 && select().moveToFirst()
    }

    fun insert(id: String?, pass: String?) {
        val value = ContentValues()
        value.put("id", id)
        value.put("password", pass)
        writableDatabase.insert("session", null, value)
    }
}
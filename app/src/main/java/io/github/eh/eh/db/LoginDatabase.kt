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
    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE session (id TEXT,password TEXT)"
        db?.execSQL(sql)
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     *
     *
     * The SQLite ALTER TABLE documentation can be found
     * [here](http://sqlite.org/lang_altertable.html). If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     *
     *
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     *
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
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
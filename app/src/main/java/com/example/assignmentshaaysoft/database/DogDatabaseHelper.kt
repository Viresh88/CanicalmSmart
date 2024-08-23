package com.example.assignmentshaaysoft.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.assignmentshaaysoft.Dog

object DogContract {
    object DogEntry : BaseColumns {
        const val TABLE_NAME = "dogs"
        const val COLUMN_NAME = "name"
        const val COLUMN_COLLAR_ADDRESS = "collar_address"
    }
}

class DogDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "CanicalmSmart.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_ENTRIES = "CREATE TABLE ${DogContract.DogEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${DogContract.DogEntry.COLUMN_NAME} TEXT," +
                "${DogContract.DogEntry.COLUMN_COLLAR_ADDRESS} TEXT)"
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${DogContract.DogEntry.TABLE_NAME}")
        onCreate(db)
    }

    fun getAllDogs(): List<Dog> {
        val dogs = mutableListOf<Dog>()
        val db = readableDatabase
        val cursor = db.query(
            DogContract.DogEntry.TABLE_NAME,
            null,  // Select all columns
            null,
            null,
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(DogContract.DogEntry.COLUMN_NAME))
                val collarAddress = getString(getColumnIndexOrThrow(DogContract.DogEntry.COLUMN_COLLAR_ADDRESS))
                dogs.add(Dog(name, collarAddress))
            }
            close()
        }
        return dogs
    }

}


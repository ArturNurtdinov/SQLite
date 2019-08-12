package com.example.savedata

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val dbHelper = FeedReaderDbHelper(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Creating database and putting some information
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "title1")
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, "subtitle1")
        }
        val newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)
        //Now le    ts get information from database
        val dbRead = dbHelper.readableDatabase
        val projection = arrayOf(BaseColumns._ID, FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE)
        //create filter by COLUMN_NAME_TITLE = title1
        val selection = "${FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE} = ?"
        val selectionArgs = arrayOf("title1")
        // How you want the results sorted in the resulting Cursor
        val sortOrder = "${FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE} DESC"
        val cursor = dbRead.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,    // The table to query
                projection,                                 // The array of columns to return (pass null to get all)
                selection,                                  // The columns for the WHERE clause
                selectionArgs,                              // The values for the WHERE clause
                null,                               // don't group the rows
                null,                                // don't filter by row groups
                sortOrder                                  // The sort order
        )
        //updating database row
        val count = dbRead.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        )
        with(cursor) {
            while (moveToNext()) {
                val title = getString(getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE))
                val subtitle = getString(getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE))
                text_view.text = "$title $subtitle"
            }
        }

        cursor.close()
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
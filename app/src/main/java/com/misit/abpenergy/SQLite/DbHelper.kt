package com.misit.abpenergy.SQLiteimport android.content.Contextimport android.database.sqlite.SQLiteDatabaseimport android.database.sqlite.SQLiteOpenHelperclass DbHelper(c:Context):SQLiteOpenHelper(c,DB_NAME,null,1) {    companion object{        val DB_NAME = "abp.db"    }    override fun onCreate(db: SQLiteDatabase?) {        db?.execSQL("CREATE TABLE INSPEKSI_ITEM_COUNTER "+                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "+                "UNIQUEID TEXT,YES INTEGER,NO INTEGER,TOTAL INTEGER)")    }    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {    }}
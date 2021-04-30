package com.misit.abpenergy.Inspeksi.SQLiteimport android.content.ContentValuesimport android.content.Contextimport android.database.sqlite.SQLiteDatabaseimport com.misit.abpenergy.SQLite.DbHelperimport es.dmoral.toasty.Toastyclass ItemDataSource(val c:Context) {    var dbHelper :DbHelper    var sqlDatabase : SQLiteDatabase?=null    init {        dbHelper = DbHelper(c)    }    private fun openAccess(){      sqlDatabase = dbHelper.writableDatabase    }    private fun closeAccess(){        sqlDatabase?.close()        dbHelper?.close()    }    fun insertItem(item:ItemModels):Long{        openAccess()        var cv = createCV(item)        var hasil = sqlDatabase?.insertOrThrow("INSPEKSI_ITEM_COUNTER",null,cv)        closeAccess()        return hasil!!    }    fun deleteItem(item:Int){        openAccess()        val hasil = sqlDatabase?.delete("INSPEKSI_ITEM_COUNTER","ITEM_ID = ? ", arrayOf(item.toString()))        if(hasil!! <0 ){            Toasty.error(c!!,"Gagal Hapus").show()        }else{            Toasty.success(c!!,"Hapus Berhasil").show()        }    }    private fun createCV(item :ItemModels):ContentValues{        var cv =ContentValues()        cv.put("ITEM_ID",item.ITEM_ID)        return cv    }}
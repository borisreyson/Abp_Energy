package com.misit.abpenergy.DataSourceimport android.content.ContentValuesimport android.content.Contextimport android.database.Cursorimport android.database.sqlite.SQLiteDatabaseimport com.misit.abpenergy.Model.PengendalianModelimport com.misit.abpenergy.SQLite.DbHelperimport es.dmoral.toasty.Toastyclass PengendalianDataSource (val c: Context) {    var dbHelper : DbHelper    var sqlDatabase : SQLiteDatabase?=null    var listItem :ArrayList<PengendalianModel>?=null    init {        listItem = ArrayList()        dbHelper = DbHelper(c)    }    private fun openAccess(){        sqlDatabase = dbHelper.writableDatabase    }    private fun closeAccess(){        sqlDatabase?.close()        dbHelper?.close()    }    fun insertItem(item: PengendalianModel):Long{        openAccess()        var cv = createCV(item)        var hasil = sqlDatabase?.insertOrThrow("$tbItem",null,cv)        closeAccess()        return hasil!!    }    fun getItem(idRisk: String): PengendalianModel {        openAccess()        val c = sqlDatabase?.rawQuery("SELECT * FROM "+                "$tbItem WHERE idRisk = ?", arrayOf(idRisk))        c?.moveToFirst()        var itemModels = PengendalianModel()        c?.let {            itemModels = fetchRow(it)        }        c?.close()        closeAccess()        return itemModels    }    fun getAll(): ArrayList<PengendalianModel> {        openAccess()        val c = sqlDatabase?.rawQuery("SELECT * FROM "+                "$tbItem ",null)        if(c!!.moveToFirst()){            do {                listItem?.add(fetchRow(c))            }while (c.moveToNext())        }        c?.close()        closeAccess()        return listItem!!    }    private fun fetchRow(cursor: Cursor): PengendalianModel {        val idRisk = cursor.getInt(cursor.getColumnIndex("idRisk"))        val risk = cursor.getString(cursor.getColumnIndex("risk"))        val descRisk = cursor.getString(cursor.getColumnIndex("descRisk"))        val txtColor = cursor.getString(cursor.getColumnIndex("txtColor"))        val bgColor = cursor.getString(cursor.getColumnIndex("bgColor"))        val userInput = cursor.getString(cursor.getColumnIndex("userInput"))        val tglInput = cursor.getString(cursor.getColumnIndex("tglInput"))        val lokasiModel = PengendalianModel()        lokasiModel.idRisk = idRisk        lokasiModel.risk = risk        lokasiModel.descRisk = descRisk        lokasiModel.txtColor = txtColor        lokasiModel.bgColor = bgColor        lokasiModel.userInput = userInput        lokasiModel.tglInput = tglInput        return lokasiModel    }    fun deleteItem(item:Int){        openAccess()        val hasil = sqlDatabase?.delete("$tbItem","idRisk = ? ", arrayOf(item.toString()))        if(hasil!! <0 ){            Toasty.error(c!!,"Gagal Hapus").show()        }else{            Toasty.success(c!!,"Hapus Berhasil").show()        }        closeAccess()    }    fun deleteAll():Boolean{        openAccess()        val hasil = sqlDatabase?.delete("$tbItem",null,null)        if(hasil!! <0 ){            return false        }        closeAccess()        return true    }    fun updateItem(item: PengendalianModel, idRisk:Int):Boolean{        openAccess()        val items = ContentValues()        items.put("risk",item.risk)        items.put("descRisk",item.descRisk)        items.put("txtColor",item.txtColor)        items.put("bgColor",item.bgColor)        items.put("tglInput",item.tglInput)        items.put("userInput",item.userInput)        val hasil = sqlDatabase?.update("$tbItem",items,"idRisk = ?", arrayOf("${idRisk}"))        if(hasil!! < 0){            return false        }        closeAccess()        return true    }    private fun createCV(item : PengendalianModel): ContentValues {        var cv = ContentValues()        cv.put("idRisk",item.idRisk)        cv.put("risk",item.risk)        cv.put("descRisk",item.descRisk)        cv.put("txtColor",item.txtColor)        cv.put("bgColor",item.bgColor)        cv.put("tglInput",item.tglInput)        cv.put("userInput",item.userInput)        return cv    }    companion object{        val tbItem = "PENGENDALIAN"    }}
package com.misit.abpenergy.HazardReport.Serviceimport android.content.Contextimport android.content.ContextWrapperimport android.content.Intentimport android.graphics.Bitmapimport android.graphics.BitmapFactoryimport android.net.Uriimport android.util.Logimport androidx.core.net.toUriimport androidx.localbroadcastmanager.content.LocalBroadcastManagerimport com.misit.abpenergy.Api.ApiClientimport com.misit.abpenergy.Api.ApiEndPointimport com.misit.abpenergy.DataSource.SchedulerDataSourceimport com.misit.abpenergy.HazardReport.Response.HazardReportResponseimport com.misit.abpenergy.HazardReport.SQLite.DataSource.HazardDetailDataSourceimport com.misit.abpenergy.HazardReport.SQLite.DataSource.HazardHeaderDataSourceimport com.misit.abpenergy.HomePage.IndexActivityimport com.misit.abpenergy.Utils.ConfigUtilimport kotlinx.coroutines.*import okhttp3.MediaType.Companion.toMediaTypeOrNullimport okhttp3.MultipartBodyimport okhttp3.RequestBodyimport okhttp3.RequestBody.Companion.asRequestBodyimport okhttp3.RequestBody.Companion.toRequestBodyimport retrofit2.Responseimport java.io.Fileimport java.io.IOExceptionclass HazardSaveOffline {    private var csrf_token:String?=null    private var bitmap: Bitmap?=null    private var bitmapBuktiSelesai: Bitmap?=null    private var bitmapPJ: Bitmap?=null    private var fileUpload: Uri?=null    private var fileUploadSelesai: Uri?=null    private var fileUploadPJ: Uri?=null    private var dir:String="HAZARD_OFFLINE"    lateinit var hazarHeader : HazardHeaderDataSource    lateinit var hazardDetail : HazardDetailDataSource    lateinit var scheduler : SchedulerDataSource    private val TAG = "HazardSaveOffline"    suspend fun getToken(c: Context,name: String,msg: String) {        scheduler= SchedulerDataSource(c)        try {            coroutineScope {                val getSchedule = async { scheduler.getAll() }                if(getSchedule.await().size>0){                    checkHazard(c,name,msg)                    scheduler.deleteAll()                }else{                    sendMessageToActivity(c,name,"KOSONG")                    Log.d(TAG,"Tidak Ada Schedule")                }            }        }catch (e:Exception){            Log.d(TAG,"Error Scheduler")            getToken(c,name,msg)        }    }    suspend fun checkHazard(c: Context,name: String,msg: String) {        try {            hazarHeader = HazardHeaderDataSource(c)            coroutineScope {                val headerRow = async { hazarHeader.getAll() }                if (headerRow.await().size>0){                    Log.d(TAG,"Offline Hazard Ada ${headerRow.await().size}")                    corotineToken(c, name, msg)                }else{                    Log.d(TAG,"Offline Hazard Kosong")                    sendMessageToActivity(c,name,"KOSONG")                    sendNotification(c,"Offline Hazard Kosong")                }            }        }catch (e:Exception){            Log.d(TAG,"${e.message}")            checkHazard(c,name,msg)        }    }    suspend fun corotineToken(c: Context,name: String,msg: String){        try {            val apiEndPoint = ApiClient.getClient(c)!!.create(ApiEndPoint::class.java)            CoroutineScope(Dispatchers.IO).launch {                val call = async { apiEndPoint.getTokenCorutine("csrf_token") }                val result = call.await()                if (result != null) {                    if (result.isSuccessful) {                        val tokenRes = async { result.body() }.await()                        if (tokenRes != null) {                            csrf_token = tokenRes.csrfToken                            Log.d(TAG, "${csrf_token}")                            Log.d(TAG,"CsrfToken Ada ${tokenRes.csrfToken}")                            loadHazardSQL(c, name, msg)                        } else {                            Log.d(TAG,"CsrfToken Kosong")                            corotineToken(c, name, msg)                        }                    } else {                        Log.d(TAG,"CsrfToken Kosong")                        corotineToken(c, name, msg)                    }                }            }        }catch (e:Exception){            Log.d(TAG,"${e.message}")            corotineToken(c, name, msg)        }    }    suspend fun loadHazardSQL(c:Context,name:String,msg: String){        try {            hazarHeader = HazardHeaderDataSource(c)            coroutineScope {                val headerRow = async { hazarHeader.getAll() }                if (headerRow.await().size>0){                    val result = headerRow.await()                    result.forEach {                        Log.d("InsertUsername","${it.user_input}")                        hazardDetail = HazardDetailDataSource(c)                        val getDetail = async { hazardDetail.getItem(it.idHazard.toString()) }                        if(getDetail.await()!=null){                            val detailFirst =getDetail.await()                            var inPerusahan = it.perusahaan?.toRequestBody(MultipartBody.FORM)                            var inTanggal = it.tgl_hazard?.toRequestBody(MultipartBody.FORM)                            var inJam = it.jam_hazard?.toRequestBody(MultipartBody.FORM)                            var lokasi = it.lokasi?.toRequestBody(MultipartBody.FORM)                            var inLokasiDet = it.lokasi_detail?.toRequestBody(MultipartBody.FORM)                            var inBahaya = it.deskripsi?.toRequestBody(MultipartBody.FORM)                            var kemungkinanID = it.idKemungkinan.toString().toRequestBody(MultipartBody.FORM)                            var keparahanID = it.idKeparahan.toString().toRequestBody(MultipartBody.FORM)                            var kemungkinanSesudahID = detailFirst.idKemungkinanSesudah.toString().toRequestBody(MultipartBody.FORM)                            var keparahanSesudahID =detailFirst.idKeparahanSesudah.toString().toRequestBody(MultipartBody.FORM)                            var hirarkiID = detailFirst.idKeparahanSesudah.toString().toRequestBody(MultipartBody.FORM)                            var inPerbaikan = detailFirst.tindakan.toString().toRequestBody(MultipartBody.FORM)                            var pjNama = detailFirst.namaPJ.toString().toRequestBody(MultipartBody.FORM)                            var pjNik = detailFirst.nikPJ.toString().toRequestBody(MultipartBody.FORM)                            var plKondisi = detailFirst.katBahaya.toString().toRequestBody(MultipartBody.FORM)                            var rbStatus= it.status_perbaikan.toString().toRequestBody(MultipartBody.FORM)                            var inTGLSelesai = detailFirst.tgl_selesai.toString().toRequestBody(MultipartBody.FORM)                            var inJamSelesai = detailFirst.jam_selesai.toString().toRequestBody(MultipartBody.FORM)                            var tglTenggat = detailFirst.tgl_tenggat.toString().toRequestBody(MultipartBody.FORM)                            var inKeteranganPJ = detailFirst.keterangan_update.toString().toRequestBody(MultipartBody.FORM)                            var username = it.user_input?.toRequestBody(MultipartBody.FORM)                            var _token: RequestBody = csrf_token!!.toRequestBody(MultipartBody.FORM)                            var imgDir =c.getExternalFilesDir(dir)                            var fileBukti = File(imgDir,detailFirst.bukti)                            //TEMUAN                            fileUpload =  "file:///${fileBukti}".toUri()                            try {                                bitmap = BitmapFactory.decodeStream(                                    c.contentResolver.openInputStream(fileUpload!!)                                )                            } catch (e: IOException) {                                e.printStackTrace();                            }                            val wrapper = ContextWrapper(c.applicationContext)                            var file = wrapper.getDir("images", Context.MODE_PRIVATE)                            file = File(file, fileBukti.name)                            ConfigUtil.streamFoto(bitmap!!, file)                            var fileUri = file.asRequestBody("image/*".toMediaTypeOrNull())                            var bukti = MultipartBody.Part.createFormData("fileToUpload", file.name, fileUri)                            //TEMUAN                            //penanggung jawab                            var fileFotoPJ = File(imgDir,detailFirst.fotoPJ)                            fileUploadPJ = "file:///${fileFotoPJ}".toUri()                            try {                                bitmapPJ = BitmapFactory.decodeStream(                                    c.contentResolver.openInputStream(fileUploadPJ!!)                                )                            } catch (e: IOException) {                                e.printStackTrace();                            }                            var pjFile = wrapper.getDir("image",Context.MODE_PRIVATE)                            pjFile = File(pjFile,fileFotoPJ.name)                            ConfigUtil.streamFoto(bitmapPJ!!, pjFile)                            var fileUriPJ = pjFile.asRequestBody("image/*".toMediaTypeOrNull())                            var fotoPJ = MultipartBody.Part.createFormData("fileToUploadPJ", pjFile.name, fileUriPJ)                            //penanggung jawab                            //bukti selesai                            var buktiSelesai:MultipartBody.Part?=null                            if(detailFirst.update_bukti!=""){                                var fotoSelesai = File(imgDir,detailFirst.update_bukti)                                fileUploadSelesai = "file:///${fotoSelesai}".toUri()                                try {                                    bitmapBuktiSelesai = BitmapFactory.decodeStream(                                        c.contentResolver.openInputStream(fileUploadSelesai!!)                                    )                                } catch (e: IOException) {                                    e.printStackTrace();                                }                                var selesaiFile = wrapper.getDir("image",Context.MODE_PRIVATE)                                selesaiFile = File(selesaiFile,fotoSelesai.name)                                ConfigUtil.streamFoto(bitmapBuktiSelesai!!, selesaiFile)                                var fileUriSelesai = selesaiFile.asRequestBody("image/*".toMediaTypeOrNull())                                buktiSelesai = MultipartBody.Part.createFormData("fileToUploadSelesai", selesaiFile.name, fileUriSelesai)                            //bukti selesai                                simpanOnline(bukti,                                    fotoPJ,                                    buktiSelesai,                                    inPerusahan!!,                                    inTanggal!!,                                    inJam!!,                                    inBahaya!!,                                    lokasi!!,                                    inLokasiDet!!,                                    kemungkinanID,                                    keparahanID,                                    plKondisi!!,                                    hirarkiID,                                    inPerbaikan,                                    pjNama,                                    pjNik,                                    rbStatus!!,                                    inTGLSelesai,                                    inJamSelesai,                                    inKeteranganPJ,                                    username!!,                                    _token, "Bukti_Selesai",                                    kemungkinanSesudahID,                                    keparahanSesudahID,                                    tglTenggat,                                    it.idHazard,                                    c,                                    name,                                    msg)                            }else{                                simpanOnline(                                    bukti,                                    fotoPJ,                                    buktiSelesai,                                    inPerusahan!!,                                    inTanggal!!,                                    inJam!!,                                    inBahaya!!,                                    lokasi!!,                                    inLokasiDet!!,                                    kemungkinanID,                                    keparahanID,                                    plKondisi!!,                                    hirarkiID,                                    inPerbaikan,                                    pjNama,                                    pjNik,                                    rbStatus!!,                                    inTGLSelesai,                                    inJamSelesai,                                    inKeteranganPJ,                                    username!!,                                    _token, "Bukti_Progress",                                    kemungkinanSesudahID,                                    keparahanSesudahID,                                    tglTenggat,                                    it.idHazard,                                    c,                                    name,                                    msg                                )                            }                        }                    }                }else{                    Log.d(TAG,"Data Tidak Ada")                    Log.d(TAG,msg)                    Log.d(TAG,name)                    sendMessageToActivity(c,name,msg)                }            }        }catch (e:Exception){            Log.d(TAG,"${e.message}")            loadHazardSQL(c, name, msg)        }    }    fun simpanOnline(        fileBukti: MultipartBody.Part,        filePJ: MultipartBody.Part?,        fileSelesai: MultipartBody.Part?,        perusahaan: RequestBody,        tanggal: RequestBody,        jam: RequestBody,        bahaya: RequestBody,        lokasi: RequestBody,        lokasiDet: RequestBody,        kemungkinan: RequestBody,        keparahan: RequestBody,        kondisi: RequestBody,        hirarki: RequestBody,        perbaikan: RequestBody,        namaPJ: RequestBody,        nikPJ: RequestBody,        status: RequestBody,        tglSelesai: RequestBody,        jamSelesai: RequestBody,        keteranganPJ: RequestBody,        user: RequestBody,        token: RequestBody,        tipe: String,        kemungkinanSesudah: RequestBody,        keparahanSesudah: RequestBody,        tglTenggat: RequestBody,        idHazard:Int?,        c: Context,        name: String,        msg: String    ){        try {            GlobalScope.launch(Dispatchers.IO) {                val apiEndPoint = ApiClient.getClient(c)!!.create(ApiEndPoint::class.java)                var post: Response<HazardReportResponse>?=null                if(tipe=="Bukti_Progress"){                    post = apiEndPoint.postHazardCorutine(                        fileBukti, filePJ, perusahaan, tanggal, jam, lokasi,                        lokasiDet, bahaya, kemungkinan, keparahan, kondisi, hirarki, perbaikan,                        namaPJ, nikPJ, status, tglTenggat, user, token                    )                }else if(tipe=="Bukti_Selesai"){                    post = apiEndPoint.postHazardSelesaiCorutine(                        fileBukti,                        filePJ,                        fileSelesai,                        perusahaan,                        tanggal,                        jam,                        lokasi,                        lokasiDet,                        bahaya,                        kemungkinan,                        keparahan,                        kemungkinanSesudah,                        keparahanSesudah,                        kondisi,                        hirarki,                        perbaikan,                        namaPJ,                        nikPJ,                        status,                        tglSelesai,                        jamSelesai,                        keteranganPJ,                        user,                        token                    )                }                if(post!!.isSuccessful){                    var res = post.body()                    if(res!!.success!!){                        hazarHeader = HazardHeaderDataSource(c)                        hazardDetail = HazardDetailDataSource(c)                        val hazardValidation = HazardDetailDataSource(c)                        try {                            if(hazarHeader.deleteItem(idHazard!!)){                                Log.d(TAG,"Hazard Header Delete")                                var file = File(fileUpload!!.path)                                if(file.exists()){                                    if(file.delete()){                                        Log.d(TAG,"File Bukti Delete")                                        if(hazardDetail.deleteItem(idHazard!!)){                                            Log.d(TAG,"Hazard Detail Delete")                                            var filePJ = File(fileUploadPJ!!.path)                                            if(filePJ.exists()){                                                if(file.delete()){                                                    Log.d(TAG,"File PJ Delete")                                                    if(hazardValidation.deleteItem(idHazard!!)){                                                        Log.d(TAG,"Hazard Detail Delete")                                                        val fileSelesai = File(fileUploadSelesai!!.path)                                                        if(fileSelesai.exists()){                                                            if(fileSelesai.delete()){                                                                Log.d(TAG,"File Selesai Delete")                                                                sendMessageToActivity(c,name,"HAZARD_DIBUAT")                                                            }                                                        }                                                    }                                                }                                            }                                        }                                    }                                }                            }                            Log.d(TAG,"Di Simpan Keserver Berhasil")                            sendMessageToActivity(c,name,msg)                        }catch (e:Exception){                            e.printStackTrace()                        }                    }else{                        Log.d(TAG,"Di Simpan Keserver Gagal")                        sendMessageToActivity(c,name,msg)                    }                    Log.d(TAG,res.toString())                }else{                    Log.d(TAG,"Mengakses Keserver Gagal")                    sendMessageToActivity(c,name,msg)                    Log.d(TAG,post.message().toString())                }            }        }catch (e:Exception){            Log.d(TAG,"${e.message}")            simpanOnline(                fileBukti,                filePJ,                fileSelesai,                perusahaan!!,                tanggal!!,                jam!!,                bahaya!!,                lokasi!!,                lokasiDet!!,                kemungkinan,                keparahan,                kondisi!!,                hirarki,                perbaikan,                namaPJ,                nikPJ,                status!!,                tglSelesai,                jamSelesai,                keteranganPJ,                user!!,                token, tipe,                kemungkinanSesudah,                keparahanSesudah,                tglTenggat,                idHazard,                c,                name,                msg            )        }    }    private fun sendMessageToActivity(c:Context,name:String,msg: String) {        Log.d(TAG,"sendMessageToActivity")        val intent = Intent()        intent.action = "com.misit.abpenergy"        intent.putExtra(name, msg)        LocalBroadcastManager.getInstance(c).sendBroadcast(intent)    }    private fun sendNotification(c:Context,pesan:String){        val intent = Intent(c, IndexActivity::class.java).apply {            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK        }        var rCode = 26011995        ConfigUtil.showNotification(c,"Job Scheduler","Notification ${pesan}",intent,rCode,"Job Scheduler")    }}
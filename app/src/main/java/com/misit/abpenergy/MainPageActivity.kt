package com.misit.abpenergyimport android.content.Contextimport android.content.Intentimport android.content.pm.PackageInfoimport android.content.pm.PackageManagerimport androidx.appcompat.app.AppCompatActivityimport android.os.Bundleimport android.os.Handlerimport android.util.Logimport com.bumptech.glide.Glideimport com.google.android.play.core.appupdate.AppUpdateManagerimport com.google.android.play.core.appupdate.AppUpdateManagerFactoryimport com.google.android.play.core.install.model.AppUpdateTypeimport com.google.android.play.core.install.model.UpdateAvailabilityimport com.misit.abpenergy.Api.ApiClientimport com.misit.abpenergy.Api.ApiEndPointimport com.misit.abpenergy.Api.ApiEndPointTwoimport com.misit.abpenergy.Main.HomePage.IndexActivityimport com.misit.abpenergy.Login.LoginActivityimport com.misit.abpenergy.Main.DataSource.PerusahaanDataSourceimport com.misit.abpenergy.Main.Model.KaryawanModelimport com.misit.abpenergy.Main.Model.PerusahaanModelimport com.misit.abpenergy.Sarpras.SQLite.DataSource.PenumpangDataSourceimport com.misit.abpenergy.Sarpras.SQLite.Model.PenumpangModelimport com.misit.abpenergy.Sarpras.SaranaResponse.PenumpangListModelimport com.misit.abpenergy.Utils.ConfigUtilimport com.misit.abpenergy.Utils.Constantsimport com.misit.abpenergy.Utils.PrefsUtilimport kotlinx.android.synthetic.main.activity_main_page.*import kotlinx.android.synthetic.main.menu_option.view.*import kotlinx.coroutines.*import java.util.*import kotlin.collections.ArrayListclass MainPageActivity : AppCompatActivity() {    private var updateApp : AppUpdateManager?=null    private var perusahaanDataSource: PerusahaanDataSource    private var penumpangDataSource: PenumpangDataSource?=null    private var penumpangModel:ArrayList<PenumpangListModel>?=null    var loginIntent :Intent?=null    var listKaryawan : ArrayList<KaryawanModel>? = null    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.activity_main_page)        updateApp = AppUpdateManagerFactory.create(this@MainPageActivity)        checkUpdate()        loginIntent=Intent(this, LoginActivity::class.java)        val id = ConfigUtil.deviceId(this@MainPageActivity)        Log.d("DeviceId",id)        dirInit(this@MainPageActivity)    }    init {        penumpangDataSource = PenumpangDataSource(this@MainPageActivity)        perusahaanDataSource = PerusahaanDataSource(this@MainPageActivity)    }    fun checkUpdate(){        if(updateApp!=null){            updateApp!!.appUpdateInfo.addOnSuccessListener { updateInfo ->                Log.d("checkUpdate","Run")                if(updateInfo.updateAvailability()==UpdateAvailability.UPDATE_AVAILABLE                    && updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){                    updateApp!!.startUpdateFlowForResult(updateInfo,AppUpdateType.IMMEDIATE,this,Constants.APP_IN_UPDATE)                }else{                    dirInit(this@MainPageActivity)                }            }        }else{            Log.d("checkUpdate","$updateApp")        }    }    private suspend fun loadPerusahaan(c: Context) {        var i = 1        var len =0        GlobalScope.launch(Dispatchers.Main) {            val deleted = async { perusahaanDataSource.deleteAll() }            if(deleted.await()){                try{                    val deferred =async {                        val apiEndPoint = ApiClient.getClient(c)?.create(ApiEndPointTwo::class.java)                        val perusahaanModel = PerusahaanModel()                        val response = apiEndPoint?.perusahaanCorutine()                        if (response != null) {                            if (response.isSuccessful) {                                val perusahaanRes = response.body()?.company                                if (perusahaanRes != null) {                                    len = perusahaanRes.size                                    perusahaanRes.forEach {                                        perusahaanModel.idPerusahaan = it.idPerusahaan                                        perusahaanModel.namaPerusahaan = it.namaPerusahaan                                        perusahaanModel.flag = it.flag                                        perusahaanModel.timeIn = it.timeIn                                        async { perusahaanDataSource.insertItem(perusahaanModel) }.await()                                        i++                                    }                                }                            }                        }                        if (i >= len) {                            true                        } else {                            false                        }                    }                    if(!deferred.await()){                        loadPerusahaan(c)                    }else{                        corutineSarana(c)                    }                }catch (e: Exception){//                    loadUsers(name,msg,c,username)                    Log.d("Error Corotine","${e.printStackTrace()}")//                        Toasty.info(c,"${e.message}")                }            }        }    }    suspend fun corutineSarana(c:Context){        try {            GlobalScope.launch(Dispatchers.Main){                var i = 1                var len =0                val def = async {                    penumpangModel?.clear()                    penumpangModel = ArrayList()                    try{                        listKaryawan?.clear()                        val apiEndPoint = ApiClient.getClient(c)!!.create(ApiEndPoint::class.java)                        val response = apiEndPoint.corutineAllSarana()                        if (response.isSuccessful) {                            val r = response.body()?.karyawan                            Log.d("UserUpdate","${r} 99")                            r.let {                                if(it!=null){                                    len = r?.size!!                                    r?.forEach {                                        val cekUser = async {  penumpangDataSource!!.cekUser("${it.nik}","${penumpangDataSource}")}                                        if(cekUser.await()<1) {                                            penumpangDataSource!!.deleteItem("${it.nik}")                                            penumpangModel!!.add(                                                PenumpangListModel(                                                    i.toLong(),                                                    it!!.nik!!,                                                    it!!.nama!!,                                                    it!!.jabatan!!,                                                    it!!.dt_update                                                )                                            )                                            i++                                        }                                    }                                    PrefsUtil.getInstance()                                        .setStringState(                                            PrefsUtil.AWAL_BULAN,                                            response.body()?.awalBulan                                        )                                    PrefsUtil.getInstance()                                        .setStringState(                                            PrefsUtil.AKHIR_BULAN,                                            response.body()?.akhirBulan                                        )                                }                            }                        } else {                            false                        }                        if (i >= len) {                            true                        } else {                            false                        }                    }catch (e: Exception){//                    loadUsers(name,msg,c,username)                        Log.d("Error Corotine","${e.printStackTrace()}")//                        Toasty.info(c,"${e.message}")                        false                    }                }                if(def.await()){                    async { copyPenumpang(penumpangModel!!) }.await()                }else{                }            }        }catch (e:Exception){            Log.d("Error Corotine","${e.printStackTrace()}")        }    }    suspend private fun copyPenumpang(penumpang: ArrayList<PenumpangListModel>){        var i = 1        var len =0        GlobalScope.launch(Dispatchers.Main){           var def = async {                var p = PenumpangModel()               if(penumpang!=null){                   len =penumpang.size                   async(Dispatchers.Main) {                       penumpang.map {                           async(Dispatchers.Main) {                               p.id = it.id                               p.nik = it.nik                               p.nama = it.nama                               p.jabatan = it.jabatan                               p.penumpang_update = it.penumpang_update                               penumpangDataSource!!.insertItem(p)                               i++                           }                       }                   }.await()               }                if (i >= len) {                    true                } else {                    false                }            }            if(def.await()){                startActivity(loginIntent)                finish()            }        }    }    fun proggressUpdate(){        updateApp!!.appUpdateInfo!!.addOnSuccessListener  { updateInfo ->            Log.d("checkUpdate","Run")            if(updateInfo.updateAvailability()==UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS                && updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){                updateApp!!.startUpdateFlowForResult(updateInfo,AppUpdateType.IMMEDIATE,this,Constants.APP_IN_UPDATE)            }else{                dirInit(this@MainPageActivity)            }        }    }    private fun firstInstall(c:Context){        if(PrefsUtil.getInstance().getBooleanState("INTRO_APP",false)){            loadingAnimate()        }else{            startActivity(Intent(c,IntroActivity::class.java))            finish()        }    }    fun loadingAnimate(){        var handler =Handler()        val runnable= java.lang.Runnable{            var besar = progressHorizontal.progress            progressHorizontal.progress = besar + 100            if(besar<100){                loadingAnimate()            } else {                if(PrefsUtil.getInstance().getBooleanState("IS_LOGGED_IN", false))                {                    val intent = Intent(this, IndexActivity::class.java)                    startActivity(intent)                    finish()                }                else                {                    GlobalScope.launch(Dispatchers.Main) {                        loadPerusahaan(this@MainPageActivity)                    }                }            }            Log.d("Besar","${besar}")        }        handler.postDelayed(            runnable        , 100)    }    override fun onResume() {        Locale.setDefault(Locale.US)        proggressUpdate()        versionApp()        super.onResume()    }    private fun dirInit(c:Context){        Glide.with(c).load(R.drawable.abp).into(imageView)        PrefsUtil.initInstance(c)        ConfigUtil.changeColor(this@MainPageActivity)        ConfigUtil.deleteInABPIMAGES(c,"ABP_IMAGES")        ConfigUtil.createFolder(c,"ABP_IMAGES")        ConfigUtil.createFolder(c,"PROFILE_IMAGE")        ConfigUtil.createFolder(c,"HAZARD_TEMP")        ConfigUtil.createFolder(c,"HAZARD_OFFLINE")        ConfigUtil.createFolder(c,"TEMUAN")        ConfigUtil.createFolder(c,"PENANGGUNG_JAWAB")        ConfigUtil.createFolder(c,"PERBAIKAN")        firstInstall(c)    }    fun versionApp(){        Use@ try {            val pInfo: PackageInfo = this.getPackageManager().getPackageInfo(packageName, 0)            tvVersionCode.text = pInfo.versionName        } catch (e: PackageManager.NameNotFoundException) {            e.printStackTrace()        }    }    companion object {        private  var TAG="MainPageLog"    }}
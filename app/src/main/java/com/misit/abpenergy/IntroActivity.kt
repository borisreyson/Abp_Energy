package com.misit.abpenergyimport android.Manifestimport android.app.Activityimport android.content.Contextimport android.content.Intentimport android.graphics.Colorimport android.os.Buildimport androidx.appcompat.app.AppCompatActivityimport android.os.Bundleimport android.util.Logimport android.view.ViewGroup.LayoutParams.WRAP_CONTENTimport android.widget.LinearLayoutimport android.widget.TextViewimport androidx.appcompat.app.AlertDialogimport androidx.core.view.getimport androidx.viewpager2.widget.ViewPager2import com.misit.abpenergy.Api.ApiClientimport com.misit.abpenergy.Api.ApiEndPointimport com.misit.abpenergy.Api.ApiEndPointTwoimport com.misit.abpenergy.Main.DataSource.PerusahaanDataSourceimport com.misit.abpenergy.Main.IntroFragment.IntroSlideimport com.misit.abpenergy.Main.IntroFragment.IntroSliderAdapterimport com.misit.abpenergy.Main.Model.KaryawanModelimport com.misit.abpenergy.Main.Model.PerusahaanModelimport com.misit.abpenergy.OlderClass.NewIndexActivityimport com.misit.abpenergy.Sarpras.SQLite.DataSource.PenumpangDataSourceimport com.misit.abpenergy.Sarpras.SQLite.Model.PenumpangModelimport com.misit.abpenergy.Sarpras.SaranaResponse.PenumpangListModelimport com.misit.abpenergy.Utils.ManagePermissionsimport com.misit.abpenergy.Utils.PrefsUtilimport kotlinx.android.synthetic.main.activity_intro.*import kotlinx.android.synthetic.main.activity_new_hazard.*import kotlinx.android.synthetic.main.fragment_slider.*import kotlinx.coroutines.*class IntroActivity : AppCompatActivity() {    private var adapter:IntroSliderAdapter?=null    private var listTex:List<IntroSlide>?=null    var perusahaanDataSource: PerusahaanDataSource    private var penumpangDataSource: PenumpangDataSource?=null    private var penumpangModel:ArrayList<PenumpangListModel>?=null    var listKaryawan : ArrayList<KaryawanModel>? = null    lateinit var activity: Activity    private val requestPermissionCode= 13    private val listPermission = listOf(        Manifest.permission.READ_PHONE_STATE,        Manifest.permission.CAMERA,        Manifest.permission.WRITE_EXTERNAL_STORAGE,    )    init {        penumpangDataSource = PenumpangDataSource(this@IntroActivity)        perusahaanDataSource = PerusahaanDataSource(this@IntroActivity)    }    override fun onResume() {        GlobalScope.launch(Dispatchers.Main) {            loadPerusahaan(this@IntroActivity)        }        super.onResume()    }    private lateinit var managePermissions: ManagePermissions    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.activity_intro)        activity= this        managePermissions = ManagePermissions(this@IntroActivity,listPermission,requestPermissionCode)        PrefsUtil.initInstance(activity)        if(PrefsUtil.getInstance().getBooleanState("INTRO_APP",false)){            startActivity(Intent(activity, NewIndexActivity::class.java))            finish()        }        listTex = listOf(IntroSlide(            "Selamat Datang",            "Aplikasi Ini Membutuhkan Izin Untuk Penggunaan Internet, lihat sambungan Wi-Fi, Penyimpanan Internal dan External untuk menyimpan data atau foto sementara sebelum di upload ke server",            "Mohon Untuk Mengizinkan Penggunaan Internet , Penyimpanan Internal dan Penyimpanan External supaya aplikasi ini berjalan dengan baik"        ),            IntroSlide("Persyaratan",                "Aplikasi Ini Membutuhkan Izin Untuk Penggunaan Camera, Audio , Video untuk pengambilan gambar, Mikrofon, memodifikasi atau menghapus konten penyimpanan USB Anda, membaca konten penyimpanan USB Anda",                "Mohon Untuk Mengizinkan Penggunaan Camera, Audio , Video untuk pengambilan gambar"            )        ,            IntroSlide(                "Persyaratan",                "Aplikasi Ini Membutuhkan Izin Untuk Penggunaan baca identitas dan status ponsel, terima data dari internet, lihat koneksi jaringan, akses jaringan penuh, cegah perangkat agar tidak tidur",                "Mohon Untuk Mengizinkan Penggunaan Internet , Penyimpanan Internal dan Penyimpanan External supaya aplikasi ini berjalan dengan baik"            )        )        adapter = IntroSliderAdapter(listTex!!)        viewPager.adapter = adapter        btnSkip.setOnClickListener {            if(viewPager.currentItem+1 < adapter!!.itemCount) {                skipIntro()            }else{                startActivity(Intent(activity, NewIndexActivity::class.java))                finish()            }        }        setupIndicator()        setCurrentIndicator(0)        viewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){            override fun onPageSelected(position:Int){                super.onPageSelected(position)                if(viewPager.currentItem+1 >= adapter!!.itemCount) {                    btnNext.text ="Setujui"                }                setCurrentIndicator(position)            }        })        btnNext.setOnClickListener {            if(viewPager.currentItem+1 < adapter!!.itemCount){                viewPager.currentItem +=1            }else{                btnNext.text ="Setujui"                val INTRO_APP = PrefsUtil.getInstance().setBooleanState("INTRO_APP",true)                if(INTRO_APP){                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)                        managePermissions.checkPermissions()                    }            }        }    }    override fun onRequestPermissionsResult(        requestCode: Int,        permissions: Array<String>,        grantResults: IntArray    ) {        when (requestCode) {            requestPermissionCode -> {                val isPermissionsGranted = managePermissions                    .processPermissionsResult(requestCode, permissions, grantResults)                if (isPermissionsGranted) {                    startActivity(Intent(this@IntroActivity, MainPageActivity::class.java))                    finish()                } else {                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {                        managePermissions.checkPermissions()                    }                }                return            }        }        super.onRequestPermissionsResult(requestCode, permissions, grantResults)    }    private fun skipIntro() {        AlertDialog.Builder(this@IntroActivity)            .setTitle("Aplikasi ini Tidak Bisa Digunakan Jika Izin Tidak Diberikan")            .setPositiveButton("Oke, Tutup", { dialog,                                                  which ->                finish()            })            .setNegativeButton("Batal",                { dialog,                  which ->                    dialog.dismiss()                })            .show()    }    private fun setupIndicator(){        val indicator = arrayOfNulls<TextView>(adapter!!.itemCount)        val layoutParams : LinearLayout.LayoutParams = LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT)        layoutParams.setMargins(0,0,0,0)        for (i in indicator.indices){            indicator[i] = TextView(applicationContext)                indicator[i].apply {                    this?.setTextColor(Color.GRAY)                    this?.textSize = 30.toFloat()                    this?.text= "-"                    this?.layoutParams= layoutParams                }            indicatorContainer.addView(indicator[i])        }    }    private fun setCurrentIndicator(position:Int){        val childCount = indicatorContainer.childCount        for (i in 0 until childCount){            val indicatortext = indicatorContainer[i] as TextView            if(i == position){                indicatortext.setTextColor(Color.BLACK)                indicatortext.setTextSize(50.toFloat())            }else{                indicatortext.setTextSize(30.toFloat())                indicatortext.setTextColor(Color.GRAY)            }        }    }    private suspend fun loadPerusahaan(c: Context) {        var i = 1        var len =0        CoroutineScope(Dispatchers.Main).launch {            val deleted = async { perusahaanDataSource.deleteAll() }            if(deleted.await()){                try{                    val deferred =async {                        val apiEndPoint = ApiClient.getClient(c)?.create(ApiEndPointTwo::class.java)                        var perusahaanModel = PerusahaanModel()                        val response = apiEndPoint?.perusahaanCorutine()                        if (response != null) {                            if (response.isSuccessful) {                                val perusahaanRes = response.body()?.company                                if (perusahaanRes != null) {                                    len = perusahaanRes?.size ?: 0                                    perusahaanRes.forEach {                                        perusahaanModel.idPerusahaan = it.idPerusahaan                                        perusahaanModel.namaPerusahaan = it.namaPerusahaan                                        perusahaanModel.flag = it.flag                                        perusahaanModel.timeIn = it.timeIn                                        perusahaanDataSource.insertItem(perusahaanModel)                                        i++                                    }                                }                            }                        }                        if (i >= len) {                            true                        } else {                            false                        }                    }                    if(!deferred.await()){                        loadPerusahaan(c)                    }else{                        corutineSarana(c)                    }                }catch (e: Exception){//                    loadUsers(name,msg,c,username)                    Log.d("Error Corotine","${e.printStackTrace()}")//                        Toasty.info(c,"${e.message}")                }            }        }    }    suspend fun corutineSarana(c:Context){        try {            penumpangModel?.clear()            penumpangModel = ArrayList()            GlobalScope.launch(Dispatchers.Main){                var i = 1                var len =0                val def = async {                    try{                        listKaryawan?.clear()                        val apiEndPoint = ApiClient.getClient(c)!!.create(ApiEndPoint::class.java)                        val response = apiEndPoint.corutineAllSarana()                        if (response.isSuccessful) {                            val r = response.body()?.karyawan                            Log.d("UserUpdate","${r} 99")                            r.let {                                if(it!=null){                                    len = r?.size!!                                    r?.forEach {                                        val cekUser = async {  penumpangDataSource!!.cekUser("${it.nik}","${penumpangDataSource}")}                                        if(cekUser.await()<1) {                                            penumpangDataSource!!.deleteItem("${it.nik}")                                            penumpangModel!!.add(                                                PenumpangListModel(                                                    i.toLong(),                                                    it!!.nik!!,                                                    it!!.nama!!,                                                    it!!.jabatan!!,                                                    it!!.dt_update                                                )                                            )                                            i++                                            Log.d("penumpangModel","${penumpangModel}")                                        }                                    }                                    PrefsUtil.getInstance()                                        .setStringState(                                            PrefsUtil.AWAL_BULAN,                                            response.body()?.awalBulan                                        )                                    PrefsUtil.getInstance()                                        .setStringState(                                            PrefsUtil.AKHIR_BULAN,                                            response.body()?.akhirBulan                                        )                                }                            }                        } else {                            false                        }                        if (i >= len) {                            Log.d("penumpangModel","${i} ${len}")                            true                        } else {                            false                        }                    }catch (e: Exception){//                    loadUsers(name,msg,c,username)                        Log.d("Error Corotine","${e.printStackTrace()}")//                        Toasty.info(c,"${e.message}")                        false                    }                }                if(def.await()){                    Log.d("penumpangModel","${penumpangModel}");                         copyPenumpang(penumpangModel!!)                }else{                }            }        }catch (e:Exception){            Log.d("Error Corotine","${e.printStackTrace()}")        }    }    suspend private fun copyPenumpang(penumpang: ArrayList<PenumpangListModel>){        var i=1        var len = 0        CoroutineScope(Dispatchers.Main).launch{            var a = async {                var p = PenumpangModel()                penumpang.forEach {                    p.id = it.id                    p.nik = it.nik                    p.nama = it.nama                    p.jabatan = it.jabatan                    p.penumpang_update = it.penumpang_update                    penumpangDataSource?.insertItem(p)                    i++                }                if (i >= len) {                    true                } else {                    false                }            }.await()            if(a){//                corutineSarana(this@IntroActivity)            }        }    }}
package com.misit.abpenergyimport android.Manifestimport android.app.Activityimport android.content.Contextimport android.content.DialogInterfaceimport android.content.Intentimport android.content.pm.PackageManagerimport android.graphics.Colorimport android.graphics.Typefaceimport android.graphics.drawable.ColorDrawableimport android.os.Buildimport android.os.Bundleimport android.util.TypedValueimport android.view.*import android.widget.TextViewimport android.widget.Toastimport androidx.appcompat.app.ActionBar.LayoutParamsimport androidx.appcompat.app.AlertDialogimport androidx.appcompat.app.AppCompatActivityimport androidx.core.app.ActivityCompatimport androidx.core.content.ContextCompatimport com.misit.abpenergy.Api.ApiClientimport com.misit.abpenergy.Api.ApiEndPointimport com.misit.abpenergy.HazardReport.*import com.misit.abpenergy.Inspeksi.*import com.misit.abpenergy.Login.FotoProfileActivityimport com.misit.abpenergy.Login.LoginActivityimport com.misit.abpenergy.Monitoring_Produksi.*import com.misit.abpenergy.Response.GetUserResponseimport com.misit.abpenergy.Rkb.RkbActivityimport com.misit.abpenergy.Sarpras.*import com.misit.abpenergy.Service.*import com.misit.abpenergy.Utils.ConfigUtilimport com.misit.abpenergy.Utils.PrefsUtilimport es.dmoral.toasty.Toastyimport kotlinx.android.synthetic.main.index_new.*import kotlinx.android.synthetic.main.menu_option.view.*import retrofit2.Callimport retrofit2.Callbackimport retrofit2.Responseimport java.util.*import kotlin.collections.ArrayListclass NewIndexActivity : AppCompatActivity(),View.OnClickListener {    private var userRule:Array<String>?=null    private val requestCodeCameraPermission = 1999    var tabindex:String?=null    private var tipe:String? = null    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.index_new)        if(ContextCompat.checkSelfPermission(                this@NewIndexActivity,                Manifest.permission.CAMERA            )!= PackageManager.PERMISSION_GRANTED){            askForCameraPermission()        }        PrefsUtil.initInstance(this)        if(PrefsUtil.getInstance().getBooleanState("IS_LOGGED_IN", false)){            USERNAME = PrefsUtil.getInstance().getStringState(PrefsUtil.USER_NAME, "")            NAMA_LENGKAP = PrefsUtil.getInstance().getStringState(PrefsUtil.NAMA_LENGKAP, "")            NIK = PrefsUtil.getInstance().getStringState(PrefsUtil.NIK, "")            DEPARTMENT = PrefsUtil.getInstance().getStringState(PrefsUtil.DEPT, "")            SECTON = PrefsUtil.getInstance().getStringState(PrefsUtil.SECTION, "")            LEVEL = PrefsUtil.getInstance().getStringState(PrefsUtil.LEVEL, "")            RULE = PrefsUtil.getInstance().getStringState(PrefsUtil.RULE, "")            tvUserName.text = NAMA_LENGKAP            if(!PrefsUtil.getInstance().getBooleanState("PHOTO_PROFILE",false)){                uploadProfile()            }            if(!PrefsUtil.getInstance().getBooleanState("INTRO_APP",false)){                uploadProfile()            }        }else{            val intent = Intent(this, LoginActivity::class.java)            startActivity(intent)            finish()        }    //        Rule User            userRule = RULE.split(",").toTypedArray()            var apprSarpras = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {                Arrays.stream(userRule).anyMatch{ t -> t== "approve sarpras"}            } else {                userRule?.contains("approve sarpras")            }            if(apprSarpras!!){                btnSarprasApproveKabag.visibility= View.VISIBLE            }else{                btnSarprasApproveKabag.visibility= View.GONE            }    //        Rule User        btnTotal.setOnClickListener(this)        btnApprove.setOnClickListener(this)        btnWaiting.setOnClickListener(this)        btnCancel.setOnClickListener(this)        btnSarpras.setOnClickListener(this)        btnNewSarpras.setOnClickListener(this)        btnClose.setOnClickListener(this)        btnSarprasApproveKabag.setOnClickListener(this)        btnOB.setOnClickListener(this)        btnHAULING.setOnClickListener(this)        btnCRUSHING.setOnClickListener(this)        btnBARGING.setOnClickListener(this)        btnSTOCKROOM.setOnClickListener(this)        btnSTOCKPRODUCT.setOnClickListener(this)        btnHazard.setOnClickListener(this)        btnNewHazard.setOnClickListener(this)        btnNewSarana.setOnClickListener(this)        btnHazardALL.setOnClickListener(this)        btnSarprasAll.setOnClickListener(this)        btnInspection.setOnClickListener(this)        btnQRCODES.setOnClickListener(this)        cvBarcodeProfile.setOnClickListener(this)        btnMenuTop.setOnClickListener(this)    }    private fun uploadProfile(){//        Toasty.info(this@NewIndexActivity,"Photo Null").show()        val intent = Intent(this@NewIndexActivity,FotoProfileActivity::class.java)        startActivityForResult(intent,1234)    }    fun Int.toDp(context: Context):Int = TypedValue.applyDimension(        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics    ).toInt()    override fun onOptionsItemSelected(item: MenuItem): Boolean {        Toasty.info(this@NewIndexActivity, item.toString()).show()        return super.onOptionsItemSelected(item)    }    private fun askForCameraPermission(){        ActivityCompat.requestPermissions(            this@NewIndexActivity,            arrayOf(Manifest.permission.CAMERA), requestCodeCameraPermission        )    }    override fun onClick(v: View?) {        btnFLMenuIndex.collapse()        if(v?.id==R.id.btnTotal){            var intent = Intent(this@NewIndexActivity, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, SECTON)            intent.putExtra(RkbActivity.LEVEL, LEVEL)            var tbindex = 0 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }        if(v?.id==R.id.btnApprove){            var intent = Intent(this@NewIndexActivity, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, SECTON)            intent.putExtra(RkbActivity.LEVEL, LEVEL)            var tbindex = 1 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }        if(v?.id==R.id.btnWaiting){            var intent = Intent(this@NewIndexActivity, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, SECTON)            intent.putExtra(RkbActivity.LEVEL, LEVEL)            var tbindex = 2 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }        if(v?.id==R.id.btnCancel){            var intent = Intent(this@NewIndexActivity, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, SECTON)            intent.putExtra(RkbActivity.LEVEL, LEVEL)            var tbindex = 3 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }        if(v?.id==R.id.btnClose){            var intent = Intent(this@NewIndexActivity, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, SECTON)            intent.putExtra(RkbActivity.LEVEL, LEVEL)            var tbindex = 4 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }        if(v?.id==R.id.btnSarpras){            var intent = Intent(                this@NewIndexActivity,                SarprasActivity::class.java            )            startActivity(intent)        }        if(v?.id==R.id.btnNewSarpras){            var intent = Intent(this@NewIndexActivity, NewSarprasActivity::class.java)            startActivity(intent)        }        if(v?.id==R.id.btnSarprasApproveKabag){            var intent = Intent(this@NewIndexActivity, KabagApprSarprasActivity::class.java)            startActivity(intent)        }        if(v?.id==R.id.btnOB){            var intent = Intent(this@NewIndexActivity, ProductionActivity::class.java)            intent.putExtra(ProductionActivity.MONITORING, "OB")            startActivity(intent)        }        if(v?.id==R.id.btnHAULING){            var  intent = Intent(this@NewIndexActivity, ProductionActivity::class.java)            intent.putExtra(ProductionActivity.MONITORING, "HAULING")            startActivity(intent)        }        if(v?.id==R.id.btnCRUSHING){            var  intent = Intent(this@NewIndexActivity, ProductionActivity::class.java)            intent.putExtra(ProductionActivity.MONITORING, "CRUSHING")            startActivity(intent)        }        if(v?.id==R.id.btnBARGING){            var intent = Intent(this@NewIndexActivity, ProductionActivity::class.java)            intent.putExtra(ProductionActivity.MONITORING, "BARGING")            startActivity(intent)        }        if(v?.id==R.id.btnSTOCKROOM){            var intent = Intent(this@NewIndexActivity, StockActivity::class.java)            intent.putExtra(ProductionActivity.MONITORING, "ROOM")            startActivity(intent)        }        if(v?.id==R.id.btnSTOCKPRODUCT){            var intent = Intent(this@NewIndexActivity, StockActivity::class.java)            intent.putExtra(ProductionActivity.MONITORING, "PRODUCT")            startActivity(intent)        }        if(v?.id==R.id.btnHazard){//            hazardReport()            userRule = RULE.split(",").toTypedArray()            var hseAdmin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {                Arrays.stream(userRule).anyMatch{ t -> t== "admin_hse"}            } else {                userRule?.contains("admin_hse")            }            if(hseAdmin!!) {                val list = arrayOf<String>("Hazard Report","Hazard Report Ke Saya","Seluruh Hazard Report")                showDialogOption(this@NewIndexActivity,list)            }else{                val list = arrayOf<String>("Hazard Report","Hazard Report Ke Saya")                showDialogOption(this@NewIndexActivity,list)            }        }        if(v!!.id==R.id.btnNewSarana){            newSarana()        }        if(v!!.id==R.id.btnNewHazard){            newHazardReport()        }        if(v?.id==R.id.content_frame){            btnFLMenuIndex.collapse()        }        if(v?.id==R.id.btnHazardALL){            var intent = Intent(this@NewIndexActivity, ALLHazardReportActivity::class.java)            startActivity(intent)        }        if(v?.id==R.id.btnSarprasAll){            var intent = Intent(this@NewIndexActivity, AllSarprasActivity::class.java)            startActivity(intent)        }        if(v?.id==R.id.btnInspection){            var intent = Intent(this@NewIndexActivity, InspeksiActivity::class.java)            startActivity(intent)        }        if(v?.id==R.id.btnInspectionALL){            var intent = Intent(this@NewIndexActivity, AllInspeksiActivity::class.java)            startActivity(intent)        }        if(v?.id==R.id.btnQRCODES){            var intent = Intent(this@NewIndexActivity, BarcodeScannerActivity::class.java)            intent.putExtra("aktivitas", "addTeam")            startActivity(intent)        }        if(v?.id==R.id.cvBarcodeProfile){            val intent = Intent(this@NewIndexActivity, QRCodeActivity::class.java)            intent.putExtra("itemCodes", NIK)            intent.putExtra("judul", "QR-Code Anda")            startActivity(intent)        }        if(v?.id==R.id.btnMenuTop){            profile(this@NewIndexActivity)        }    }    fun showDialogOption(c:Context, title: Array<String>){        val alertDialog = AlertDialog.Builder(c)        alertDialog.setTitle("Silahkan Pilih")        alertDialog!!.setItems(title, DialogInterface.OnClickListener{ dialog, which ->            when (which) {                0 ->hazardReport(c,"hazard")                1 ->hazardReport(c,"hazard_saya")                2 ->hazardReport(c,"hazard_hse")            }        })        alertDialog.create()        alertDialog.show()    }    private fun profile(c:Context){        val mDialogView = LayoutInflater.from(c).inflate(R.layout.menu_option,null)    val mBuilder = AlertDialog.Builder(c)        mBuilder.setView(mDialogView)        val dialog =mBuilder.show()        userRule = RULE.split(",").toTypedArray()        var apprSarpras = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {            Arrays.stream(userRule).anyMatch { t -> t == "master perusahaan" }        } else {            userRule?.contains("master perusahaan")        }        if (apprSarpras!!) {            mDialogView.btnMasterPerusahaan.visibility = View.VISIBLE        }else{            mDialogView.btnMasterPerusahaan.visibility = View.GONE        }        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))        mDialogView.dialogDismis.setOnClickListener {            dialog.dismiss()        }        mDialogView.btnChangePWD.setOnClickListener {            ConfigUtil.changePassword(c, USERNAME)            dialog.dismiss()        }        mDialogView.btnKeluarApp.setOnClickListener{            logOut()        }        mDialogView.btnUpdateData.setOnClickListener {        }        mDialogView.btnMasterPerusahaan.setOnClickListener {            ConfigUtil.masterPerusahaan(c)        }    }    override fun onRequestPermissionsResult(        requestCode: Int,        permissions: Array<out String>,        grantResults: IntArray    ) {//        if(requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()  && grantResults[0] == PackageManager.PERMISSION_GRANTED){////        }else{//            Toasty.error(this@BarcodeScannerActivity,"Permission Denied!").show()//        }        when (requestCode) {            requestCodeCameraPermission -> {                // If request is cancelled, the result arrays are empty.                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {                    // permission was granted, yay! Do the                    // contacts-related task you need to do.                    Toasty.info(this@NewIndexActivity, "PERMISSION_GRANTED", Toasty.LENGTH_SHORT)                        .show()                } else {                    // permission denied, boo! Disable the                    // functionality that depends on this permission.                    Toasty.info(this@NewIndexActivity, "PERMISSION_DENIED", Toasty.LENGTH_SHORT)                        .show()                }                return            }            // Add other 'when' lines to check for other            // permissions this app might request.            else -> {                // Ignore all other requests.                Toasty.info(this@NewIndexActivity, "PERMISSION_Ignore", Toasty.LENGTH_SHORT).show()            }        }        super.onRequestPermissionsResult(requestCode, permissions, grantResults)    }    //Get Token    private fun getToken() {        val apiEndPoint = ApiClient.getClient(this@NewIndexActivity)!!.create(ApiEndPoint::class.java)        val call = apiEndPoint.getDataUser(USERNAME)        call?.enqueue(object : Callback<GetUserResponse> {            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {                Toast.makeText(this@NewIndexActivity, "Error : $t", Toast.LENGTH_SHORT).show()            }            override fun onResponse(                call: Call<GetUserResponse>,                response: Response<GetUserResponse>            ) {                var res = response.body()                if (res != null) {                    var r = res.dataUser!!                    if(r.photoProfile!=null){                        PrefsUtil.getInstance()                            .setBooleanState("PHOTO_PROFILE",                                true)                    }else{                        PrefsUtil.getInstance()                            .setBooleanState("PHOTO_PROFILE",                                false)                    }                    if (r.rule != null) {                        RULE = r.rule!!                        PrefsUtil.getInstance()                            .setStringState(                                PrefsUtil.TOTAL_HAZARD_USER,                                res!!.dataHazard!!.toString()                            )                        tvHazardUser.text = res!!.dataHazard!!.toString()                        tvNIK.text = res!!.dataUser!!.nik.toString()                        tvDept.text = res!!.dataUser!!.dept                        tvSect.text = res!!.dataUser!!.sect                        rvCompany.text = res!!.dataUser!!.namaPerusahaan                        if (res!!.dataUser!!.perusahaan == 0) {                            lnSaranaPrasarana.visibility = View.VISIBLE                            lnRKBsystem.visibility = View.VISIBLE                            btnNewSarana.visibility = View.VISIBLE                        } else {                            lnSaranaPrasarana.visibility = View.GONE                            lnRKBsystem.visibility = View.GONE                            btnNewSarana.visibility = View.GONE                        }                        tvInspeksiUser.text = res!!.datInspeksi!!.toString()                        userRule = RULE.split(",").toTypedArray()                        var apprSarpras = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {                            Arrays.stream(userRule).anyMatch { t -> t == "approve sarpras" }                        } else {                            userRule?.contains("approve sarpras")                        }                        if (apprSarpras!!) {                            btnSarprasApproveKabag.visibility = View.VISIBLE                        } else {                            btnSarprasApproveKabag.visibility = View.GONE                        }                        var security = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {                            Arrays.stream(userRule).anyMatch { t -> t == "security" }                        } else {                            userRule?.contains("security")                        }                        if (security!!) {                            btnSarprasAll.visibility = View.VISIBLE                        } else {                            btnSarprasAll.visibility = View.GONE                        }                        var allHazard = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {                            Arrays.stream(userRule).anyMatch { t -> t == "allHazard" }                        } else {                            userRule?.contains("allHazard")                        }                        if (allHazard!!) {                            btnHazardALL.visibility = View.VISIBLE                        } else {                            btnHazardALL.visibility = View.GONE                        }                        var allInspeksi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {                            Arrays.stream(userRule).anyMatch { t -> t == "allInspeksi" }                        } else {                            userRule?.contains("allInspeksi")                        }                        if (allInspeksi!!) {                            btnInspectionALL.visibility = View.VISIBLE                        } else {                            btnInspectionALL.visibility = View.GONE                        }                        lnLoading.visibility = View.GONE                    }                }            }        })    }//    Get Token    //    On Resume    override fun onResume() {        lnLoading.visibility=View.VISIBLE//        Session        getToken()        tipe =  intent.getStringExtra(TIPE)        if(tipe=="rkb"){            rkbNotif("0")            intent.putExtra(TIPE, "")        }else if(tipe=="sarpras"){            sarprasNotif()            intent.putExtra(TIPE, "")        }        super.onResume()    }//On Resume    //    Sarpras Notif    private fun sarprasNotif(){        var intent = Intent(            this@NewIndexActivity,            SarprasActivity::class.java        )        intent.putExtra(RkbActivity.TIPE, "notif")        startActivity(intent)    }    //    Sarpras Notif//    NotifRkb    private fun rkbNotif(tabindex: String?){        var intent = Intent(this@NewIndexActivity, RkbActivity::class.java)        intent.putExtra(RkbActivity.USERNAME, USERNAME)        intent.putExtra(RkbActivity.DEPARTMENT, DEPARTMENT)        intent.putExtra(RkbActivity.SECTON, SECTON)        intent.putExtra(RkbActivity.LEVEL, LEVEL)        intent.putExtra(RkbActivity.Tab_INDEX, tabindex)        intent.putExtra(RkbActivity.TIPE, "notif")        startActivity(intent)    }//    NotifRkb    //    LogOut    private fun logOut() {        AlertDialog.Builder(this)            .setTitle("Confirmation")            .setPositiveButton("OK , Sign Out", { dialog,                                                  which ->                if (PrefsUtil.getInstance().getBooleanState(                        "IS_LOGGED_IN", true                    )                ) {                    PrefsUtil.getInstance().setBooleanState(                        "IS_LOGGED_IN", false                    )                    PrefsUtil.getInstance().setStringState(                        PrefsUtil.USER_NAME, null                    )                    val intent = Intent(this, LoginActivity::class.java)                    startActivity(intent)                    finish()                }            })            .setNegativeButton("Cancel",                { dialog,                  which ->                    dialog.dismiss()                })            .show()    }    //LogOut//    Companion    companion object{        var USERNAME = "username"        var DEPARTMENT="department"        var SECTON="section"        var LEVEL="level"        var Tab_INDEX ="tab_index"        var NAMA_LENGKAP = "nama_lengkap"        var NO_RKB = "NO_RKB"        var TIPE = null        var RULE = "RULE"        var NIK = "NIK"    }//    Companion//    HazardRepord    fun hazardReport(c:Context,pilih:String) {        var intent:Intent?=null        when(pilih){            "hazard"-> intent = Intent(c, HazardReportActivity::class.java)            "hazard_saya"->intent = Intent(c, HazardSayaActivity::class.java)            "hazard_hse"->intent = Intent(c, HazardHSEActivity::class.java)        }        startActivity(intent)    }    //    onActivityResult    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {        if(resultCode== Activity.RESULT_OK && requestCode==101){            var intent = Intent(this@NewIndexActivity,HazardReportActivity::class.java)            startActivity(intent)        }        super.onActivityResult(requestCode, resultCode, data)    }    //    onActivityResult    fun newSarana(){        btnFLMenuIndex.collapse()        var intent = Intent(this@NewIndexActivity, NewSarprasActivity::class.java)        startActivityForResult(intent, 102)    }    fun newHazardReport() {        btnFLMenuIndex.collapse()        var intent = Intent(this@NewIndexActivity, NewHazardActivity::class.java)        startActivityForResult(intent, 101)    }}
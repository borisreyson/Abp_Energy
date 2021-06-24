package com.misit.abpenergyimport android.Manifestimport android.app.Activityimport android.app.ActivityManagerimport android.content.*import android.content.pm.PackageManagerimport android.graphics.Colorimport android.graphics.drawable.ColorDrawableimport android.os.Buildimport android.os.Bundleimport android.os.CountDownTimerimport android.util.Logimport android.util.TypedValueimport android.view.*import android.widget.*import androidx.appcompat.app.AlertDialogimport androidx.appcompat.app.AppCompatActivityimport androidx.core.app.ActivityCompatimport androidx.core.content.ContextCompatimport androidx.localbroadcastmanager.content.LocalBroadcastManagerimport androidx.swiperefreshlayout.widget.CircularProgressDrawableimport com.bumptech.glide.Glideimport com.facebook.shimmer.ShimmerFrameLayoutimport com.google.android.gms.tasks.OnCompleteListenerimport com.google.android.material.bottomsheet.BottomSheetDialogimport com.google.firebase.messaging.FirebaseMessagingimport com.misit.abpenergy.Api.ApiClientimport com.misit.abpenergy.Api.ApiEndPointimport com.misit.abpenergy.HazardReport.*import com.misit.abpenergy.HazardReport.Service.HazardServiceimport com.misit.abpenergy.Inspeksi.*import com.misit.abpenergy.Login.FotoProfileActivityimport com.misit.abpenergy.Login.LoginActivityimport com.misit.abpenergy.Master.ListUserActivityimport com.misit.abpenergy.Monitoring_Produksi.*import com.misit.abpenergy.Response.GetUserResponseimport com.misit.abpenergy.Rkb.RkbActivityimport com.misit.abpenergy.Sarpras.*import com.misit.abpenergy.Sarpras.Service.SaranaServiceimport com.misit.abpenergy.Service.*import com.misit.abpenergy.Utils.ConfigUtilimport com.misit.abpenergy.Utils.Constantsimport com.misit.abpenergy.Utils.PrefsUtilimport es.dmoral.toasty.Toastyimport kotlinx.android.synthetic.main.index_new.*import kotlinx.android.synthetic.main.menu_option.view.*import kotlinx.coroutines.runBlockingimport retrofit2.Callimport retrofit2.Callbackimport retrofit2.Responseimport java.security.AccessController.getContextimport java.util.*class NewIndexActivity : AppCompatActivity(),View.OnClickListener {    private var timer:CountDownTimer?=null    private var userRule:Array<String>?=null    private val requestCodeCameraPermission = 1999    private var  mDialogView:View?=null    private var tipe:String? = null    var fotoURL:String?=null    private var shimmerLayout:View?=null    private var shimmerProfileImage:ShimmerFrameLayout?=null    private var shimmerHazardUser:ShimmerFrameLayout?=null    private var shimmerInspeksiUser:ShimmerFrameLayout?=null    private var shimmerUserName:ShimmerFrameLayout?=null    private var shimmerNIK:ShimmerFrameLayout?=null    private var shimmerView:ShimmerFrameLayout?=null    private var shimmerView1:ShimmerFrameLayout?=null    private var shimmerSection:ShimmerFrameLayout?=null    private var shimmerCompany:ShimmerFrameLayout?=null    private val tokenPassingReceiver: BroadcastReceiver = object : BroadcastReceiver() {        override fun onReceive(context: Context, intent: Intent) {            val bundle = intent.extras            if (bundle != null) {                if (bundle.containsKey("MyData")) {                    val tokenData = bundle.getString("MyData")                    Log.e("MainActivity--","token--$tokenData")                    tvTimer.text = tokenData                }            }        }    }    lateinit var saranaService:Intent    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.index_new)        title= "PT ALAMJAYA BARA PRATAMA"//        Glide.with(this).load(R.drawable.abp_logo).into(fotoProfile)//        Glide.with(this).load(R.drawable.abp_logo).into(miniFotoProfile)        shimmerLayout = findViewById(R.id.shimmerLayout)        shimmerProfileImage = shimmerLayout?.findViewById(R.id.shimmerProfileImage)        shimmerHazardUser = shimmerLayout?.findViewById(R.id.shimmerHazardUser)        shimmerHazardUser = shimmerLayout?.findViewById(R.id.shimmerHazardUser)        shimmerInspeksiUser = shimmerLayout?.findViewById(R.id.shimmerInspeksiUser)        shimmerUserName = shimmerLayout?.findViewById(R.id.shimmerUserName)        shimmerNIK = shimmerLayout?.findViewById(R.id.shimmerNIK)        shimmerView = shimmerLayout?.findViewById(R.id.shimmerView)        shimmerView1 = shimmerLayout?.findViewById(R.id.shimmerView1)        shimmerSection = shimmerLayout?.findViewById(R.id.shimmerSection)        shimmerCompany = shimmerLayout?.findViewById(R.id.shimmerCompany)        if(ContextCompat.checkSelfPermission(                this@NewIndexActivity,                Manifest.permission.CAMERA            )!= PackageManager.PERMISSION_GRANTED){            askForCameraPermission()        }        PrefsUtil.initInstance(this@NewIndexActivity)        if(PrefsUtil.getInstance().getBooleanState("IS_LOGGED_IN", false)){            USERNAME = PrefsUtil.getInstance().getStringState(PrefsUtil.USER_NAME, "")            NAMA_LENGKAP = PrefsUtil.getInstance().getStringState(PrefsUtil.NAMA_LENGKAP, "")            NIK = PrefsUtil.getInstance().getStringState(PrefsUtil.NIK, "")            DEPARTMENT = PrefsUtil.getInstance().getStringState(PrefsUtil.DEPT, "")            SECTON = PrefsUtil.getInstance().getStringState(PrefsUtil.SECTION, "")            LEVEL = PrefsUtil.getInstance().getStringState(PrefsUtil.LEVEL, "")            RULE = PrefsUtil.getInstance().getStringState(PrefsUtil.RULE, "")            PHOTO_PROFILE = PrefsUtil.getInstance().getBooleanState(PrefsUtil.PHOTO_PROFILE,false)            INTRO_APP = PrefsUtil.getInstance().getBooleanState("INTRO_APP",false)            COMPANY = PrefsUtil.getInstance().getStringState("COMPANY_NAME","")            tvUserName.text = NAMA_LENGKAP        }else{            val intent = Intent(this, LoginActivity::class.java)            startActivity(intent)            finish()        }    //        Rule User            userRule = RULE.split(",").toTypedArray()            var apprSarpras = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {                Arrays.stream(userRule).anyMatch{ t -> t== "approve sarpras"}            } else {                userRule?.contains("approve sarpras")            }            if(apprSarpras!!){                btnSarprasApproveKabag.visibility= View.VISIBLE            }else{                btnSarprasApproveKabag.visibility= View.GONE            }    //        Rule User        val c = this@NewIndexActivity        btnTotal.setOnClickListener(c)        btnApprove.setOnClickListener(c)        btnWaiting.setOnClickListener(c)        btnCancel.setOnClickListener(c)        btnSarpras.setOnClickListener(c)        btnNewSarpras.setOnClickListener(c)        btnClose.setOnClickListener(c)        btnSarprasApproveKabag.setOnClickListener(this@NewIndexActivity)        btnOB.setOnClickListener(this@NewIndexActivity)        btnHAULING.setOnClickListener(this@NewIndexActivity)        btnCRUSHING.setOnClickListener(this@NewIndexActivity)        btnBARGING.setOnClickListener(this@NewIndexActivity)        btnSTOCKROOM.setOnClickListener(this@NewIndexActivity)        btnSTOCKPRODUCT.setOnClickListener(this@NewIndexActivity)        btnHazard.setOnClickListener(this@NewIndexActivity)        btnNewHazard.setOnClickListener(this@NewIndexActivity)        btnNewSarana.setOnClickListener(this@NewIndexActivity)        btnHazardALL.setOnClickListener(this@NewIndexActivity)        btnSarprasAll.setOnClickListener(this@NewIndexActivity)        btnInspection.setOnClickListener(this@NewIndexActivity)        btnQRCODES.setOnClickListener(this@NewIndexActivity)        cvBarcodeProfile.setOnClickListener(this@NewIndexActivity)        btnMenuTop.setOnClickListener(this@NewIndexActivity)        first_menu_item.setOnClickListener(this@NewIndexActivity)        second_menu_item.setOnClickListener(this@NewIndexActivity)        third_menu_item.setOnClickListener(this@NewIndexActivity)        fourth_menu_item.setOnClickListener(this@NewIndexActivity)        fabIC.setOnClickListener(this@NewIndexActivity)        if(!PHOTO_PROFILE){            uploadProfile()        }        LocalBroadcastManager.getInstance(this).registerReceiver(tokenPassingReceiver, IntentFilter("com.misit.abpenergy"))        saranaService = Intent(this@NewIndexActivity, SaranaService::class.java)        stopService(saranaService)    }    private fun startStopService() {        if(isMyServiceRunning(HazardService::class.java)){            stopService(Intent(this@NewIndexActivity, HazardService::class.java).apply {                this.action = Constants.SERVICE_STOP                LocalBroadcastManager.getInstance(this@NewIndexActivity).unregisterReceiver(tokenPassingReceiver)            })        }else{            startService(Intent(this@NewIndexActivity, HazardService::class.java).apply {                this.action = Constants.SERVICE_START            })        }    }    private fun isMyServiceRunning(mClass: Class<HazardService>): Boolean {        val manager: ActivityManager =getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager        for (service: ActivityManager.RunningServiceInfo in manager.getRunningServices(Integer.MAX_VALUE)){            if(mClass.name.equals(service.service.className)){                return true            }        }        return false    }    private fun loadImageResources(c:Context){//        Glide.with(c).load(R.drawable.ic_appr_rkb).into(imgDrAppr)    }    private fun uploadProfile(){        val intent = Intent(this@NewIndexActivity,FotoProfileActivity::class.java)        intent.putExtra("fotoURL",fotoURL)        startActivityForResult(intent,1234)    }    fun Int.toDp(context: Context):Int = TypedValue.applyDimension(        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics    ).toInt()    override fun onOptionsItemSelected(item: MenuItem): Boolean {        return super.onOptionsItemSelected(item)    }    private fun askForCameraPermission(){        ActivityCompat.requestPermissions(            this@NewIndexActivity,            arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE), requestCodeCameraPermission        )    }    override fun onClick(v: View?) {        val c = this@NewIndexActivity        if(v?.id==R.id.fabIC){            fabIC.setColorFilter(ContextCompat.getColor(c, R.color.red_smooth), android.graphics.PorterDuff.Mode.SRC_IN)            val bottomSheetDialog = BottomSheetDialog(c,R.style.BottomSheetDialogTheme)            val bottomSheetView = LayoutInflater.from(c).inflate(R.layout.bottom_sheet_dialog,findViewById<LinearLayout>(R.id.bottomSheet))            bottomSheetView?.findViewById<View>(R.id.btnNewSarprasMenu)?.setOnClickListener {                newSarana()                bottomSheetDialog.dismiss()            }            bottomSheetView?.findViewById<View>(R.id.btnNewHazardMenu)?.setOnClickListener {                newHazardReport()                bottomSheetDialog.dismiss()            }            bottomSheetView.findViewById<View>(R.id.btnSaving)?.setOnClickListener {                startStopService()            }            bottomSheetDialog.setOnDismissListener {                fabIC.setColorFilter(ContextCompat.getColor(c, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.SRC_IN)            }            bottomSheetDialog.setContentView(bottomSheetView!!)            bottomSheetDialog.show()        }        btnFLMenuIndex.collapse()        if(v?.id==R.id.btnTotal){            var intent = Intent(c, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, SECTON)            intent.putExtra(RkbActivity.LEVEL, LEVEL)            var tbindex = 0 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }        if(v?.id==R.id.btnApprove){            var intent = Intent(c, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, SECTON)            intent.putExtra(RkbActivity.LEVEL, LEVEL)            var tbindex = 1 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }        if(v?.id==R.id.btnWaiting){            var intent = Intent(c, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, SECTON)            intent.putExtra(RkbActivity.LEVEL, LEVEL)            var tbindex = 2 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }        if(v?.id==R.id.btnCancel){            var intent = Intent(c, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, SECTON)            intent.putExtra(RkbActivity.LEVEL, LEVEL)            var tbindex = 3 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }        if(v?.id==R.id.btnClose){            var intent = Intent(c, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, SECTON)            intent.putExtra(RkbActivity.LEVEL, LEVEL)            var tbindex = 4 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }        if(v?.id==R.id.btnSarpras  || v!!.id==R.id.second_menu_item){            var intent = Intent(                c,                SarprasActivity::class.java            )            startActivity(intent)        }        if(v?.id==R.id.btnNewSarpras){            var intent = Intent(c, NewSarprasActivity::class.java)            startActivity(intent)        }        if(v?.id==R.id.btnSarprasApproveKabag){            var intent = Intent(c, KabagApprSarprasActivity::class.java)            startActivity(intent)        }        if(v?.id==R.id.btnOB){            var intent = Intent(c, ProductionActivity::class.java)            intent.putExtra(ProductionActivity.MONITORING, "OB")            startActivity(intent)        }        if(v?.id==R.id.btnHAULING){            var  intent = Intent(c, ProductionActivity::class.java)            intent.putExtra(ProductionActivity.MONITORING, "HAULING")            startActivity(intent)        }        if(v?.id==R.id.btnCRUSHING){            var  intent = Intent(c, ProductionActivity::class.java)            intent.putExtra(ProductionActivity.MONITORING, "CRUSHING")            startActivity(intent)        }        if(v?.id==R.id.btnBARGING){            var intent = Intent(c, ProductionActivity::class.java)            intent.putExtra(ProductionActivity.MONITORING, "BARGING")            startActivity(intent)        }        if(v?.id==R.id.btnSTOCKROOM){            var intent = Intent(c, StockActivity::class.java)            intent.putExtra(ProductionActivity.MONITORING, "ROOM")            startActivity(intent)        }        if(v?.id==R.id.btnSTOCKPRODUCT){            var intent = Intent(c, StockActivity::class.java)            intent.putExtra(ProductionActivity.MONITORING, "PRODUCT")            startActivity(intent)        }        if(v?.id==R.id.btnHazard|| v!!.id==R.id.first_menu_item){//            hazardReport()            first_menu_item.setColorFilter(ContextCompat.getColor(c, R.color.red_smooth), android.graphics.PorterDuff.Mode.SRC_IN)            userRule = RULE.split(",").toTypedArray()            var hseAdmin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {                Arrays.stream(userRule).anyMatch{ t -> t== "admin_hse"}            } else {                userRule?.contains("admin_hse")            }            if(hseAdmin!!) {                val list = arrayOf<String>("Hazard Report","Hazard Report Ke Saya","Seluruh Hazard Report")                showDialogOption(c,list)            }else{                val list = arrayOf<String>("Hazard Report","Hazard Report Ke Saya")                showDialogOption(c,list)            }        }        if(v!!.id==R.id.btnNewSarana){            newSarana()        }        if(v!!.id==R.id.btnNewHazard){            newHazardReport()        }        if(v?.id==R.id.content_frame){            btnFLMenuIndex.collapse()        }        if(v?.id==R.id.btnHazardALL){            var intent = Intent(c, ALLHazardReportActivity::class.java)            startActivity(intent)        }        if(v?.id==R.id.btnSarprasAll){            var intent = Intent(c, AllSarprasActivity::class.java)            startActivity(intent)        }        if(v?.id==R.id.btnInspection){            var intent = Intent(c, InspeksiActivity::class.java)            startActivity(intent)        }        if(v?.id==R.id.btnInspectionALL){            var intent = Intent(c, AllInspeksiActivity::class.java)            startActivity(intent)        }        if(v?.id==R.id.btnQRCODES){            var intent = Intent(c, BarcodeScannerActivity::class.java)            intent.putExtra("aktivitas", "addTeam")            startActivity(intent)        }        if(v?.id==R.id.cvBarcodeProfile || v!!.id==R.id.third_menu_item){            val intent = Intent(c, QRCodeActivity::class.java)            intent.putExtra("itemCodes", NIK)            intent.putExtra("judul", "QR-Code Anda")            startActivity(intent)        }        if(v?.id==R.id.btnMenuTop || v!!.id==R.id.fourth_menu_item){            profile(c)        }    }    fun showDialogOption(c:Context, title: Array<String>){        val alertDialog = AlertDialog.Builder(c)        alertDialog.setTitle("Silahkan Pilih")        alertDialog!!.setItems(title, DialogInterface.OnClickListener{ dialog, which ->            when (which) {                0 ->hazardReport(c,"hazard")                1 ->hazardReport(c,"hazard_saya")                2 ->hazardReport(c,"hazard_hse")            }        })        alertDialog.setOnDismissListener {            first_menu_item.setColorFilter(ContextCompat.getColor(c, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)        }        alertDialog.create()        alertDialog.show()    }    private fun profile(c:Context){        mDialogView = LayoutInflater.from(c).inflate(R.layout.menu_option,null)        val nama = mDialogView?.findViewById<View>(R.id.mnNama) as TextView        val nik = mDialogView?.findViewById<View>(R.id.mnNik) as TextView        nama.text = NAMA_LENGKAP        nik.text = NIK        val mnFoto = mDialogView?.findViewById<View>(R.id.mnFoto) as ImageView        Glide.with(c).load(fotoURL).into(mnFoto)    val mBuilder = AlertDialog.Builder(c)        mBuilder.setView(mDialogView)        val dialog =mBuilder.show()        userRule = RULE.split(",").toTypedArray()        var apprSarpras = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {            Arrays.stream(userRule).anyMatch { t -> t == "master perusahaan" }        } else {            userRule?.contains("master perusahaan")        }        if (apprSarpras!!) {            mDialogView?.btnMasterPerusahaan!!.visibility = View.VISIBLE        }else{            mDialogView?.btnMasterPerusahaan!!.visibility = View.GONE        }        val administrator= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {            Arrays.stream(userRule).anyMatch { t -> t == "administrator" }        } else {            userRule?.contains("administrator")        }        if (administrator!!) {            mDialogView?.btnListUser!!.visibility = View.VISIBLE        }else{            mDialogView?.btnListUser!!.visibility = View.GONE        }        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))        mDialogView?.btnListUser!!.setOnClickListener {            val intent = Intent(c,ListUserActivity::class.java)            intent.putExtra(ListUserActivity.DataExtra,"Index")            startActivity(intent)            dialog.dismiss()        }        mDialogView?.dialogDismis!!.setOnClickListener {            dialog.dismiss()        }        mDialogView?.btnChangePWD!!.setOnClickListener {            ConfigUtil.changePassword(c, USERNAME)            dialog.dismiss()        }        mDialogView?.btnKeluarApp!!.setOnClickListener{            logOut()            dialog.dismiss()        }        mDialogView?.btnUpdateData!!.setOnClickListener {            dialog.dismiss()        }        mDialogView?.btnUploadFoto!!.setOnClickListener {            uploadProfile()            dialog.dismiss()        }        mDialogView?.btnMasterPerusahaan!!.setOnClickListener {            ConfigUtil.masterPerusahaan(c)            dialog.dismiss()        }    }    override fun onRequestPermissionsResult(        requestCode: Int,        permissions: Array<out String>,        grantResults: IntArray    ) {//        if(requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()  && grantResults[0] == PackageManager.PERMISSION_GRANTED){////        }else{//            Toasty.error(this@BarcodeScannerActivity,"Permission Denied!").show()//        }        when (requestCode) {            requestCodeCameraPermission -> {                // If request is cancelled, the result arrays are empty.                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {                    // permission was granted, yay! Do the                    // contacts-related task you need to do.                    Toasty.info(this@NewIndexActivity, "PERMISSION_GRANTED", Toasty.LENGTH_SHORT)                        .show()                } else {                    // permission denied, boo! Disable the                    // functionality that depends on this permission.                    Toasty.info(this@NewIndexActivity, "PERMISSION_DENIED", Toasty.LENGTH_SHORT)                        .show()                }                return            }            // Add other 'when' lines to check for other            // permissions this app might request.            else -> {                // Ignore all other requests.                Toasty.info(this@NewIndexActivity, "PERMISSION_Ignore", Toasty.LENGTH_SHORT).show()            }        }        super.onRequestPermissionsResult(requestCode, permissions, grantResults)    }    //Get Token    private fun getToken() {        val apiEndPoint = ApiClient.getClient(this@NewIndexActivity)!!.create(ApiEndPoint::class.java)        val call = apiEndPoint.getDataUser(USERNAME)        call?.enqueue(object : Callback<GetUserResponse> {            override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {                Toast.makeText(this@NewIndexActivity, "Error : $t", Toast.LENGTH_SHORT).show()            }            override fun onResponse(                call: Call<GetUserResponse>,                response: Response<GetUserResponse>            )   {                val circularProgressDrawable = CircularProgressDrawable(this@NewIndexActivity)                circularProgressDrawable.strokeWidth = 5f                circularProgressDrawable.centerRadius = 30f                circularProgressDrawable.start()                var res = response.body()                if (res != null) {                    if (res.dataUser != null) {                        var r = res.dataUser!!                        if (r.photoProfile != null) {                            PrefsUtil.getInstance().setBooleanState(PrefsUtil.PHOTO_PROFILE, true)//                            Glide.with(this@NewIndexActivity).load(r.photoProfile.toString()).into(miniFotoProfile)                            Glide.with(this@NewIndexActivity).load(r.photoProfile.toString()).into(fotoProfileA)                            fotoURL = r.photoProfile                            PrefsUtil.getInstance()                                .setStringState(PrefsUtil.PHOTO_URL, r.photoProfile)                        } else {                            PrefsUtil.getInstance()                                .setBooleanState(                                    PrefsUtil.PHOTO_PROFILE,                                    false                                )                        }                        if (r.rule != null) {                            RULE = r.rule!!                            PrefsUtil.getInstance()                                .setStringState(                                    PrefsUtil.TOTAL_HAZARD_USER,                                    res!!.dataHazard!!.toString()                                )                            tvHazardUser.text = res!!.dataHazard!!.toString()                            tvNIK.text = res!!.dataUser!!.nik.toString()                            tvDept.text = res!!.dataUser!!.dept                            tvSect.text = res!!.dataUser!!.sect                            rvCompany.text = res!!.dataUser!!.namaPerusahaan                            PrefsUtil.getInstance().setStringState("COMPANY_NAME",res!!.dataUser!!.namaPerusahaan)                            if (res!!.dataUser!!.perusahaan == 0) {                                lnSaranaPrasarana.visibility = View.VISIBLE                                lnRKBsystem.visibility = View.VISIBLE                                btnNewSarana.visibility = View.VISIBLE                            } else {                                lnSaranaPrasarana.visibility = View.GONE                                lnRKBsystem.visibility = View.GONE                                btnNewSarana.visibility = View.GONE                            }                            tvInspeksiUser.text = res!!.datInspeksi!!.toString()                            userRule = RULE.split(",").toTypedArray()                            var apprSarpras = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {                                Arrays.stream(userRule).anyMatch { t -> t == "approve sarpras" }                            } else {                                userRule?.contains("approve sarpras")                            }                            if (apprSarpras!!) {                                btnSarprasApproveKabag.visibility = View.VISIBLE                            } else {                                btnSarprasApproveKabag.visibility = View.GONE                            }                            var security = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {                                Arrays.stream(userRule).anyMatch { t -> t == "security" }                            } else {                                userRule?.contains("security")                            }                            if (security!!) {                                btnSarprasAll.visibility = View.VISIBLE                            } else {                                btnSarprasAll.visibility = View.GONE                            }                            var allHazard = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {                                Arrays.stream(userRule).anyMatch { t -> t == "allHazard" }                            } else {                                userRule?.contains("allHazard")                            }                            if (allHazard!!) {                                btnHazardALL.visibility = View.VISIBLE                            } else {                                btnHazardALL.visibility = View.GONE                            }                            var allInspeksi = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {                                Arrays.stream(userRule).anyMatch { t -> t == "allInspeksi" }                            } else {                                userRule?.contains("allInspeksi")                            }                            if (allInspeksi!!) {                                btnInspectionALL.visibility = View.VISIBLE                            } else {                                btnInspectionALL.visibility = View.GONE                            }                            lnLoading.visibility = View.GONE                            shimmerLayout?.visibility = View.GONE                            shimmerProfileImage?.stopShimmer()                            shimmerHazardUser?.stopShimmer()                            shimmerHazardUser?.stopShimmer()                            shimmerInspeksiUser?.stopShimmer()                            shimmerUserName?.stopShimmer()                            shimmerNIK?.stopShimmer()                            shimmerView?.stopShimmer()                            shimmerView1?.stopShimmer()                            shimmerSection?.stopShimmer()                            shimmerCompany?.stopShimmer()                            rvTop.visibility= View.VISIBLE                        }                    }                }            }        })    }//    Get Token    //    On Resume    override fun onResume() {        loadImageResources(this@NewIndexActivity)        PrefsUtil.initInstance(this)        lnLoading.visibility=View.VISIBLE//        Session        if(ConfigUtil.cekKoneksi(this@NewIndexActivity)){            getToken()        }else{            lnLoading.visibility = View.GONE            shimmerLayout?.visibility = View.VISIBLE            rvTop.visibility= View.GONE            timer=object : CountDownTimer(5000, 1000) {                    override fun onTick(millisUntilFinished: Long) {                    }                    override fun onFinish() {                        lnLoading.visibility = View.GONE                        shimmerLayout?.visibility = View.GONE                        rvTop.visibility= View.VISIBLE                        val profilePic = PrefsUtil.getInstance()                            .getStringState(PrefsUtil.PHOTO_URL,null)                        Glide.with(this@NewIndexActivity).load(profilePic).into(fotoProfileA)                        tvUserName.text = NAMA_LENGKAP                        tvNIK.text = NIK                        tvDept.text = DEPARTMENT                        tvSect.text = SECTON                        rvCompany.text = COMPANY                    }                }            timer?.start()        }        tipe =  intent.getStringExtra(TIPE)        if(tipe=="rkb"){            rkbNotif("0")            intent.putExtra(TIPE, "")        }else if(tipe=="sarpras"){            sarprasNotif()            intent.putExtra(TIPE, "")        }//        Toasty.info(this@NewIndexActivity,tipe.toString()).show()        firebase()        super.onResume()    }//On Resume    fun firebase(){        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->            if (!task.isSuccessful) {                Log.w("FIREBASE", "Fetching FCM registration token failed", task.exception)                return@OnCompleteListener            }            // Get new FCM registration token            val token = task.result            // Log and toast            val msg = getString(R.string.msg_token_fmt, token)            Log.d("FIREBASE", msg)//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()        })    }    //    Sarpras Notif    private fun sarprasNotif(){        var intent = Intent(            this@NewIndexActivity,            SarprasActivity::class.java        )        intent.putExtra(RkbActivity.TIPE, "notif")        startActivity(intent)    }    //    Sarpras Notif//    NotifRkb    private fun rkbNotif(tabindex: String?){        var intent = Intent(this@NewIndexActivity, RkbActivity::class.java)        intent.putExtra(RkbActivity.USERNAME, USERNAME)        intent.putExtra(RkbActivity.DEPARTMENT, DEPARTMENT)        intent.putExtra(RkbActivity.SECTON, SECTON)        intent.putExtra(RkbActivity.LEVEL, LEVEL)        intent.putExtra(RkbActivity.Tab_INDEX, tabindex)        intent.putExtra(RkbActivity.TIPE, "notif")        startActivity(intent)    }//    NotifRkb    //    LogOut    private fun logOut() {        AlertDialog.Builder(this)            .setTitle("Confirmation")            .setPositiveButton("OK , Sign Out", { dialog,                                                  which ->                if (PrefsUtil.getInstance().getBooleanState(                        "IS_LOGGED_IN", true                    )                ) {                    PrefsUtil.getInstance().setBooleanState(                        "IS_LOGGED_IN", false                    )                    PrefsUtil.getInstance().setStringState(                        PrefsUtil.USER_NAME, null                    )                    val intent = Intent(this, LoginActivity::class.java)                    startActivity(intent)                    finish()                }            })            .setNegativeButton("Cancel",                { dialog,                  which ->                    dialog.dismiss()                })            .show()    }    //LogOut//    Companion    companion object{        var USERNAME = "username"        var DEPARTMENT="department"        var SECTON="section"        var LEVEL="level"        var Tab_INDEX ="tab_index"        var NAMA_LENGKAP = "nama_lengkap"        var NO_RKB = "NO_RKB"        var TIPE = null        var RULE = "RULE"        var NIK = "NIK"        var COMPANY = "COMPANY"        var PHOTO_PROFILE=false        var INTRO_APP=false    }//    Companion//    HazardRepord    fun hazardReport(c:Context,pilih:String) {        var intent:Intent?=null        when(pilih){            "hazard" -> {                intent = Intent(c, HazardReportActivity::class.java)                first_menu_item.setColorFilter(ContextCompat.getColor(c, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)            }            "hazard_saya" -> {                intent = Intent(c, HazardSayaActivity::class.java)                first_menu_item.setColorFilter(ContextCompat.getColor(c, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)            }            "hazard_hse" -> {                intent = Intent(c, HazardHSEActivity::class.java)                first_menu_item.setColorFilter(ContextCompat.getColor(c, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)            }        }        startActivity(intent)    }    //    onActivityResult    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {        if(resultCode== Activity.RESULT_OK && requestCode==101){            var intent = Intent(this@NewIndexActivity,HazardReportActivity::class.java)            startActivity(intent)        }        super.onActivityResult(requestCode, resultCode, data)    }    //    onActivityResult    fun newSarana(){        btnFLMenuIndex.collapse()        var intent = Intent(this@NewIndexActivity, NewSarprasActivity::class.java)        startActivityForResult(intent, 102)    }    fun newHazardReport() {        btnFLMenuIndex.collapse()        var intent = Intent(this@NewIndexActivity, NewHazardActivity::class.java)        startActivityForResult(intent, 101)    }}
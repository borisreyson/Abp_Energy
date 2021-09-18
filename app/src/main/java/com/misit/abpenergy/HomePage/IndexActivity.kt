package com.misit.abpenergy.HomePageimport android.app.Activityimport android.content.BroadcastReceiverimport android.content.Contextimport android.content.Intentimport android.content.IntentFilterimport android.graphics.Colorimport android.graphics.drawable.ColorDrawableimport android.os.Buildimport androidx.appcompat.app.AppCompatActivityimport android.os.Bundleimport android.util.Logimport android.view.LayoutInflaterimport android.view.Viewimport android.widget.ImageViewimport android.widget.LinearLayoutimport android.widget.TextViewimport androidx.appcompat.app.AlertDialogimport androidx.core.content.ContextCompatimport androidx.lifecycle.ViewModelProviderimport androidx.lifecycle.observeimport androidx.localbroadcastmanager.content.LocalBroadcastManagerimport com.bumptech.glide.Glideimport com.google.android.material.bottomsheet.BottomSheetDialogimport com.misit.abpenergy.DataSource.DataUsersSourceimport com.misit.abpenergy.HazardReport.Service.HazardServiceimport com.misit.abpenergy.Login.FotoProfileActivityimport com.misit.abpenergy.Login.LoginActivityimport com.misit.abpenergy.Master.ListUserActivityimport com.misit.abpenergy.NewIndexActivityimport com.misit.abpenergy.Rimport com.misit.abpenergy.Rkb.RkbActivityimport com.misit.abpenergy.Sarpras.Service.SaranaServiceimport com.misit.abpenergy.Service.ConnectionServiceimport com.misit.abpenergy.Service.InitServiceimport com.misit.abpenergy.Service.InitUtilimport com.misit.abpenergy.Utils.ConfigUtilimport com.misit.abpenergy.Utils.ConnectionLiveDataimport com.misit.abpenergy.Utils.PrefsUtilimport com.misit.abpenergy.ViewModel.IndexViewModelimport es.dmoral.toasty.Toastyimport kotlinx.android.synthetic.main.index_new.*import kotlinx.android.synthetic.main.menu_option.view.*import java.util.*class IndexActivity : AppCompatActivity() ,View.OnClickListener{    private var shimmerLayout: View?=null    private var shimmerSarana:View?=null    private var userRule:Array<String>?=null    var fotoURL:String?=null    lateinit var connectionService:Intent    var tokenPassingReceiver : BroadcastReceiver?=null    private lateinit var cld:ConnectionLiveData    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.index_new)        supportActionBar?.hide()        title= "PT ALAMJAYA BARA PRATAMA"        reciever()        LocalBroadcastManager.getInstance(this).registerReceiver(tokenPassingReceiver!!, IntentFilter("com.misit.abpenergy"))        connectionService = Intent(this@IndexActivity, ConnectionService::class.java)        startService(connectionService)        initIndex(this,this)    }    private fun btnListener(click:View.OnClickListener){        fourth_menu_item.setOnClickListener(click)        first_menu_item.setOnClickListener(click)        refreshConnection.setOnClickListener(click)        btnTotal.setOnClickListener(click)        btnApprove.setOnClickListener(click)        btnWaiting.setOnClickListener(click)        btnCancel.setOnClickListener(click)        btnSarpras.setOnClickListener(click)        btnOB.setOnClickListener(click)        btnHAULING.setOnClickListener(click)        btnCRUSHING.setOnClickListener(click)        btnBARGING.setOnClickListener(click)        btnSTOCKROOM.setOnClickListener(click)        btnSTOCKPRODUCT.setOnClickListener(click)        btnHazard.setOnClickListener(click)        btnInspection.setOnClickListener(click)        btnQRCODES.setOnClickListener(click)        btnMenuTop.setOnClickListener(click)        first_menu_item.setOnClickListener(click)        second_menu_item.setOnClickListener(click)        third_menu_item.setOnClickListener(click)        fourth_menu_item.setOnClickListener(click)        fabIC.setOnClickListener(click)        fabIC.setOnClickListener(click)    }    override fun onClick(v: View?) {        val c = this@IndexActivity        if(v?.id==R.id.fabIC){            fabIC.setColorFilter(ContextCompat.getColor(c, R.color.red_smooth), android.graphics.PorterDuff.Mode.SRC_IN)            val bottomSheetDialog = BottomSheetDialog(c,R.style.BottomSheetDialogTheme)            val bottomSheetView = LayoutInflater.from(c).inflate(R.layout.bottom_sheet_dialog,findViewById<LinearLayout>(R.id.bottomSheet))            bottomSheetView?.findViewById<View>(R.id.btnNewHazardMenu)?.setOnClickListener {                newHazardReport()                bottomSheetDialog.dismiss()            }            bottomSheetView.findViewById<View>(R.id.btnStopSchedule)?.setOnClickListener {                if(!ConfigUtil.isJobServiceOn(this@IndexActivity,1995)) {                    scheduler?.cancelAll()                    Log.d("JobScheduler","Job Canceled")                    Toasty.info(this@IndexActivity,"Job Canceled").show()                }            }            bottomSheetDialog.setOnDismissListener {                fabIC.setColorFilter(ContextCompat.getColor(c, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.SRC_IN)            }            if(ConfigUtil.cekKoneksi(this@IndexActivity)){                bottomSheetView.findViewById<View>(R.id.btnNewSarprasMenu)?.visibility = View.VISIBLE                bottomSheetView.findViewById<View>(R.id.btnNewCuti)?.visibility = View.VISIBLE            }else{                bottomSheetView.findViewById<View>(R.id.btnNewSarprasMenu)?.visibility = View.GONE                bottomSheetView.findViewById<View>(R.id.btnNewCuti)?.visibility = View.GONE            }            bottomSheetDialog.setContentView(bottomSheetView!!)            bottomSheetDialog.show()        }        btnFLMenuIndex.collapse()        if(v?.id==R.id.fourth_menu_item){            profile(this@IndexActivity)        }else if(v?.id==R.id.first_menu_item){        }else if(v?.id==R.id.refreshConnection){            checkNetworkConnection()        }        if(v?.id==R.id.btnTotal){            var intent = Intent(c, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, NewIndexActivity.USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, NewIndexActivity.DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, NewIndexActivity.SECTON)            intent.putExtra(RkbActivity.LEVEL, NewIndexActivity.LEVEL)            var tbindex = 0 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }        if(v?.id==R.id.btnApprove){            var intent = Intent(c, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, NewIndexActivity.USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, NewIndexActivity.DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, NewIndexActivity.SECTON)            intent.putExtra(RkbActivity.LEVEL, NewIndexActivity.LEVEL)            var tbindex = 1 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }        if(v?.id==R.id.btnWaiting){            var intent = Intent(c, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, NewIndexActivity.USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, NewIndexActivity.DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, NewIndexActivity.SECTON)            intent.putExtra(RkbActivity.LEVEL, NewIndexActivity.LEVEL)            var tbindex = 2 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }        if(v?.id==R.id.btnCancel){            var intent = Intent(c, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, NewIndexActivity.USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, NewIndexActivity.DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, NewIndexActivity.SECTON)            intent.putExtra(RkbActivity.LEVEL, NewIndexActivity.LEVEL)            var tbindex = 3 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }        if(v?.id==R.id.btnClose){            var intent = Intent(c, RkbActivity::class.java)            intent.putExtra(RkbActivity.USERNAME, NewIndexActivity.USERNAME)            intent.putExtra(RkbActivity.DEPARTMENT, NewIndexActivity.DEPARTMENT)            intent.putExtra(RkbActivity.SECTON, NewIndexActivity.SECTON)            intent.putExtra(RkbActivity.LEVEL, NewIndexActivity.LEVEL)            var tbindex = 4 as Int            intent.putExtra(RkbActivity.Tab_INDEX, tbindex)            startActivity(intent)        }    }    //    Companion    companion object{        var USERNAME = "username"        var DEPARTMENT="department"        var SECTON="section"        var LEVEL="level"        var Tab_INDEX ="tab_index"        var NAMA_LENGKAP = "nama_lengkap"        var NO_RKB = "NO_RKB"        var TIPE = null        var RULE = "RULE"        var NIK = "NIK"        var COMPANY = "COMPANY"        var PHOTO_PROFILE=false        var INTRO_APP=false    }    //    Companion    private fun profile(c:Context){        var mDialogView = LayoutInflater.from(c).inflate(R.layout.menu_option,null)        val nama = mDialogView?.findViewById<View>(R.id.mnNama) as TextView        val nik = mDialogView?.findViewById<View>(R.id.mnNik) as TextView        nama.text = NAMA_LENGKAP        nik.text = NIK        val mnFoto = mDialogView?.findViewById<View>(R.id.mnFoto) as ImageView        Glide.with(c).load(fotoURL).into(mnFoto)        val mBuilder = AlertDialog.Builder(c)        mBuilder.setView(mDialogView)        val dialog =mBuilder.show()        userRule = RULE.split(",").toTypedArray()        var apprSarpras = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {            Arrays.stream(userRule).anyMatch { t -> t == "master perusahaan" }        } else {            userRule?.contains("master perusahaan")        }        if (apprSarpras!!) {            mDialogView?.btnMasterPerusahaan!!.visibility = View.VISIBLE        }else{            mDialogView?.btnMasterPerusahaan!!.visibility = View.GONE        }        val administrator= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {            Arrays.stream(userRule).anyMatch { t -> t == "administrator" }        } else {            userRule?.contains("administrator")        }        if (administrator!!) {            mDialogView?.btnListUser!!.visibility = View.VISIBLE        }else{            mDialogView?.btnListUser!!.visibility = View.GONE        }        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))        mDialogView?.btnListUser!!.setOnClickListener {            val intent = Intent(c, ListUserActivity::class.java)            intent.putExtra(ListUserActivity.DataExtra,"Index")            startActivity(intent)            dialog.dismiss()        }        mDialogView?.dialogDismis!!.setOnClickListener {            dialog.dismiss()        }        mDialogView?.btnChangePWD!!.setOnClickListener {            ConfigUtil.changePassword(c, USERNAME)            dialog.dismiss()        }        mDialogView?.btnKeluarApp!!.setOnClickListener{            ConfigUtil.logOut(this@IndexActivity)            dialog.dismiss()        }        mDialogView?.btnUpdateData!!.setOnClickListener {            dialog.dismiss()        }        mDialogView?.btnUploadFoto!!.setOnClickListener {            uploadProfile()            dialog.dismiss()        }        mDialogView?.btnMasterPerusahaan!!.setOnClickListener {            ConfigUtil.masterPerusahaan(c)            dialog.dismiss()        }    }    private fun uploadProfile(){        val intent = Intent(this@IndexActivity, FotoProfileActivity::class.java)        intent.putExtra("fotoURL",fotoURL)        startActivityForResult(intent,1234)    }    private fun initIndex(c:Context,a:Activity) {        PrefsUtil.initInstance(c)        if(PrefsUtil.getInstance().getBooleanState("IS_LOGGED_IN", false)){            USERNAME = PrefsUtil.getInstance().getStringState(PrefsUtil.USER_NAME, "")            NAMA_LENGKAP = PrefsUtil.getInstance().getStringState(PrefsUtil.NAMA_LENGKAP, "")            NIK = PrefsUtil.getInstance().getStringState(PrefsUtil.NIK, "")            DEPARTMENT = PrefsUtil.getInstance().getStringState(PrefsUtil.DEPT, "")            SECTON = PrefsUtil.getInstance().getStringState(PrefsUtil.SECTION, "")            LEVEL = PrefsUtil.getInstance().getStringState(PrefsUtil.LEVEL, "")            RULE = PrefsUtil.getInstance().getStringState(PrefsUtil.RULE, "")            PHOTO_PROFILE = PrefsUtil.getInstance().getBooleanState(PrefsUtil.PHOTO_PROFILE,false)            INTRO_APP = PrefsUtil.getInstance().getBooleanState("INTRO_APP",false)            COMPANY = PrefsUtil.getInstance().getStringState("COMPANY_NAME","")            tvUserName.text = NAMA_LENGKAP        }else{            val intent = Intent(this, LoginActivity::class.java)            startActivity(intent)            finish()        }        checkNetworkConnection()        shimmerLayout = findViewById(R.id.shimmerLayout)        shimmerSarana = findViewById(R.id.shimmerSarana)        indexViewModel()        btnListener(this@IndexActivity)    }    private fun indexViewModel() {        val viewModel:IndexViewModel = ViewModelProvider(this@IndexActivity).get(IndexViewModel::class.java)        viewModel.getUserObserver().observe(this@IndexActivity,{            if(it!=null){                shimmerToggle(false)                viewVisible(true)                setHeader("${it.dataHazard}","${it.dataInspeksi}","${it.nama_lengkap}","${it.nik}","${it.sect}","${it.dept}","${it.nama_perusahaan}","${it.photo_profile}")            }else{                shimmerToggle(true)                viewVisible(false)                viewModel.loadUser(this@IndexActivity)            }        })        viewModel.loadUser(this@IndexActivity)    }    private fun shimmerToggle(state:Boolean){        if(state){            shimmerLayout?.visibility = View.VISIBLE            shimmerSarana?.visibility = View.VISIBLE        }else{            shimmerLayout?.visibility = View.GONE            shimmerSarana?.visibility = View.GONE        }    }    private fun viewVisible(state:Boolean){        if(state){            rvTop.visibility = View.VISIBLE            lnRKBsystem.visibility = View.VISIBLE            lnMonitoringProduksi.visibility = View.VISIBLE            lnHSEsystem.visibility = View.VISIBLE        }else{            rvTop.visibility = View.GONE            lnRKBsystem.visibility = View.GONE            lnMonitoringProduksi.visibility=View.GONE            lnHSEsystem.visibility = View.GONE        }    }    private fun setHeader(hazard:String,inspeksi:String, namaLengkap:String, nik:String, section:String, dept:String, company:String, foto:String) {        tvHazardUser.text = hazard        tvInspeksiUser.text = inspeksi        tvUserName.text = namaLengkap        tvNIK.text = nik        tvSect.text = section        tvDept.text = dept        rvCompany.text = company        Glide.with(this@IndexActivity).load(foto).into(fotoProfileA)        fotoURL = foto        Log.d("fotoURL",fotoURL)    }    private fun reciever() {        tokenPassingReceiver = object : BroadcastReceiver() {            override fun onReceive(context: Context, intent: Intent) {                val bundle = intent.extras                if (bundle != null) {                    if (bundle.containsKey("fgUser")) {                        val tokenData = bundle.getString("fgUser")                        if(tokenData=="fgDone"){                            ConfigUtil.startStopService(InitService::class.java,context, USERNAME)                            Log.d("ServiceName", tokenData)                        }                    }                    if (bundle.containsKey("bsConnection")) {                        val tokenData = bundle.getString("bsConnection")                        Log.d("ServiceName","${tokenData} Main")                        if(tokenData=="Online"){                            internetConnection.visibility= View.GONE                            ConfigUtil.startStopService(InitService::class.java,context, USERNAME)                            Log.d("ConnectionCheck",tokenData)                        }else if(tokenData=="Offline"){                            Log.d("ConnectionCheck",tokenData)                            Toasty.error(this@IndexActivity,"No Internet Connection").show()                            internetConnection.visibility= View.VISIBLE                        }else if(tokenData=="Disabled"){                            Log.d("ConnectionCheck",tokenData)                            internetConnection.visibility= View.VISIBLE                            Toasty.error(this@IndexActivity,"Network Disabled").show()                        }                    }                }            }        }    }    private fun checkNetworkConnection() {        cld = ConnectionLiveData(application)        cld.observe(this@IndexActivity,{ isConnected->            if (isConnected){                startService(connectionService)                Log.d("internetconnection",isConnected.toString())                internetConnection.visibility = View.GONE//                startScheduler()            }else{                stopService(connectionService)                internetConnection.visibility= View.VISIBLE                Log.d("internetconnection",isConnected.toString())            }        })    }}
package com.misit.abpenergy.Loginimport android.app.Activityimport android.content.Contextimport android.content.Intentimport androidx.appcompat.app.AppCompatActivityimport android.os.Bundleimport android.util.Logimport android.view.Viewimport android.widget.Toastimport com.misit.abpenergy.Api.ApiClientimport com.misit.abpenergy.Api.ApiEndPointimport com.misit.abpenergy.Login.Response.DaftarAkunResponseimport com.misit.abpenergy.Login.Response.DataUserResponseimport com.misit.abpenergy.Rimport com.misit.abpenergy.Rkb.Response.CsrfTokenResponseimport com.misit.abpenergy.Utils.ConfigUtilimport com.misit.abpenergy.Utils.PopupUtilimport es.dmoral.toasty.Toastyimport kotlinx.android.synthetic.main.activity_register.*import retrofit2.Callimport retrofit2.Callbackimport retrofit2.Responseclass RegisterActivity : AppCompatActivity(),View.OnClickListener {    private var dataUser: MutableList<DataUserResponse>?=null    private var idDept:String?=null    var csrf_token:String?=null    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        setContentView(R.layout.activity_register)        ConfigUtil.changeColor(this)        dataUser=ArrayList()        btnHaveAccount.setOnClickListener(this)        btnCekData.setOnClickListener(this)        InSection.setOnClickListener(this)        btnDaftar.setOnClickListener(this)    }    override fun onResume() {        getToken()        super.onResume()    }    private fun getToken() {        val apiEndPoint = ApiClient.getClient(this)!!.create(ApiEndPoint::class.java)        val call = apiEndPoint.getToken("csrf_token")        call?.enqueue(object : Callback<CsrfTokenResponse> {            override fun onFailure(call: Call<CsrfTokenResponse>, t: Throwable) {                Toast.makeText(this@RegisterActivity,"Error : $t", Toast.LENGTH_SHORT).show()            }            override fun onResponse(                call: Call<CsrfTokenResponse>,                response: Response<CsrfTokenResponse>            ) {                csrf_token = response.body()?.csrfToken            }        })    }    override fun onClick(v: View?) {        if(v?.id==R.id.btnHaveAccount){            finish()        }        if(v?.id==R.id.btnCekData){            if(!InNik.text!!.isEmpty()){                var nik = InNik.text.toString()                cekUser(nik,this@RegisterActivity)            }else{                tilNik.error = "Tidak Boleh Kosong!"                InNik.requestFocus()                return            }        }        if (v?.id==R.id.InSection){            val intent = Intent(this@RegisterActivity,SectionActivity::class.java)            intent.putExtra(SectionActivity.ID_DEPARTEMEN,idDept)            startActivityForResult(intent,13)        }        if(v?.id==R.id.btnDaftar){            daftarAkun()        }    }    private fun daftarAkun(){        if(!isValidate()){            return        }        var nik =InNik.text.toString()        var username = InUsername.text.toString()        var password = InPassword.text.toString()        var fullname = InFullName.text.toString()        var email = InEmail.text.toString()        var departemen = idDept        var devisi = ID_DEVISI        PopupUtil.showProgress(this@RegisterActivity,"Loading...","Mendaftarkan Pengguna Baru!")        val apiEndPoint = ApiClient.getClient(this)!!.create(ApiEndPoint::class.java)        val call = apiEndPoint.daftarkanAkun(            nik,            username,            password,            fullname,            email,            departemen,            devisi,            csrf_token!!        )        call?.enqueue(object : Callback<DaftarAkunResponse> {            override fun onResponse(                call: Call<DaftarAkunResponse>,                response: Response<DaftarAkunResponse>            ) {                var r = response.body()                if(r!=null){                    if(r.success!! && r.login) {                        Toasty.success(this@RegisterActivity, "Berhasil Mendaftar Akun!").show()                        val intent = Intent()                        intent.putExtra("RegUser", username)                        setResult(Activity.RESULT_OK, intent)                        finish()                    }else if(r.success && r.login==false){                    }else{                        Toasty.error(this@RegisterActivity,"Username Sudah Terpakai").show()                        InUsername.requestFocus()                        tilUsername.error = "Username tidak tersedia!"                    }                }else{                    Toasty.error(this@RegisterActivity,"Gagal Mendaftar Akun! Coba Lagi1").show()                    clearData()                    clearError()                    InNik.requestFocus()                }                PopupUtil.dismissDialog()            }            override fun onFailure(call: Call<DaftarAkunResponse>, t: Throwable) {                Log.v("DaftarAkun",t.printStackTrace().toString())                PopupUtil.dismissDialog()            }        })    }    private fun isValidate():Boolean{        clearError()        if(InUsername.text!!.isEmpty()){            tilUsername.error="Please Input Someting"            InUsername.requestFocus()            return false        }        if(InPassword.text!!.isEmpty()){            tilPassword.error="Please Input Someting"            InPassword.requestFocus()            return false        }        if(InFullName.text!!.isEmpty()){            tilUsername.error="Please Input Someting"            tilFullName.requestFocus()            return false        }        if(InEmail.text!!.isEmpty()){            tilEmail.error="Please Input Someting"            InEmail.requestFocus()            return false        }        if(InDept.text!!.isEmpty()){            tilDept.error="Please Input Someting"            InDept.requestFocus()            return false        }        if(InSection.text!!.isEmpty()){            tilSection.error="Please Input Someting"            InSection.requestFocus()            scrlDaftar.fullScroll(View.FOCUS_DOWN);            return false        }        return true    }    private fun clearError() {        tilNik.error=null        tilUsername.error=null        tilFullName.error=null        tilPassword.error=null        tilEmail.error=null        tilDept.error=null        tilSection.error=null    }    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {        if(resultCode==Activity.RESULT_OK && requestCode==13){            if(data!=null){                DEVISI = data!!.getStringExtra("DEVISI")                ID_DEVISI = data!!.getStringExtra("ID_DEVISI")                InSection.setText(DEVISI)            }        }        super.onActivityResult(requestCode, resultCode, data)    }    private fun cekUser(nik:String,c:Context){        PopupUtil.showLoading(this@RegisterActivity,"Mencari Data","Mohon Menunggu!!")        val apiEndPoint = ApiClient.getClient(this@RegisterActivity)!!.create(ApiEndPoint::class.java)        val call = apiEndPoint.getDataForNewUser(nik)        call?.enqueue(object : Callback<DataUserResponse> {            override fun onResponse(call: Call<DataUserResponse>, response: Response<DataUserResponse>) {                var r = response.body()                clearData()                if(r!=null){                    if(r.dataUser!=null){                        tilNik.error = null                        InUsername.isEnabled=true                        InPassword.isEnabled=true                        InFullName.isEnabled=true                        InEmail.isEnabled=true                        InDept.isEnabled=true                        InSection.isEnabled=true                        btnDaftar.isEnabled=true                        InFullName.setText(r!!.dataUser!!.nama)                        InDept.setText(r!!.dataUser!!.dept)                        InSection.setText(r!!.dataUser!!.devisi)                        idDept=r!!.dataUser!!.idDept                        InUsername.requestFocus()                    }else{                        btnDaftar.isEnabled=false                        InUsername.isEnabled=false                        InPassword.isEnabled=false                        InFullName.isEnabled=false                        InEmail.isEnabled=false                        InDept.isEnabled=false                        InSection.isEnabled=false                        tilNik.error = "Tidak Boleh Kosong!"                        Toasty.error(this@RegisterActivity,"Akun Sudah Terdaftar!").show()                    }                    PopupUtil.dismissDialog()                }else{                    InUsername.isEnabled=false                    InPassword.isEnabled=false                    InFullName.isEnabled=false                    InEmail.isEnabled=false                    InDept.isEnabled=false                    InSection.isEnabled=false                    Toasty.error(this@RegisterActivity,"Akun Sudah Terdaftar!").show()                    PopupUtil.dismissDialog()                }            }            override fun onFailure(call: Call<DataUserResponse>, t: Throwable) {                Toasty.error(c,t.printStackTrace().toString()).show()                PopupUtil.dismissDialog()            }        })    }    fun clearData(){        InUsername.setText(null)        InPassword.setText(null)        InFullName.setText(null)        InDept.setText(null)        InEmail.setText(null)        InSection.setText(null)    }    companion object{    var DEVISI = "DEVISI"    var ID_DEVISI = "ID_DEVISI"}}
package com.misit.abpenergy.Sarpras.Serviceimport android.app.IntentServiceimport android.app.Serviceimport android.content.Intentimport android.os.IBinderimport android.util.Logclass SaranaService : IntentService("realmPenumpang") {    val TAG = "SaranaService"    lateinit var sarana: LoadSarana    override fun onBind(intent: Intent): IBinder? {        return null    }    override fun onHandleIntent(intent: Intent?) {        showLog("intent")        sarana.run(this@SaranaService)    }    override fun onCreate() {        sarana = LoadSarana()        showLog("onCreate")        super.onCreate()    }    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {        showLog("onStartCommand")        return super.onStartCommand(intent, flags, startId)    }    override fun onDestroy() {        showLog("onDestroy")        super.onDestroy()    }    private fun showLog(s: String) {        Log.d(TAG,s)    }}
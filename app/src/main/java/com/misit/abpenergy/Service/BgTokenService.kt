package com.misit.abpenergy.Serviceimport android.app.IntentServiceimport android.content.Intentimport android.util.Logclass BgTokenService :IntentService("BgTokenService"){    var TAG ="BgToken"    lateinit var getToken :GetToken    private var username:String?=null    override fun onHandleIntent(intent: Intent?) {        Log.d(TAG,"onHandleIntent")        username = intent?.getStringExtra("username")        getToken.getToken(this@BgTokenService,username!!,"LoadData","bgTokenService")    }    override fun onCreate() {        Log.d(TAG,"onCreate")        getToken = GetToken()        super.onCreate()    }    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {        Log.d(TAG,"onStartCommand")        return super.onStartCommand(intent, flags, startId)    }    override fun onDestroy() {        Log.d(TAG,"onDestroy")        super.onDestroy()    }}
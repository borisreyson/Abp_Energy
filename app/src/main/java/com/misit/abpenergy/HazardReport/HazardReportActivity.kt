package com.misit.abpenergy.HazardReport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.misit.abpenergy.Api.ApiClient
import com.misit.abpenergy.Api.ApiEndPoint
import com.misit.abpenergy.HazardReport.Adapter.ListHazardReportAdapter
import com.misit.abpenergy.HazardReport.Response.DataItem
import com.misit.abpenergy.HazardReport.Response.ListHazard
import com.misit.abpenergy.Login.LoginActivity
import com.misit.abpenergy.R
import com.misit.abpenergy.Utils.PopupUtil
import com.misit.abpenergy.Utils.PrefsUtil
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_hazard_report.*
import kotlinx.android.synthetic.main.activity_sarpras.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HazardReportActivity : AppCompatActivity(), ListHazardReportAdapter.OnItemClickListener {

    private var adapter: ListHazardReportAdapter? = null
    private var hazardList:MutableList<DataItem>?=null
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var page : Int=1
    private var visibleItem : Int=0
    private var total : Int=0
    private var pastVisibleItem : Int=0
    private var loading : Boolean=false
    var curentPosition: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hazard_report)
        title="HAZARD REPORT"
        PrefsUtil.initInstance(this)
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        if(PrefsUtil.getInstance().getBooleanState("IS_LOGGED_IN",false)){
            USERNAME = PrefsUtil.getInstance().getStringState(PrefsUtil.USER_NAME,"")
        }else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        hazardList= ArrayList()
        adapter = ListHazardReportAdapter(this@HazardReportActivity,hazardList!!)
        val linearLayoutManager = LinearLayoutManager(this@HazardReportActivity)
        rvHazardList?.layoutManager = linearLayoutManager
        rvHazardList.adapter =adapter
        adapter?.setListener(this)

        swipeRefreshLayout = findViewById(R.id.pullRefreshHazard)
        swipeRefreshLayout.setOnRefreshListener(object :SwipeRefreshLayout.OnRefreshListener{
            override fun onRefresh() {
                rvHazardList.adapter = adapter
                page=1
                hazardList?.clear()
                load(page.toString())
//                swipeRefreshLayout.isRefreshing=false
                //PopupUtil.dismissDialog()
            }
        })
        floatingNewHazard.setOnClickListener {
            var intent = Intent(this@HazardReportActivity,NewHazardActivity::class.java)
            startActivity(intent)
        }
        hazardList?.clear()
        load("1")
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.newForm){
            var intent = Intent(this@HazardReportActivity,NewHazardActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    fun load(hal:String){
        swipeRefreshLayout.isRefreshing=true
        PopupUtil.showProgress(this@HazardReportActivity,"Loading...","Membuat Hazard Report!")
        val apiEndPoint = ApiClient.getClient(this)!!.create(ApiEndPoint::class.java)
        val call = apiEndPoint.getListHazard(USERNAME,hal)
        call?.enqueue(object : Callback<ListHazard> {
            override fun onFailure(call: Call<ListHazard>, t: Throwable) {
                swipeRefreshLayout.isRefreshing=false
                Toasty.error(this@HazardReportActivity,"Error : $t", Toasty.LENGTH_SHORT).show()
                PopupUtil.dismissDialog()
            }

            override fun onResponse(call: Call<ListHazard>, response: Response<ListHazard>) {
                var listHazard = response.body()
                if(listHazard!=null){
                    if (listHazard.data!=null){
                        loading=true
                        hazardList!!.addAll(listHazard.data!!)
                        adapter?.notifyDataSetChanged()
                    }else{
                        curentPosition = (rvHazardList.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                    }
                }
                rvHazardList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                        if (dy > 0) {
                            visibleItem = recyclerView.layoutManager!!.childCount
                            total = recyclerView.layoutManager!!.itemCount
                            pastVisibleItem =
                                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                            if (loading) {
                                if ((visibleItem + pastVisibleItem) >= total) {
                                    loading = false
                                    page++
                                    load(hal)
                                }
                            }
                        }
                    }
                    override fun onScrollStateChanged(
                        recyclerView: RecyclerView,
                        newState: Int
                    ) {
                        super.onScrollStateChanged(recyclerView, newState)
                    }
                })

                    PopupUtil.dismissDialog()
                    swipeRefreshLayout.isRefreshing=false

            }
        })
    }
    companion object{
        var USERNAME="USERNAME"
    }

    override fun onItemClick(uid: String?) {
        var intent = Intent(this@HazardReportActivity,DetailHazardActivity::class.java)
        intent.putExtra(DetailHazardActivity.UID,uid.toString())
        startActivity(intent)
    }
}
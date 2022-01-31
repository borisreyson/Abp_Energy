package com.misit.abpenergy.HazardReport.Adapterimport android.content.Contextimport android.database.sqlite.SQLiteExceptionimport android.graphics.Colorimport android.util.Logimport android.view.LayoutInflaterimport android.view.Viewimport android.view.ViewGroupimport android.widget.Buttonimport android.widget.TextViewimport androidx.cardview.widget.CardViewimport androidx.recyclerview.widget.LinearLayoutManagerimport androidx.recyclerview.widget.RecyclerViewimport com.misit.abpenergy.HazardReport.Response.DetHirarkiItemimport com.misit.abpenergy.HazardReport.Response.HirarkiItemimport com.misit.abpenergy.HazardReport.Response.HirarkiItemFullimport com.misit.abpenergy.HazardReport.SQLite.DataSource.DetHirarkiDataSourceimport com.misit.abpenergy.Rimport org.joda.time.format.DateTimeFormatimport org.joda.time.format.DateTimeFormatterimport java.text.SimpleDateFormatclass HirarkiAdapter(private val c: Context?,                     private var hirarkiDipilih: String?,                     private val hirarkiList:MutableList<HirarkiItemFull>):    RecyclerView.Adapter<HirarkiAdapter.MyViewHolder>() {    private var onItemClickListener: OnItemClickListener? = null    private val layoutInflater: LayoutInflater    private var simpleDateFormat: SimpleDateFormat? = null    lateinit var view:View    private var idDipilih:Int?=null    private var itemAdapter: DetHirarkiAdapter? = null    private var detHirarkiList:MutableList<DetHirarkiItem>?=null    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {        view = layoutInflater.inflate(R.layout.bahaya,parent,false)        return MyViewHolder(view)    }    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {        var hirarkiList =hirarkiList[position]        val fmt: DateTimeFormatter = DateTimeFormat.forPattern("d MMMM, yyyy")        holder.rbSumberBahaya.text = hirarkiList.namaPengendalian        if(hirarkiDipilih==hirarkiList.namaPengendalian){            holder.rbSumberBahaya.setTextColor(Color.parseColor("#FFFFFF"))            holder.rbSumberBahaya.setBackgroundColor(Color.parseColor("#4F8E50"))            holder.lnPilih.setBackgroundColor(Color.parseColor("#4F8E50"))            idDipilih=hirarkiList.idHirarki            holder.btnPengendalian.visibility=View.GONE        }else{            holder.rbSumberBahaya.setTextColor(Color.parseColor("#4F8E50"))            holder.rbSumberBahaya.setBackgroundColor(Color.parseColor("#FFFFFF"))            holder.lnPilih.setBackgroundColor(Color.parseColor("#FFFFFF"))            holder.btnPengendalian.visibility=View.VISIBLE        }        holder.btnPengendalian.setOnClickListener{            onItemClickListener?.onItemClick(hirarkiList.idHirarki.toString(),hirarkiList.namaPengendalian!!)        }        detHirarkiList = hirarkiList.listKet        holder.rvDetHirarki.layoutManager = LinearLayoutManager(c,            LinearLayoutManager.VERTICAL,false)        holder.rvDetHirarki.layoutManager        holder.rvDetHirarki.isNestedScrollingEnabled =false        itemAdapter = DetHirarkiAdapter(c,detHirarkiList,idDipilih)        holder.rvDetHirarki.adapter = itemAdapter        itemAdapter?.notifyDataSetChanged()    }    override fun getItemCount(): Int {        return hirarkiList.size    }    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {        var lnPilih = itemView.findViewById<View>(R.id.lnPilih) as CardView        var rbSumberBahaya = itemView.findViewById<View>(R.id.rbSumberBahaya) as TextView        var rvDetHirarki = itemView.findViewById<View>(R.id.rvDetHirarki) as RecyclerView        var btnPengendalian= itemView.findViewById<View>(R.id.btnPengendalian) as Button    }    interface OnItemClickListener{        fun onItemClick(uid:String,pengendalian:String)    }    fun setListener (listener: OnItemClickListener){        onItemClickListener = listener    }    init {        layoutInflater = LayoutInflater.from(c)        simpleDateFormat= SimpleDateFormat("yyyy-MM-dd")    }}
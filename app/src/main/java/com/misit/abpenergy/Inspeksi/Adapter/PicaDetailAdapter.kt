package com.misit.abpenergy.Inspeksi.Adapterimport android.content.Contextimport android.view.LayoutInflaterimport android.view.Viewimport android.view.ViewGroupimport android.widget.ImageViewimport android.widget.LinearLayoutimport android.widget.TextViewimport androidx.recyclerview.widget.RecyclerViewimport com.bumptech.glide.Glideimport com.bumptech.glide.load.engine.DiskCacheStrategyimport com.misit.abpenergy.Inspeksi.Response.InspeksiPicaDetailItemimport com.misit.abpenergy.Rimport com.misit.abpenergy.Utils.ConfigUtilimport org.joda.time.LocalDateimport org.joda.time.format.DateTimeFormatimport org.joda.time.format.DateTimeFormatterimport java.text.SimpleDateFormatclass PicaDetailAdapter(private var c: Context?,                        private val picaList:MutableList<InspeksiPicaDetailItem>):    RecyclerView.Adapter<PicaDetailAdapter.MyViewHolder>() {    private var onItemsClickListener: OnItemsClickListener? = null    private val layoutInflater: LayoutInflater    private var simpleDateFormat: SimpleDateFormat? = null    lateinit var view: View    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {        view = layoutInflater.inflate(R.layout.pica_view,parent,false)        return MyViewHolder(view)    }    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {        var listPica = picaList[position]        holder.picaTemuan.text =listPica.picaTemuan        if(listPica.picaTindakan!=null){            holder.picaTindakan.text = listPica.picaTindakan        }else{            holder.picaTindakan.text = listPica.picaTindakan        }        holder.picaNikPJ.text = listPica.picaNikPJ        holder.picaNamaPJ.text = listPica.picaNamaPJ        holder.picaTenggat.text = ConfigUtil.dMY(listPica.picaTenggat!!)        Glide.with(c!!)            .load("https://abpjobsite.com/bukti_inspeksi/sebelum/"+listPica?.picaSebelum)            .diskCacheStrategy(DiskCacheStrategy.NONE)            .into(holder.picaSebelum)        if(listPica.picaSesudah!=null) {            holder.lnPicaSesudah.visibility = View.VISIBLE            holder.picaSesudah.visibility = View.VISIBLE            holder.picaSesudah.visibility = View.VISIBLE            Glide.with(c!!)                .load("https://abpjobsite.com/bukti_inspeksi/sesudah/" + listPica?.picaSesudah)                .diskCacheStrategy(DiskCacheStrategy.NONE)                .into(holder.picaSesudah)        }else{            holder.tvGambarSesudah.visibility = View.GONE            holder.picaSesudah.visibility = View.GONE            holder.lnPicaSesudah.visibility = View.GONE        }    }    override fun getItemCount(): Int {        return  picaList.size    }    interface OnItemsClickListener{        fun onItemClick(nikTeam:String)    }    fun setListener (listeners: OnItemsClickListener){        onItemsClickListener = listeners    }    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {        var picaTemuan = itemView.findViewById<View>(R.id.picaTemuan) as TextView        var picaTindakan = itemView.findViewById<View>(R.id.picaTindakan) as TextView        var picaSebelum = itemView.findViewById<View>(R.id.picaSebelum) as ImageView        var picaSesudah = itemView.findViewById<View>(R.id.picaSesudah) as ImageView        var picaTenggat = itemView.findViewById<View>(R.id.picaTenggat) as TextView        var picaNikPJ = itemView.findViewById<View>(R.id.picaNikPJ) as TextView        var picaNamaPJ = itemView.findViewById<View>(R.id.picaNamaPJ) as TextView        var tvGambarSesudah = itemView.findViewById<View>(R.id.tvGambarSesudah) as TextView        var lnPicaSesudah = itemView.findViewById<View>(R.id.lnPicaSesudah) as LinearLayout    }    init {        layoutInflater = LayoutInflater.from(c)        simpleDateFormat= SimpleDateFormat("yyyy-MM-dd")    }}
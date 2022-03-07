package com.misit.abpenergy.HSE.Buletin.Adapterimport android.content.Contextimport android.view.LayoutInflaterimport android.view.Viewimport android.view.ViewGroupimport android.widget.TextViewimport androidx.recyclerview.widget.RecyclerViewimport com.misit.abpenergy.Main.Response.MessageInfoItemimport com.misit.abpenergy.Rimport org.joda.time.LocalDateimport org.joda.time.format.DateTimeFormatimport org.joda.time.format.DateTimeFormatterimport java.text.SimpleDateFormatclass BuletinAdapter(private var context: Context,private var listInfo:ArrayList<MessageInfoItem>):RecyclerView.Adapter<BuletinAdapter.MyViewHolder>(){    private val layoutInflater: LayoutInflater    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {        val view = layoutInflater.inflate(R.layout.list_buletin,parent,false)        return MyViewHolder(view)    }    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {        var buletinList = listInfo[position]        val fmt: DateTimeFormatter = DateTimeFormat.forPattern("d MMMM, yyyy")        holder.tvJudulBuletin.text = buletinList.judul        holder.tvPesanBuletin.text = buletinList.pesan        holder.tvTglBuletin.text = LocalDate.parse(buletinList.tgl).toString(fmt)    }    override fun getItemCount(): Int {        return listInfo.size    }    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {        var tvJudulBuletin = itemView.findViewById<View>(R.id.tvJudulBuletin) as TextView        var tvPesanBuletin = itemView.findViewById<View>(R.id.tvPesanBuletin) as TextView        var tvTglBuletin = itemView.findViewById<View>(R.id.tvTglBuletin) as TextView    }    init {        layoutInflater = LayoutInflater.from(context)    }}
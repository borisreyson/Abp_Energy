package com.misit.abpenergy.HSE.HazardReport.Adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.misit.abpenergy.HSE.HazardReport.Response.HazardItem
import com.misit.abpenergy.R
import com.misit.abpenergy.Utils.PrefsUtil
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class ListHazardReportAdapter (private val context: Context,
                               private val rule:String,
                               private val activityName :String,
                               private val hazardList:MutableList<HazardItem>):
    RecyclerView.Adapter<ListHazardReportAdapter.MyViewHolder>(){
    private var userRule:Array<String>?=null

    private var onItemClickListener: OnItemClickListener? = null

    private val layoutInflater: LayoutInflater
    private var simpleDateFormat: SimpleDateFormat? = null
    lateinit var view:View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        view = layoutInflater.inflate(R.layout.hazard_list,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hazardList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var hazardList =hazardList[position]
        val fmt: DateTimeFormatter = DateTimeFormat.forPattern("d MMMM, yyyy")
        holder.tvJamHazard.text = hazardList.jamHazard
        holder.tvTglHazard.text = LocalDate.parse(hazardList.tglHazard).toString(fmt)
        holder.tvPerusahaan.text = hazardList.perusahaan
        holder.tvLokasi.text = hazardList.lokasiHazard
        holder.tvDeskripsi.text = hazardList.deskripsi
        holder.tvStatus.text = hazardList.statusPerbaikan
        holder.tvUSER.text = hazardList.namaLengkap
        holder.tvPJ.text = hazardList.namaPJ
        if(hazardList.tgl_tenggat!=null){
            holder.tvDueDate.text =LocalDate.parse(hazardList.tgl_tenggat).toString(fmt)
            holder.lnDueDate.visibility = View.VISIBLE
        }else{
            holder.tvDueDate.text = "-"
            holder.lnDueDate.visibility = View.GONE
        }
        if(hazardList.statusPerbaikan=="BELUM SELESAI"){
            holder.lnHeader.setBackgroundResource(R.color.bgCancel)
            holder.tvDeskripsiBahaya.setBackgroundResource(R.color.bgCancel)
            holder.tvStatus.setBackgroundResource(R.color.bgCancel)
        }else if(hazardList.statusPerbaikan=="SELESAI"){
            holder.lnHeader.setBackgroundResource(R.color.bgApprove)
            holder.tvDeskripsiBahaya.setBackgroundResource(R.color.bgApprove)
            holder.tvStatus.setBackgroundResource(R.color.bgApprove)
        }else if(hazardList.statusPerbaikan=="DIKERJAKAN"){
            holder.lnHeader.setBackgroundResource(R.color.bgWaiting)
            holder.tvDeskripsiBahaya.setBackgroundResource(R.color.bgWaiting)
            holder.tvStatus.setBackgroundResource(R.color.bgWaiting)
        }else if(hazardList.statusPerbaikan=="BERLANJUT"){
            holder.lnHeader.setBackgroundResource(R.color.bgTotal)
            holder.tvDeskripsiBahaya.setBackgroundResource(R.color.bgTotal)
            holder.tvStatus.setBackgroundResource(R.color.bgTotal)
        }
        if(hazardList.uservalid!=null){
            if(hazardList.option_flag==1){
                holder.tvVerfikasi.text = "Di Setujui Oleh Safety"
                holder.tvVerfikasi.setBackgroundResource(R.color.bgApprove)
                holder.lnBatal.visibility=View.GONE
                holder.btnRubah.visibility=View.GONE
            }else if(hazardList.option_flag==0){
                holder.tvVerfikasi.text = "Di Batalkan Oleh Safety"
                holder.tvVerfikasi.setBackgroundResource(R.color.bgCancel)
                holder.lnBatal.visibility=View.VISIBLE
                holder.tvKetCancel.text=hazardList.keterangan_admin
                holder.btnRubah.visibility=View.VISIBLE
            }else{
                holder.tvVerfikasi.text = "Di Setujui Oleh Safety"
                holder.tvVerfikasi.setBackgroundResource(R.color.bgApprove)
                holder.lnBatal.visibility=View.GONE
                holder.btnRubah.visibility=View.GONE
            }
        }else{
            holder.tvVerfikasi.setBackgroundResource(R.color.bgWaiting)
            holder.tvVerfikasi.text = "Belum Disetujui Oleh Safety"
            holder.lnBatal.visibility=View.GONE
            holder.btnRubah.visibility=View.GONE
        }
        holder.cvHazard.setOnClickListener{
            onItemClickListener?.onItemClick(hazardList.uid.toString())
        }
        holder.btnUpdateStatus.setOnClickListener {
            onItemClickListener?.onUpdateClick(hazardList.uid.toString())
        }
        holder.bntHSEappr.setOnClickListener {
            onItemClickListener?.onVerify(hazardList.uid.toString(),1)
        }
        holder.btnHSEdeny.setOnClickListener {
            onItemClickListener?.onVerify(hazardList.uid.toString(),0)
        }
        holder.btnRubah.setOnClickListener {
            onItemClickListener?.rubahHazard(hazardList.uid)
        }
        if(activityName=="ALL") {
            if (rule != null) {
                userRule = rule.split(",").toTypedArray()
                var hseAdmin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Arrays.stream(userRule).anyMatch { t -> t == "admin_hse" }
                } else {
                    userRule?.contains("admin_hse")
                }
                var admin = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                    Arrays.stream(userRule).anyMatch { t->t=="administrator" }
                }else{
                    userRule?.contains("administrator")
                }
                if (hseAdmin!!) {
                    holder.btnRubah.visibility=View.GONE
                    holder.btnDel.visibility=View.GONE
                    if(hazardList?.uservalid==null || hazardList?.uservalid==""){
                        holder.lnHSEAdmin.visibility = View.VISIBLE
                    }else{
                        holder.lnHSEAdmin.visibility = View.GONE
                    }
                } else {
                    holder.btnRubah.visibility=View.GONE
                    holder.lnHSEAdmin.visibility = View.GONE
                }

                if(admin!!){
                    holder.btnDel.visibility=View.VISIBLE
                }else{
                    holder.btnDel.visibility=View.GONE
                }
            }else{
                holder.lnHSEAdmin.visibility = View.GONE
            }
        }else{
            holder.lnHSEAdmin.visibility = View.GONE
        }
        holder.btnDel.setOnClickListener{
            onItemClickListener?.deleteItem(hazardList.uid)
        }
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cvHazard = itemView.findViewById<View>(R.id.cvHazard) as CardView
        var tvJamHazard = itemView.findViewById<View>(R.id.tvJamHazard) as TextView
        var tvTglHazard = itemView.findViewById<View>(R.id.tvTglHazard) as TextView
        var tvPerusahaan = itemView.findViewById<View>(R.id.tvPerusahaan) as TextView
        var tvLokasi = itemView.findViewById<View>(R.id.tvLokasi) as TextView
        var tvDeskripsi = itemView.findViewById<View>(R.id.tvDeskripsi) as TextView
        var tvStatus = itemView.findViewById<View>(R.id.tvStatus) as TextView
        var lnHeader = itemView.findViewById<View>(R.id.lnHeader) as LinearLayout
        var tvDeskripsiBahaya = itemView.findViewById<View>(R.id.tvDeskripsiBahaya) as TextView
        var btnUpdateStatus = itemView.findViewById<View>(R.id.btnUpdateStatus) as Button
        var tvUSER = itemView.findViewById<View>(R.id.tvUSER) as TextView
        var tvPJ = itemView.findViewById<View>(R.id.tvPJ) as TextView
        var tvVerfikasi = itemView.findViewById<View>(R.id.tvVerfikasi) as TextView
        var lnHSEAdmin = itemView.findViewById<View>(R.id.lnHSEAdmin) as LinearLayout
        var bntHSEappr = itemView.findViewById<View>(R.id.bntHSEappr) as Button
        var btnHSEdeny = itemView.findViewById<View>(R.id.btnHSEdeny) as Button
        var tvDueDate = itemView.findViewById<View>(R.id.tvDueDate) as TextView
        var tvKetCancel = itemView.findViewById<View>(R.id.tvKetCancel) as TextView
        var lnDueDate = itemView.findViewById<View>(R.id.lnDueDate) as LinearLayout
        var lnBatal = itemView.findViewById<View>(R.id.lnBatal) as LinearLayout
        var btnDel= itemView.findViewById<View>(R.id.btnDel) as Button
        var btnRubah= itemView.findViewById<View>(R.id.btnRubah) as Button
    }
    interface OnItemClickListener{
        fun onItemClick(uid:String?)
        fun onUpdateClick(uid:String?)
        fun onVerify(uid: String?,option:Int?)
        fun deleteItem(uid: String?)
        fun rubahHazard(uid: String?)
    }
    fun setListener (listener: OnItemClickListener){
        onItemClickListener = listener
    }
    init {
        PrefsUtil.initInstance(context)
        layoutInflater = LayoutInflater.from(context)
        simpleDateFormat= SimpleDateFormat("yyyy-MM-dd")
    }
}
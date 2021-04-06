package com.misit.abpenergy.Api

import com.misit.abpenergy.HazardReport.Response.DataItem
import com.misit.abpenergy.HazardReport.Response.HazardReportResponse
import com.misit.abpenergy.HazardReport.Response.ListHazard
import com.misit.abpenergy.HazardReport.Response.SumberBahayaResponse
import com.misit.abpenergy.Login.Response.DataUserResponse
import com.misit.abpenergy.Login.Response.SectionItem
import com.misit.abpenergy.Login.Response.SectionResponse
import com.misit.abpenergy.Monitoring_Produksi.Response.ProduksiResponse
import com.misit.abpenergy.Monitoring_Produksi.Response.StockResponse
import com.misit.abpenergy.Response.AbpResponse
import com.misit.abpenergy.Response.GetUserResponse
import com.misit.abpenergy.Sarpras.SaranaResponse.ListSaranaResponse
import com.misit.abpenergy.Rkb.Response.*
import com.misit.abpenergy.Sarpras.SaranaResponse.IzinKeluarSaranaResponse
import com.misit.abpenergy.Sarpras.SarprasResponse.KaryawanResponse
import com.misit.abpenergy.Sarpras.SarprasResponse.LihatSarprasResponse
import com.misit.abpenergy.Sarpras.SarprasResponse.UserSarprasResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiEndPoint{
    @GET("refresh-csrf")
    fun getToken(@Query("csrf_token") tokenId:String): Call<CsrfTokenResponse>?

    @FormUrlEncoded
    @POST("api/android/login/validate")
    fun loginChecklogin(@Field("username") username:String?,
                        @Field("password") password:String?,
                        @Field("_token") csrf_token:String?,
                        @Field("android_token") android_token:String?,
                        @Field("app_version") app_version:String?,
                        @Field("app_name") app_name:String?): Call<UserResponse>

    @GET("api/android/get/rkb/user")
    fun getRkbUser(@Query("username") username:String ,
                   @Query("department") department:String,
                   @Query("close_rkb") close_rkb:String?,
                   @Query("search") search:String?,
                   @Query("disetujui") disetujui:String?,
                   @Query("diketahui") diketahui:String?,
                   @Query("approve") approve:String?,
                   @Query("cancel") cancel:String?,
                   @Query("page") page:String?,
                   @Query("level") level:String?)
            : Call<RkbResponse>?
    @GET("api/android/get/rkb/admin")

    fun getRkbAdmin(@Query("username") username:String ,
                    @Query("department") department:String,
                    @Query("close_rkb") close_rkb:String?,
                    @Query("search") search:String?,
                    @Query("disetujui") disetujui:String?,
                    @Query("diketahui") diketahui:String?,
                    @Query("approve") approve:String?,
                    @Query("cancel") cancel:String?,
                    @Query("page") page:String?,
                    @Query("level") level:String?)
            : Call<RkbResponse>?

    @GET("api/android/get/rkb/detail")
    fun getRkbDetail(@Query("no_rkb") no_rkb:String): Call<DetailRkbResponse>?

    @GET("/v3/rkb")
    fun getRkbKtt(@Query("android") tokenId:String): Call<RkbResponse>?

    @FormUrlEncoded
    @POST("api/android/post/rkb/kabag/approve")
    fun approveKabag(@Field("username") username:String?,
                     @Field("no_rkb") password:String?,
                     @Field("_token") _token:String?): Call<ApproveRkbResponse>

    @FormUrlEncoded
    @POST("api/android/post/rkb/ktt/approve")
    fun approveKTT(@Field("username") username:String?,
                     @Field("no_rkb") password:String?,
                     @Field("_token") _token:String?): Call<ApproveRkbResponse>

    @GET("api/android/app/version")
    fun getAppVersion(@Query("app") app:String?)
            : Call<AppVersionResponse>?

    @FormUrlEncoded
    @POST("api/android/api/cancel/rkb")
    fun cancelRkb(@Field("username") username:String?,
                  @Field("no_rkb") password:String?,
                  @Field("section") section:String?,
                  @Field("remarks") remarks:String?,
                   @Field("_token") _token:String?): Call<CancelRKBResponse>

    @GET("api/android/sarpras/user")
    fun getSarprasUser(@Query("nik") nik:String?,
                       @Query("page") page:Int)
            : Call<UserSarprasResponse>?
    @GET("/sarpras/android/get/sarana")
    fun getAllSarana()
            : Call<ListSaranaResponse>?

    @GET("/sarpras/android/get/karyawan")
    fun getAllKaryawan()
            : Call<KaryawanResponse>?

    @FormUrlEncoded
    @POST("/sarpras/android/keluar-masuk/post")
    fun keluarSarana(
                    @Field("username") username:String?,
                    @Field("pemohon") pemohon:String?,
                    @Field("no_lv") no_lv:String?,
                    @Field("driver") driver:String?,
                    @Field("no_pol") no_pol:String?,
                    @Field("penumpang[]") penumpang:List<String>?,
                    @Field("keterangan") keterangan:String?,
                    @Field("tgl_keluar") tgl_keluar:String?,
                    @Field("jam_keluar") jam_keluar:String?,
                    @Field("waktu_masuk") waktu_masuk:Boolean?,
                    @Field("tgl_masuk") tgl_masuk:String?,
                    @Field("jam_masuk") jam_masuk:String?,
                    @Field("_token") _token:String?
    ): Call<IzinKeluarSaranaResponse>

    @GET("/sarpras/android/keluar-masuk/kabag")
    fun getSarprasKabag(@Query("dept") dept:String?,
                        @Query("sect") sect:String?,
                        @Query("page") page:Int)
            : Call<UserSarprasResponse>?

    @GET("/api/android/sarpras/user/lihat")
    fun getLihatSarpras(@Query("noid_out") dept:String)
            : Call<LihatSarprasResponse>?

    @GET("abp.php")
    fun cekLokasi(): Call<AbpResponse>?

    @GET("/android/api/get/user")
    fun getDataUser(@Query("username") username:String)
            : Call<GetUserResponse>?

    @GET("/android/api/monitoring/ob")
    fun getOBList(@Query("mtr") mtr:String,
                  @Query("fDate") fDate:String,
                  @Query("lDate") lDate:String)
            : Call<ProduksiResponse>?

    @GET("/android/api/monitoring/stock")
    fun getStockList(@Query("fDate") fDate:String,
                  @Query("lDate") lDate:String)
            : Call<StockResponse>?

    @GET("/android/api/hse/sumber/bahaya")
    fun getBahayaList()
            : Call<SumberBahayaResponse>?

    @Multipart
    @POST("/android/api/hse/hazard/reportPost")
    fun postHazardReport(
        @Part fileToUpload: MultipartBody.Part?,
        @Part("perusahaan") perusahaan: RequestBody?,
        @Part("tgl_hazard") tgl_hazard:RequestBody?,
        @Part("jam_hazard") jam_hazard:RequestBody?,
        @Part("kat_bahaya") kat_bahaya:RequestBody?,
        @Part("deskripsi") deskripsi:RequestBody?,
        @Part("status_perbaikan") status_perbaikan:RequestBody?,
        @Part("lokasi") lokasi:RequestBody?,
        @Part("tindakan") tindakan:RequestBody?,
        @Part("penanggung_jawab") penanggung_jawab:RequestBody?,
        @Part("sumber_bahaya") sumber_bahaya:RequestBody?,
        @Part("tgl_selesai") tgl_selesai:RequestBody?,
        @Part("jam_selesai") jam_selesai:RequestBody?,
        @Part("user_input") user_input:RequestBody?,
        @Part("_token") _token:RequestBody?
                         ) : Call<HazardReportResponse>?

    @GET("/android/api/hse/list/hazard/report")
    fun getListHazard(@Query("username") username:String,
                      @Query("page") page:String)
            : Call<ListHazard>?

    @GET("/android/api/hse/item/hazard/report")
    fun getItemHazard(@Query("uid") uid:String)
            : Call<DataItem>?

    @GET("/android/api/user/check/data")
    fun getDataForNewUser(@Query("nik") nik:String)
            : Call<DataUserResponse>?
    @GET("/android/api/check/section/dept")
    fun checkSection(@Query("idDept") idDept:String)
            : Call<SectionResponse>?

}
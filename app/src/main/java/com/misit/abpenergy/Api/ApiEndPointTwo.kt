package com.misit.abpenergy.Apiimport com.misit.abpenergy.HazardReport.Response.HazardReportResponseimport com.misit.abpenergy.HazardReport.Response.ListHazardimport com.misit.abpenergy.Inspeksi.Response.*import com.misit.abpenergy.Login.Response.CompanyResponseimport com.misit.abpenergy.Login.Response.DaftarAkunResponseimport com.misit.abpenergy.Login.Response.DataProfileResponseimport com.misit.abpenergy.Login.Response.DepartmentResponseimport okhttp3.MultipartBodyimport okhttp3.RequestBodyimport retrofit2.Callimport retrofit2.http.*interface ApiEndPointTwo {    @GET("/android/api/get/perusahaan")    fun getCompany()            : Call<CompanyResponse>?    @GET("/android/api/get/department")    fun getDepartment(@Query("company") company:String)            : Call<DepartmentResponse>?    @FormUrlEncoded    @POST("/android/api/daftar/akun/baru/mitra")    fun daftarkanAkunMitra(@Field("nik") nik:String?,                            @Field("username") username:String?,                            @Field("password") password:String?,                            @Field("nama") nama:String?,                            @Field("email") email:String?,                            @Field("perusahaan") perusahaan:String?,                            @Field("departemen") departemen:String?,                            @Field("devisi") devisi:String?,                            @Field("_token") csrf_token:String?                        ) : Call<DaftarAkunResponse>?    @Multipart    @POST("/hse/android/inspeksi/new/submit")    fun inspeksiSave(        @Part("idTemp") idTemp: RequestBody?,        @Part("idForm") idForm: RequestBody?,        @Part("user") user: RequestBody?,        @Part("tglInspeksi") tglInspeksi: RequestBody?,        @Part("perusahaan") perusahaan: RequestBody?,        @Part("lokasi") lokasi: RequestBody?,        @Part("saran") saran: RequestBody?,        @Part("_token") csrf_token: RequestBody?    )            : Call<ItemTempResponse>?    @GET("/hse/android/inspeksi/list/user")    fun getInspeksiUser(@Query("userInput") userInput:String,                        @Query("idForm") idForm:String)            : Call<DataInspeksiResponse>?    @GET("/hse/android/inspeksi/pica/detail")    fun listInspeksiPica(@Query("idInspeksi") idInspeksi:String?)            : Call<InspeksiPicaDetailResponse>?    @GET("/hse/android/inspeksi/list/team")    fun teamInspeksi(@Query("idInspeksi") idInspeksi:String?)            : Call<TeamDetailResponse>?    @GET("/hse/android/inspeksi/detail")    fun getListDetInspeksi(@Query("idForm") idForm:String,                           @Query("idInspeksi") idInspeksi:String):            Call<ItemDetailInspeksiResponse>?//   saveNewSandi    @FormUrlEncoded    @POST("/hse/android/ganti/sandi")    fun saveNewSandi(            @Field("username") username:String?,            @Field("oldPass") oldPass:String?,            @Field("newPass") newPass:String?,            @Field("reNewPass") reNewPass:String?,            @Field("_token") csrf_token:String?    ) : Call<DaftarAkunResponse>?//updateProfile    @GET("/hse/android/load/data/profile")    fun updateProfile(@Query("username") username:String):            Call<DataProfileResponse>?//    simpanDataProfile    @FormUrlEncoded    @POST("/hse/android/load/data/profile")    fun simpanDataProfile(        @Field("nik") nik:String?,        @Field("namaLengkap") namaLengkap:String?,        @Field("email") email:String?,        @Field("perusahaan") perusahaan:String?,        @Field("department") department:String?,        @Field("devisi") devisi:String?,        @Field("_token") csrf_token:String?    ) : Call<DaftarAkunResponse>?//    saveCompany@FormUrlEncoded@POST("/hse/android/save/data/company")fun saveCompany(    @Field("perusahaan") perusahaan:String?,    @Field("_token") csrf_token:String?) : Call<DaftarAkunResponse>?//    saveCompany//updateCompany    @FormUrlEncoded    @POST("/hse/android/save/data/company")    fun updateCompany(    @Field("idCompany") idCompany:String?,    @Field("perusahaan") perusahaan:String?,    @Field("_method") _method:String?,        @Field("_token") csrf_token:String?    ) : Call<DaftarAkunResponse>?//    updateCompany//    getListHazardSaya@GET("/android/api/hse/list/hazard/report/saya")fun getListHazardSaya(@Query("nik") nik:String,                      @Query("dari") dari:String,                      @Query("sampai") sampai:String,                      @Query("page") page:String)        : Call<ListHazard>?//getListHazardSaya//    getListHazardSaya@GET("/android/api/hse/list/hazard/report/hse")fun getListHazardHSE(@Query("dari") dari:String,                     @Query("sampai") sampai:String,                     @Query("page") page:String)        : Call<ListHazard>?//getListHazardSaya    //    doVerifyHazard    @GET("/android/api/hse/list/hazard/report/hse")    fun doVerifyHazard(@Query("option") option:Int,                         @Query("uid") uid:String)            : Call<DaftarAkunResponse>?//doVerifyHazard//    UPLOAD FOTO PROFILE@Multipart@POST("/android/api/user/foto/profile")fun postFotoProfile(    @Part fileToUpload: MultipartBody.Part,    @Part("nik") nik:RequestBody,    @Part("_token") _token:RequestBody) : Call<DaftarAkunResponse>?}
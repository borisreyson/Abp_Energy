package com.misit.abpenergy.Login.Responseimport com.google.gson.annotations.SerializedNamedata class NewLoginResponse(    @field:SerializedName("login")    val login: Login? = null)data class UserLogin(    @field:SerializedName("tglentry")    val tglentry: String? = null,    @field:SerializedName("user_update")    val userUpdate: String? = null,    @field:SerializedName("level")    val level: String? = null,    @field:SerializedName("ttd")    val ttd: String? = null,    @field:SerializedName("photo_profile")    val photoProfile: String? = null,    @field:SerializedName("token_password")    val tokenPassword: String? = null,    @field:SerializedName("nama_lengkap")    val namaLengkap: String? = null,    @field:SerializedName("id_session")    val idSession: String? = null,    @field:SerializedName("rule")    val rule: String? = null,    @field:SerializedName("perusahaan")    val perusahaan: Int? = null,    @field:SerializedName("section")    val section: String? = null,    @field:SerializedName("id_user")    val idUser: Int? = null,    @field:SerializedName("nik")    val nik: String? = null,    @field:SerializedName("password")    val password: String? = null,    @field:SerializedName("department")    val department: String? = null,    @field:SerializedName("email")    val email: String? = null,    @field:SerializedName("username")    val username: String? = null,    @field:SerializedName("status")    val status: Int? = null)data class Login(    @field:SerializedName("success")    val success: Boolean? = null,    @field:SerializedName("user_login")    val userLogin: UserLogin? = null)
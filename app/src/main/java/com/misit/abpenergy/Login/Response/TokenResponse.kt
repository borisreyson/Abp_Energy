package com.misit.abpenergy.Login.Responseimport com.google.gson.annotations.SerializedNamedata class TokenResponse(	@field:SerializedName("create_token")	val createToken: String? = null,	@field:SerializedName("success")	val success: Boolean? = null)
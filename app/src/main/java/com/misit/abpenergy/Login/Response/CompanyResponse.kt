package com.misit.abpenergy.Login.Response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CompanyResponse(

	@field:SerializedName("company")
	var company: List<CompanyItem>? = null
)

@Keep
data class CompanyItem(

	@field:SerializedName("flag")
	var flag: Int? = null,

	@field:SerializedName("id_perusahaan")
	var idPerusahaan: Int? = null,

	@field:SerializedName("time_in")
	var timeIn: String? = null,

	@field:SerializedName("nama_perusahaan")
	var namaPerusahaan: String? = null
)

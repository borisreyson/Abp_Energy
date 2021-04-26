package com.misit.abpenergy.Inspeksi.Response

import com.google.gson.annotations.SerializedName

data class SubInspeksiResponse(

	@field:SerializedName("dataSub")
	var dataSub: List<DataSubItem>? = null
)

data class DataSubItem(

	@field:SerializedName("idForm")
	var idForm: Int? = null,

	@field:SerializedName("tgl_input")
	var tglInput: String? = null,

	@field:SerializedName("numSub")
	var numSub: String? = null,

	@field:SerializedName("idSub")
	var idSub: Int? = null,

	@field:SerializedName("flag")
	var flag: Int? = null,

	@field:SerializedName("user_input")
	var userInput: String? = null,

	@field:SerializedName("nameSub")
	var nameSub: String? = null
)
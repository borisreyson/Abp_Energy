package com.misit.abpenergy.HSE.HazardReport.Response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class HirarkiResponse(

	@field:SerializedName("hirarki")
	var hirarki: List<HirarkiItem>? = null
)

@Keep
data class HirarkiItem(

	@field:SerializedName("tgl_input")
	var tglInput: String? = null,

	@field:SerializedName("flag")
	var flag: Int? = null,

	@field:SerializedName("userInput")
	var userInput: String? = null,

	@field:SerializedName("namaPengendalian")
	var namaPengendalian: String? = null,

	@field:SerializedName("idHirarki")
	var idHirarki: Int? = null
)
@Keep
data class HirarkiItemFull(

	@field:SerializedName("tgl_input")
	var tglInput: String? = null,

	@field:SerializedName("flag")
	var flag: Int? = null,

	@field:SerializedName("userInput")
	var userInput: String? = null,

	@field:SerializedName("namaPengendalian")
	var namaPengendalian: String? = null,

	@field:SerializedName("idHirarki")
	var idHirarki: Int? = null,

	@field:SerializedName("listKet")
	var listKet: MutableList<DetHirarkiItem>? = null
)

package com.misit.abpenergy.HGE.Sarpras.SaranaResponse

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DataItem(

	@field:SerializedName("no_pol")
	var noPol: String? = null,

	@field:SerializedName("flag")
	var flag: String? = null,

	@field:SerializedName("driver")
	var driver: String? = null,

	@field:SerializedName("no_lv")
	var noLv: String? = null,

	@field:SerializedName("pic_lv")
	var picLv: String? = null
)
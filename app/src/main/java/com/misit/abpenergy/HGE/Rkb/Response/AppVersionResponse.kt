package com.misit.abpenergy.HGE.Rkb.Response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AppVersionResponse(

	@field:SerializedName("version")
	var version: String? = null
)
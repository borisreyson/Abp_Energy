package com.misit.abpenergy.HGE.Rkb.Response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DetailRkbResponse(

	@field:SerializedName("detailRkb")
	var detailRkb: List<DetailRkbItem>? = null
)
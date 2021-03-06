package com.misit.abpenergy.HGE.Rkb.Response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CsrfTokenResponse(

    @field:SerializedName("csrf_token")
    val csrfToken: String? = null
)
package com.misit.abpenergy.HGE.Rkb.Model

import androidx.lifecycle.ViewModel

class ApproveModel :ViewModel(){
    var approve = false
    override fun onCleared() {
        super.onCleared()
    }
    fun updateStatus(appr:Boolean){
        approve=appr
    }
}
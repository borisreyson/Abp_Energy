package com.misit.abpenergy.HGE.Rkb.FragmentRKB


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.misit.abpenergy.R

/**
 * A simple [Fragment] subclass.
 */
class ListRkbFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_rkb, container, false)
    }


}

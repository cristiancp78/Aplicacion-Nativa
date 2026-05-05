package com.example.tecnotech.Vendedor.Nav_Fragments_Vendedor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.R
import com.example.tecnotech.Vendedor.Bottom_Nav_Fragments_Vendedor.FragmentAgregarProductosV
import com.example.tecnotech.Vendedor.Bottom_Nav_Fragments_Vendedor.FragmentMisProductosV
import com.example.tecnotech.Vendedor.Bottom_Nav_Fragments_Vendedor.FragmentOrdenesV
import com.example.tecnotech.databinding.FragmentInicioVBinding


class FragmentInicioV : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_inicio_v, container, false)
    }



}
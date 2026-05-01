package com.example.tecnotech.Administrador.Nav_Fragments_Administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Administrador.Bottom_Nav_Fragments_Administrador.FragmentOrdenesA
import com.example.tecnotech.Administrador.Bottom_Nav_Fragments_Administrador.FragmentPanelA
import com.example.tecnotech.Administrador.Bottom_Nav_Fragments_Administrador.FragmentPerfilA
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentProductosABinding


class FragmentProductosA : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_productos_a, container, false)
    }


}
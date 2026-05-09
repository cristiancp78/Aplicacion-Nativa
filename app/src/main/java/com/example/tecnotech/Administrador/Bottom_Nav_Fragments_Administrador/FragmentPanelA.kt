package com.example.tecnotech.Administrador.Bottom_Nav_Fragments_Administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Administrador.MainActivityAdministrador
import com.example.tecnotech.Administrador.Nav_Fragments_Administrador.FragmentAdministradoresA
import com.example.tecnotech.Administrador.Nav_Fragments_Administrador.FragmentProductosA
import com.example.tecnotech.Administrador.Nav_Fragments_Administrador.FragmentUsuariosA
import com.example.tecnotech.Administrador.Nav_Fragments_Administrador.FragmentVendedoresA
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentPanelABinding


class FragmentPanelA : Fragment(R.layout.fragment_panel_a) {



    override fun onViewCreated(view: View,  savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPanelABinding.bind(view)

        binding.cardUsuarios.setOnClickListener {
            (activity as MainActivityAdministrador).replaceFragment(FragmentUsuariosA())
        }
        binding.cardVendedores.setOnClickListener {
            (activity as MainActivityAdministrador).replaceFragment(FragmentVendedoresA())
        }
        binding.cardAdministradores.setOnClickListener {
            (activity as MainActivityAdministrador).replaceFragment(FragmentAdministradoresA())
        }
        binding.cardProductos.setOnClickListener {
            (activity as MainActivityAdministrador).replaceFragment(FragmentProductosA())
        }
    }

}
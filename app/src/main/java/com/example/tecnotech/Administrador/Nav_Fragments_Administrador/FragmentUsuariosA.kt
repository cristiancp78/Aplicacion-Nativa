package com.example.tecnotech.Administrador.Nav_Fragments_Administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Adaptadores.AdaptadorUsuariosA
import com.example.tecnotech.Modelos.ModeloUsuarios
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentUsuariosABinding


class FragmentUsuariosA : Fragment() {


    private lateinit var binding: FragmentUsuariosABinding
    private lateinit var usuariosArrayList: ArrayList<ModeloUsuarios>
    private lateinit var adaptadorUsuariosA: AdaptadorUsuariosA

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUsuariosABinding.inflate(inflater, container, false)

        listarUsuarios()
        return binding.root
    }

    private fun listarUsuarios() {
        usuariosArrayList = ArrayList()
        usuariosArrayList.add(ModeloUsuarios("1","Alejandro Martinez","alejandro@gmail.com","Carrera 56a #45-83","cliente", R.drawable.icofotousuario))
        usuariosArrayList.add(ModeloUsuarios("2","Juan Perez","juan@gmail.com","Carrera 47a #12-84","cliente", R.drawable.icofotousuario))
        usuariosArrayList.add(ModeloUsuarios("3","Maria Lopez","maria@gmail.com","Carrera 24 #24-56","cliente", R.drawable.icofotousuario))
        usuariosArrayList.add(ModeloUsuarios("4","Carlos Ramirez","carlos@gmail.com","Carrera 112 #55-23","cliente", R.drawable.icofotousuario))

        adaptadorUsuariosA = AdaptadorUsuariosA(requireContext(), usuariosArrayList)
        binding.usuariosRv.adapter = adaptadorUsuariosA
    }


}
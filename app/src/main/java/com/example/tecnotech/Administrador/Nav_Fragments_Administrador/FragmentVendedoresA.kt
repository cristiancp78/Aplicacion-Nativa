package com.example.tecnotech.Administrador.Nav_Fragments_Administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Adaptadores.AdaptadorVendedoresA
import com.example.tecnotech.Modelos.ModeloUsuarios
import com.example.tecnotech.Modelos.ModeloVendedores
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentVendedoresABinding


class FragmentVendedoresA : Fragment() {

    private lateinit var binding: FragmentVendedoresABinding
    private lateinit var adapter: AdaptadorVendedoresA
    private lateinit var vendedoresArrayList: ArrayList<ModeloVendedores>



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentVendedoresABinding.inflate(inflater, container, false)
        listarVendedores()

        return binding.root
    }

    private fun listarVendedores() {
        vendedoresArrayList = ArrayList()
        vendedoresArrayList.add(ModeloVendedores("1","Camilo Lopez","camilo@gmail.com","TecnoStore", R.drawable.icofotovendedor))
        vendedoresArrayList.add(ModeloVendedores("2","Julian Cardenas","julian@gmail.com","MobilCompac", R.drawable.icofotovendedor))
        vendedoresArrayList.add(ModeloVendedores("3","Sebastian Martinez","sebastian@gmail.com","perifericStore", R.drawable.icofotovendedor))
        vendedoresArrayList.add(ModeloVendedores("4","Andres Flores","andres@gmail.com","CumpoGaming", R.drawable.icofotovendedor))

        adapter = AdaptadorVendedoresA(requireContext(), vendedoresArrayList)
        binding.vendedoresRv.adapter = adapter
    }


}
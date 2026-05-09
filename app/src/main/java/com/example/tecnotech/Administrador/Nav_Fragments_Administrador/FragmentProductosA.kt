package com.example.tecnotech.Administrador.Nav_Fragments_Administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Adaptadores.AdaptadorProductosA
import com.example.tecnotech.Modelos.ModeloProductosV
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentProductosABinding


class FragmentProductosA : Fragment() {

    private lateinit var binding: FragmentProductosABinding
    private lateinit var productosArrayList: ArrayList<ModeloProductosV>
    private lateinit var adaptadorProducto: AdaptadorProductosA


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductosABinding.inflate(inflater, container, false)

        listarProductos()

        return binding.root
    }

    private fun listarProductos() {
        productosArrayList = ArrayList()

        productosArrayList.add(ModeloProductosV("1","Mouse Gamer", "150.000", R.drawable.icoproduc ))
        productosArrayList.add(ModeloProductosV("2","Samsung S25 Ultra", "3.700.000", R.drawable.icoproduc ))
        productosArrayList.add(ModeloProductosV("3","Monitos Samsung 24\"", "850.000", R.drawable.icoproduc ))
        productosArrayList.add(ModeloProductosV("4","Lenovo ThinkPad", "2.500.000", R.drawable.icoproduc ))

        adaptadorProducto = AdaptadorProductosA(requireContext(), productosArrayList)
        binding.productosRV.adapter = adaptadorProducto
    }

}
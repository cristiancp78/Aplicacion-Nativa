package com.example.tecnotech.Vendedor.Bottom_Nav_Fragments_Vendedor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Adaptadores.AdaptadorProductoV
import com.example.tecnotech.Modelos.ModeloProductosV
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentMisProductosVBinding


class FragmentMisProductosV : Fragment() {

    private lateinit var binding: FragmentMisProductosVBinding
    private lateinit var productosArrayList: ArrayList<ModeloProductosV>
    private lateinit var adaptadorProductoV: AdaptadorProductoV

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMisProductosVBinding.inflate(inflater, container, false)

        listarProductos()
        return binding.root
    }

    private fun listarProductos() {
        productosArrayList = ArrayList()

        productosArrayList.add(ModeloProductosV("1","Mouse Gamer", "150.000", R.drawable.icoproduc ))
        productosArrayList.add(ModeloProductosV("2","Samsung S25 Ultra", "3.700.000", R.drawable.icoproduc ))
        productosArrayList.add(ModeloProductosV("3","Monitos Samsung 24\"", "850.000", R.drawable.icoproduc ))
        productosArrayList.add(ModeloProductosV("4","Lenovo ThinkPad", "2.500.000", R.drawable.icoproduc ))

        adaptadorProductoV = AdaptadorProductoV(requireContext(), productosArrayList)
        binding.productosRV.adapter = adaptadorProductoV
    }

}
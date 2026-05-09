package com.example.tecnotech.Cliente.Bottom_Nav_Fragments_Cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Adaptadores.AdaptadorProductoCarrito
import com.example.tecnotech.Modelos.ModeloProductoC
import com.example.tecnotech.Modelos.ModeloProductoCarrito
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentCarritoBinding

class FragmentCarrito : Fragment() {

    private lateinit var binding: FragmentCarritoBinding
    private lateinit var adapter: AdaptadorProductoCarrito
    private lateinit var productosCarritoArrayList: ArrayList<ModeloProductoCarrito>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCarritoBinding.inflate(inflater, container, false)

        listarProductosCarrito()
        return binding.root
    }

    private fun listarProductosCarrito() {
        productosCarritoArrayList = ArrayList()

        productosCarritoArrayList.add(ModeloProductoCarrito("1","Teclado RGB","150.000",R.drawable.icoproduc, 1))
        productosCarritoArrayList.add(ModeloProductoCarrito("2","iPhone 15","4.500.000",R.drawable.icoproduc, 3))
        productosCarritoArrayList.add(ModeloProductoCarrito("3","MacBook Pro","8.000.000",R.drawable.icoproduc, 1))
        productosCarritoArrayList.add(ModeloProductoCarrito("4","Audífonos Sony","900.000",R.drawable.icoproduc, 2))

        adapter = AdaptadorProductoCarrito(requireContext(), productosCarritoArrayList)
        binding.carritoRv.adapter = adapter
    }

}
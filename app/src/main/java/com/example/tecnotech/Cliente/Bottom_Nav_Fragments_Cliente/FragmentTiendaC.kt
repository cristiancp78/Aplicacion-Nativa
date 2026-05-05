package com.example.tecnotech.Cliente.Bottom_Nav_Fragments_Cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Adaptadores.AdaptadorCategoriaC
import com.example.tecnotech.Modelos.ModeloCategoriaC
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentTiendaCBinding


class FragmentTiendaC : Fragment() {

    private lateinit var binding: FragmentTiendaCBinding

    private lateinit var categoriasArrayList: ArrayList<ModeloCategoriaC>
    private lateinit var adaptadorCategoriaC: AdaptadorCategoriaC

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTiendaCBinding.inflate(inflater, container, false)
        listarCategorias()
        return binding.root
    }

    private fun listarCategorias() {
        categoriasArrayList = ArrayList()

        categoriasArrayList.add(ModeloCategoriaC("1", categoria = "Celulares", imagen = R.drawable.icoimagen))
        categoriasArrayList.add(ModeloCategoriaC("2", categoria = "Laptops", imagen = R.drawable.icoimagen))
        categoriasArrayList.add(ModeloCategoriaC("3", categoria = "Accesorios", imagen = R.drawable.icoimagen))
        categoriasArrayList.add(ModeloCategoriaC("4", categoria = "Pantallas", imagen = R.drawable.icoimagen))

        adaptadorCategoriaC = AdaptadorCategoriaC(requireContext(), categoriasArrayList)
        binding.categoriasRV.adapter = adaptadorCategoriaC
    }

}
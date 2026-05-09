package com.example.tecnotech.Cliente.Bottom_Nav_Fragments_Cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Adaptadores.AdaptadorProductoC
import com.example.tecnotech.Modelos.ModeloProductoC
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentFavoritosCBinding

class FragmentFavoritosC : Fragment() {

    private lateinit var binding: FragmentFavoritosCBinding
    private lateinit var favoritosArrayList: ArrayList<ModeloProductoC>
    private lateinit var adaptadorProductoC: AdaptadorProductoC


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritosCBinding.inflate(inflater, container, false)

        listarFavoritos()
        return binding.root
    }

    private fun listarFavoritos() {
        favoritosArrayList = ArrayList()

        favoritosArrayList.add(ModeloProductoC("1", "Smartphone Samsung", "3.500.000", R.drawable.icoproduc, "Celulares", true))
        favoritosArrayList.add(ModeloProductoC("2", "Audífonos Sony", "1.200.000", R.drawable.icoproduc, "Audio", true))
        favoritosArrayList.add(ModeloProductoC("3", "Reloj Inteligente", "850.000", R.drawable.icoproduc, "Accesorios", true))
        favoritosArrayList.add(ModeloProductoC("4", "Cargador Carga Rápida", "95.000", R.drawable.icoproduc, "Accesorios", true))

        adaptadorProductoC = AdaptadorProductoC(requireContext(), favoritosArrayList)

        binding.favoritosRv.adapter = adaptadorProductoC
    }

}
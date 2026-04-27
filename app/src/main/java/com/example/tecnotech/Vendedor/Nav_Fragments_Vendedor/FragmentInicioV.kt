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

    private lateinit var binding: FragmentInicioVBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInicioVBinding.inflate(inflater, container, false)
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mis_productos_v -> {
                    replaceFragment(FragmentMisProductosV())

                }
                R.id.agregar_producto_v -> {
                    replaceFragment(FragmentAgregarProductosV())

                }
                R.id.mis_ordenes_v -> {
                    replaceFragment(FragmentOrdenesV())

                }
            }
            true
        }

        replaceFragment(FragmentMisProductosV())
        binding.bottomNavigation.selectedItemId = R.id.mis_productos_v

        return binding.root
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()

    }


}
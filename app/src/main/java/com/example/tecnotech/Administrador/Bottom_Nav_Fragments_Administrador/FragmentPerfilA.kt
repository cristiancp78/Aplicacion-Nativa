package com.example.tecnotech.Administrador.Bottom_Nav_Fragments_Administrador

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentPerfilABinding

class FragmentPerfilA : Fragment(R.layout.fragment_perfil_a) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPerfilABinding.bind(view)

        binding.btnCerrarSesion.setOnClickListener {
            Toast.makeText(requireContext(), "Cerrando sesión...", Toast.LENGTH_SHORT).show()
        }
    }
}
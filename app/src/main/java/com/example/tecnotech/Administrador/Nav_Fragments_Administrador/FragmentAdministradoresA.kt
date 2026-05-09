package com.example.tecnotech.Administrador.Nav_Fragments_Administrador

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tecnotech.Adaptadores.AdaptadorAdministradores
import com.example.tecnotech.Administrador.RegistroAdministradorActivity
import com.example.tecnotech.Modelos.ModeloUsuarios
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentAdministradoresABinding


class FragmentAdministradoresA : Fragment() {

    private lateinit var binding: FragmentAdministradoresABinding
    private lateinit var listaAdministradores: ArrayList<ModeloUsuarios>
    private lateinit var adaptadorAdministradores: AdaptadorAdministradores

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdministradoresABinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        listarAdministradores()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_admin, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_agregar_admin) {
            startActivity(Intent(requireContext(), RegistroAdministradorActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
    private fun listarAdministradores() {
        listaAdministradores = ArrayList()
        listaAdministradores.add(ModeloUsuarios("1", "Carlos Peralta", "carlos@gmail.com","carrera 27 #98-25","Administrador" ,R.drawable.icofotoadministrador))
        listaAdministradores.add(ModeloUsuarios("2", "Elena Lopez", "elena@gmail.com","carrera 112 #56-98","Administrador" ,R.drawable.icofotoadministrador))

        adaptadorAdministradores = AdaptadorAdministradores(requireContext(), listaAdministradores)
        binding.administradoresRv.adapter = adaptadorAdministradores
    }

}
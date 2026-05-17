package com.example.tecnotech.Administrador.Nav_Fragments_Administrador

import android.content.Context
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
import com.example.tecnotech.Modelos.ModeloAdministradores
import com.example.tecnotech.Modelos.ModeloUsuarios
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentAdministradoresABinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentAdministradoresA : Fragment() {

    private lateinit var binding: FragmentAdministradoresABinding

    private lateinit var mContexto: Context
    private lateinit var listaAdministradores: ArrayList<ModeloAdministradores>
    private lateinit var adaptadorAdministradores: AdaptadorAdministradores

    override fun onAttach(context: Context) {
        mContexto = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdministradoresABinding.inflate(LayoutInflater.from(mContexto), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        listarAdministradores()
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

        val ref = FirebaseDatabase.getInstance().getReference("Administradores")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaAdministradores.clear()
                for (ds in snapshot.children) {
                    val modelAdministradores = ds.getValue(ModeloAdministradores::class.java)
                    listaAdministradores.add(modelAdministradores!!)
                }
                adaptadorAdministradores = AdaptadorAdministradores(mContexto, listaAdministradores)
                binding.administradoresRv.adapter = adaptadorAdministradores

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}
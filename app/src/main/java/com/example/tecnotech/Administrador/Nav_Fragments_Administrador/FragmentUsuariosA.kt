package com.example.tecnotech.Administrador.Nav_Fragments_Administrador

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Adaptadores.AdaptadorUsuariosA
import com.example.tecnotech.Modelos.ModeloUsuarios
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentUsuariosABinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentUsuariosA : Fragment() {


    private lateinit var binding: FragmentUsuariosABinding

    private lateinit var mContexto: Context
    private lateinit var usuariosArrayList: ArrayList<ModeloUsuarios>
    private lateinit var adaptadorUsuariosA: AdaptadorUsuariosA

    override fun onAttach(context: Context) {
        mContexto = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUsuariosABinding.inflate(LayoutInflater.from(mContexto), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listarUsuarios()
    }

    private fun listarUsuarios() {
        usuariosArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usuariosArrayList.clear()
                for (ds in snapshot.children) {
                    val modeloUsuario = ds.getValue(ModeloUsuarios::class.java)
                    usuariosArrayList.add(modeloUsuario!!)
                }
                adaptadorUsuariosA = AdaptadorUsuariosA(mContexto, usuariosArrayList)
                binding.usuariosRv.adapter = adaptadorUsuariosA
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


}
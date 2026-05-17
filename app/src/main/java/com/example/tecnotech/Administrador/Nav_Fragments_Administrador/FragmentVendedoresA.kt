package com.example.tecnotech.Administrador.Nav_Fragments_Administrador

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Adaptadores.AdaptadorVendedoresA
import com.example.tecnotech.Modelos.ModeloUsuarios
import com.example.tecnotech.Modelos.ModeloVendedores
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentVendedoresABinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentVendedoresA : Fragment() {

    private lateinit var binding: FragmentVendedoresABinding

    private lateinit var mContexto: Context
    private lateinit var adapter: AdaptadorVendedoresA
    private lateinit var vendedoresArrayList: ArrayList<ModeloVendedores>


    override fun onAttach(context: Context) {
        mContexto = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentVendedoresABinding.inflate(LayoutInflater.from(mContexto), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listarVendedores()
    }

    private fun listarVendedores() {
        vendedoresArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Vendedores")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                vendedoresArrayList.clear()
                for (ds in snapshot.children) {
                    val modeloVendedor = ds.getValue(ModeloVendedores::class.java)
                    vendedoresArrayList.add(modeloVendedor!!)
                }
                adapter = AdaptadorVendedoresA(mContexto, vendedoresArrayList)
                binding.vendedoresRv.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}
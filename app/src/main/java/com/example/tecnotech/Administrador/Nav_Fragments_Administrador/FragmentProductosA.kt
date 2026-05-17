package com.example.tecnotech.Administrador.Nav_Fragments_Administrador

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Adaptadores.AdaptadorProductosA
import com.example.tecnotech.Modelos.ModeloProductosV
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentProductosABinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentProductosA : Fragment() {

    private lateinit var binding: FragmentProductosABinding

    private lateinit var mContexto: Context
    private lateinit var productosArrayList: ArrayList<ModeloProductosV>
    private lateinit var adaptadorProductosA: AdaptadorProductosA

    override fun onAttach(context: Context) {
        mContexto = context
        super.onAttach(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductosABinding.inflate(LayoutInflater.from(mContexto), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listarProductos()
    }

    private fun listarProductos() {
        productosArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productosArrayList.clear()
                for (ds in snapshot.children) {
                    val modeloProducto = ds.getValue(ModeloProductosV::class.java)
                    productosArrayList.add(modeloProducto!!)
                }
                adaptadorProductosA = AdaptadorProductosA(mContexto, productosArrayList)
                binding.productosRV.adapter = adaptadorProductosA

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
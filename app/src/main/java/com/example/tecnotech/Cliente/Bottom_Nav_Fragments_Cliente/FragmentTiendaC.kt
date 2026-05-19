package com.example.tecnotech.Cliente.Bottom_Nav_Fragments_Cliente

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Adaptadores.AdaptadorCategoriaC
import com.example.tecnotech.Adaptadores.AdaptadorProductoAleatorio
import com.example.tecnotech.Modelos.ModeloCategoriaC
import com.example.tecnotech.Modelos.ModeloProductoC
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentTiendaCBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FragmentTiendaC : Fragment() {

    private lateinit var binding: FragmentTiendaCBinding

    private lateinit var mContext: Context

    private lateinit var categoriasArrayList: ArrayList<ModeloCategoriaC>
    private lateinit var adaptadorCategoriaC: AdaptadorCategoriaC

    private lateinit var productosArrayList: ArrayList<ModeloProductoC>
    private lateinit var adaptadorProducto: AdaptadorProductoAleatorio

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTiendaCBinding.inflate(LayoutInflater.from(mContext), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listarCategorias()
        obtenerProductosAleatorios()
    }

    private fun obtenerProductosAleatorios() {
        productosArrayList = ArrayList()

        var ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productosArrayList.clear()
                for (ds in snapshot.children) {
                    val modelo = ds.getValue(ModeloProductoC::class.java)
                    productosArrayList.add(modelo!!)
                }

                val listaAleatoria = productosArrayList.shuffled().take(3)

                adaptadorProducto = AdaptadorProductoAleatorio(mContext, listaAleatoria)
                binding.productosAleatoriosRV.adapter = adaptadorProducto

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun listarCategorias() {
        categoriasArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categorias").orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriasArrayList.clear()
                for (ds in snapshot.children) {
                    val modelo = ds.getValue(ModeloCategoriaC::class.java)
                    categoriasArrayList.add(modelo!!)
                }
                adaptadorCategoriaC = AdaptadorCategoriaC(mContext, categoriasArrayList)
                binding.categoriasRV.adapter = adaptadorCategoriaC
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}
package com.example.tecnotech.Cliente.Bottom_Nav_Fragments_Cliente

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Adaptadores.AdaptadorProductoCarrito
import com.example.tecnotech.Modelos.ModeloProductoC
import com.example.tecnotech.Modelos.ModeloProductoCarrito
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentCarritoBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentCarrito : Fragment() {

    private lateinit var binding: FragmentCarritoBinding
    private lateinit var productoAdapter: AdaptadorProductoCarrito
    private lateinit var mContexto: Context
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var productosArrayList: ArrayList<ModeloProductoCarrito>

    override fun onAttach(context: Context) {
        mContexto = context
        super.onAttach(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCarritoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        cargarProductoCarrito()
        sumaProductos()
    }

    private fun sumaProductos() {
        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.child(firebaseAuth.uid!!).child("CarritoCompras")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var suma = 0.0
                    for (ds in snapshot.children) {
                        val precioFinal = ds.child("precioFinal").getValue(String::class.java)

                        if (precioFinal != null) {
                            val precioFinalDouble = precioFinal.replace("[^0-9.E+\\-]".toRegex(), "").toDoubleOrNull() ?: 0.0
                            suma += precioFinalDouble
                        }
                        binding.sumaProductos.text = String.format("%,.0f COP", suma)

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun cargarProductoCarrito() {
        productosArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.child(firebaseAuth.uid!!).child("CarritoCompras")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    productosArrayList.clear()
                    for (ds in snapshot.children) {
                        val modelo = ds.getValue(ModeloProductoCarrito::class.java)
                        productosArrayList.add(modelo!!)
                    }
                    productoAdapter = AdaptadorProductoCarrito(mContexto, productosArrayList)
                    binding.carritoRv.adapter = productoAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


}
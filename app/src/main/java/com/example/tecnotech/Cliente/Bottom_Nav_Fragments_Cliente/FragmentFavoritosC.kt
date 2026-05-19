package com.example.tecnotech.Cliente.Bottom_Nav_Fragments_Cliente

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tecnotech.Adaptadores.AdaptadorProductoC
import com.example.tecnotech.Modelos.ModeloProductoC
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentFavoritosCBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentFavoritosC : Fragment() {

    private lateinit var binding: FragmentFavoritosCBinding
    private lateinit var mContext : Context
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var favoritosArrayList: ArrayList<ModeloProductoC>
    private lateinit var adaptadorProductoC: AdaptadorProductoC

    override fun onAttach(context: Context) {
        this.mContext = context
        super.onAttach(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritosCBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        listarFavoritos()
    }

    private fun listarFavoritos() {
        favoritosArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.child(firebaseAuth.uid!!).child("Favoritos")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    favoritosArrayList.clear()
                    for (ds in snapshot.children) {
                        val idProducto = "${ds.child("idProducto").value}"

                        val refProduc = FirebaseDatabase.getInstance().getReference("Productos")
                        refProduc.child(idProducto)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    try {
                                        val modelo = snapshot.getValue(ModeloProductoC::class.java)
                                        favoritosArrayList.add(modelo!!)
                                    }catch (e: Exception){

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })
                    }
                    Handler().postDelayed({
                        adaptadorProductoC = AdaptadorProductoC(mContext, favoritosArrayList)
                        binding.favoritosRv.adapter = adaptadorProductoC
                    }, 500)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}
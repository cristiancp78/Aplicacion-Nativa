package com.example.tecnotech

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Constantes {
    fun obtenerTiempoD(): Long{
        return System.currentTimeMillis()
    }

    fun agregarProductoFav(context: Context, idProducto: String){
        val firebaseAuth = FirebaseAuth.getInstance()
        val tiempo = Constantes().obtenerTiempoD()

        val hashMap = HashMap<String, Any>()
        hashMap["idProducto"] = idProducto
        hashMap["idFav"] = tiempo

        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.child(firebaseAuth.uid!!).child("Favoritos").child(idProducto)
            .setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(context, "Agregado a favoritos", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                Toast.makeText(context, "Error al agregar a favoritos", Toast.LENGTH_SHORT).show()
            }
    }

    fun eliminarProductoFav(context: Context, idProducto: String){
        val firebaseAuth = FirebaseAuth.getInstance()


        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.child(firebaseAuth.uid!!).child("Favoritos").child(idProducto)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                Toast.makeText(context, "Error al eliminar de favoritos", Toast.LENGTH_SHORT).show()
            }
    }

}
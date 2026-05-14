package com.example.tecnotech

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.Administrador.LoginAdministradorActivity
import com.example.tecnotech.Administrador.MainActivityAdministrador
import com.example.tecnotech.Cliente.LoginClienteActivity
import com.example.tecnotech.Cliente.MainActivityCliente
import com.example.tecnotech.Vendedor.LoginVendedorActivity
import com.example.tecnotech.Vendedor.MainActivityVendedor
import com.example.tecnotech.databinding.ActivitySeleccionarTipoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SeleccionarTipoActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeleccionarTipoBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionarTipoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            val info = "Sesión activa: ${firebaseUser.email}\nUID: ${firebaseUser.uid}"
            binding.root.visibility = android.view.View.GONE
            println(info)
            verificarTipoUsuario()

        }else{
            binding.root.visibility = android.view.View.VISIBLE
        }

        binding.tipoAdministrador.setOnClickListener {
            startActivity(Intent(this, LoginAdministradorActivity::class.java))
        }
        binding.tipoCliente.setOnClickListener {
            startActivity(Intent(this, LoginClienteActivity::class.java))
        }
        binding.tipoVendedor.setOnClickListener {
            startActivity(Intent(this, LoginVendedorActivity::class.java))
        }
    }

    private fun verificarTipoUsuario() {
        val uid = firebaseAuth.uid!!
        val refV = FirebaseDatabase.getInstance().getReference("Vendedores")
        refV.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        startActivity(Intent(this@SeleccionarTipoActivity, MainActivityVendedor::class.java))
                        finish()
                    } else {
                        verificarCliente()
                    }
                }

            override fun onCancelled(error: DatabaseError) {
                binding.root.visibility = android.view.View.VISIBLE
            }
            })
    }

    private fun verificarCliente() {
        val uid = firebaseAuth.uid!!
        println("UID que llega: $uid")
        val refC = FirebaseDatabase.getInstance().getReference("Clientes")
        refC.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    startActivity(Intent(this@SeleccionarTipoActivity, MainActivityCliente::class.java))
                    finish()
                } else {
                    verificarAdministrador(uid)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                binding.root.visibility = android.view.View.VISIBLE
            }
        })
    }

    private fun verificarAdministrador(uid: String) {
        val refA = FirebaseDatabase.getInstance().getReference("Administradores")
        refA.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    startActivity(Intent(this@SeleccionarTipoActivity, MainActivityAdministrador::class.java))
                    finish()
                } else {
                    binding.root.visibility = android.view.View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                binding.root.visibility = android.view.View.VISIBLE
            }
        })
    }

}
package com.example.tecnotech.Administrador.Administrar

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityEditarAdministradoresBinding
import com.google.firebase.database.FirebaseDatabase

class ActivityEditarAdministradores : AppCompatActivity() {

    private lateinit var binding: ActivityEditarAdministradoresBinding
    private lateinit var progressDialog: ProgressDialog
    private var uid = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarAdministradoresBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)

        uid = intent.getStringExtra("idAdmin")?: ""

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        obtenerInfo()
        binding.etEmail.isEnabled = false

        binding.btnEditarAdministrador.setOnClickListener {
            actualizarAdmin()
        }

    }

    private fun actualizarAdmin() {
        val nombres = binding.etNombresA.text.toString().trim()
        val correo = binding.etEmail.text.toString().trim()
        val direccion = binding.etDireccionA.text.toString().trim()

        if(nombres.isEmpty()){
            binding.etNombresA.error = "El campo debe contener un nombre"
            binding.etNombresA.requestFocus()
        }else if(direccion.isEmpty()){
            binding.etDireccionA.error = "El campo debe contener una direccion"
            binding.etDireccionA.requestFocus()
        }else{
            progressDialog.setMessage("Actualizando administrador")
            progressDialog.show()

            val hashMap = HashMap<String, Any>()
            hashMap["nombres"] = nombres
            hashMap["direccion"] = direccion

            val ref = FirebaseDatabase.getInstance().getReference("Administradores")
            ref.child(uid)
                .updateChildren(hashMap)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Administrador actualizado", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "No se pudo actualizar el administrador", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun obtenerInfo() {
        val ref = FirebaseDatabase.getInstance().getReference("Administradores")
        ref.child(uid).get()
            .addOnSuccessListener {
                val nombres = "${it.child("nombres").value}"
                val correo = "${it.child("correo").value}"
                val direccion = "${it.child("direccion").value}"

                binding.etNombresA.setText(nombres)
                binding.etEmail.setText(correo)
                binding.etDireccionA.setText(direccion)
            }
            .addOnFailureListener {
                Toast.makeText(this, "No se pudo cargar la informacion", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
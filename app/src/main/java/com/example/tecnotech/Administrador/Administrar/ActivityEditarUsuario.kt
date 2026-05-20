package com.example.tecnotech.Administrador.Administrar

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityEditarUsuarioBinding
import com.google.firebase.database.FirebaseDatabase

class ActivityEditarUsuario : AppCompatActivity() {

    private lateinit var binding: ActivityEditarUsuarioBinding
    private lateinit var progressDialog: ProgressDialog

    private var uid = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)


        uid = intent.getStringExtra("idUsuario")?: ""

        cargarInfo(uid)

        binding.etEmail.isEnabled = false
        binding.etDireccion.isEnabled = false


        binding.btnEditarCliente.setOnClickListener {
            actualizarCliente()
        }


    }

    private fun actualizarCliente() {


        val nombres = binding.etNombresC.text.toString().trim()
        val correo = binding.etEmail.text.toString().trim()
        val cedula = binding.etCedula.text.toString().trim()
        val direccion = binding.etDireccion.text.toString().trim()

        if(nombres.isEmpty()){
            binding.etNombresC.error = "El campo debe contener un nombre"
            binding.etNombresC.requestFocus()
        }else if(cedula.isEmpty()){
            binding.etCedula.error = "El campo debe contener una cedula"
            binding.etCedula.requestFocus()
        } else{
            progressDialog.setMessage("Actualizando cliente")
            progressDialog.show()

            val hashMap = HashMap<String, Any>()
            hashMap["nombres"] = nombres
            hashMap["cedula"] = cedula

            val ref = FirebaseDatabase.getInstance().getReference("Clientes")
            ref.child(uid)
                .updateChildren(hashMap)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Cliente actualizado", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "No se pudo actualizar el cliente", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun cargarInfo(idUsuario: String) {
        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.child(idUsuario).get()
            .addOnSuccessListener { snapshot ->
                val nombres = "${snapshot.child("nombres").value}"
                val cedula = "${snapshot.child("cedula").value}"
                val correo = "${snapshot.child("correo").value}"
                val direccion = "${snapshot.child("direccion").value}"
                val imagenUrl = "${snapshot.child("imagen").value}"

                binding.etNombresC.setText(nombres)
                binding.etCedula.setText(cedula)
                binding.etEmail.setText(correo)
                binding.etDireccion.setText(direccion)

                try {
                    Glide.with(this)
                        .load(imagenUrl)
                        .placeholder(R.drawable.img_perfil)
                        .circleCrop()
                        .into(binding.imgPerfilUsuario)
                }catch (e: Exception){

                }
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
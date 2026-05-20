package com.example.tecnotech.Administrador.Administrar

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityEditarVendedoresBinding
import com.google.firebase.database.FirebaseDatabase

class ActivityEditarVendedores : AppCompatActivity() {

    private lateinit var binding: ActivityEditarVendedoresBinding
    private lateinit var progressDialog: ProgressDialog

    private var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarVendedoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)

        uid = intent.getStringExtra("idVendedor")?: ""

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        cargarInfo(uid)
        binding.etEmail.isEnabled = false
        binding.etDireccionV.isEnabled = false



        binding.btnEditarVendedor.setOnClickListener {
            actualizarVendedor()
        }

    }

    private fun actualizarVendedor() {
        val nombres = binding.etNombresV.text.toString().trim()
        val correo = binding.etEmail.text.toString().trim()
        val cedula = binding.etCedula.text.toString().trim()
        val tienda = binding.etNombreTienda.text.toString().trim()
        val direccion = binding.etDireccionV.text.toString().trim()


        if(nombres.isEmpty()){
            binding.etNombresV.error = "El campo debe contener un nombre"
            binding.etNombresV.requestFocus()
        }else if(cedula.isEmpty()){
            binding.etCedula.error = "El campo debe contener una cedula"
            binding.etCedula.requestFocus()
        } else if(tienda.isEmpty()){
            binding.etNombreTienda.error = "El campo debe contener un nombre de tienda"
            binding.etNombreTienda.requestFocus()
        }else{
            progressDialog.setMessage("Actualizando vendedor")
            progressDialog.show()

            val hashMap = HashMap<String, Any>()
            hashMap["nombres"] = nombres
            hashMap["cedula"] = cedula
            hashMap["tienda"] = tienda

            val ref = FirebaseDatabase.getInstance().getReference("Vendedores")
            ref.child(uid)
                .updateChildren(hashMap)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Vendedor actualizado", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "No se pudo actualizar el vendedor", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun cargarInfo(uid: String) {
        val ref = FirebaseDatabase.getInstance().getReference("Vendedores")
        ref.child(uid).get()
            .addOnSuccessListener {
                val nombres = "${it.child("nombres").value}"
                val correo = "${it.child("correo").value}"
                val cedula = "${it.child("cedula").value}"
                val tienda = "${it.child("tienda").value}"
                val direccion = "${it.child("direccion").value}"


                binding.etNombresV.setText(nombres)
                binding.etEmail.setText(correo)
                binding.etCedula.setText(cedula)
                binding.etNombreTienda.setText(tienda)
                binding.etDireccionV.setText(direccion)

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
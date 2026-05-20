package com.example.tecnotech.Administrador.Administrar

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tecnotech.Mapas.SeleccionarUbicacionActivity
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

        binding.ubicacion.setOnClickListener {
            val intent = Intent(this, SeleccionarUbicacionActivity::class.java)
            intent.putExtra("latitud", latitud)
            intent.putExtra("longitud", longitud)

            obtenerUbicacion_ARL.launch(intent)
        }

    }

    private var latitud = 0.0
    private var longitud = 0.0
    private var direccion = ""
    private val obtenerUbicacion_ARL = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
        if (resultado.resultCode == Activity.RESULT_OK) {
            val data = resultado.data
            if(data != null){
                latitud = data.getDoubleExtra("latitud", 0.0)
                longitud = data.getDoubleExtra("longitud", 0.0)
                direccion = data.getStringExtra("direccion") ?: ""

                binding.ubicacion.setText(direccion)
            }
        } else {
            Toast.makeText(this, "Accion Cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizarAdmin() {
        val nombres = binding.etNombresA.text.toString().trim()
        val correo = binding.etEmail.text.toString().trim()
        val cedula = binding.etCedula.text.toString().trim()
        val direccion = binding.ubicacion.text.toString().trim()

        if(nombres.isEmpty()){
            binding.etNombresA.error = "El campo debe contener un nombre"
            binding.etNombresA.requestFocus()
        }else if(cedula.isEmpty()){
            binding.etCedula.error = "El campo debe contener una cedula"
            binding.etCedula.requestFocus()
        }else if(direccion.isEmpty()){
            binding.ubicacion.error = "El campo debe contener una direccion"
        } else{
            progressDialog.setMessage("Actualizando administrador")
            progressDialog.show()

            val hashMap = HashMap<String, Any>()
            hashMap["nombres"] = nombres
            hashMap["cedula"] = cedula
            hashMap["direccion"] = direccion
            hashMap["latitud"] = latitud
            hashMap["longitud"] = longitud


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
                val cedula = "${it.child("cedula").value}"
                val correo = "${it.child("correo").value}"
                val direccion = "${it.child("direccion").value}"
                longitud = it.child("longitud").value?.toString()?.toDouble()?:0.0
                latitud = it.child("latitud").value?.toString()?.toDouble()?:0.0



                binding.etNombresA.setText(nombres)
                binding.etCedula.setText(cedula)
                binding.etEmail.setText(correo)
                binding.ubicacion.setText(direccion)
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
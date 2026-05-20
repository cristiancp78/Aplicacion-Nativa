package com.example.tecnotech.Administrador

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.Mapas.SeleccionarUbicacionActivity
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityRegistroAdministradorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistroAdministradorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroAdministradorBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroAdministradorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.txtRegistroA)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnRegistrarA.setOnClickListener {
            validarInformacion()
        }

        binding.ubicacion.setOnClickListener {
            val intent = Intent(this, SeleccionarUbicacionActivity::class.java)
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

    private var nombre = ""
    private var correo = ""
    private var password = ""
    private var confirmarPassword = ""

    private fun validarInformacion() {
        nombre = binding.etNombresA.text.toString().trim()
        correo = binding.etEmail.text.toString().trim()
        direccion = binding.ubicacion.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        confirmarPassword = binding.etConfirmarPassword.text.toString().trim()

        if (nombre.isEmpty()) {
            binding.etNombresA.error = "Ingrese nombre"
            binding.etNombresA.requestFocus()
        } else if (correo.isEmpty()) {
            binding.etEmail.error = "Ingrese correo"
            binding.etEmail.requestFocus()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            binding.etEmail.error = "Ingrese un correo valido"
            binding.etEmail.requestFocus()
        }else if (direccion.isEmpty()) {
            binding.ubicacion.error = "Ingrese direccion"
            binding.ubicacion.requestFocus()
        } else if (password.isEmpty()) {
            binding.etPassword.error = "Ingrese contraseña"
            binding.etPassword.requestFocus()
        }else if (password.length < 6) {
            binding.etPassword.error = "La contraseña debe tener al menos 6 caracteres"
            binding.etPassword.requestFocus()
        }else if (confirmarPassword.isEmpty()) {
            binding.etConfirmarPassword.error = "Ingrese confirmar contraseña"
            binding.etConfirmarPassword.requestFocus()
        }else if (password != confirmarPassword) {
            binding.etConfirmarPassword.error = "Las contraseñas no coinciden"
            binding.etConfirmarPassword.requestFocus()
        }else{
            registrarAdministrador()
        }
    }

    private fun registrarAdministrador()  {
        progressDialog.setMessage("Registrando administrador")
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(correo, password)
            .addOnSuccessListener {
                insertarInfoBD()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "No se pudo registrar el administrador ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun insertarInfoBD() {
        progressDialog.setMessage("Guardando informacion")
        val uidBD = firebaseAuth.uid
        val nombresBD = nombre
        val direccionBD = direccion
        val correoBD = correo
        val tiempoBD = System.currentTimeMillis()

        val ref = FirebaseDatabase.getInstance().getReference("Administradores")
        val datosAdministrador = HashMap<String, Any>()
        datosAdministrador["uid"] = "$uidBD"
        datosAdministrador["nombres"] = "$nombresBD"
        datosAdministrador["correo"] = "$correoBD"
        datosAdministrador["direccion"] = "$direccionBD"
        datosAdministrador["tipoUsuario"] = "Administrador"
        datosAdministrador["tiempo_registro"] = "$tiempoBD"
        datosAdministrador["imagen"] = ""

        ref.child(uidBD!!)
            .setValue(datosAdministrador)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivityAdministrador::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "No se pudo registrar el administrador en BD debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
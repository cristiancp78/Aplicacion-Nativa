package com.example.tecnotech.Vendedor

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.Constantes
import com.example.tecnotech.Mapas.SeleccionarUbicacionActivity
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityRegistroVendedorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class RegistroVendedorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroVendedorBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cuenta.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnRegistrarV.setOnClickListener {
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
    private var tienda = ""
    private var cedula = ""
    private var password = ""
    private var confirmarPassword = ""

    private fun validarInformacion() {
        nombre = binding.etNombresV.text.toString().trim()
        correo = binding.etEmail.text.toString().trim()
        tienda = binding.etNombreTienda.text.toString().trim()
        cedula = binding.etCedula.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        confirmarPassword = binding.etConfirmarPassword.text.toString().trim()

        if (nombre.isEmpty()) {
            binding.etNombresV.error = "Ingrese nombre"
            binding.etNombresV.requestFocus()
        } else if (correo.isEmpty()) {
            binding.etEmail.error = "Ingrese correo"
            binding.etEmail.requestFocus()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.etEmail.error = "Ingrese un correo valido"
            binding.etEmail.requestFocus()
        }else if (cedula.isEmpty()) {
            binding.etCedula.error = "Ingrese cedula"
            binding.etCedula.requestFocus()
        } else if (tienda.isEmpty()) {
            binding.etNombreTienda.error = "Ingrese nombre de la tienda"
            binding.etNombreTienda.requestFocus()
        } else if (direccion.isEmpty()) {
            binding.ubicacion.error = "Ingrese direccion"
            binding.ubicacion.requestFocus()
        } else if (password.isEmpty()) {
            binding.etPassword.error = "Ingrese contraseña"
            binding.etPassword.requestFocus()
        } else if (password.length < 6) {
            binding.etPassword.error = "La contraseña debe tener al menos 6 caracteres"
            binding.etPassword.requestFocus()
        } else if (confirmarPassword.isEmpty()) {
            binding.etConfirmarPassword.error = "Ingrese confirmar contraseña"
            binding.etConfirmarPassword.requestFocus()
        } else if (password != confirmarPassword) {
            binding.etConfirmarPassword.error = "Las contraseñas no coinciden"
            binding.etConfirmarPassword.requestFocus()
        } else {
            registrarVendedor()
        }
        
    }

    private fun registrarVendedor() {
        progressDialog.setMessage("Registrando vendedor")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(correo, password)
            .addOnSuccessListener {
                insertarInfoBD()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "No se pudo registrar el vendedor ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun insertarInfoBD() {
        progressDialog.setMessage("Guardando informacion")

        val uidBD = firebaseAuth.uid
        val nombresBD = nombre
        val correoBD = correo
        val cedulaBD = cedula
        val direccionBD = direccion
        val tiendaBD = tienda
        val tiempoBD = Constantes().obtenerTiempoD()

        val datosVendedor = HashMap<String, Any>()

        datosVendedor["uid"] = "$uidBD"
        datosVendedor["nombres"] = "$nombresBD"
        datosVendedor["correo"] = "$correoBD"
        datosVendedor["cedula"] = "$cedulaBD"
        datosVendedor["tienda"] = "$tiendaBD"
        datosVendedor["direccion"] = "$direccionBD"
        datosVendedor["tipoUsuario"] = "Vendedor"
        datosVendedor["tiempo_registro"] = "$tiempoBD"

        val reference = FirebaseDatabase.getInstance().getReference("Vendedores")
        reference.child(uidBD!!)
            .setValue(datosVendedor)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityVendedor::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "No se pudo registrar el vendedor en BD debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
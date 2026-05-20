package com.example.tecnotech.Cliente

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
import com.example.tecnotech.databinding.ActivityRegistroClienteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistroClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroClienteBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var progressDialog : ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnRegistrarC.setOnClickListener {
            validarInformacion()
        }


        binding.cuenta.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
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
    private var cedula = ""
    private var password = ""
    private var confirmarPassword = ""
    private fun validarInformacion() {
        nombre = binding.etNombresC.text.toString().trim()
        correo = binding.etEmail.text.toString().trim()
        cedula = binding.etCedula.text.toString().trim()
        direccion = binding.ubicacion.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        confirmarPassword = binding.etConfirmarPassword.text.toString().trim()

        if (nombre.isEmpty()) {
            binding.etNombresC.error = "Ingrese nombre"
            binding.etNombresC.requestFocus()
        } else if (correo.isEmpty()) {
            binding.etEmail.error = "Ingrese correo"
            binding.etEmail.requestFocus()
        } else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            binding.etEmail.error = "Ingrese un correo valido"
            binding.etEmail.requestFocus()
        }else if (cedula.isEmpty()) {
            binding.etCedula.error = "Ingrese cedula"
            binding.etCedula.requestFocus()
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
            binding.etConfirmarPassword.error = "Confirme contraseña"
            binding.etConfirmarPassword.requestFocus()
        } else if (password != confirmarPassword) {
            binding.etConfirmarPassword.error = "Las contraseñas no coinciden"
            binding.etConfirmarPassword.requestFocus()
        } else {
            registrarCliente()
        }

    }

    private fun registrarCliente(){
        progressDialog.setMessage("Creando cuenta de cliente")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(correo, password)
            .addOnSuccessListener {
                insertarInfoBD()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "No se pudo crear la cuenta debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun insertarInfoBD() {
        progressDialog.setMessage("Guardando informacion")
        val uidBD = firebaseAuth.uid
        val nombresC = nombre
        val correoC = correo
        val cedulaC = cedula
        val direccionC = direccion
        val tiempoRegistr = Constantes().obtenerTiempoD()

        val reference = FirebaseDatabase.getInstance().getReference("Clientes")
        val datosCliente = HashMap<String, Any>()

        datosCliente["uid"] = "$uidBD"
        datosCliente["nombres"] = "$nombresC"
        datosCliente["correo"] = "$correoC"
        datosCliente["cedula"] = "$cedulaC"
        datosCliente["proveedor"] = "email"
        datosCliente["direccion"] = "$direccionC"
        datosCliente["tipoUsuario"] = "Cliente"
        datosCliente["tiempo_registro"] = "$tiempoRegistr"
        datosCliente["imagen"] = ""


        reference.child(uidBD!!)
            .setValue(datosCliente)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityCliente::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "No se pudo registrar el cliente en BD debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
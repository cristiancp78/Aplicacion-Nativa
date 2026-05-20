package com.example.tecnotech.Administrador

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnotech.R
import com.example.tecnotech.RecuperarPasswordActivity
import com.example.tecnotech.databinding.ActivityLoginAdministradorBinding
import com.google.firebase.auth.FirebaseAuth

class LoginAdministradorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginAdministradorBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var progressDialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdministradorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.btnLoginA.setOnClickListener {
            validarInformacion()

        }

        binding.tvRecuperarPassword.setOnClickListener {
            startActivity(Intent(applicationContext, RecuperarPasswordActivity::class.java))
        }

    }

    private var correo = ""
    private var password = ""
    private fun validarInformacion() {
        correo = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

        if (correo.isEmpty()) {
            binding.etEmail.error = "Ingrese correo"
            binding.etEmail.requestFocus()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            binding.etEmail.error = "Ingrese un correo valido"
            binding.etEmail.requestFocus()
        }else if (password.isEmpty()) {
            binding.etPassword.error = "Ingrese contraseña"
            binding.etPassword.requestFocus()
        }else{
            loginAdministrador()
        }
    }

    private fun loginAdministrador() {
        progressDialog.setMessage("Iniciando sesion")
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(correo, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityAdministrador::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "No se pudo iniciar sesion debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
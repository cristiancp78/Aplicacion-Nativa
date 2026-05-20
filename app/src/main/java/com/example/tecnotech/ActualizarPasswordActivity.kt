package com.example.tecnotech

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tecnotech.Cliente.LoginClienteActivity
import com.example.tecnotech.databinding.ActivityActualizarPasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ActualizarPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActualizarPasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualizarPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.app_name)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)
        firebaseUser = firebaseAuth.currentUser!!

        binding.btnActualizarPassword.setOnClickListener {
            validarInformacion()
        }


    }

    private var pass_actual = ""
    private var pass_nueva = ""
    private var pass_nuevar = ""

    private fun validarInformacion() {
        pass_actual = binding.etPasswordActual.text.toString().trim()
        pass_nueva = binding.etPasswordNueva.text.toString().trim()
        pass_nuevar = binding.etPasswordNuevaR.text.toString().trim()

        if (pass_actual.isEmpty()) {
            binding.etPasswordActual.error = "Ingrese su contraseña actual"
            binding.etPasswordActual.requestFocus()
        } else if (pass_nueva.isEmpty()) {
            binding.etPasswordNueva.error = "Ingrese su nueva contraseña"
            binding.etPasswordNueva.requestFocus()
        } else if (pass_nueva.length < 6) {
            binding.etPasswordNueva.error = "La contraseña debe tener al menos 6 caracteres"
            binding.etPasswordNueva.requestFocus()
        } else if (pass_nuevar.isEmpty()) {
            binding.etPasswordNuevaR.error = "Ingrese confirmar contraseña"
            binding.etPasswordNuevaR.requestFocus()
        } else if (pass_nueva != pass_nuevar) {
            binding.etPasswordNuevaR.error = "Las contraseñas no coinciden"
            binding.etPasswordNuevaR.requestFocus()
        } else {
            autenticarUsuario()
        }
    }

    private fun autenticarUsuario() {
        progressDialog.setMessage("Verificando contraseña actual")
        progressDialog.show()

        val autoCredencial = EmailAuthProvider.getCredential(firebaseUser.email.toString(), pass_actual)
        firebaseUser.reauthenticate(autoCredencial)
            .addOnSuccessListener {
                progressDialog.dismiss()
                actualizarPassword()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "No se pudo actualizar la contraseña debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun actualizarPassword() {
        progressDialog.setMessage("Actualizando contraseña")
        progressDialog.show()

        firebaseUser.updatePassword(pass_nueva)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                firebaseAuth.signOut()
                startActivity(Intent(this, SeleccionarTipoActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "No se pudo actualizar la contraseña debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

}
package com.example.tecnotech.Cliente

import android.app.ProgressDialog
import android.content.Intent
import androidx.credentials.exceptions.GetCredentialException
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.lifecycleScope
import com.example.tecnotech.Constantes
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ActivityLoginClienteBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class LoginClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginClienteBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        credentialManager = CredentialManager.create(this)


        binding.btnLoginC.setOnClickListener {
            validarInfo()
        }

        binding.btnLoginGoogle.setOnClickListener {
            iniciarSesionGoogle()
        }

        binding.tvRegistrarC.setOnClickListener {
            startActivity(Intent(applicationContext, RegistroClienteActivity::class.java))
        }
    }



    private var correo = ""
    private var password = ""
    private fun validarInfo() {
        correo = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

        if (correo.isEmpty()){
            binding.etEmail.error = "Ingrese su correo electronico"
            binding.etEmail.requestFocus()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            binding.etEmail.error = "Ingrese un correo electronico valido"
            binding.etEmail.requestFocus()
        }else if (password.isEmpty()){
            binding.etPassword.error = "Ingrese su contraseña"
            binding.etPassword.requestFocus()
        }else{
            loginCliente()
        }
    }

    private fun loginCliente() {
        progressDialog.setMessage("Iniciando sesion")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(correo, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(applicationContext, MainActivityCliente::class.java))
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                finishAffinity()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this, "No se pudo iniciar sesion debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun iniciarSesionGoogle() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.default_web_client_id))
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(this@LoginClienteActivity, request)
                handleSignIn(result)

            }catch (e: GetCredentialException){
                Toast.makeText(this@LoginClienteActivity, "${e}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential

        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken

                autenticarConFirebase(idToken)

            } catch (e: Exception) {
                Toast.makeText(this, "Error al procesar token", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun autenticarConFirebase(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        progressDialog.setMessage("Autenticando con Google")
        progressDialog.show()

        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                val firebaseUser = firebaseAuth.currentUser
                if(it.additionalUserInfo!!.isNewUser == true){
                    llenarInfoBD(firebaseUser)
                }
                else{
                    progressDialog.dismiss()
                    startActivity(Intent(this, MainActivityCliente::class.java))
                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                    finishAffinity()
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Fallo en Firebase: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun llenarInfoBD(firebaseUser: FirebaseUser?) {
        val uid = firebaseUser?.uid ?:return
        val email = firebaseUser?.email ?:return
        val nombre = firebaseUser?.displayName ?:return
        val tiempoRegistro = Constantes().obtenerTiempoD()

        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        val datos = HashMap<String, Any>()
        datos["uid"] = "$uid"
        datos["nombres"] = "$nombre"
        datos["correo"] = "$email"
        datos["tiempo_registro"] = "$tiempoRegistro"
        datos["tipoUsuario"] = "Cliente"
        datos["imagen"] = ""

        ref.child(uid)
            .setValue(datos)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivityCliente::class.java))
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                finishAffinity()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Fallo en Firebase: ${e.message}", Toast.LENGTH_SHORT).show()
            }


    }

}
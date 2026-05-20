package com.example.tecnotech.Cliente.Nav_Fragments_Cliente

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.tecnotech.ActualizarPasswordActivity
import com.example.tecnotech.Constantes
import com.example.tecnotech.Mapas.SeleccionarUbicacionActivity
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentMiPerfilCBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


class FragmentMiPerfilC : Fragment() {

    private lateinit var binding: FragmentMiPerfilCBinding
    private lateinit var mContext: Context
    private lateinit var firebaseAuth: FirebaseAuth
    private var imageUri : Uri? = null


    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMiPerfilCBinding.inflate(LayoutInflater.from(mContext), container, false)

        binding.imgCPerfil.setOnClickListener {
            seleccionarImg()
        }

        binding.btnGuardarInfoC.setOnClickListener {
            actualizarInfo()
        }

        binding.ubicacion.setOnClickListener {
            val intent = Intent(mContext, SeleccionarUbicacionActivity::class.java)
            obtenerUbicacion_ARL.launch(intent)
        }


        binding.btnActualizarContraseA.setOnClickListener {
            val intent = Intent(mContext, ActualizarPasswordActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }

    private var nombres = ""
    private var correo = ""
    private var cedula = ""
    private fun actualizarInfo() {
        nombres = binding.nombresCPerfil.text.toString().trim()
        correo = binding.correoCPerfil.text.toString().trim()
        cedula = binding.cedulaCPerfil.text.toString().trim()
        direccion = binding.ubicacion.text.toString().trim()

        val hashMap : HashMap<String, Any> = HashMap()
        hashMap["nombres"] = "${nombres}"
        hashMap["correo"] = "${correo}"
        hashMap["cedula"] = "${cedula}"
        hashMap["direccion"] = "${direccion}"
        hashMap["latitud"] = "${latitud}"
        hashMap["longitud"] = "${longitud}"



        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                Toast.makeText(mContext, "Informacion actualizada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(mContext, "No se pudo actualizar la informacion debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        leerInformacion()

    }

    private fun leerInformacion() {
        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres = "${snapshot.child("nombres").value}"
                    val correo = "${snapshot.child("correo").value}"
                    val direccion = "${snapshot.child("direccion").value}"
                    val cedula = "${snapshot.child("cedula").value}"
                    val imagen = "${snapshot.child("imagen").value}"
                    val proveedor = "${snapshot.child("proveedor").value}"
                    val fechaRegistro = "${snapshot.child("tiempo_registro").value}"

                    val fecha = Constantes().obtenerFecha(fechaRegistro.toLong())

                    binding.nombresCPerfil.setText(nombres)
                    binding.correoCPerfil.setText(correo)
                    binding.cedulaCPerfil.setText(cedula)
                    binding.fechaRegistroCPerfil.text = "Se unio el $fecha"
                    binding.ubicacion.setText(direccion)


                    try {
                        Glide.with(mContext)
                            .load(imagen)
                            .placeholder(R.drawable.img_perfil)
                            .into(binding.imgCPerfil)
                    }catch (e: Exception){

                    }

                    if(proveedor == "email"){
                        binding.proveedorCPerfil.text = "La cuenta fue creada a traves de su correo"
                        binding.correoCPerfil.isEnabled = false
                        binding.btnActualizarContraseA.visibility = View.VISIBLE
                    }else{
                        binding.proveedorCPerfil.text = "La cuenta fue creada a traves de su cuenta de Google"
                        binding.correoCPerfil.isEnabled = false
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun seleccionarImg(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent ->
                resultadoImg.launch(intent)
            }
    }

    private val resultadoImg = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
        if (resultado.resultCode == Activity.RESULT_OK) {
            val data = resultado.data
            imageUri = data!!.data
            subirImagenStorage(imageUri)
        } else {
            Toast.makeText(this.context, "Accion Cancelada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun subirImagenStorage(imageUri: Uri?) {
        val rutaImagen = "imagenesPerfil/"+firebaseAuth.uid
        val ref = FirebaseStorage.getInstance().getReference(rutaImagen)
        ref.putFile(imageUri!!)
            .addOnSuccessListener {taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val urlImagenCargada = uriTask.result.toString()
                if(uriTask.isSuccessful){
                    actualizarImagenBD(urlImagenCargada)
                }

            }
            .addOnFailureListener {e ->
                Toast.makeText(mContext, "No se pudo subir la imagen debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }



    }

    private fun actualizarImagenBD(urlImagenCargada: String) {
        val hashMap : HashMap<String, Any> = HashMap()
        if(imageUri != null){
            hashMap["imagen"] = urlImagenCargada
        }
        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                Toast.makeText(mContext, "Imagen actualizada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(mContext, "No se pudo actualizar la imagen debido a ${e.message}", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this.context, "Accion Cancelada", Toast.LENGTH_SHORT).show()
        }
    }


}
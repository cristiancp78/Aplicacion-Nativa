package com.example.tecnotech.Vendedor.Nav_Fragments_Vendedor

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tecnotech.Adaptadores.AdaptadorCategoriaV
import com.example.tecnotech.Modelos.ModeloCategoriaV
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentCategoriasVBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class FragmentCategoriasV : Fragment() {

    private lateinit var binding: FragmentCategoriasVBinding
    private lateinit var mContext: Context
    private lateinit var progressDialog: ProgressDialog

     override fun onAttach(context: Context) {
         mContext = context
         super.onAttach(context)
     }

    private var imageUri : Uri? = null

    private lateinit var categoriaArrayList: ArrayList<ModeloCategoriaV>
    private lateinit var adaptadorCategoriaV: AdaptadorCategoriaV


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCategoriasVBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(mContext)
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.imgCategorias.setOnClickListener {
            seleccionarImg()
        }

        binding.btnAgregarCategoria.setOnClickListener {
            validarInformacion()
        }

        listarCategorias()

        return binding.root
    }

    private fun listarCategorias() {
        categoriaArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categorias").orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriaArrayList.clear()
                for (ds in snapshot.children){
                    val modelo = ds.getValue(ModeloCategoriaV::class.java)
                    categoriaArrayList.add(modelo!!)
                }
                adaptadorCategoriaV = AdaptadorCategoriaV(mContext, categoriaArrayList)
                binding.rvCategorias.adapter = adaptadorCategoriaV
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private var categoria = ""
    private fun validarInformacion() {
        categoria = binding.etCategoria.text.toString().trim()
        if (categoria.isEmpty()) {
            binding.etCategoria.error = "Ingrese categoria"
            binding.etCategoria.requestFocus()
        }else if (imageUri == null){
            Toast.makeText(mContext, "Seleccione una imagen", Toast.LENGTH_SHORT).show()
        }
        else {
            agregarCategoriaBD()
        }
    }

    private fun agregarCategoriaBD() {
        progressDialog.setMessage("Agregando categoria")
        progressDialog.show()

        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
        val keyId = ref.push().key

        val hashMapCategoria = HashMap<String, Any>()
        hashMapCategoria["id"] = "$keyId"
        hashMapCategoria["categoria"] = "$categoria"

        ref.child(keyId!!)
            .setValue(hashMapCategoria)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(mContext, "Categoria agregada", Toast.LENGTH_SHORT).show()
                binding.etCategoria.setText("")
                subirImgStorage(keyId)
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(mContext, "No se pudo agregar la categoria debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun subirImgStorage(keyId: String) {
        progressDialog.setMessage("Subiendo imagen")
        progressDialog.show()

        val nombreImagen = keyId
        val nombreCarpeta = "Categorias/$nombreImagen"
        val storageReference = FirebaseStorage.getInstance().getReference(nombreCarpeta)
        storageReference.putFile(imageUri!!)
            .addOnSuccessListener {taskSnapshot ->
                progressDialog.dismiss()
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val urlImagen = "${uriTask.result}"

                if (uriTask.isSuccessful){
                    val hashMap = HashMap<String, Any>()
                    hashMap["imagen"] = "$urlImagen"
                    val ref = FirebaseDatabase.getInstance().getReference("Categorias")
                    ref.child(nombreImagen).updateChildren(hashMap)
                    Toast.makeText(mContext, "Se agrego la categoria con exito", Toast.LENGTH_SHORT).show()
                    binding.etCategoria.setText("")
                    imageUri = null
                    binding.imgCategorias.setImageURI(imageUri)
                    binding.imgCategorias.setImageResource(R.drawable.agregarproducto)
                }
            }
            .addOnFailureListener {e ->
                progressDialog.dismiss()
                Toast.makeText(mContext, "No se pudo subir la imagen debido a ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

    private fun seleccionarImg() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                resultadoImg.launch(intent)
            }
    }

    private val resultadoImg = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            resultado ->
        if (resultado.resultCode == Activity.RESULT_OK){
            val data = resultado.data
            imageUri = data!!.data
            binding.imgCategorias.setImageURI(imageUri)
        }else{
            Toast.makeText(this.context, "Accion Cancelada", Toast.LENGTH_SHORT).show()

        }
    }
}
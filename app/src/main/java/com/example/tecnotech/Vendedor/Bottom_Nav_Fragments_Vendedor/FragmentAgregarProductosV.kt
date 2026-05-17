package com.example.tecnotech.Vendedor.Bottom_Nav_Fragments_Vendedor

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tecnotech.R
import com.example.tecnotech.databinding.FragmentAgregarProductosVBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import androidx.core.view.WindowCompat
import com.example.tecnotech.Adaptadores.AdaptadorImagenSeleccionada
import com.example.tecnotech.Modelos.ModeloCategoriaV
import com.example.tecnotech.Modelos.ModeloImagenSeleccionada
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


class FragmentAgregarProductosV : Fragment() {

    private lateinit var binding: FragmentAgregarProductosVBinding
    private var imagenUri : Uri? = null

    private lateinit var categoriaArrayList : ArrayList<ModeloCategoriaV>

    private lateinit var imagenSelecArrayList: ArrayList<ModeloImagenSeleccionada>
    private lateinit var adaptadorImagenSeleccionada: AdaptadorImagenSeleccionada
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAgregarProductosVBinding.inflate(inflater, container, false)

        cargarCategorias()

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.etPrecioConDescuentoP.visibility = View.GONE
        binding.etNotaDescuentoP.visibility = View.GONE

        binding.descuentoSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.etPrecioConDescuentoP.visibility = View.VISIBLE
                binding.etNotaDescuentoP.visibility = View.VISIBLE
            } else{
                binding.etPrecioConDescuentoP.visibility = View.GONE
                binding.etNotaDescuentoP.visibility = View.GONE
            }
        }

        imagenSelecArrayList = ArrayList()

        binding.imgAgregarProducto.setOnClickListener {
            seleccionarImg()
        }
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)

        binding.Categoria.setOnClickListener {
            selecCategorias()
        }

        binding.btnAgregarProducto.setOnClickListener {
            validarInfo()
        }

        cargarImagenes()
        return binding.root
    }

    private var nombreP = ""
    private var descripcionP = ""
    private var categoriaP = ""
    private var precioP = ""
    private var descuentoHab = false
    private var precioConDescuentoP = ""
    private var notaDescuentoP = ""
    private fun validarInfo() {

        nombreP = binding.etNombreP.text.toString().trim()
        descripcionP = binding.etDescripcionP.text.toString().trim()
        categoriaP = binding.Categoria.text.toString().trim()
        precioP = binding.etPrecioP.text.toString().trim()
        descuentoHab = binding.descuentoSwitch.isChecked

        if (nombreP.isEmpty()){
            binding.etNombreP.error = "Ingrese nombre del producto"
            binding.etNombreP.requestFocus()
        }
        else if (descripcionP.isEmpty()){
            binding.etDescripcionP.error = "Ingrese descripcion del producto"
            binding.etDescripcionP.requestFocus()
        }
        else if (categoriaP.isEmpty()){
            binding.Categoria.error = "Seleccione una categoria"
            binding.Categoria.requestFocus()
        }
        else if (precioP.isEmpty()){
            binding.etPrecioP.error = "Ingrese precio del producto"
            binding.etPrecioP.requestFocus()
        }
        else if(imagenUri == null){
            Toast.makeText(requireContext(), "Seleccione almenos una imagen", Toast.LENGTH_SHORT).show()
        }else{
            if (descuentoHab){
                precioConDescuentoP = binding.etPrecioConDescuentoP.text.toString().trim()
                notaDescuentoP = binding.etNotaDescuentoP.text.toString().trim()
                if (precioConDescuentoP.isEmpty()){
                    binding.etPrecioConDescuentoP.error = "Ingrese precio con descuento"
                    binding.etPrecioConDescuentoP.requestFocus()
                }else if (notaDescuentoP.isEmpty()){
                    binding.etNotaDescuentoP.error = "Ingrese nota del descuento"
                    binding.etNotaDescuentoP.requestFocus()
                }else{
                    agregarProducto()
                }
            }else{
                precioConDescuentoP = "0"
                notaDescuentoP = ""
                agregarProducto()
            }
        }

    }

    private fun agregarProducto() {
        progressDialog.setMessage("Agregando producto")
        progressDialog.show()

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        val keyId = ref.push().key

        val hashMapProducto = HashMap<String, Any>()
        hashMapProducto["id"] = "$keyId"
        hashMapProducto["nombre"] = "$nombreP"
        hashMapProducto["descripcion"] = "$descripcionP"
        hashMapProducto["categoria"] = "$categoriaP"
        hashMapProducto["precio"] = "$precioP"
        hashMapProducto["precioDesc"] = "$precioConDescuentoP"
        hashMapProducto["notaDesc"] = "$notaDescuentoP"

        ref.child(keyId!!)
            .setValue(hashMapProducto)
            .addOnSuccessListener {
                subirImgsStorage(keyId)
            }
            .addOnFailureListener {e ->
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "No se pudo agregar el producto debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun subirImgsStorage(keyId: String) {
        for (i in imagenSelecArrayList.indices){
            val modeloImagenSel = imagenSelecArrayList[i]
            val nombreImagen = modeloImagenSel.id
            val rutaImagen = "Productos/$nombreImagen"

            val storageRef = FirebaseStorage.getInstance().getReference(rutaImagen)
            storageRef.putFile(modeloImagenSel.imageUri!!)
                .addOnSuccessListener {taskSnapshot ->
                    val uriTask = taskSnapshot.storage.downloadUrl
                    while (!uriTask.isSuccessful);
                    val urlImgCargada = "${uriTask.result}"

                    if (uriTask.isSuccessful) {
                        val hashMap = HashMap<String, Any>()
                        hashMap["id"] = "${modeloImagenSel.id}"
                        hashMap["imagenUrl"] = "$urlImgCargada"

                        val ref = FirebaseDatabase.getInstance().getReference("Productos")
                        ref.child(keyId).child("Imagenes")
                            .child(nombreImagen)
                            .updateChildren(hashMap)
                        progressDialog.dismiss()
                        Toast.makeText(requireContext(), "Se agrego el producto", Toast.LENGTH_SHORT).show()
                        limpiarCampos()
                    }

                }
                .addOnFailureListener {e ->
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "No se pudo subir la imagen debido a ${e.message}", Toast.LENGTH_SHORT).show()
                }

        }
    }

    private fun limpiarCampos() {
        imagenSelecArrayList.clear()
        adaptadorImagenSeleccionada.notifyDataSetChanged()
        binding.etNombreP.setText("")
        binding.etDescripcionP.setText("")
        binding.Categoria.setText("")
        binding.etPrecioP.setText("")
        binding.descuentoSwitch.isChecked = false
        binding.etPrecioConDescuentoP.setText("")
        binding.etNotaDescuentoP.setText("")
    }

    private fun cargarCategorias() {
        categoriaArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categorias").orderByChild("categoria")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriaArrayList.clear()
                for (ds in snapshot.children){
                    val modelo = ds.getValue(ModeloCategoriaV::class.java)
                    categoriaArrayList.add(modelo!!)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private var idcat = ""
    private var tituloCat = ""
    private fun selecCategorias(){
        val categoriasArray = arrayOfNulls<String>(categoriaArrayList.size)
        for (i in categoriaArrayList.indices){
            categoriasArray[i] = categoriaArrayList[i].categoria
        }
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Seleccione una Categoria")
            .setItems(categoriasArray){dialog, wich ->
                idcat = categoriaArrayList[wich].id
                tituloCat = categoriaArrayList[wich].categoria
                binding.Categoria.text = tituloCat
            }
            .show()

    }

    private fun cargarImagenes() {
        adaptadorImagenSeleccionada = AdaptadorImagenSeleccionada(requireContext(), imagenSelecArrayList)
        binding.RVImagenesProducto.adapter = adaptadorImagenSeleccionada
    }

    private fun seleccionarImg(){
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                resultadoImg.launch(intent)
            }

    }

    private val resultadoImg =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){resultado ->
        if (resultado.resultCode == Activity.RESULT_OK){
            val data = resultado.data
            imagenUri = data!!.data
            val tiempo = System.currentTimeMillis()
            val tiempoString = "$tiempo"

            val modeloImgSel = ModeloImagenSeleccionada(tiempoString,imagenUri,null,false)
            imagenSelecArrayList.add(modeloImgSel)
            cargarImagenes()
        }else{
            Toast.makeText(this.context, "Accion Cancelada", Toast.LENGTH_SHORT).show()
        }

    }


}
package com.example.tecnotech.Vendedor.Bottom_Nav_Fragments_Vendedor

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
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
import com.example.tecnotech.Vendedor.MainActivityVendedor
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

    private var Edicion = false
    private var idProducto = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAgregarProductosVBinding.inflate(inflater, container, false)

        cargarCategorias()

        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Por favor espere")
        progressDialog.setCanceledOnTouchOutside(false)

        Edicion = arguments?.getBoolean("Edicion", false)?: false

        binding.etPorcentajeDescuentoP.visibility = View.GONE
        binding.btnCalcularPrecioDesc.visibility = View.GONE
        binding.precioConDescuentoPTXT.visibility = View.GONE
        binding.etPrecioConDescuentoP.visibility = View.GONE
        binding.etNotaDescuentoP.visibility = View.GONE

        binding.descuentoSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.etPorcentajeDescuentoP.visibility = View.VISIBLE
                binding.btnCalcularPrecioDesc.visibility = View.VISIBLE
                binding.precioConDescuentoPTXT.visibility = View.VISIBLE
                binding.etPrecioConDescuentoP.visibility = View.VISIBLE
                binding.etNotaDescuentoP.visibility = View.VISIBLE
            } else{
                binding.etPorcentajeDescuentoP.visibility = View.GONE
                binding.btnCalcularPrecioDesc.visibility = View.GONE
                binding.precioConDescuentoPTXT.visibility = View.GONE
                binding.etPrecioConDescuentoP.visibility = View.GONE
                binding.etNotaDescuentoP.visibility = View.GONE
            }
        }

        if (Edicion){
            idProducto = arguments?.getString("idProducto")?: ""
            binding.txtAgregarProductos.text = "Editar Producto"
            binding.btnAgregarProducto.text = "Actualizar Producto"
            cargarInfo()
        }else{
            binding.txtAgregarProductos.text = "Agregar Producto"
            binding.btnAgregarProducto.text = "Agregar Producto"
        }

        imagenSelecArrayList = ArrayList()

        binding.imgAgregarProducto.setOnClickListener {
            seleccionarImg()
        }
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)

        binding.Categoria.setOnClickListener {
            selecCategorias()
        }

        binding.btnCalcularPrecioDesc.setOnClickListener {
            calcularPrecioDesc()
        }


        binding.btnAgregarProducto.setOnClickListener {
            validarInfo()
        }

        cargarImagenes()
        return binding.root
    }

    private fun cargarInfo() {
        var ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nombre = "${snapshot.child("nombre").value}"
                val descripcion = "${snapshot.child("descripcion").value}"
                val categoria = "${snapshot.child("categoria").value}"
                val precio = "${snapshot.child("precio").value}"
                val precioDesc = "${snapshot.child("precioDesc").value}"
                val notaDesc = "${snapshot.child("notaDesc").value}"

                binding.etNombreP.setText(nombre)
                binding.etDescripcionP.setText(descripcion)
                binding.Categoria.setText(categoria)
                binding.etPrecioP.setText(precio)
                binding.etPrecioConDescuentoP.setText(precioDesc)
                binding.etNotaDescuentoP.setText(notaDesc)

                val refImagenes = snapshot.child("Imagenes").ref
                refImagenes.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (ds in snapshot.children) {
                            val id = "${ds.child("id").value}"
                            val imagenUrl = "${ds.child("imagenUrl").value}"

                            val modeloImgSel = ModeloImagenSeleccionada(id ,null,imagenUrl,true)
                            imagenSelecArrayList.add(modeloImgSel)

                        }
                        cargarImagenes()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun calcularPrecioDesc() {
        val precioOriginal = binding.etPrecioP.text.toString().toString()
        val notaDescuento = binding.etNotaDescuentoP.text.toString().toString()
        val porcentaje = binding.etPorcentajeDescuentoP.text.toString().toString()

        if (precioOriginal.isEmpty()){
            binding.etPrecioP.error = "Ingrese precio"
            binding.etPrecioP.requestFocus()
        }else if (notaDescuento.isEmpty()){
            binding.etNotaDescuentoP.error = "Ingrese nota del descuento"
            binding.etNotaDescuentoP.requestFocus()
        }else if (porcentaje.isEmpty()){
            binding.etPorcentajeDescuentoP.error = "Ingrese porcentaje"
            binding.etPorcentajeDescuentoP.requestFocus()
        }else{
            val precioOriginalDouble = precioOriginal.toDouble()
            val porcentajeDouble = porcentaje.toDouble()
            val descuento = precioOriginalDouble * (porcentajeDouble / 100)
            val precioFinal = precioOriginalDouble - descuento
            binding.etPrecioConDescuentoP.text = precioFinal.toInt().toString()
        }

    }

    private var nombreP = ""
    private var descripcionP = ""
    private var categoriaP = ""
    private var precioP = ""
    private var descuentoHab = false
    private var precioConDescuentoP = ""
    private var notaDescuentoP = ""
    private var porcentajeDescP = ""
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
        }else{
            if (descuentoHab){
                notaDescuentoP = binding.etNotaDescuentoP.text.toString().trim()
                porcentajeDescP = binding.etPorcentajeDescuentoP.text.toString().trim()
                precioConDescuentoP = binding.etPrecioConDescuentoP.text.toString().trim()

                if (notaDescuentoP.isEmpty()){
                    binding.etNotaDescuentoP.error = "Ingrese nota del descuento"
                    binding.etNotaDescuentoP.requestFocus()
                }else if (porcentajeDescP.isEmpty()){
                    binding.etPorcentajeDescuentoP.error = "Ingrese un porcentaje"
                    binding.etPorcentajeDescuentoP.requestFocus()
                } else if (precioConDescuentoP.isEmpty()){
                    binding.etPrecioConDescuentoP.setText("No se establecio el precio con descuento")
                }else{
                    if(Edicion){
                        actualizarInfo()
                    }else{
                        if(imagenUri == null){
                            Toast.makeText(requireContext(), "Seleccione al menos una imagen", Toast.LENGTH_SHORT).show()
                        }else{
                            agregarProducto()
                        }
                    }
                }
            }else{
                precioConDescuentoP = "0"
                notaDescuentoP = ""
                if (Edicion) {
                    actualizarInfo()
                }else{
                    if(imagenUri == null){
                        Toast.makeText(requireContext(), "Seleccione al menos una imagen", Toast.LENGTH_SHORT).show()
                    }else{
                        agregarProducto()
                    }
                }
            }
        }

    }

    private fun actualizarInfo() {
        progressDialog.setMessage("Actualizando producto")
        progressDialog.show()


        val hashMapProducto = HashMap<String, Any>()
        hashMapProducto["nombre"] = "$nombreP"
        hashMapProducto["descripcion"] = "$descripcionP"
        hashMapProducto["categoria"] = "$categoriaP"
        hashMapProducto["precio"] = "$precioP"
        hashMapProducto["precioDesc"] = "$precioConDescuentoP"
        hashMapProducto["notaDesc"] = "$notaDescuentoP"

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto)
            .updateChildren(hashMapProducto)
            .addOnSuccessListener {
                subirImgsStorage(idProducto)
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "No se pudo actualizar el producto debido a ${e.message}", Toast.LENGTH_SHORT).show()
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
        val imagenesNuevas = imagenSelecArrayList.filter { !it.deInternet }

        if (imagenesNuevas.isEmpty()){
            finalizarProceso()
            return
        }

        var subidasCompletadas = 0

        for (modelo in imagenesNuevas) {
            val nombreImagen = modelo.id
            val rutaImagen = "Productos/$nombreImagen"
            val storageRef = FirebaseStorage.getInstance().getReference(rutaImagen)

            storageRef.putFile(modelo.imageUri!!)
                .continueWithTask { task ->
                    if (!task.isSuccessful) task.exception?.let { throw it }
                    storageRef.downloadUrl
                }
                .addOnSuccessListener { uri ->
                    val urlImgCargada = uri.toString()
                    val hashMap = HashMap<String, Any>()
                    hashMap["id"] = modelo.id
                    hashMap["imagenUrl"] = urlImgCargada

                    FirebaseDatabase.getInstance().getReference("Productos")
                        .child(keyId).child("Imagenes").child(nombreImagen)
                        .updateChildren(hashMap)
                        .addOnCompleteListener {
                            subidasCompletadas++
                            // Cuando la última imagen termine, finalizamos
                            if (subidasCompletadas == imagenesNuevas.size) {
                                finalizarProceso()
                            }
                        }
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(requireContext(), "Error al subir: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun finalizarProceso() {
        progressDialog.dismiss()
        if(Edicion){
            Toast.makeText(requireContext(), "Producto actualizado con éxito", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }else{
            limpiarCampos()
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
        binding.etNotaDescuentoP.setText("")
        binding.etPorcentajeDescuentoP.setText("")
        binding.etPrecioConDescuentoP.setText("")

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
        adaptadorImagenSeleccionada = AdaptadorImagenSeleccionada(requireContext(), imagenSelecArrayList,idProducto)
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
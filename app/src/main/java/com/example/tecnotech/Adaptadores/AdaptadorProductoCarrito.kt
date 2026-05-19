package com.example.tecnotech.Adaptadores

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tecnotech.Modelos.ModeloProductoCarrito
import com.example.tecnotech.R
import com.example.tecnotech.databinding.ItemCarritoCBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdaptadorProductoCarrito : RecyclerView.Adapter<AdaptadorProductoCarrito.HolderProductoCarrito> {

    private lateinit var binding: ItemCarritoCBinding
    private val mContext: Context
    private val productosArrayList: ArrayList<ModeloProductoCarrito>
    private var firebaseAuth: FirebaseAuth

    constructor(mContext: Context, productosArrayList: ArrayList<ModeloProductoCarrito>) : super() {
        this.mContext = mContext
        this.productosArrayList = productosArrayList
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProductoCarrito {
        binding = ItemCarritoCBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderProductoCarrito(binding.root)
    }

    var costo : Double = 0.0
    override fun onBindViewHolder(holder: HolderProductoCarrito, position: Int) {
        val modelo = productosArrayList[position]

        val nombre = modelo.nombre
        var cantidad = modelo.cantidad
        var precioFinal = modelo.precioFinal.replace("[^0-9.E+\\-]".toRegex(), "").toDoubleOrNull() ?:0.0
        var precio = modelo.precio.replace("[^0-9.E+\\-]".toRegex(), "").toDoubleOrNull() ?:0.0
        var precioDesc = modelo.precioDesc.replace("[^0-9.E+\\-]".toRegex(), "").toDoubleOrNull() ?:0.0


        holder.nombrePCar.text = nombre
        holder.canrtidadPCar.text = cantidad.toString()

        cargarPrimeraImg(modelo, holder)

        visualizarDescuento(modelo, holder)

        holder.btnEliminarPCar.setOnClickListener {
            eliminarProductoCarrito(mContext, modelo.idProducto)
        }

        var miPrecioFinalDouble = precioFinal

        holder.btnAumentarPCar.setOnClickListener {
            if(!modelo.precioDesc.equals("0")){
                costo = precioDesc
            }else{
                costo = precio
            }

            miPrecioFinalDouble += costo
            cantidad++

            holder.precioFinalPCar.text = String.format("%,.0f COP", miPrecioFinalDouble)
            holder.canrtidadPCar.text = cantidad.toString()

            var precioFinalString = String.format("%,.0f COP", miPrecioFinalDouble)

            calcularNuevoPrecio(mContext, modelo.idProducto, precioFinalString, cantidad)
        }

        holder.btnDisminuirPCar.setOnClickListener {
            if (cantidad > 1) {
                if(!modelo.precioDesc.equals("0")){
                    costo = precioDesc
                }else{
                    costo = precio
                }

                miPrecioFinalDouble -= costo
                cantidad--

                holder.precioFinalPCar.text = String.format("%,.0f COP", miPrecioFinalDouble)
                holder.canrtidadPCar.text = cantidad.toString()

                var precioFinalString = String.format("%,.0f COP", miPrecioFinalDouble)
                calcularNuevoPrecio(mContext, modelo.idProducto, precioFinalString, cantidad)
            }
        }
    }

    private fun calcularNuevoPrecio(mContext: Context, idProducto: String, precioFinalString: String, cantidad: Int) {
        val hashMap : HashMap<String, Any> = HashMap()

        hashMap["cantidad"] = cantidad
        hashMap["precioFinal"] = precioFinalString

        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.child(firebaseAuth.uid!!).child("CarritoCompras").child(idProducto)
            .updateChildren(hashMap)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                Toast.makeText(mContext, "No se pudo actualizar la cantidad debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }



    }

    private fun eliminarProductoCarrito(mContext: Context, idProducto: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val ref = FirebaseDatabase.getInstance().getReference("Clientes")
        ref.child(firebaseAuth.uid!!).child("CarritoCompras").child(idProducto)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(mContext, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(mContext, "No se pudo eliminar el producto debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun visualizarDescuento(modelo: ModeloProductoCarrito, holder: HolderProductoCarrito) {
        val precio = (modelo.precio ?: "0").replace("[^0-9.E+\\-]".toRegex(), "").toDoubleOrNull() ?: 0.0
        val precioFinal = (modelo.precioFinal ?: "0").replace("[^0-9.E+\\-]".toRegex(), "").toDoubleOrNull() ?: 0.0
        if(!modelo.precioDesc.equals("0")){
            holder.precioFinalPCar.text = String.format("%,.0f COP", precioFinal)
            holder.precioOriginalPCar.text = String.format("%,.0f COP", precio)
            holder.precioOriginalPCar.paintFlags = holder.precioOriginalPCar.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            holder.precioOriginalPCar.visibility = View.GONE
            holder.precioFinalPCar.text = String.format("%,.0f COP", precioFinal)
        }
    }

    private fun cargarPrimeraImg(modelo: ModeloProductoCarrito, holder: HolderProductoCarrito) {
        val idProducto = modelo.idProducto

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes")
            .limitToFirst(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children) {
                        val imagenUrl = "${ds.child("imagenUrl").value}"

                        try {
                            Glide.with(mContext)
                                .load(imagenUrl)
                                .placeholder(R.drawable.icoproduc)
                                .into(holder.imagenPCar)
                        }catch (e: Exception){

                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun getItemCount(): Int {
        return productosArrayList.size
    }


    inner class HolderProductoCarrito(itemView: View): RecyclerView.ViewHolder(itemView){
        var imagenPCar = binding.imagenPCar
        var nombrePCar = binding.nombrePCar
        var precioFinalPCar = binding.precioFinalPCar
        var precioOriginalPCar = binding.precioOriginalPCar
        var btnDisminuirPCar = binding.btnDisminuir
        var canrtidadPCar = binding.cantidadPCar
        var btnAumentarPCar = binding.brnAumentar
        var btnEliminarPCar = binding.btnEliminar
    }

}
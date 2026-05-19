package com.example.tecnotech.Modelos

class ModeloProductoCarrito{

    var idProducto: String =""
    var nombre: String =""
    var precio: String =""
    var precioDesc: String =""
    var precioFinal: String =""
    var cantidad: Int = 0

    constructor()
    constructor(
        idProducto: String,
        nombre: String,
        precioDesc: String,
        precio: String,
        precioFinal: String,
        cantidad: Int
    ) {
        this.idProducto = idProducto
        this.nombre = nombre
        this.precioDesc = precioDesc
        this.precio = precio
        this.precioFinal = precioFinal
        this.cantidad = cantidad
    }


}
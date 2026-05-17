package com.example.tecnotech.Modelos

class ModeloProductosV{
    var id: String = ""
    var nombre: String = ""
    var descripcion: String = ""
    var categoria: String = ""
    var precio: String = ""
    var precioDesc: String = ""
    var notaDesc: String = ""

    constructor()
    constructor(
        id: String,
        nombre: String,
        descripcion: String,
        precio: String,
        categoria: String,
        precioDesc: String,
        notaDesc: String
    ) {
        this.id = id
        this.nombre = nombre
        this.descripcion = descripcion
        this.precio = precio
        this.categoria = categoria
        this.precioDesc = precioDesc
        this.notaDesc = notaDesc
    }


}
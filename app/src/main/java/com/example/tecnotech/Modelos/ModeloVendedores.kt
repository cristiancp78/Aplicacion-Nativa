package com.example.tecnotech.Modelos

class ModeloVendedores {
    var id: String =""
    var nombres: String =""
    var correo: String =""
    var tienda: String =""

    constructor()
    constructor(id: String, nombre: String, correo: String, nombreTienda: String) {
        this.id = id
        this.nombres = nombre
        this.correo = correo
        this.tienda = nombreTienda
    }
}
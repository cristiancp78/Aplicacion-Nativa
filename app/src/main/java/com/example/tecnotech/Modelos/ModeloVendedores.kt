package com.example.tecnotech.Modelos

class ModeloVendedores {
    var uid: String =""
    var nombres: String =""
    var correo: String =""
    var tienda: String =""

    constructor()
    constructor(uid: String, nombre: String, correo: String, nombreTienda: String) {
        this.uid = uid
        this.nombres = nombre
        this.correo = correo
        this.tienda = nombreTienda
    }
}
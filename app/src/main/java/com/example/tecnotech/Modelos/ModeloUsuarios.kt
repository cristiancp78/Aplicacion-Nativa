package com.example.tecnotech.Modelos

class ModeloUsuarios {
    var uid: String =""
    var nombres: String =""
    var correo: String =""
    var direccion: String =""
    var tipoUsuario: String =""

    constructor()
    constructor(uid: String, nombre: String, correo: String, direccion: String, tipo: String) {
        this.uid = uid
        this.nombres = nombre
        this.correo = correo
        this.direccion = direccion
        this.tipoUsuario = tipo
    }


}
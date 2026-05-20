package com.example.tecnotech.Modelos

class ModeloAdministradores {
    var uid: String =""
    var nombres: String =""
    var correo: String =""
    var direccion: String =""
    var cedula: String =""
    var imagen: String =""
    var tipoUsuario: String =""

    constructor()
    constructor(
        uid: String,
        nombres: String,
        correo: String,
        direccion: String,
        imagen: String,
        cedula: String,
        tipoUsuario: String
    ) {
        this.uid = uid
        this.nombres = nombres
        this.correo = correo
        this.direccion = direccion
        this.imagen = imagen
        this.cedula = cedula
        this.tipoUsuario = tipoUsuario
    }


}
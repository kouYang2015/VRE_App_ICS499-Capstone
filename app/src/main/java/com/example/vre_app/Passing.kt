package com.example.vre_app
//Tried to save username and password (First Time login-in)
object Passing {
    var username : String =""
    var password: String =""

    @JvmName("setUsername1")
    fun setUsername(username: String){
        username.also { Passing.username=it }
    }

    @JvmName("setPassword1")
    fun setPassword(password:String){
        password.also { Passing.password=it }
    }
}
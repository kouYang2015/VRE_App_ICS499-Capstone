package com.example.vre_app


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class CreateAccount : AppCompatActivity() {
    private lateinit var btnCreateAccount: Button
    private lateinit var userName: EditText
    private lateinit var fullName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        btnCreateAccount = findViewById(R.id.buttonCreateAccount)
        email = findViewById(R.id.editTextEmailAddress)
        confirmPassword = findViewById(R.id.editConfirmPassword)
        password = findViewById(R.id.editPassword)
        userName = findViewById(R.id.editTextUserName)
        fullName = findViewById(R.id.editTextPersonName)



        btnCreateAccount.setOnClickListener {
            //Calls this to verify
            validateEmptyForm()
        }
    }

    private fun validateEmptyForm(){
        if(fullName.text.toString().isNotEmpty() && userName.text.toString().isNotEmpty() && email.text.toString().isNotEmpty() && password.text.toString().isNotEmpty() && confirmPassword.text.toString().isNotEmpty()){
            //Need a valid Email to work (ex. abc123@gmail.net)
            if(email.text.toString().matches(Regex("[a-zA-Z0-9._]+@[a-z].+[a-z]"))) {
                //Maximum length for the full name, username, password, and confirm password
                if(fullName.length()<=20 && userName.length()<=20 && password.length()<=10 && confirmPassword.length()<=10 ) {
                    if (password.text.toString() == confirmPassword.text.toString()) {
                        Toast.makeText(this, "Created Account Successful!", Toast.LENGTH_LONG)
                            .show()


                        //Setting the Username and Password
                        Passing.setUsername(userName.text.toString())
                        Passing.setPassword(password.text.toString())

                        //Calls this method to save info from user
                        //  saveData()

                        // Goes to new Activity (Main Activity)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                        //Clearing the info for next user
                        fullName.text.clear()
                        userName.text.clear()
                        email.text.clear()
                        password.text.clear()
                        confirmPassword.text.clear()

                    }else{
                        fullName.setError("Full Name must be less than 20 character long")
                        userName.setError("User Name must be less than 20 character long")
                        password.setError("Password must be less than 10 character long")
                        confirmPassword.setError("Confirm Password doesn't match the password")
                    }
                }
            }
            else{
                email.setError("Please Enter a Valid Email")
            }
        }
        else{
            Toast.makeText(this,"Need to All Information",Toast.LENGTH_SHORT).show()
        }
    }
}




   // private fun saveData(){
        //Save user Info, but not stored
  //      val sharedPref = getSharedPreferences("UserAccount",Context.MODE_PRIVATE)
  //      val edit = sharedPref.edit()
        //Save their name,username,email and password
   //     edit.putString("Name", fullName.text.toString())
   //     edit.putString("User Name", userName.text.toString())
    //    edit.putString("Email",email.text.toString())
    //    edit.putString("Password",password.text.toString())

   //     edit.apply()


  //      Toast.makeText(this,"Data has been saved",Toast.LENGTH_LONG).show()
   //     sharedPref.getString("Name","User Name")?.let { Log.d("Debug", it) }

   // }





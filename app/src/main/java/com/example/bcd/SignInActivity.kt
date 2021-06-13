wpackage com.example.bcd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    lateinit var firebaseauth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        firebaseauth = FirebaseAuth.getInstance()
        val sgnUpButton =findViewById<Button>(R.id.LoginButton)
        sgnUpButton.setOnClickListener{
            //   Toast.makeText(this, "WORKING", Toast.LENGTH_SHORT).show()
            SignInUser()
        }
    }
    private fun SignInUser(){
        val eid = findViewById<EditText>(R.id.LoginEmailAddress)   //  id for email address
        val pss = findViewById<EditText>(R.id.LoginPassword)         //  id for password view
        val email:String = eid.text.toString()
        val password:String = pss.text.toString()

        if(email.isBlank() || password.isBlank() ){                                    // Handling the case when email , password is left blank
            Toast.makeText(this, "Email and password cant be blank.", Toast.LENGTH_SHORT).show()
            return
        }
        firebaseauth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    // code
                    Toast.makeText(this, "Logged In Successfully!.", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "Error.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
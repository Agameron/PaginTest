package com.example.faceloginteste

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val callbackManager = CallbackManager.Factory.create()
    var auth= FirebaseAuth.getInstance()
    var tag=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val loginbutton=findViewById<LoginButton>(R.id.login_button)
        loginbutton.setOnClickListener {
            if(userLoggedIn()){
                auth.signOut()
            }
            else{
                LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile","email"))
            }
        }

        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d(tag, "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d(tag, "facebook:onCancel")
                // ...
            }

            override fun onError(error: FacebookException) {
                Log.d(tag, "facebook:onError", error)
                // ...
            }
        })
        // ...


    }

    private fun userLoggedIn(): Boolean {
        if(auth.currentUser!=null && !AccessToken.getCurrentAccessToken()!!.isExpired){
            return true
        }
        return false
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(tag, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(tag, "signInWithCredential:success")
                    val user = auth.currentUser
                    startActivity(Intent(this,MainActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(tag, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }

}
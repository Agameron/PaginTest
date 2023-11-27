package com.example.faceloginteste

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private var auth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val name=findViewById<TextView>(R.id.textView)
        val image=findViewById<ImageView>(R.id.imageView)
        name.setText(auth.currentUser?.displayName)
        Picasso.get().load(auth.currentUser?.photoUrl).into(image)
    }
}
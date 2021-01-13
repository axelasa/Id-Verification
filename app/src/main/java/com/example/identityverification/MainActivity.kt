package com.example.identityverification

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import com.example.identityverification.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var sharedpreferences: SharedPreferences? = null
    val MyPREFERENCES = "MyPrefs"
    var Identification = 0
    private var Id = ""
    private var idText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)

         idText = binding.idNumber.toString()

      var nxt = binding.nxt

        nxt.setOnClickListener {
                click()
            }
        }



    private fun click() {

        Id = idNumber!!.text.toString()

        if (Id.isEmpty()) {
            validate()
            Log.e("###Empty>>>>", Id)
            Toast.makeText(
                this@MainActivity ,
                "Enter Your ID Number!" ,
                Toast.LENGTH_LONG
            ).show()

        }else {

            val editor = sharedpreferences!!.edit()
            Identification = 1
            editor.putInt("identified", Identification)
            editor.putString("Id", Id)
            editor.apply()

            startActivity(Intent(this@MainActivity, MemberRegistration::class.java))
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            finish()
        }
    }

    private fun validate(): Boolean {
        var valid = true
        Id = idNumber.text.toString()

        if (Id.isEmpty()) {
            idNumber.error = "enter your Id number"
        }else{
            idNumber.error = null
            valid =true
        }
        return valid
    }
}



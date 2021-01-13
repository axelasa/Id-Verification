package com.example.identityverification

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import com.example.identityverification.databinding.ActivityMemberRegistrationBinding
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import kotlinx.android.synthetic.main.activity_member_registration.*
import org.json.JSONObject
import java.util.regex.Matcher
import java.util.regex.Pattern

class MemberRegistration : AppCompatActivity() {

    val MyPREFERENCES = "MyPrefs"
    var sharedpreferences: SharedPreferences? = null

    var fetchURL = ""
    private var Id =""
    private var emailId:String=""
    private var phone:String=""
    private var mProgressDialog: ProgressDialog? = null

    private lateinit var binding: ActivityMemberRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMemberRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedpreferences = getSharedPreferences(MyPREFERENCES , Context.MODE_PRIVATE)
        Id = sharedpreferences?.getString("Id","").toString()
        Log.e("INTERGER>>>",Id)
        fetchURL = "https://app.bongasms.co.ke/api/kyc?kyc_token=IyDMKF7Hk4T3Q486pTU5QY4VhjMOKOl&search_param="+Id
        Log.e("URL>>", fetchURL)

        details()

        emailId= binding.email.toString()
        phone = binding.cellPhone.toString()

        next.setOnClickListener {
            proceed()
        }
    }

    private fun details(){

        showSimpleProgressDialog(this@MemberRegistration,null,"verifyingMember...",false)
        try {
            Fuel.get(fetchURL).responseJson { request, response, result ->
                Log.e("response", result.get().content)
                onTaskCompleted(result.get().content)
            }

        }catch (e:Exception){
            Log.e("Exception", e.toString())

            }
    }

    private fun onTaskCompleted(response: String) {

        Log.e("responsejson", response)
        removeSimpleProgressDialog()  //will remove progress dialog
        Log.e("check>>>>:", "one")
        try {

            Log.e("CHECK>>:" , "two")

            val jsonObject = JSONObject(response)
            Log.e("JSON>>: ", jsonObject.toString())

//            surName.text = jsonObject.getJSONObject("search_result").getString("surname")
//
//            otherName.text =  jsonObject.getJSONObject("search_result").getString("other_name")

            var name:String= jsonObject.getJSONObject("search_result").getString("first_name")+" " +jsonObject.getJSONObject("search_result").getString("other_name")+" " +jsonObject.getJSONObject("search_result").getString("surname")

            fullName.text = name

            idPassPortNo.text = jsonObject.getJSONObject("search_result").getString("id_number")

            gender.text = jsonObject.getJSONObject("search_result").getString("gender")

            dateOfBirth.text = jsonObject.getJSONObject("search_result").getString("date_of_birth")

            citizenship.text = jsonObject.getJSONObject("search_result").getString("citizenship")
        }catch (e: java.lang.Exception){

            Log.e("####details>>", e.toString())

        }

        Log.e("##CHECK >>" , "three")
    }

    fun showSimpleProgressDialog(context: Context , title: String? , msg: String , isCancelable: Boolean) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context , title , msg)
                mProgressDialog!!.setCancelable(isCancelable)
            }
            if (!mProgressDialog!!.isShowing) {
                mProgressDialog!!.show()
            }

        } catch (ie: IllegalArgumentException) {
            ie.printStackTrace()
        } catch (re: RuntimeException) {
            re.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog!!.isShowing) {
                    mProgressDialog!!.dismiss()
                    mProgressDialog = null
                }
            }
        } catch (ie: IllegalArgumentException) {
            ie.printStackTrace()

        } catch (re: RuntimeException) {
            re.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun proceed(){
        emailId = email!!.text.toString()
        phone = cellPhone.text.toString()

        if (emailId.isEmpty()||phone.isEmpty()) {
            validate()
            Log.e("###Empty>>>>",emailId)
            Log.e("###Phone>>",phone)
            Toast.makeText(
                this@MemberRegistration ,
                "Enter all fields!" ,
                Toast.LENGTH_LONG
            ).show()
        } else {
            startActivity(Intent(this@MemberRegistration, MemberRegistration::class.java))
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            finish()
        }
    }

    private fun validate():Boolean {
        val EMAILIDPATTERN =
            "^[_A-Za-z0-9-]+(\\.[A-za-z0-9-]+)*@[A-za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        val pattern: Pattern = Pattern.compile(EMAILIDPATTERN)
        val matcher: Matcher = pattern.matcher(emailId)

        var valid = true
        emailId = email.text.toString()
        phone = cellPhone.text.toString()

        if (emailId.isEmpty()) {
            email.error = "Enter Your EmailAdress"

        } else if
          (!emailIdValidationFunc(emailId)){
            email.error ="Not Valid Email Adress"

        }else{
            email.error =null
            valid = true
        }

        if (phone.isEmpty()){
            cellPhone.error = "Enter Your Phone Number"
        }else{
            cellPhone.error = null

            valid = true
        }
        return matcher.matches() && valid


    }
    private fun emailIdValidationFunc(emailId:String):Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()
    }
}
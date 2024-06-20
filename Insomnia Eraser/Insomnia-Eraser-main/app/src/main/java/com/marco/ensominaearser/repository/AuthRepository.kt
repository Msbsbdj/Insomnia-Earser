package com.marco.ensominaearser.repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.marco.ensominaearser.R
import com.marco.ensominaearser.data.pojo.User
import com.marco.ensominaearser.utilites.Constants
import com.marco.ensominaearser.utilites.PreferenceManager
import io.github.muddz.styleabletoast.StyleableToast


class AuthRepository(private var application: Application) {
    private  var  user :MutableLiveData<FirebaseUser> = MutableLiveData()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mFireStore = FirebaseFirestore.getInstance()

    fun getCurrentUser(): MutableLiveData<FirebaseUser>{
        return  user
    }

    fun signup(user: User){
        firebaseAuth.createUserWithEmailAndPassword(user.email,user.password).addOnCompleteListener {
            saveUserInFireStore(user,it.result.user!!.uid)
            this.user.value = it.result.user
            StyleableToast.makeText(application, "Your Account Was Created Successfully...Please Login", Toast.LENGTH_LONG, R.style.mytoast).show();
        }.addOnFailureListener {
            Log.d("SignInError",it.message.toString())
        }
    }
    fun login(email:String,password:String){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            this.user.value = it.user
            getUserInfFromFireStore(it.user!!.uid)
        }.addOnFailureListener {
            Toast.makeText(application.applicationContext,it.message,Toast.LENGTH_LONG).show()
        }
    }

    private fun saveUserInFireStore(user: User, id:String){
        mFireStore.collection(Constants.KEY_COLLECTION_PATIENTS).document(id)
            .set(user)
    }
    private fun getUserInfFromFireStore(id:String){
        mFireStore.collection(Constants.KEY_COLLECTION_PATIENTS).document(id).get().addOnSuccessListener {
            val userDoc = it.toObject(User::class.java)
            saveDataInPreference(userDoc!!)
        }

    }

    private fun saveDataInPreference(user: User){
        val preferenceManager = PreferenceManager(application.baseContext)
        preferenceManager.putBoolean("Login",true)
        preferenceManager.putString("PatientFirstName",user.firstName)
        preferenceManager.putString("PatientLastName",user.lastName)
        preferenceManager.putString("PatientId",this.user.value!!.uid)
        preferenceManager.putString("PatientEmail",user.email)
        preferenceManager.putString("PatientPhone",user.phone)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            preferenceManager.putString(Constants.KEY_FCM_TOKEN, it)
            val documentReference: DocumentReference =
                mFireStore.collection(Constants.KEY_COLLECTION_PATIENTS).document(
                    preferenceManager.getString("PatientId")
                )
            documentReference.update(Constants.KEY_FCM_TOKEN, it)
                .addOnFailureListener {
                    StyleableToast.makeText(application.baseContext,application.getString(R.string.unable_to_updated_token),Toast.LENGTH_LONG).show()

                }
        }

    }

}
package com.doctor.ensomnia.repositories

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.doctor.ensomnia.R
import com.doctor.ensomnia.data.pojo.Doctor
import com.doctor.ensomnia.utilites.Constants
import com.doctor.ensomnia.utilites.PreferenceManager
import com.doctor.ensomnia.utilites.SupportFunctions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.github.muddz.styleabletoast.StyleableToast


class AuthRepository(private var application: Application) {
    private  var  user :MutableLiveData<FirebaseUser> = MutableLiveData()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mFireStore = FirebaseFirestore.getInstance()
    val preferenceManager = PreferenceManager(application.baseContext)

    fun getCurrentUser(): MutableLiveData<FirebaseUser>{
        return  user
    }

    fun signup(user: Doctor,fragment: Fragment, imageFileUri: Uri?){
        firebaseAuth.createUserWithEmailAndPassword(user.email!!,user.password!!).addOnCompleteListener {
            this.user.value = it.result.user
            user.id = it.result.user!!.uid
            uploadImageToCloudStorage(fragment,imageFileUri,user)

            StyleableToast.makeText(application, "Your Account Was Created Successfully...Please Login", Toast.LENGTH_LONG, R.style.mytoast).show();
        }.addOnFailureListener {
            Log.d("SignInError",it.message.toString())
        }
    }

    fun uploadImageToCloudStorage(fragment: Fragment, imageFileUri: Uri?,user:Doctor){
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            "doctor"+"."+ System.currentTimeMillis() +"." + SupportFunctions.getFileExtensions(fragment.requireActivity(),imageFileUri)
        )
        sRef.putFile(imageFileUri!!).addOnSuccessListener {taskSnapShot ->
            taskSnapShot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { Uri ->
                user.image = Uri.toString()
                saveUserInFireStore(user)
            }
                .addOnFailureListener { Exception ->
                    Log.e("Upload Image Error",Exception.message,Exception)
                }
        }
    }


    fun login(email:String,password:String){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            this.user.value = it.result.user
            getUserInfFromFireStore(it.result.user!!.uid)
        }.addOnFailureListener {

            Toast.makeText(application.applicationContext,it.message,Toast.LENGTH_LONG).show()
        }
    }

    private fun saveUserInFireStore(user: Doctor){
        mFireStore.collection(Constants.KEY_COLLECTION_DOCTORS).document(user.id!!)
            .set(user).addOnSuccessListener {
                this.user.value = null
            }

    }
    private fun getUserInfFromFireStore(id:String){
        preferenceManager.putString("doctorId",id)
        mFireStore.collection(Constants.KEY_COLLECTION_DOCTORS).document(id).get().addOnSuccessListener {
            val userDoc = it.toObject(Doctor::class.java)
            saveDataInPreference(userDoc!!)
        }

    }

    private fun saveDataInPreference(user: Doctor){
        Log.d("state","saved")
        preferenceManager.putBoolean("Login",true)
        preferenceManager.putString("doctorName",user.name!!)
        preferenceManager.putString("doctorEmail",user.email!!)
        preferenceManager.putString("doctorImage",user.image!!)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            preferenceManager.putString(Constants.KEY_FCM_TOKEN, it)
            val documentReference: DocumentReference =
                mFireStore.collection(Constants.KEY_COLLECTION_DOCTORS).document(
                    preferenceManager.getString("doctorId")
                )
            documentReference.update(Constants.KEY_FCM_TOKEN, it)
                .addOnFailureListener {
                    StyleableToast.makeText(application.baseContext,application.getString(R.string.unable_to_updated_token),Toast.LENGTH_LONG).show()

                }
        }

    }

}
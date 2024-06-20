package com.marco.ensominaearser.utilites
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment


import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.marco.ensominaearser.R
import com.marco.ensominaearser.databinding.FragmentHomeBinding


import java.io.IOException


class SupportFunctions {

    companion object {
        private lateinit var mProgressBar: Dialog

        fun showToast(context: Context, message: String) {
            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.BOTTOM, 0, 0)
            toast.show()
        }

        fun loading(isLoading: Boolean, button: View?, progressBar: View) {
            if (button != null) {
                if (isLoading) {
                    button.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.INVISIBLE
                    button.visibility = View.VISIBLE
                }
            } else {
                if (isLoading) {
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                }
            }

        }

        fun showProgressBar(context: Context, text: String) {
            mProgressBar = Dialog(context)
            mProgressBar.setContentView(R.layout.dialog_progress)
            mProgressBar.setCancelable(false)
            mProgressBar.setCanceledOnTouchOutside(false)

            mProgressBar.show()
        }

        fun hideDialog() {
            mProgressBar.dismiss()
        }

        fun loadPicture(context: Context, imageURI: String, ImageView: ImageView) {
            try {
                Glide
                    .with(context)
                    .load(Uri.parse(imageURI))
                    .centerCrop()
                    .into(ImageView)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun checkForInternet(context: Context): Boolean {
            // register activity with the connectivity manager service
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            // if the android version is equal to M
            // or greater we need to use the
            // NetworkCapabilities to check what type of
            // network has the internet connection

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false
            // Representation of the capabilities of an active network.
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        }

        fun showNoInternetSnackBar(fragment: Fragment) {
            val snackBarView = Snackbar.make(
                fragment.requireView(),
                " please check internet connection",
                Snackbar.LENGTH_LONG
            )
            val view = snackBarView.view
            val params = view.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.BOTTOM
            view.layoutParams = params
            snackBarView.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
            snackBarView.show()
        }
    }


}












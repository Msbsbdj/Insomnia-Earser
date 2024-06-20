package com.marco.ensominaearser.ui.fragments
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.marco.ensominaearser.R
import com.marco.ensominaearser.data.pojo.AiModelResponse
import com.marco.ensominaearser.data.remote.RetrofitInstance
import com.marco.ensominaearser.databinding.FragmentMedicalConsultantBinding
import com.marco.ensominaearser.databinding.ResultAlertDialogBinding
import io.github.muddz.styleabletoast.StyleableToast
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MedicalConsultantFragment : Fragment() {
    private lateinit var binding: FragmentMedicalConsultantBinding
  private lateinit var resultAlertBinding : ResultAlertDialogBinding
    private lateinit var alert: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMedicalConsultantBinding.inflate(layoutInflater)
setListeners()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resultAlertBinding = ResultAlertDialogBinding.inflate(
            layoutInflater, binding.root, false
        )
    }

    private fun setListeners() {
        binding.checkBtn.setOnClickListener {
            binding.checkBtn.visibility = View.INVISIBLE
            binding.loadingGif.visibility = View.VISIBLE
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Age", binding.ageTv.text.toString())
                .addFormDataPart("Sleep Duration", binding.SleepDurationTv.text.toString())
                .addFormDataPart("Quality of Sleep", binding.qualityOfSleepTv.text.toString())
                .addFormDataPart("Physical Activity Level", binding.activityTv.text.toString())
                .addFormDataPart("Stress Level", binding.stressLevelTv.text.toString())
                .addFormDataPart("BMI Category", binding.bmiTv.text.toString())
                .addFormDataPart("Heart Rate", binding.heartRateTv.text.toString())
                .addFormDataPart("Daily Steps", binding.dailyStepsTv.text.toString())
                .addFormDataPart("systolic_bp",binding.systolicBpEt.text.toString())
                .addFormDataPart("diastolic_bp",binding.diastolicBpEt.text.toString())
                .build()
            RetrofitInstance.api.getResult(requestBody).enqueue(object : Callback<AiModelResponse> {
                override fun onResponse(p0: Call<AiModelResponse>, p1: Response<AiModelResponse>) {
                    if (p1.isSuccessful){
                        binding.checkBtn.visibility = View.VISIBLE
                        binding.loadingGif.visibility = View.INVISIBLE
                       showDialog(p1.body()!!.prediction)
                    }
                }
                override fun onFailure(p0: Call<AiModelResponse>, p1: Throwable) {
                    Log.d("res", p1.message!!)
                }
            })
        }

    }


    private fun showDialog(result:String) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        if (resultAlertBinding.root.parent != null) {
            (resultAlertBinding.root.parent as ViewGroup).removeView(
                resultAlertBinding.root
            )
        }
        builder.setView(resultAlertBinding.root)

        alert = builder.create()
        resultAlertBinding.tvResultResponse.text = result
        if (alert.window != null) {
            alert.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alert.show()



    }
}
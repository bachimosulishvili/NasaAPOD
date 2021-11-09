package com.example.nasaapod


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nasaapod.databinding.FragmentSplashScreenBinding
import com.example.nasaapod.fragment.pList
import com.example.nasaapod.network.RetrofitInstance
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

val auth = FirebaseAuth.getInstance()

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        lifecycleScope.launch {
            delay(5000)
            findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
        }
        request()
    }

    fun request() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getPhotos(RetrofitInstance.API_KEY, RetrofitInstance.TOTAL)
            } catch (e: IOException) {
                Log.d("TAG", "$e")
                return@launch
            } catch (e: HttpException) {
                Log.d("TAG", "$e")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                response.body() ?: mutableListOf()
                pList = response.body()!!.toMutableList()
                Log.d("TAG", "$response : ${response.body()}")
                return@launch
            } else {
                Log.d("TAG", "Else called in ViewModel. Unexpected.")
                Log.d("RESPONSE", "${response.body()}")
            }

            return@launch
        }
    }


}
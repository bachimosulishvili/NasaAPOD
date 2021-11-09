package com.example.nasaapod.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.nasaapod.PhotosAdapter
import com.example.nasaapod.R
import com.example.nasaapod.databinding.FragmentApodBinding
import com.example.nasaapod.model.Photo
import com.example.nasaapod.network.RetrofitInstance
import com.example.nasaapod.network.RetrofitInstance.API_KEY
import com.example.nasaapod.network.RetrofitInstance.TOTAL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

var pList = mutableListOf<Photo>()

class APODFragment : Fragment() {
    private lateinit var binding: FragmentApodBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentApodBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        listeners()
        request()
        setupRecycler()
    }

    private fun listeners() {
        binding.ivNasa.setOnClickListener {
            findNavController().navigate(R.id.action_APODFragment_to_profileFragment)
        }
        binding.toggle.setOnClickListener {
            toggleVisibility(binding.title)
            toggleVisibility(binding.description)
            toggleVisibility(binding.author)
            toggleVisibility(binding.date)
            toggleVisibility(binding.current)
            toggleVisibility(binding.slash)
            toggleVisibility(binding.total)
            toggleVisibility(binding.gradient)
        }
    }


    private fun setupRecycler() {
        val adapter = PhotosAdapter(pList)
        binding.ViewPager.adapter = adapter
        binding.ViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                val current = adapter.photos[position]
                binding.current.text = (position + 1).toString()
                binding.total.text = adapter.photos.size.toString()
                binding.title.text = current.title
                binding.description.text = current.explanation
                binding.author.text = current.copyright
                binding.date.text = current.date
            }
        })
    }

    private fun request() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getPhotos(API_KEY, TOTAL)
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

    private fun toggleVisibility(view: View) {
        if (view.visibility == View.VISIBLE) {
            view.visibility = View.INVISIBLE
        } else {
            view.visibility = View.VISIBLE
        }
    }
}

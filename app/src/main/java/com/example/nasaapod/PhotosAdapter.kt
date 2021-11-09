package com.example.nasaapod

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nasaapod.databinding.LayoutPhotoBinding
import com.example.nasaapod.model.Photo

class PhotosAdapter (val photos: List<Photo>) : RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder>() {
    inner class PhotosViewHolder(val binding: LayoutPhotoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PhotosAdapter.PhotosViewHolder {
        return PhotosViewHolder(LayoutPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PhotosAdapter.PhotosViewHolder, position: Int) {
        val current = photos[position]
        Glide.with(holder.binding.photo).load(current.hdurl).into(holder.binding.photo)
    }

    override fun getItemCount(): Int = photos.size
}
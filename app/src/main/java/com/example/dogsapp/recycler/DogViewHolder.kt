package com.example.dogsapp.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.dogsapp.databinding.DogItemBinding
import com.example.dogsapp.models.Dog

class DogViewHolder(private val binding: DogItemBinding, val listener: OnDogItemClickListener) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Dog) {
        binding.dogData.text = "ID: ${item.id}, NAME: ${item.name},"
        binding.dogDataSecondary.text = "AGE: ${item.age}, BREED: ${item.breed}"
        binding.deleteButton.setOnClickListener {
            listener.onDeleteClick(item, bindingAdapterPosition)

        }
        binding.editButton.setOnClickListener {
            listener.onEditClick(it, item, bindingAdapterPosition)
        }
    }
}



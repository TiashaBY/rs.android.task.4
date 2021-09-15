package com.example.dogsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dogsapp.databinding.EditDogFragmentBinding
import com.example.dogsapp.models.Dog
import com.example.dogsapp.utils.ENTRY


class EditEntryFragment : Fragment() {

    private var _binding: EditDogFragmentBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditDogFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dogArgs = arguments?.getParcelable(ENTRY) as Dog?
        dogArgs?.let {
            binding.name.setText(it.name)
            binding.age.setText(it.age.toString())
            binding.breed.setText(it.breed)
            it.id?.let { it1 -> binding.entryId.setText(it1.toString()) }
        }
        binding.addEmployeeButton.setOnClickListener {
            if (checkFieldsNotEmpty()) {
                var id: Long? = null
                if (binding.entryId.text.isNotEmpty()) {
                    id = binding.entryId.text?.toString()?.toLong()
                }
                val dog = Dog(id)
                    .apply {
                        name = binding.name.text.toString()
                        age = binding.age.text.toString().toInt()
                        breed = binding.breed.text.toString()
                    }
                findNavController().previousBackStackEntry?.savedStateHandle?.set(ENTRY, dog)
                findNavController().navigateUp()
            } else {
                Toast.makeText(context, getString(R.string.error_label), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkFieldsNotEmpty(): Boolean {
        return !(binding.name.text.isNullOrEmpty()
                || binding.age.text.isNullOrEmpty()
                || binding.breed.text.isNullOrEmpty())

    }
}
package com.example.dogsapp.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import com.example.dogsapp.databinding.DogItemBinding
import com.example.dogsapp.models.Dog

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.dogsapp.R
import com.example.dogsapp.database.DogsRepository
import com.example.dogsapp.utils.ENTRY
import com.example.dogsapp.utils.RELOAD_LIST
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DogsListAdapter(private val dbRepository: DogsRepository,
                      private val context: Context,
                      private val onListChangeListener: OnListChangeListener
                      ) : ListAdapter<Dog, DogViewHolder>(itemComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = DogItemBinding.inflate(layoutInflater, parent, false)
        return DogViewHolder(binding, object : OnDogItemClickListener {
            override fun onDeleteClick(dog: Dog, position: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    if (dbRepository.delete(dog) > 0) {
                        val mutableList = currentList.toMutableList()
                        mutableList.remove(dog)
                        submitList(mutableList)
                        notifyItemRemoved(position)
                    }
                    cancel()
                }
            }

            override fun onEditClick(view: View, dog: Dog, position: Int) {
                Navigation.findNavController(view)
                    .navigate(R.id.action_dogsListFragment_to_editEntryFragment, bundleOf(ENTRY to dog))
            }
        })
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCurrentListChanged(previousList: MutableList<Dog>, currentList: MutableList<Dog>) {
        super.onCurrentListChanged(previousList, currentList)
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(RELOAD_LIST, false)) {
            onListChangeListener.onListChange()
            with (PreferenceManager.getDefaultSharedPreferences(context).edit()) {
                this?.putBoolean(RELOAD_LIST, false)
                this?.apply()
            }
        }
    }

    private companion object {
        private val itemComparator = object : DiffUtil.ItemCallback<Dog>() {

            override fun areItemsTheSame(oldItem: Dog, newItem: Dog): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Dog, newItem: Dog): Boolean {
                return oldItem.id == newItem.id && oldItem.name == newItem.name &&
                        oldItem.age == newItem.age && oldItem.breed == newItem.breed
            }
        }
    }
}

interface OnDogItemClickListener {
    fun onDeleteClick(dog: Dog, position: Int)
    fun onEditClick(view: View, dog: Dog, position: Int)
}

interface OnListChangeListener {
    fun onListChange()
}
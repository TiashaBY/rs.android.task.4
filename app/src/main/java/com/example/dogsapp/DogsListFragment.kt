package com.example.dogsapp

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogsapp.database.DogsRepository
import com.example.dogsapp.database.DogsRepositoryHelper
import com.example.dogsapp.databinding.DogsListFragmentBinding
import com.example.dogsapp.models.Dog
import com.example.dogsapp.recycler.DogsListAdapter
import com.example.dogsapp.recycler.OnListChangeListener
import com.example.dogsapp.utils.ENTRY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DogsListFragment : Fragment() {

    var repo: DogsRepository? = null

    private var _binding: DogsListFragmentBinding? = null
    private val binding get() = checkNotNull(_binding)

    private var job: Job = Job()
    private val scope = CoroutineScope(job + Dispatchers.Main)

    private var _dogslistadapter: DogsListAdapter? = null
    private val dogslistadapter get() = checkNotNull(_dogslistadapter)

    private var _sharedPreferences: SharedPreferences? = null
    private val sharedPreferences get() = checkNotNull(_sharedPreferences)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DogsListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.progressBar.visibility = VISIBLE
        _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.context)
        repo = DogsRepositoryHelper.getDao(view.context, sharedPreferences)?.let { DogsRepository(it) }

        initAdapter(view)
        displayDogsData()

        //listeners
        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.dogsListFragment) {
                binding.floatingButtonAdd.visibility = VISIBLE
            } else {
                binding.floatingButtonAdd.visibility = GONE
            }
        }

        binding.floatingButtonAdd.setOnClickListener{
            findNavController().navigate(R.id.action_dogsListFragment_to_editEntryFragment)
        }

        binding.listRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                recyclerView.verticalScrollbarPosition
                if (dy > 0 && binding.floatingButtonAdd.isShown()) {
                    binding.floatingButtonAdd.hide()
                } else {
                    binding.floatingButtonAdd.show()
                }
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        job.cancel()
    }

    private suspend fun loadListFromDb(): List<Dog> {
        val name: String = sharedPreferences.getString(getString(R.string.sorting_key), "") ?: ""
        val dogsList = if (name == "" || name == "opt0") {
            repo?.queryAll()
        } else {
            repo?.queryAllWithSorting(name)
        }
        return dogsList ?: emptyList()
    }

    private suspend fun updateDog(dog: Dog): Int {
        return repo?.update(dog) ?: 0
    }

    private suspend fun addDog(dog: Dog): Long {
        val res = repo?.insert(dog) ?: -1
        if (res > -1) {
            dog.id = res
        }
        return res
    }

    private fun displayDogsData() {
        val updatedItem =
            findNavController().currentBackStackEntry?.savedStateHandle?.get<Dog>(ENTRY)
        if (updatedItem != null) {
            //clear in order to refresh the data
            findNavController().currentBackStackEntry?.savedStateHandle?.set(ENTRY, null)
            scope.launch {
                var res: Long = -1L
                if (updatedItem.id == null) {
                    Log.d("add", "Add item, result $res")
                    res = addDog(updatedItem)
                    if (res == -1L) {
                        Toast.makeText(context, "Error while item inserted", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    res = updateDog(updatedItem).toLong()
                    Log.d("update", "Update item, result " + res)
                    if (res == 0L) {
                        Toast.makeText(context, "Error while item update", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                val newDogsList = loadListFromDb()
                dogslistadapter.submitList(newDogsList)
                binding.progressBar.visibility = GONE
            }
        } else {
            scope.launch {
                val dogsList: List<Dog> = loadListFromDb()
                dogslistadapter.submitList(dogsList)
                binding.progressBar.visibility = GONE
            }
        }
    }

    private fun initAdapter(view: View) {
        if (_dogslistadapter == null) {
            repo?.let {
                _dogslistadapter = DogsListAdapter(it, view.context, object :
                    OnListChangeListener {
                    override fun onListChange() {
                        binding.listRecyclerView.scrollToPosition(0)
                    }
                }
                )
            }
        }
        binding.listRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = dogslistadapter
            adapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        }
    }
}
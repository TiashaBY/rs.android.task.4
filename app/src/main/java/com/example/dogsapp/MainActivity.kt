package com.example.dogsapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dogsapp.databinding.ActivityMainBinding
import com.example.dogsapp.models.Dog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.dogsapp.database.DogsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController : NavController

    var _binding : ActivityMainBinding? = null
    val binding get() = checkNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.fragments_container_nav)
            .apply { setGraph(R.navigation.nav_graph) }
            appBarConfiguration = AppBarConfiguration(navController.graph)
            setupActionBarWithNavController(navController, appBarConfiguration)
    }

    //MENU STUFF

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.filterPreferences -> {
            item.onNavDestinationSelected(navController)
        }
        R.id.dummyData -> {
            CoroutineScope(Dispatchers.Main).launch {
                generateDummyData()
                navController.navigate(R.id.action_dogsListFragment_self)
            }
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }


    private suspend fun generateDummyData(): Boolean {
        val repo = DogsRepository.getRepo(this, true)
        for (i in 1..100) {
            val dog = Dog().apply {
                name = "New dog $i"
            breed = "$i breed"
            age = (1..50).random()}
            repo.insert(dog)
        }
        return true
    }
}
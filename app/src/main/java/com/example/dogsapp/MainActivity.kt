package com.example.dogsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dogsapp.databinding.ActivityMainBinding
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.dogsapp.utils.generateDummyData
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController : NavController

    private var _binding : ActivityMainBinding? = null
    private val binding get() = checkNotNull(_binding)

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
        navController.addOnDestinationChangedListener { _, destination, _ ->
            menu?.findItem(R.id.dummyData)?.isVisible = destination.id == R.id.dogsListFragment
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.filterPreferences -> {
            item.onNavDestinationSelected(navController)
        }
        R.id.dummyData -> {
            CoroutineScope(Dispatchers.Main).launch {
                generateDummyData(this@MainActivity)
                navController.navigate(R.id.action_dogsListFragment_self)
                this.cancel()
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
}
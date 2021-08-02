package cl.neoxcore.saifu.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import cl.neoxcore.saifu.R
import cl.neoxcore.saifu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (binding == null) {
            binding = ActivityMainBinding.inflate(layoutInflater)
        }
        setContentView(binding?.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.saifu_nav_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActivity()
    }

    private fun setupActivity() {
        setupInjection()
        setupNavigation()
        destinationManager()
    }

    private fun setupInjection() {
        TODO("Not yet implemented")
    }

    private fun setupNavigation() {
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setSupportActionBar(binding?.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun destinationManager() {
        navController.addOnDestinationChangedListener { _, _, _ ->
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowHomeEnabled(true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

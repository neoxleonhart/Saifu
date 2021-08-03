package cl.neoxcore.saifu.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import cl.neoxcore.saifu.R
import cl.neoxcore.saifu.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var binding: ActivityMainBinding? = null
    private var navHostFragment = NavHostFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (binding == null) {
            binding = ActivityMainBinding.inflate(layoutInflater)
        }
        setContentView(binding?.root)
        setupNavigation()
        destinationManager()
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.saifu_nav_fragment) as NavHostFragment
        val navGraph = navHostFragment.navController.graph
        navHostFragment.navController.setGraph(navGraph, intent.extras)
        appBarConfiguration = AppBarConfiguration
            .Builder(
                R.id.balanceFragment,
                R.id.transactionFragment
            )
            .build()
        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
    }

    private fun destinationManager() {
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.transactionFragment -> {
                    supportActionBar?.apply {
                        setDisplayHomeAsUpEnabled(true)
                        setDisplayShowHomeEnabled(true)
                        show()
                    }
                }
                else -> {
                    supportActionBar?.show()
                }
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

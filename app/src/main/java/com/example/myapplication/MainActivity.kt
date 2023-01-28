package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.di.MyApplication
import com.example.myapplication.phoneAuth.PhoneAuthActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var appBarConfiguration:AppBarConfiguration

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(MyApplication.getInstanse()!!.localeHelper!!.setLocale(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        loadLocale()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // using toolbar as ActionBar
       setSupportActionBar(binding.toolbar);

        //supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_flower)
       // supportActionBar?.setDisplayHomeAsUpEnabled(true);
        //supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_flower);// set drawable icon

      //  binding.toolbar.navigationIcon = AppCompatResources.getDrawable(this,R.drawable.ic_flower)


        /*supportActionBar?.setHomeButtonEnabled(true);
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_flower);*/

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
         appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_wishlist, R.id.navigation_orders
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_activity_main).navigateUp(appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.getItemId()) {
            R.id.id_menuTamil -> {
                setLocale("ta")
                recreate()
                true
            }
            R.id.id_menuEnglish -> {
                setLocale("en")
                recreate()
                true
            }
            R.id.id_logout -> {
                FirebaseAuth.getInstance().signOut()
                startPhoneAuthActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startPhoneAuthActivity() {
        val intent = Intent(this, PhoneAuthActivity::class.java)
        startActivity(intent)
    }
    private fun setLocale(s: String) {
      /* val locale = Locale(s)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        val overrideConfiguration = baseContext.resources.configuration
        /*val context: Context = createConfigurationContext(overrideConfiguration)
        val resources: Resources = context.getResources()
        resources.displayMetrics*/
       // baseContext.createConfigurationContext(overrideConfiguration)

        //baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            baseContext.createConfigurationContext(config);
        } else {
            baseContext.resources.updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }*/


        MyApplication.getInstanse()?.localeHelper?.setLocale(this,s)


       // updateResources(this,s)

        var editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang",s)
        editor.apply()

    }



    private fun loadLocale(){
        var pref = getSharedPreferences("Settings", MODE_PRIVATE)
        var lang = pref.getString("My_Lang","")

        setLocale(lang!!)
    }

    private fun updateResources(context: Context, language: String) {
        var context = context
        val locale = Locale(language)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
            context = context.createConfigurationContext(config)
        } else {
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
        }
    }
}
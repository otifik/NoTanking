package com.cse.ntv2.ui.main

import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import com.cse.ntv2.R
import com.cse.ntv2.base.BaseActivity
import com.cse.ntv2.databinding.ActivityMainBinding
import com.cse.ntv2.ui.settings.SettingsFragment
import com.cse.ntv2.ui.todo.TodoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

const val TAG_TODO = "TODO"
const val TAG_SETTINGS = "SETTINGS"

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toolbarTitle: TextView
    private lateinit var todoFragment: TodoFragment
    private lateinit var settingsFragment: SettingsFragment

    private val viewModel: MainViewModel by viewModels()

    override fun initRef() {
        bottomNav = binding.bottomNav
        toolbarTitle = binding.toolbarTitle
    }

    override fun initView() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initImmersiveStatusBar()
        initFrag()
        bottomNav.apply {
            itemIconTintList = null
            setOnNavigationItemSelectedListener {
                showFrag(it.itemId)
                true
            }
        }
        toolbarTitle.text = getString(R.string.toolbar_title_todo)
    }

    private fun initImmersiveStatusBar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setImmerseLayout(binding.toolbar)
    }

    private fun initFrag(){
        todoFragment = TodoFragment()
        settingsFragment = SettingsFragment()
        val fragmentTransaction = supportFragmentManager
        val transaction = fragmentTransaction.beginTransaction()
        transaction.add(R.id.frag_container, todoFragment, TAG_TODO)
        transaction.add(R.id.frag_container, settingsFragment, TAG_SETTINGS)
        transaction.show(todoFragment)
        transaction.hide(settingsFragment)
        transaction.commit()
    }

    private fun showFrag(index: Int){
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        when(index){
            R.id.nav_todo -> {
                hideFrag(transaction)
                transaction.show(todoFragment)
                transaction.commit()
                toolbarTitle.text = getString(R.string.toolbar_title_todo)
            }

            R.id.nav_settings -> {
                hideFrag(transaction)
                transaction.show(settingsFragment)
                transaction.commit()
                toolbarTitle.text = getString(R.string.toolbar_title_settings)
            }
        }
    }

    private fun hideFrag(transaction: FragmentTransaction){
        transaction.hide(todoFragment)
        transaction.hide(settingsFragment)
    }

    private fun setImmerseLayout(view: View){
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
        var height = 0
        if (resourceId>0){
            height = this.resources.getDimensionPixelSize(resourceId)
        }
        view.setPadding(0,height,0,0)
    }

    override fun initLiveData() {}
}
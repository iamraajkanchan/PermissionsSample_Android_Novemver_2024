package com.permissions.chinky.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import com.permissions.chinky.R
import com.permissions.chinky.fragments.LifecycleInfoFragment
import com.permissions.chinky.utility.Utility

class FragmentLifecycleInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Utility.printLog(FragmentLifecycleInfoActivity::class.java, Thread.currentThread().stackTrace[2], "")
        setContentView(R.layout.activity_fragment_lifecycle_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (savedInstanceState == null) {
            loadFragment()
        }
    }

    private fun loadFragment() {
        val lifecycleInfoFragment = LifecycleInfoFragment.newInstance()
        val fcvFragmentContainer = findViewById<FragmentContainerView>(R.id.fcvFragmentContainer)
        supportFragmentManager.beginTransaction().add(fcvFragmentContainer.id, lifecycleInfoFragment, LifecycleInfoFragment::class.java.simpleName).commit()
    }

    override fun onStart() {
        super.onStart()
        Utility.printLog(FragmentLifecycleInfoActivity::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onResume() {
        super.onResume()
        Utility.printLog(FragmentLifecycleInfoActivity::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onPause() {
        super.onPause()
        Utility.printLog(FragmentLifecycleInfoActivity::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onStop() {
        super.onStop()
        Utility.printLog(FragmentLifecycleInfoActivity::class.java, Thread.currentThread().stackTrace[2], "")
    }

    override fun onDestroy() {
        super.onDestroy()
        Utility.printLog(FragmentLifecycleInfoActivity::class.java, Thread.currentThread().stackTrace[2], "")
    }
}
package com.example.admin

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.admin.fragment.LoadImageFragment
import com.example.admin.fragment.LoginFragment
import com.example.admin.fragment.RegisterFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var mBackPressed: Long = 0
    private val mHandler: Handler? = null
    var isLarge = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isLarge = resources.getBoolean(R.bool.isLarge)
        setContentView(R.layout.activity_main)
        if (isLarge) {
            initTablet()
        } else {
            initPhone()
        }
    }

    private fun initPhone() {
        tvRegister.setOnClickListener {
            addFragment(RegisterFragment())
        }

        tvLogin.setOnClickListener {
            addFragment(LoginFragment())
        }
        tvLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }

        tvUpload.setOnClickListener {
            addFragment(LoadImageFragment())
        }
    }

    override fun onBackPressed() {
        if (isLarge) flContent.visibility = View.VISIBLE else lnContent.visibility = View.VISIBLE
        supportFragmentManager.popBackStackImmediate()
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.lnContent) as  Fragment
//        if (currentFragment is RegisterFragment || currentFragment is LoginFragment){
//            if (mBackPressed + 2000 > System.currentTimeMillis()) {
//                try {
//                    Thread.sleep(300)
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//                finishAffinity()
//            } else {
//                Toast.makeText(baseContext, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
//            }
//            mBackPressed = System.currentTimeMillis()
//        } else  supportFragmentManager.popBackStackImmediate()
    }

    private fun initTablet() {
        addFragment(RegisterFragment(),R.id.flContent)
        tvRegister.setOnClickListener {
            addFragment(RegisterFragment(),R.id.flContent)
        }

        tvLogin.setOnClickListener {
            addFragment(LoginFragment(),R.id.flContent)
        }
        tvLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }

    fun addFragment(fragment: Fragment, id: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTran: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTran.replace(id, fragment, fragment.javaClass.simpleName).addToBackStack(fragment.javaClass.simpleName).commit()
    }

    private fun addFragment(fragment: Fragment) {
        lnContent.visibility = View.GONE
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTran: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTran.replace(R.id.lnContent, fragment, fragment.javaClass.simpleName).addToBackStack(fragment.javaClass.simpleName).commit()
    }
}
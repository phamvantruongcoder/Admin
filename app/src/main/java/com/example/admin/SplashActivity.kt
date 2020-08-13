package com.example.admin

import android.content.Intent
import android.os.Handler


class SplashActivity : BaseActivity() {

    override fun initViews() {
        setContentView(R.layout.activity_spalsh)
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        },1000)
    }
}
package com.kos.tailwindbookingapp.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kos.tailwindbookingapp.R
import com.kos.tailwindbookingapp.dialog.LoginDialog

/**
 * Created by schnell on 30,August,2021
 */
class SplashScreen : AppCompatActivity() {

    private var startView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acivity_splashscreen)

        startView = this.findViewById(R.id.start_view)

        startView!!.setOnClickListener {
          /*  val intent = Intent(this@SplashScreen, LoginScreen::class.java);
            startActivity(intent)
            finish()*/

            val loginDialog = LoginDialog()
            loginDialog.show(supportFragmentManager, "Login")
        }

    }
}
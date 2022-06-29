package com.mawinda.refresh_token_sample_app

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.mawinda.refresh_token_sample_app.data.SessionManger
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        viewModel.status.observe(this) {
            Timber.i("Response: $it")
            Timber.i("Access Token: ${SessionManger.accessToken(this)}")
            showBtns()
        }

        findViewById<MaterialButton>(R.id.login_btn).setOnClickListener {
            viewModel.login(mail = "fieldenrono97@gmail.com", password = "string")
        }

        findViewById<MaterialButton>(R.id.refresh_token_btn).setOnClickListener {
            viewModel.refreshToken()
        }

        findViewById<MaterialButton>(R.id.get_data_btn).setOnClickListener {
            viewModel.getData()
        }


    }

    override fun onResume() {
        super.onResume()
        showBtns()
    }

    private fun showBtns() {
        if (SessionManger.accessToken(context = this).isNotBlank()) {
            findViewById<MaterialButton>(R.id.refresh_token_btn).visibility = View.VISIBLE
            findViewById<MaterialButton>(R.id.get_data_btn).visibility = View.VISIBLE
        }
    }
}
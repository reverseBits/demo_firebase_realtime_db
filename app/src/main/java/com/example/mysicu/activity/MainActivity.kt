package com.example.mysicu.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.mysicu.R
import com.example.mysicu.viewModels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get()



    }

}
package com.chai.xiangyang.stickerheader.view

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.chai.xiangyang.stickerheader.R

import kotlinx.android.synthetic.main.activity_sticker.*

class StickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sticker)
        setSupportActionBar(toolbar)


    }

}

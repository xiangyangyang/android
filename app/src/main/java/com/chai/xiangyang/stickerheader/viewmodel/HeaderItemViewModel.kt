package com.chai.xiangyang.stickerheader.viewmodel

import androidx.databinding.ObservableField

class HeaderItemViewModel (val title:String){
    var headerTitle = ObservableField("header")

    init {
        headerTitle.set(title)
    }
}
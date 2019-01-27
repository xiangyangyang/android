package com.chai.xiangyang.groupedgridstickerheaderadapter.viewmodel

import android.databinding.ObservableField

class HeaderItemViewModel (val title:String){
    var headerTitle = ObservableField("header")

    init {
        headerTitle.set(title)
    }
}
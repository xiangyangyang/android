package com.chai.xiangyang.gridstickerheaderlist.viewmodel

import androidx.databinding.ObservableField

class HeaderItemViewModel (val title:String){
    var headerTitle = ObservableField("header")

    init {
        headerTitle.set(title)
    }
}
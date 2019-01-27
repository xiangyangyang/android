package com.chai.xiangyang.groupedgridstickerheaderadapter.viewmodel

import android.databinding.ObservableField

class GroupItemViewModel(val name: String) {
    var title = ObservableField("funeasy")

    init {
        title.set(name)
    }


    fun buttonClicked() {
        title.set("Button clicked!")
    }
}

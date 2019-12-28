package com.chai.xiangyang.stickerheader.viewmodel


import androidx.databinding.ObservableField

class GroupItemViewModel(val name: String, val image: Int) {
    var title = ObservableField<String>()
    var imageId = ObservableField<Int>()

    init {
        title.set(name)
        imageId.set(image)
    }
    fun buttonClicked() {
        title.set("$name clicked!")
    }
}

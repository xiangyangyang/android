package com.chai.xiangyang.gridstickerheaderlist.viewmodel

import android.databinding.ObservableField

class GroupItemViewModel(val name: String) {
    var title = ObservableField("funeasy")
    var imageUrl = ObservableField("")

    init {
        title.set(name)
        imageUrl.set("https://www.google.com/imgres?imgurl=https%3A%2F%2Fmedia.gettyimages.com%2Fphotos%2Fstack-of-books-picture-id157482029%3Fs%3D612x612&imgrefurl=https%3A%2F%2Fwww.gettyimages.co.jp%2F%25E5%2586%2599%25E7%259C%259F%2Fbook&docid=fmmy_cXMQohK0M&tbnid=9SeBVL2f7-InDM%3A&vet=10ahUKEwi678y9zO3iAhVFXrwKHQ2MAJ8QMwhCKAIwAg..i&w=612&h=522&hl=ja&authuser=0&bih=722&biw=1536&q=book%20image&ved=0ahUKEwi678y9zO3iAhVFXrwKHQ2MAJ8QMwhCKAIwAg&iact=mrc&uact=8")
    }


    fun buttonClicked() {
        title.set("Button clicked!")
    }
}

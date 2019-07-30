package com.chai.xiangyang.gridstickerheaderlist.view.utils

import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso


@BindingAdapter(value = ["imageUrl", "error"], requireAll = false)
fun setImage(view: ImageView, url: String, error: Drawable) {
    Picasso.get().load(url).error(error).into(view)
}

@BindingAdapter("action_id","edit_action_listener")
fun setEditActionListener(view: EditText, actionId: Int, searchEvent: (String) -> Unit) {
    // TODO
    (view as TextView).setOnEditorActionListener { textView: TextView, i: Int, keyEvent: KeyEvent ->
        if (EditorInfo.IME_ACTION_SEARCH == actionId) {
            searchEvent(textView.text.toString())
            true
        }
        false
    }
}
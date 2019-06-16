package com.chai.xiangyang.groupedgridstickerheaderadapter.view.utils

import android.databinding.BindingAdapter
import android.databinding.BindingMethod
import android.databinding.BindingMethods
import android.graphics.drawable.Drawable
import android.view.KeyEvent
import android.view.SearchEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

object BindingUtil {

    @BindingAdapter(value =["app:imageUrl", "app:error"],requireAll = false)
    @JvmStatic fun setImage(view: ImageView, url: String, error: Drawable) {
        Picasso.get().load(url).error(error).into(view)
    }

    @BindingAdapter("edit_action_listener")
    @JvmStatic fun setEditActionListener(view: EditText, actionId: Int, searchEvent: (String) -> Unit){
        // TODO
        (view as TextView).setOnEditorActionListener { textView: TextView, i: Int, keyEvent: KeyEvent ->
            if(EditorInfo.IME_ACTION_SEARCH == actionId){
                searchEvent(textView.text.toString())
                true
            }
        false
        }
    }
}
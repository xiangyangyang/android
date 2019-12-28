package com.chai.xiangyang.gridstickerheaderlist.view.utils

import android.graphics.drawable.Drawable
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.chai.xiangyang.gridstickerheaderlist.R
import com.squareup.picasso.Picasso
import java.text.NumberFormat


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

@BindingAdapter("srcId")
fun setImageResourceId(view: ImageView, srcId: Int?) {
    srcId?.let {
        view.setImageResource(srcId)
    }
}

@BindingAdapter("soundUrl")
fun setSoundUrlAndClickListener(view: View, soundUrl: String?) {
    soundUrl?.let {
        val mediaPlayer = MediaPlayerManager.instance
        view.setOnClickListener{
            mediaPlayer.reset()
            mediaPlayer.setDataSource(soundUrl)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    }
}

@BindingAdapter(value = ["textSrc", "prefix", "suffix"], requireAll = false)
fun setFormatStr(view: TextView, src: String?, prefix: String?, suffix: String?) {
    if(!src.isNullOrBlank()){
        var textStr = NumberFormat.getNumberInstance().format(src.toLong())
        if (prefix != null) {
            textStr = prefix + textStr
        }

        if (suffix != null) {
            textStr += suffix
        }

        view.text = textStr
    }
}

@BindingAdapter("source", "format")
fun setStringFormat(view: TextView, source: String?, format: String) {
    if (!source.isNullOrBlank()) {
        view.text = String.format(format, source)
    }
}

@BindingAdapter("editable")
fun setEditable(view: EditText, editable: Boolean) {
    if(editable){
        view.inputType = InputType.TYPE_TEXT_VARIATION_NORMAL or InputType.TYPE_CLASS_TEXT
        view.minWidth = 200
        view.isClickable = true
        view.isFocusableInTouchMode = true
    }else{
        view.isClickable = false
        view.isFocusableInTouchMode = false
        view.background = null
    }
}

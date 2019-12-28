package com.chai.xiangyang.stickerheader.view.utils

import android.media.MediaPlayer

class MediaPlayerManager {
    companion object {
        private var INSTANCE: MediaPlayer? = null

        val instance: MediaPlayer
            get() {
                if (INSTANCE == null) {
                    INSTANCE = MediaPlayer()
                }

                return INSTANCE!!
            }
    }
}
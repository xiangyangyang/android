package com.chai.xiangyang.stickerheader.view.handler

import com.chai.xiangyang.stickerheader.data.entity.BookEntity

interface ViewResultHandler {
    fun resultCompleted(list:List<BookEntity>)
}
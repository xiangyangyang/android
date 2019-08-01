package com.chai.xiangyang.gridstickerheaderlist.view.handler

import com.chai.xiangyang.gridstickerheaderlist.data.entity.BookEntity

interface ViewResultHandler {
    fun resultCompleted(list:List<BookEntity>)
}
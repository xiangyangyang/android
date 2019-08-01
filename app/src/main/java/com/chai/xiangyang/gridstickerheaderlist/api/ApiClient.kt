package com.chai.xiangyang.gridstickerheaderlist.api

import com.chai.xiangyang.gridstickerheaderlist.data.entity.BookEntity
import retrofit2.http.GET
import rx.Observable

interface ApiClient {
    @GET("noteshare?id=e140b5d93f4b1f7d4c57b99defcf32d7")
    fun getBookList(): Observable<List<BookEntity>>
}
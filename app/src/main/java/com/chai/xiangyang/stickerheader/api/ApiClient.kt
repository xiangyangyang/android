package com.chai.xiangyang.stickerheader.api

import com.chai.xiangyang.stickerheader.data.entity.BookEntity
import retrofit2.http.GET
import rx.Observable

interface ApiClient {
    @GET("bookList.json")
    fun getBookList(): Observable<List<BookEntity>>
}
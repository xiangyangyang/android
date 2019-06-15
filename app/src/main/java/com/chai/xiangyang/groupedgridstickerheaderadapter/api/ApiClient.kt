package com.chai.xiangyang.groupedgridstickerheaderadapter.api

import com.chai.xiangyang.groupedgridstickerheaderadapter.data.entity.BookEntity
import retrofit2.http.GET
import rx.Observable

interface ApiClient {
    @GET()
    fun getBookList(): Observable<List<BookEntity>>
}
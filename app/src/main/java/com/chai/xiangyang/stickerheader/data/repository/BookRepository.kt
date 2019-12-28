package com.chai.xiangyang.stickerheader.data.repository

import com.chai.xiangyang.stickerheader.api.ApiClient
import com.chai.xiangyang.stickerheader.api.ApiClientManager
import com.chai.xiangyang.stickerheader.data.entity.BookEntity
import rx.Observable


class BookRepository() {
    private val apiClient: ApiClient= ApiClientManager.apiClient

    fun getBookList(): Observable<List<BookEntity>> {
       return apiClient.getBookList()
    }
}

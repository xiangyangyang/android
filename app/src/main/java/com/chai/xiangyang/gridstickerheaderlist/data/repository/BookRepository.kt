package com.chai.xiangyang.gridstickerheaderlist.data.repository

import com.chai.xiangyang.gridstickerheaderlist.api.ApiClient
import com.chai.xiangyang.gridstickerheaderlist.api.ApiClientManager
import com.chai.xiangyang.gridstickerheaderlist.data.entity.BookEntity
import rx.Observable


class BookRepository {
    private val apiClient: ApiClient= ApiClientManager.apiClient

    fun getBookList(): Observable<List<BookEntity>> {
       return apiClient.getBookList()
    }
}

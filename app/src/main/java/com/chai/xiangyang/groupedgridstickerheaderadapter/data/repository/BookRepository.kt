package com.chai.xiangyang.groupedgridstickerheaderadapter.data.repository

import com.chai.xiangyang.groupedgridstickerheaderadapter.api.ApiClient
import com.chai.xiangyang.groupedgridstickerheaderadapter.api.ApiClientManager
import com.chai.xiangyang.groupedgridstickerheaderadapter.data.entity.BookEntity
import org.koin.java.KoinJavaComponent.inject
import rx.Observable


class BookRepository {
    val apiClient: ApiClient= ApiClientManager.apiClient

    fun getBookList(): Observable<List<BookEntity>> {
       return apiClient.getBookList()
    }
}

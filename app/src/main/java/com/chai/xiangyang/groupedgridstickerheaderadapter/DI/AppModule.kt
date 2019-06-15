package com.chai.xiangyang.groupedgridstickerheaderadapter.DI

import com.chai.xiangyang.groupedgridstickerheaderadapter.api.ApiClient
import com.chai.xiangyang.groupedgridstickerheaderadapter.api.ApiClientManager
import com.chai.xiangyang.groupedgridstickerheaderadapter.data.repository.BookRepository
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    // single instance of HelloRepository
    single<BookRepository> { get() }
}
package com.chai.xiangyang.gridstickerheaderlist.DI

import com.chai.xiangyang.gridstickerheaderlist.data.repository.BookRepository
import org.koin.dsl.module

val appModule = module {
    // single instance of HelloRepository
    single<BookRepository> { get() }
}
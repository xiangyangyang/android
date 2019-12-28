package com.chai.xiangyang.stickerheader.DI

import com.chai.xiangyang.stickerheader.data.repository.BookRepository
import com.chai.xiangyang.stickerheader.viewmodel.MainViewModel
import org.koin.dsl.module

val appModule = module {
    // single instance of BookRepository
    single { BookRepository() }

    factory { MainViewModel(get()) }

}
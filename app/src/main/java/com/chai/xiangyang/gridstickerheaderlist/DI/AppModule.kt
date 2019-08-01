package com.chai.xiangyang.gridstickerheaderlist.DI

import com.chai.xiangyang.gridstickerheaderlist.data.repository.BookRepository
import com.chai.xiangyang.gridstickerheaderlist.viewmodel.MainViewModel
import org.koin.dsl.module

val appModule = module {
    // single instance of BookRepository
    single { BookRepository() }

    factory { MainViewModel(get()) }

}
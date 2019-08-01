package com.chai.xiangyang.gridstickerheaderlist.viewmodel

import android.util.Log
import com.chai.xiangyang.gridstickerheaderlist.data.entity.BookEntity
import com.chai.xiangyang.gridstickerheaderlist.data.repository.BookRepository
import com.chai.xiangyang.gridstickerheaderlist.view.handler.ViewResultHandler
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class MainViewModel (val bookRepository: BookRepository){

   private lateinit var books:List<BookEntity>

   fun getBookList(resultHandler: ViewResultHandler){
        bookRepository.getBookList().subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<List<BookEntity>>() {
                   override fun onNext(list:List<BookEntity>) {
                      resultHandler.resultCompleted(list)
                    }

                    override fun onCompleted() {
                        Log.e("","get books is completed")
                    }

                    override fun onError(e: Throwable) {
                        Log.e("","get books has error")
                    }
                })
   }
}
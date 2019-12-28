package com.chai.xiangyang.stickerheader.viewmodel

import com.chai.xiangyang.stickerheader.R
import com.chai.xiangyang.stickerheader.data.repository.BookRepository


class MainViewModel (val bookRepository: BookRepository){

   fun getBookList():List<Int>{
//        bookRepository.getBookList().subscribeOn(Schedulers.io())
//               .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Subscriber<List<BookEntity>>() {
//                   override fun onNext(list:List<BookEntity>) {
//                      resultHandler.resultCompleted(list)
//                    }
//
//                    override fun onCompleted() {
//                        Log.e("","get books is completed")
//                    }
//
//                    override fun onError(e: Throwable) {
//                        Log.e("","get books has error ${e.localizedMessage}")
//                    }
//                })

       val bookList = ArrayList<Int>()
       bookList.add(R.drawable.book11)
       bookList.add(R.drawable.book12)
       bookList.add(R.drawable.book13)
       bookList.add(R.drawable.book14)
       bookList.add(R.drawable.book14)
       bookList.add(R.drawable.book14)
       bookList.add(R.drawable.book15)
       bookList.add(R.drawable.book21)
       bookList.add(R.drawable.book22)
       bookList.add(R.drawable.book23)
       bookList.add(R.drawable.book23)
       bookList.add(R.drawable.book23)
       bookList.add(R.drawable.book24)
       bookList.add(R.drawable.book31)
       bookList.add(R.drawable.book32)
       bookList.add(R.drawable.book33)
       bookList.add(R.drawable.book34)
       bookList.add(R.drawable.book34)
       bookList.add(R.drawable.book34)
       return bookList;
   }
}
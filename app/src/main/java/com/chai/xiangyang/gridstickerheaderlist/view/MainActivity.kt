package com.chai.xiangyang.gridstickerheaderlist.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chai.xiangyang.gridstickerheaderlist.R
import com.chai.xiangyang.gridstickerheaderlist.data.entity.BookEntity
import com.chai.xiangyang.gridstickerheaderlist.data.repository.BookRepository
import com.chai.xiangyang.gridstickerheaderlist.view.handler.ViewResultHandler
import com.chai.xiangyang.gridstickerheaderlist.viewmodel.MainViewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() ,ViewResultHandler{


    private val mainViewModel:MainViewModel by inject()
    private lateinit var recyclerView:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel.getBookList(this)
        recyclerView=findViewById<RecyclerView>(R.id.my_recycler_view)
        recyclerView.setHasFixedSize(true)

    }

    override fun resultCompleted(list: List<BookEntity>) {
        val viewAdapter = GridStickerHeaderAdapter(this,list)
        val viewManager = GridLayoutManager(this,2).apply {
            spanSizeLookup= object:GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    if(viewAdapter.isHeaderItem(position)||viewAdapter.isBottomItem(position)||viewAdapter.isTopItem(position)){
                        return 2;
                    }else{
                        return 1;
                    }

                }
            }
        }

       recyclerView.layoutManager=viewManager
    }
}

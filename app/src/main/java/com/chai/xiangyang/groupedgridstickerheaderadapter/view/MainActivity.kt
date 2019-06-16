package com.chai.xiangyang.groupedgridstickerheaderadapter.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.chai.xiangyang.groupedgridstickerheaderadapter.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewAdapter = GridStickerHeaderAdapter(this)
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

        val recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)


            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter


        }

        recyclerView.addItemDecoration(HeaderItemDecoration(recyclerView, viewAdapter))
    }
}

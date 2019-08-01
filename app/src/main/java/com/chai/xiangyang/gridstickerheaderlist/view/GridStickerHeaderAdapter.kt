package com.chai.xiangyang.gridstickerheaderlist.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chai.xiangyang.gridstickerheaderlist.R
import com.chai.xiangyang.gridstickerheaderlist.data.entity.BookEntity
import com.chai.xiangyang.gridstickerheaderlist.databinding.BottomViewBinding
import com.chai.xiangyang.gridstickerheaderlist.databinding.GroupItemViewBinding
import com.chai.xiangyang.gridstickerheaderlist.databinding.HeaderItemViewBinding
import com.chai.xiangyang.gridstickerheaderlist.databinding.TopViewBinding
import com.chai.xiangyang.gridstickerheaderlist.viewmodel.BottomViewModel
import com.chai.xiangyang.gridstickerheaderlist.viewmodel.GroupItemViewModel
import com.chai.xiangyang.gridstickerheaderlist.viewmodel.HeaderItemViewModel
import com.chai.xiangyang.gridstickerheaderlist.viewmodel.TopViewModel
import java.util.ArrayList

class GridStickerHeaderAdapter(private val mContext:Context,private val books:List<BookEntity>):RecyclerView.Adapter<GridStickerHeaderAdapter.ItemViewHolder>() {
//    override fun getHeaderPositionForItem(itemPosition: Int): Int {
//        var headerPosition = 0
//        var item = itemPosition
//        do {
//            if (this.isHeader(item)) {
//                headerPosition = item
//                break
//            }
//            item -= 1
//        } while (item >= 0)
//        return headerPosition
//    }
//
//    override fun getHeaderLayout(headerPosition: Int): Int {
//        return R.layout.header_item_view
//    }
//
//    override fun bindHeaderData(header: View?, headerPosition: Int) {
//        if (header != null) {
//        val headerItemViewBinding = DataBindingUtil.getBinding<HeaderItemViewBinding>(header)
//            if(headerItemViewBinding!=null) {
//                headerItemViewBinding.viewModel = HeaderItemViewModel("header$headerPosition")
//                headerItemViewBinding.executePendingBindings()
//            }
//    }
//}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return when (viewType) {
            TOP -> ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.top_view, parent, false))
            ITEM_HEADER -> ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.header_item_view, parent, false))
            BOTTOM -> ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.bottom_view, parent, false))
            else -> ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.group_item_view, parent, false))
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TOP
            1, 12, 23 -> ITEM_HEADER
            34 -> BOTTOM
            else -> ITEM_ITEM
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size + 5
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val viewType = getItemViewType(position)

        when (viewType) {
            TOP -> {
                val topViewBinding = holder.viewDataBinding as TopViewBinding
                topViewBinding.viewModel = TopViewModel()
                topViewBinding.executePendingBindings()
            }
            ITEM_HEADER -> {
                val headerItemViewBinding = holder.viewDataBinding as HeaderItemViewBinding
                headerItemViewBinding.viewModel = HeaderItemViewModel("header$position")
                headerItemViewBinding.executePendingBindings()
            }
            BOTTOM -> {
                val bottomViewBinding = holder.viewDataBinding as BottomViewBinding
                bottomViewBinding.viewModel = BottomViewModel()
                bottomViewBinding.executePendingBindings()
            }
            else -> {
                val itemViewBinding = holder.viewDataBinding as GroupItemViewBinding
                if (position in 2..11) {
                    itemViewBinding.viewModel = GroupItemViewModel(dataSet[position - 2])

                } else if (position in 13..22) {
                    itemViewBinding.viewModel = GroupItemViewModel(dataSet[position - 3])

                } else {
                    itemViewBinding.viewModel = GroupItemViewModel(dataSet[position - 4])
                }
                itemViewBinding.executePendingBindings()
            }
        }

    }

    class ItemViewHolder(var viewDataBinding: ViewDataBinding) : RecyclerView.ViewHolder(viewDataBinding.root)

    fun isHeaderItem(position: Int): Boolean {
        return position == 1 || position == 12 || position == 23
    }

    fun isTopItem(position: Int): Boolean {
        return position == 0
    }

    fun isBottomItem(position: Int): Boolean {
        return position == itemCount - 1
    }

    companion object {

        val dataSet: List<String> = object : ArrayList<String>() {
            init {
                for (i in 0..9) {
                    add("1 group $i")
                }

                for (j in 0..9) {
                    add("2 group $j")
                }

                for (m in 0..9) {
                    add("3 group $m")
                }
            }
        }

        private const val TOP = 0
        private const val ITEM_HEADER = 1
        private const val ITEM_ITEM = 2
        private const val BOTTOM = 3
    }
}



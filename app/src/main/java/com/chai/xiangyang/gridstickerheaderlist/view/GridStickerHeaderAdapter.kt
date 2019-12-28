package com.chai.xiangyang.gridstickerheaderlist.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.chai.xiangyang.gridstickerheaderlist.ItemModel
import com.chai.xiangyang.gridstickerheaderlist.R
import com.chai.xiangyang.gridstickerheaderlist.databinding.BottomViewBinding
import com.chai.xiangyang.gridstickerheaderlist.databinding.GroupItemViewBinding
import com.chai.xiangyang.gridstickerheaderlist.databinding.HeaderItemViewBinding
import com.chai.xiangyang.gridstickerheaderlist.databinding.TopViewBinding
import com.chai.xiangyang.gridstickerheaderlist.viewmodel.BottomViewModel
import com.chai.xiangyang.gridstickerheaderlist.viewmodel.GroupItemViewModel
import com.chai.xiangyang.gridstickerheaderlist.viewmodel.HeaderItemViewModel
import com.chai.xiangyang.gridstickerheaderlist.viewmodel.TopViewModel
import java.util.*

class GridStickerHeaderAdapter(private val mContext: Context, private val books: List<Int>) : RecyclerView.Adapter<GridStickerHeaderAdapter.ItemViewHolder>() {

    private lateinit var itemList: List<ItemModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return when (viewType) {
            TOP -> ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.top_view, parent, false))
            ITEM_HEADER -> ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.header_item_view, parent, false))
            BOTTOM -> ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.bottom_view, parent, false))
            else -> ItemViewHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.group_item_view, parent, false))
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TOP
            isBottomItem(position) -> BOTTOM
            isHeaderItem(position) -> ITEM_HEADER
            else -> ITEM_ITEM
        }
    }

    override fun getItemCount(): Int {
        itemList = getItemDataSet()
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        val itemModel = itemList[position]

        when (viewType) {
            TOP -> {
                val topViewBinding = holder.viewDataBinding as TopViewBinding
                topViewBinding.viewModel = TopViewModel()
            }
            ITEM_HEADER -> {
                val headerItemViewBinding = holder.viewDataBinding as HeaderItemViewBinding
                headerItemViewBinding.viewModel = HeaderItemViewModel(itemModel.groupTitle!!)
            }
            BOTTOM -> {
                val bottomViewBinding = holder.viewDataBinding as BottomViewBinding
                bottomViewBinding.viewModel = BottomViewModel()
            }
            else -> {
                val itemViewBinding = holder.viewDataBinding as GroupItemViewBinding
                itemViewBinding.viewModel = GroupItemViewModel("postition $position",itemModel.imageId!!)
            }
        }

    }

    class ItemViewHolder(var viewDataBinding: ViewDataBinding) : RecyclerView.ViewHolder(viewDataBinding.root)

    fun isHeaderItem(position: Int): Boolean {
        return itemList[position].isHeader
    }

    fun isTopItem(position: Int): Boolean {
        return position == 0
    }

    fun isBottomItem(position: Int): Boolean {
        return position == itemCount - 1
    }

    companion object {

        private const val TOP = 0
        private const val ITEM_HEADER = 1
        private const val ITEM_ITEM = 2
        private const val BOTTOM = 3
    }

    private fun getItemDataSet(): List<ItemModel> {
        val itemList = ArrayList<ItemModel>()
        itemList.add(ItemModel(null, "topSection", true))
        itemList.add(ItemModel(null, "first group", true))
        for (i in 0..6) {
            itemList.add(ItemModel(books[i]))
        }
        itemList.add(ItemModel(null, "second group", true))

        for (i in 7..12) {
            itemList.add(ItemModel(books[i]))
        }
        itemList.add(ItemModel(null, "third group", true))

        for (i in 13..18) {
            itemList.add(ItemModel(books[i]))
        }
        itemList.add(ItemModel(null, "bottomSection", true))

        return itemList;
    }
}



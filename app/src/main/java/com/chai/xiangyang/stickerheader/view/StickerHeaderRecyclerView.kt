package com.chai.xiangyang.stickerheader.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.AbsListView
import androidx.recyclerview.widget.RecyclerView

class StickerHeaderRecyclerView : RecyclerView, AbsListView.OnScrollListener {
    constructor(context: Context) : super(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var headerView: View? = null
    private var headerHeight: Int = 0
    private var headerWidth: Int = 0
    private var headerUpdateListener: OnHeaderUpdateListener? = null

    init {
        initView()
    }

    interface OnHeaderUpdateListener {

        fun getStickyHeader(): View

        fun updateStickyHeader(headerItemPosition: Int)

        fun isHeaderItem(position: Int): Boolean
    }

    fun setOnHeaderUpdateListener(listener: OnHeaderUpdateListener) {
        headerUpdateListener = listener
        headerView = listener.getStickyHeader()
        requestLayout()
        postInvalidate()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        // measure header's widthSpec and heightSpec
        measureChild(headerView, widthSpec, heightSpec)
        headerWidth = headerView?.measuredWidth ?: 0
        headerHeight = headerView?.measuredHeight ?: 0
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        // layout at top position at first time
        headerView?.layout(0, 0, headerWidth, headerHeight)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        drawChild(canvas, headerView, drawingTime)
    }


    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
    }

    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

        if (totalItemCount != 0 && headerView != null && headerUpdateListener != null) {
            var nextHeaderPosition = 0
            if (headerUpdateListener!!.isHeaderItem(firstVisibleItem + 1)) {
                nextHeaderPosition = firstVisibleItem + 1
                val nextHeaderView = getChildAt(nextHeaderPosition)
                val delta = headerHeight.minus(nextHeaderView.top)
                headerView!!.layout(0, -delta, headerWidth, nextHeaderView.top)
            } else {
                headerView!!.layout(0, 0, headerWidth, headerHeight)
            }

            headerUpdateListener!!.updateStickyHeader(nextHeaderPosition)

        }
    }

    private fun initView() {
        setFadingEdgeLength(0)
    }
}
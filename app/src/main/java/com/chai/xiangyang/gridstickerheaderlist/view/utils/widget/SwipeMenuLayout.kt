package jp.co.solxyz.fleeksorm.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import jp.co.solxyz.fleeksorm.R


class SwipeMenuLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ViewGroup(context, attrs, defStyleAttr) {
    private var mScaleTouchSlop: Int = 0
    private var mMaxVelocity: Int = 0   //スライド速度を計算します
    private var mPointerId: Int = 0     //マルチタッチは1本目の指の速度だけです
    private var mHeight: Int = 0    //自分の高さ
    private var mRightMenuWidths: Int = 0   //右メニューの幅の合計(最大スライド距離)

    //スライディング判定臨界値（右メニューの幅の40%）が指を上げた時、展開を超えてしまいました
    private var mLimit: Int = 0

    private var mContentView: View? = null //保存contentView（最初のView）

    //前回のxy
    private val mLastP = PointF()
    //サイドメニューを展開する時は、サイドメニュー以外のエリアをクリックし、サイドメニューを閉じます
    //ブール値変数を追加します。dispatch関数では、ダウンするたびにtrue、moveと判断します。スライド動作ならfalseとして設定します
    //Intercept関数のupにおいて、この変数を判断し、trueにイベントをクリックしたと説明した場合は、メニューを閉じます
    private var isUnMoved = true

    //指の始点を判断します。距離がスライドしたら、すべてのクリックを遮断します
    //up-downの座標は、スライドかどうかを判断します
    private val mFirstP = PointF()
    private var isUserSwiped: Boolean = false

    private var mVelocityTracker: VelocityTracker? = null   //スライド速度変数


    var isSwipeEnable: Boolean = false


    private var isIos: Boolean = false

    private var iosInterceptFlag: Boolean = false


    private var isLeftSwipe: Boolean = false

    /**
     * 滑らかな展開
     */
    private var mExpandAnim: ValueAnimator? = null
    private var mCloseAnim: ValueAnimator? = null

    private var isExpand: Boolean = false   //現在は展開状態ですか

    init {
        init(context, attrs, defStyleAttr)
    }


    fun isIos(): Boolean {
        return isIos
    }


    fun setIos(ios: Boolean): SwipeMenuLayout {
        isIos = ios
        return this
    }

    fun isLeftSwipe(): Boolean {
        return isLeftSwipe
    }

    /**
     * 左スライドアウトメニューを開くかどうかを設定し、falseを右スライドメニューに設定します
     *
     * @param leftSwipe
     * @return
     */
    fun setLeftSwipe(leftSwipe: Boolean): SwipeMenuLayout {
        isLeftSwipe = leftSwipe
        return this
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        mScaleTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mMaxVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity

        isSwipeEnable = true

        isIos = true

        isLeftSwipe = true
        val ta = context.theme.obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout, defStyleAttr, 0)
        val count = ta.indexCount
        for (i in 0 until count) {
            when (val attr = ta.getIndex(i)) {
                R.styleable.SwipeMenuLayout_swipeEnable -> isSwipeEnable = ta.getBoolean(attr, true)
                R.styleable.SwipeMenuLayout_ios -> isIos = ta.getBoolean(attr, true)
                R.styleable.SwipeMenuLayout_leftSwipe -> isLeftSwipe = ta.getBoolean(attr, true)
            }
        }
        ta.recycle()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        isClickable = true

        mRightMenuWidths = 0
        mHeight = 0
        var contentWidth = 0
        val childCount = childCount

        val measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY
        var isNeedMeasureChildHeight = false

        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            //各サブViewをクリックして、タッチイベントを取得することができます
            childView.isClickable = true
            if (childView.visibility != View.GONE) {
                measureChild(childView, widthMeasureSpec, heightMeasureSpec)
                val lp = childView.layoutParams as MarginLayoutParams
                mHeight = Math.max(mHeight, childView.measuredHeight)
                if (measureMatchParentChildren && lp.height == LayoutParams.MATCH_PARENT) {
                    isNeedMeasureChildHeight = true
                }
                if (i > 0) {    //第一のレイアウトはLeft itemで、第二からはRightMenuです
                    mRightMenuWidths += childView.measuredWidth
                } else {
                    mContentView = childView
                    contentWidth = childView.measuredWidth
                }
            }
        }
        setMeasuredDimension(
            paddingLeft + paddingRight + contentWidth,
            mHeight + paddingTop + paddingBottom
        )
        mLimit = mRightMenuWidths * 4 / 10  //スライディング判定の臨界値
        if (isNeedMeasureChildHeight) {
            forceUniformHeight(childCount, widthMeasureSpec)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    /**
     * Match Partentのサブビューに高さを設定します
     *
     * @param count
     * @param widthMeasureSpec
     */
    private fun forceUniformHeight(count: Int, widthMeasureSpec: Int) {
        // Pretend that the linear layout has an exact size. This is the measured height of
        // ourselves. The measured height should be the max height of the children, changed
        // to accommodate the heightMeasureSpec from the parent
        val uniformMeasureSpec = MeasureSpec.makeMeasureSpec(
            measuredHeight,
            MeasureSpec.EXACTLY
        )   //親レイアウトの高さでExactlyの測定パラメータを構築します
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                val lp = child.layoutParams as MarginLayoutParams
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    val oldWidth = lp.width
                    lp.width = child.measuredWidth
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0)
                    lp.width = oldWidth
                }
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childCount = childCount
        var left = 0 + paddingLeft
        var right = 0 + paddingLeft
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView.visibility != View.GONE) {
                if (i == 0) {   //最初のサブViewは、コンテンツの幅をフルスクリーンに設定します
                    childView.layout(
                        left,
                        paddingTop,
                        left + childView.measuredWidth,
                        paddingTop + childView.measuredHeight
                    )
                    left += childView.measuredWidth
                } else {
                    if (isLeftSwipe) {
                        childView.layout(
                            left,
                            paddingTop,
                            left + childView.measuredWidth,
                            paddingTop + childView.measuredHeight
                        )
                        left += childView.measuredWidth
                    } else {
                        childView.layout(
                            right - childView.measuredWidth,
                            paddingTop,
                            right,
                            paddingTop + childView.measuredHeight
                        )
                        right -= childView.measuredWidth
                    }

                }
            }
        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (isSwipeEnable) {
            acquireVelocityTracker(ev)
            val verTracker = mVelocityTracker
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    isUserSwiped = false
                    isUnMoved = true
                    iosInterceptFlag = false
                    if (isTouching) {
                        return false
                    } else {
                        isTouching = true
                    }
                    mLastP.set(ev.rawX, ev.rawY)
                    mFirstP.set(ev.rawX, ev.rawY)

                    if (viewCache != null) {
                        if (viewCache !== this) {
                            viewCache!!.smoothClose()

                            iosInterceptFlag = isIos
                        }
                        //サイドメニューが一つあれば、外側のレイアウトを上下にスライドさせません
                        //getParent().requestDisallowInterceptTouchEvent(true);
                    }

                    mPointerId = ev.getPointerId(0)
                }
                MotionEvent.ACTION_MOVE -> {
                    if (!iosInterceptFlag) {
                        val gap = mLastP.x - ev.rawX
                        //水平スライドの中で父ListViewなどの縦スライドを禁止するために
                        if (Math.abs(gap) > 10 || Math.abs(scrollX) > 10) {
                            //getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        //サイドメニューを展開する時は、コンテンツエリアをクリックし、サイドメニューを閉じます。begin
                        if (Math.abs(gap) > mScaleTouchSlop) {
                            isUnMoved = false
                        }
                        //サイドメニューを展開する時は、コンテンツエリアをクリックし、サイドメニューを閉じます。end
                        scrollBy(gap.toInt(), 0)

                        //境界修正
                        if (isLeftSwipe) {  //左滑り
                            if (scrollX < 0) {
                                scrollTo(0, 0)
                            }
                            if (scrollX > mRightMenuWidths) {
                                scrollTo(mRightMenuWidths, 0)
                            }
                        } else {    //右滑り
                            if (scrollX < -mRightMenuWidths) {
                                scrollTo(-mRightMenuWidths, 0)
                            }
                            if (scrollX > 0) {
                                scrollTo(0, 0)
                            }
                        }

                        mLastP.set(ev.rawX, ev.rawY)
                    }

                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (Math.abs(ev.rawX - mFirstP.x) > mScaleTouchSlop) {
                        isUserSwiped = true
                    }
                    if (!iosInterceptFlag) {
                        //擬似瞬間速度を求めます
                        verTracker!!.computeCurrentVelocity(1000, mMaxVelocity.toFloat())
                        val velocityX = verTracker.getXVelocity(mPointerId)
                        if (Math.abs(velocityX) > 1000) {   //スライド速度が閾値を超える
                            if (velocityX < -1000) {
                                if (isLeftSwipe) {
                                    smoothExpand()
                                } else {
                                    smoothClose()
                                }
                            } else {
                                if (isLeftSwipe) {
                                    smoothClose()
                                } else {
                                    smoothExpand()
                                }
                            }
                        } else {
                            if (Math.abs(scrollX) > mLimit) {
                                smoothExpand()
                            } else {
                                smoothClose()
                            }
                        }
                    }
                    //釈放する
                    releaseVelocityTracker()
                    isTouching = false
                }
                else -> {
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        //横滑り禁止の場合は、イベントをクリックしても邪魔されません
        if (isSwipeEnable) {
            when (ev.action) {

                // fix 長押し事件と横滑りの衝突
                MotionEvent.ACTION_MOVE ->
                    //スライド時のイベントをブロック
                    if (Math.abs(ev.rawX - mFirstP.x) > mScaleTouchSlop) {
                        return true
                    }
                MotionEvent.ACTION_UP -> {
                    //横滑り時にシールドビューをクリックするためのイベントです
                    if (isLeftSwipe) {
                        if (scrollX > mScaleTouchSlop) {
                            if (ev.x < width - scrollX) {
                                if (isUnMoved) {
                                    smoothClose()
                                }
                                return true
                            }
                        }
                    } else {
                        if (-scrollX > mScaleTouchSlop) {
                            if (ev.x > -scrollX) {
                                if (isUnMoved) {
                                    smoothClose()
                                }
                                return true
                            }
                        }
                    }
                    if (isUserSwiped) {
                        return true
                    }
                }
            }
            if (iosInterceptFlag) {
                return true
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    fun smoothExpand() {
        viewCache = this@SwipeMenuLayout

        //サイドスライドメニューを展開し、ブロックを長押します
        if (null != mContentView) {
            mContentView!!.isLongClickable = false
        }
        cancelAnim()
        mExpandAnim = ValueAnimator.ofInt(scrollX, if (isLeftSwipe) mRightMenuWidths else -mRightMenuWidths)
        mExpandAnim!!.addUpdateListener { animation -> scrollTo(animation.animatedValue as Int, 0) }
        mExpandAnim!!.interpolator = OvershootInterpolator()
        mExpandAnim!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isExpand = true
            }
        })
        mExpandAnim!!.setDuration(300).start()
    }

    private fun cancelAnim() {
        if (mCloseAnim != null && mCloseAnim!!.isRunning) {
            mCloseAnim!!.cancel()
        }
        if (mExpandAnim != null && mExpandAnim!!.isRunning) {
            mExpandAnim!!.cancel()
        }
    }


    fun smoothClose() {
        viewCache = null
        if (null != mContentView) {
            mContentView!!.isLongClickable = true
        }
        cancelAnim()
        mCloseAnim = ValueAnimator.ofInt(scrollX, 0)
        mCloseAnim!!.addUpdateListener { animation -> scrollTo(animation.animatedValue as Int, 0) }
        mCloseAnim!!.interpolator = AccelerateInterpolator()
        mCloseAnim!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isExpand = false

            }
        })
        mCloseAnim!!.setDuration(300).start()
    }


    private fun acquireVelocityTracker(event: MotionEvent) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
    }

    private fun releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker!!.clear()
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    override fun onDetachedFromWindow() {
        if (this === viewCache) {
            viewCache!!.smoothClose()
            viewCache = null
        }
        super.onDetachedFromWindow()
    }

    override fun performLongClick(): Boolean {
        return if (Math.abs(scrollX) > mScaleTouchSlop) {
            false
        } else super.performLongClick()
    }



    fun quickClose() {
        if (this === viewCache) {
            cancelAnim()
            viewCache!!.scrollTo(0, 0)
            viewCache = null
        }
    }

    companion object {

        var viewCache: SwipeMenuLayout? = null
            private set

        private var isTouching: Boolean = false
    }

}

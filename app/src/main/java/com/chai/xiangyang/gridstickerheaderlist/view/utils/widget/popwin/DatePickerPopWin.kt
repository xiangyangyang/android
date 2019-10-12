package jp.co.solxyz.fleeksorm.widget.popwin

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.core.content.ContextCompat
import jp.co.solxyz.fleeksorm.R
import jp.co.solxyz.fleeksorm.utils.PopFullScreenUtil
import jp.co.solxyz.fleeksorm.widget.wheel.LoopScrollListener
import jp.co.solxyz.fleeksorm.widget.wheel.LoopView
import java.util.*

/**
 * PopWindow for Date Pick
 */
class DatePickerPopWin(builder: Builder) : PopupWindow(), OnClickListener {
    private lateinit var cancelBtn: TextView
    private lateinit var confirmBtn: TextView
    private lateinit var tvTitle: TextView
    
    private lateinit var yearLoopView: LoopView
    private lateinit var monthLoopView: LoopView
    private lateinit var dayLoopView: LoopView
    
    private lateinit var startYearLoopView: LoopView
    private lateinit var startMonthLoopView: LoopView
    private lateinit var endYearLoopView: LoopView
    private lateinit var endMonthLoopView: LoopView

    private var isInterval : Boolean = false       //区間選択
    
    private lateinit var container_toolbar: RelativeLayout
    private lateinit var rl_container: RelativeLayout
    private lateinit var ll_date_content: LinearLayout
    private lateinit var ll_date_interval_content: LinearLayout
    private lateinit var pickerContainerV: View
    private lateinit var contentV : View

    private val minYear: Int
    private val maxYear: Int
    private var yearPos = 0
    private var monthPos = 0
    private val title: String
    private var dayPos = 0
    private var startYearPos = 0
    private var startMonthPos = 0
    private var endYearPos = 0
    private var endMonthPos = 0
    private val mContext: Context?
    private val textCancel: String
    private val textConfirm: String
    private val colorToolBar: Int
    private val colorContent: Int
    private val colorCancel: Int
    private val colorConfirm: Int
    private val btnTextSize: Int
    private val loopViewTextSize : Int
    private val isShowDay : Boolean

    private var yearList: ArrayList<String> = ArrayList()
    private var monthList: ArrayList<String> = ArrayList()
    private var dayList: ArrayList<String> = ArrayList()

    private var onDatePickedListener: OnDatePickedListener? = null
    private var onDateIntervalPickedListener : OnDateIntervalPickedListener? = null

    class Builder(context: Context){
        val context: Context = context
        var onDatePickedListener: OnDatePickedListener? = null
        var onDateIntervalPickedListener: OnDateIntervalPickedListener? = null

        constructor(context: Context,onDatePickedListener: OnDatePickedListener) : this(context){
            this.onDatePickedListener = onDatePickedListener
        }

        constructor(context: Context,onDateIntervalPickedListener: OnDateIntervalPickedListener) : this(context){
            this.onDateIntervalPickedListener = onDateIntervalPickedListener
        }

        //Option
        var minYear = DEFAULT_MIN_YEAR
        var maxYear = Calendar.getInstance().get(Calendar.YEAR) + 1
        var textCancel: String = context.getString(R.string.cancel)
        var textConfirm: String = context.getString(R.string.confirm)
        var colorCancel = ContextCompat.getColor(context,R.color.white)
        var colorConfirm = ContextCompat.getColor(context,R.color.white)
        var colorToolBar = ContextCompat.getColor(context,R.color.colorPrimary)
        var colorContent = ContextCompat.getColor(context,R.color.gray_300)
        var btnTextSize = 16
        var loopViewTextSize = 16
        var tvTitle: String = context.getString(R.string.employee_detail_date_picker_title)
        var yearPos = 0
        var monthPos = 0
        var dayPos = 0
        var startYearPos = 0
        var startMonthPos = 0
        var endYearPos = 0
        var endMonthPos = 0
        var isInterval = false
        var isShowDay = true

        fun setDateTextSize(size : Int) : Builder{
            this.loopViewTextSize = size
            return this
        }

        fun setButtonTextSize(size : Int) : Builder{
            this.btnTextSize = size
            return this
        }

        fun setYear(year : String?) : Builder{
            year?.let {
                if(year.isBlank()){
                    this.yearPos = Calendar.getInstance().get(Calendar.YEAR) - minYear
                }else{
                    this.yearPos = it.toInt() - minYear
                }

            }
            return this
        }

        fun setMonth(month : String?) : Builder{
            month?.let {
                if(month.isBlank()){
                    this.monthPos = Calendar.getInstance().get(Calendar.MONTH)
                }else{
                    this.monthPos = it.toInt() - 1
                }
            }
            return this
        }

        fun setDay(day : String?) : Builder{
            day?.let{
                if(day.isBlank()){
                    this.dayPos = Calendar.getInstance().get(Calendar.DATE) - 1
                }else{
                    this.dayPos = it.toInt() - 1
                }
            }
            return this
        }

        fun setStartYear(startYear : String?) : Builder{
            startYear?.let {
                if(startYear.isBlank()){
                    this.startYearPos = Calendar.getInstance().get(Calendar.YEAR) - minYear
                }else{
                    this.startYearPos = it.toInt() - minYear
                }

            }
            return this
        }

        fun setStartMonth(startMonth : String?) : Builder{
            startMonth?.let {
                if(startMonth.isBlank()){
                    this.startMonthPos = Calendar.getInstance().get(Calendar.MONTH)
                }else{
                    this.startMonthPos = it.toInt() - 1
                }
            }
            return this
        }

        fun setEndYear(endYear : String?) : Builder{
            endYear?.let {
                if(endYear.isBlank()){
                    this.endYearPos = Calendar.getInstance().get(Calendar.YEAR) - minYear
                }else{
                    this.endYearPos = it.toInt() - minYear
                }
            }
            return this
        }

        fun setEndMonth(endMonth : String?) : Builder{
            endMonth?.let {
                if(endMonth.isBlank()){
                    this.endMonthPos = Calendar.getInstance().get(Calendar.MONTH)
                }else{
                    this.endMonthPos = it.toInt() - 1
                }
            }
            return this
        }

        fun setIsInterval(isInterval : Boolean) : Builder{
            this.isInterval = isInterval
            return this
        }

        fun setIsShowDay(isShowDay : Boolean) : Builder{
            this.isShowDay = isShowDay
            return this
        }

        fun minYear(minYear: Int): Builder {
            this.minYear = minYear
            return this
        }

        fun maxYear(maxYear: Int): Builder {
            this.maxYear = maxYear
            return this
        }

        fun textCancel(textCancel: String): Builder {
            this.textCancel = textCancel
            return this
        }

        fun colorToolBar(color: Int): Builder {
            this.colorToolBar = color
            return this
        }

        fun colorContent(color: Int): Builder {
            this.colorContent = color
            return this
        }

        fun textConfirm(textConfirm: String): Builder {
            this.textConfirm = textConfirm
            return this
        }

        fun setTvTitle(tvTitle: String): Builder {
            this.tvTitle = tvTitle
            return this
        }

        fun colorCancel(colorCancel: Int): Builder {
            this.colorCancel = colorCancel
            return this
        }

        fun colorConfirm(colorConfirm: Int): Builder {
            this.colorConfirm = colorConfirm
            return this
        }
        fun btnTextSize(textSize: Int): Builder {
            this.btnTextSize = textSize
            return this
        }

        fun build(): DatePickerPopWin {
            if (minYear > maxYear) {
                throw IllegalArgumentException()
            }
            return DatePickerPopWin(this)
        }
    }

    init {
        this.minYear = builder.minYear
        this.maxYear = builder.maxYear
        this.textCancel = builder.textCancel
        this.textConfirm = builder.textConfirm
        this.mContext = builder.context
        this.onDatePickedListener = builder.onDatePickedListener
        this.onDateIntervalPickedListener = builder.onDateIntervalPickedListener
        this.colorCancel = builder.colorCancel
        this.colorConfirm = builder.colorConfirm
        this.btnTextSize = builder.btnTextSize
        this.loopViewTextSize = builder.loopViewTextSize
        this.yearPos = builder.yearPos
        this.monthPos = builder.monthPos
        this.dayPos = builder.dayPos
        this.startYearPos = builder.startYearPos
        this.startMonthPos = builder.startMonthPos
        this.endYearPos = builder.endYearPos
        this.isInterval = builder.isInterval
        this.endMonthPos = builder.endMonthPos
        this.title = builder.tvTitle
        this.colorToolBar = builder.colorToolBar
        this.colorContent = builder.colorContent
        this.isShowDay = builder.isShowDay
        initView()
    }

    private fun initView() {
        contentV = LayoutInflater.from(mContext).inflate(R.layout.layout_date_picker, null)
        container_toolbar = contentV.findViewById(R.id.container_toolbar)
        rl_container = contentV.findViewById(R.id.rl_container)
        container_toolbar.setBackgroundColor(colorToolBar)
        rl_container.setBackgroundColor(colorContent)
        contentV.let {
            cancelBtn = it.findViewById(R.id.cancel)
            tvTitle = it.findViewById(R.id.title)
            confirmBtn = it.findViewById(R.id.confirm)
            pickerContainerV = it.findViewById(R.id.container_picker)
            ll_date_content = it.findViewById(R.id.ll_date_picker_content)
            ll_date_interval_content = it.findViewById(R.id.ll_date_interval_content)
            startYearLoopView = it.findViewById(R.id.picker_start_year) as LoopView
            startMonthLoopView = it.findViewById(R.id.picker_start_month) as LoopView
            endYearLoopView = it.findViewById(R.id.picker_end_year) as LoopView
            endMonthLoopView = it.findViewById(R.id.picker_end_month) as LoopView
            yearLoopView = it.findViewById(R.id.picker_year) as LoopView
            monthLoopView = it.findViewById(R.id.picker_month) as LoopView
            dayLoopView = it.findViewById(R.id.picker_day) as LoopView
            setLoopViewStyle()
            if(isInterval){

                initPickerViewsForInterval()
                startYearLoopView.setLoopListener(object : LoopScrollListener{
                    override fun onItemSelect(item: Int) {
                        startYearPos = item
                    }
                })
                endYearLoopView.setLoopListener(object : LoopScrollListener{
                    override fun onItemSelect(item: Int) {
                        endYearPos = item
                    }
                })
                startMonthLoopView.setLoopListener(object : LoopScrollListener{
                    override fun onItemSelect(item: Int) {
                        startMonthPos = item
                    }
                })
                endMonthLoopView.setLoopListener(object : LoopScrollListener{
                    override fun onItemSelect(item: Int) {
                        endMonthPos = item
                    }
                })
            }else{
                initPickerViews()
                initDayPickerView()
                yearLoopView.setLoopListener(object : LoopScrollListener{
                    override fun onItemSelect(item: Int) {
                        yearPos = item
                        initDayPickerView()
                    }
                })
                monthLoopView.setLoopListener(object : LoopScrollListener{
                    override fun onItemSelect(item: Int) {
                        monthPos = item
                        initDayPickerView()
                    }
                })
                dayLoopView.setLoopListener(object : LoopScrollListener{
                    override fun onItemSelect(item: Int) {
                        dayPos = item
                    }
                })
            }
        }
        setLoopViewStyle()
        cancelBtn.setOnClickListener(this)
        confirmBtn.setOnClickListener(this)
        contentV.setOnClickListener(this)
        isTouchable = true
        isFocusable = true
        isClippingEnabled = false//7.0シャドウがステータスバーの問題をカバーしていません
        contentView = contentV
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    private fun initPickerViews() {
        val yearCount = maxYear - minYear

        for (i in 0 until yearCount) {
            yearList.add(format2LenStr(minYear + i) + "年")
        }

        for (j in 0..11) {
            monthList.add(format2LenStr(j + 1) + "月")

        }
        yearLoopView.setDataList(yearList)
        yearLoopView.setInitPosition(yearPos)

        monthLoopView.setDataList(monthList)
        monthLoopView.setInitPosition(monthPos)
    }

    private fun initDayPickerView() {
        val dayMaxInMonth: Int
        val calendar = Calendar.getInstance()
        dayList = ArrayList()
        calendar.set(Calendar.YEAR, minYear + yearPos)
        calendar.set(Calendar.MONTH, monthPos)
        //get max day in month
        dayMaxInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 0 until dayMaxInMonth) {
            dayList.add(format2LenStr(i + 1) + "日")
        }
        dayLoopView.setInitPosition(dayPos)
        dayLoopView.setDataList(dayList)
    }

    private fun initPickerViewsForInterval() {

        val yearCount = maxYear - minYear
        for (i in 0 until yearCount) {
            yearList.add(format2LenStr(minYear + i) + "年")
        }

        for (j in 0..11) {
            monthList.add(format2LenStr(j + 1) + "月")

        }

        startYearLoopView.setDataList(yearList)
        startYearLoopView.setInitPosition(startYearPos)
        startMonthLoopView.setDataList(monthList)
        startMonthLoopView.setInitPosition(startMonthPos)

        endYearLoopView.setDataList(yearList)
        endYearLoopView.setInitPosition(endYearPos)
        endMonthLoopView.setDataList(monthList)
        endMonthLoopView.setInitPosition(endMonthPos)
    }

    fun showPopWin(context : Context) {
        val trans = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
            0f, Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        showAtLocation(
            (context as Activity).window.decorView, Gravity.BOTTOM,
            0, PopFullScreenUtil.getNavigationBarHeightIfRoom(mContext!!)
        )
        trans.duration = 400
        trans.interpolator = AccelerateDecelerateInterpolator()

        pickerContainerV.startAnimation(trans)
    }

    private fun dismissPopWin() {

        val trans = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f
        )

        trans.duration = 400
        trans.interpolator = AccelerateInterpolator()
        trans.setAnimationListener(object : AnimationListener {

            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {

                dismiss()
            }
        })

        pickerContainerV.startAnimation(trans)
    }

    override fun onClick(v: View) {

        if (v === contentV || v === cancelBtn) {

            dismissPopWin()
        } else if (v === confirmBtn) {
            val calender = Calendar.getInstance()

            if(!isInterval){
                val year = minYear + yearPos
                val month = monthPos + 1
                val day = dayPos + 1
                var isValidate = true
                if(year > calender.get(Calendar.YEAR)){
                    isValidate = false
                }
                if(year >= calender.get(Calendar.YEAR) && month > calender.get(Calendar.MONTH) + 1){
                    isValidate = false
                }
                if(year >= calender.get(Calendar.YEAR) && month >= calender.get(Calendar.MONTH) + 1 && day > calender.get(Calendar.DATE)){
                    isValidate = false
                }
                if(isValidate){
                    if(isShowDay){
                        onDatePickedListener?.onDatePickCompleted(year, month, day,"$year/${format2LenStr(month)}/${format2LenStr(day)}")
                    }else{
                        onDatePickedListener?.onDatePickCompleted(year, month, day,"$year/${format2LenStr(month)}")
                    }
                    dismissPopWin()
                }else{
                    onDatePickedListener?.onDateError()
                }


            }else{
                val startYear = minYear + startYearPos
                val endYear = minYear + endYearPos
                val startMonth = startMonthPos + 1
                val endMonth = endMonthPos + 1
                var isValidate = true
                if(startYear > calender.get(Calendar.YEAR)){
                    isValidate = false
                }
                if(startYear >= calender.get(Calendar.YEAR) && startMonth > calender.get(Calendar.MONTH) + 1){
                    isValidate = false
                }
                if(endYear > calender.get(Calendar.YEAR)){
                    isValidate = false
                }
                if(endYear >= calender.get(Calendar.YEAR) && endMonth > calender.get(Calendar.MONTH) + 1){
                    isValidate = false
                }
                if(isValidate){
                    val dateDesc = "$startYear / ${format2LenStr(startMonth)}  ~  $endYear / ${format2LenStr(endMonth)}"
                    onDateIntervalPickedListener?.onDateIntervalCompleted(startYear,endYear,startMonth,endMonth,dateDesc)
                    dismissPopWin()
                }else{
                    onDateIntervalPickedListener?.onDateError()
                }

            }
        }
    }

    private fun setLoopViewStyle(){
        cancelBtn.setTextColor(colorCancel)
        cancelBtn.textSize = btnTextSize.toFloat()
        cancelBtn.text = textCancel
        confirmBtn.setTextColor(colorConfirm)
        confirmBtn.textSize = btnTextSize.toFloat()
        confirmBtn.text = textConfirm
        yearLoopView.setTextSize(loopViewTextSize.toFloat())
        monthLoopView.setTextSize(loopViewTextSize.toFloat())
        dayLoopView.setTextSize(loopViewTextSize.toFloat())
        startYearLoopView.setTextSize(loopViewTextSize.toFloat())
        startMonthLoopView.setTextSize(loopViewTextSize.toFloat())
        endYearLoopView.setTextSize(loopViewTextSize.toFloat())
        endMonthLoopView.setTextSize(loopViewTextSize.toFloat())
        if(isInterval){
            ll_date_content.visibility = View.GONE
            ll_date_interval_content.visibility = View.VISIBLE
        }else{
            ll_date_content.visibility = View.VISIBLE
            ll_date_interval_content.visibility = View.GONE
            if(isShowDay){
                dayLoopView.visibility = View.VISIBLE
            }else{
                dayLoopView.visibility = View.GONE
            }
        }
    }


    companion object {
        private const val DEFAULT_MIN_YEAR = 1900
        /**
         * Transform int to String with prefix "0" if less than 10
         * @param num
         * @return
         */
        fun format2LenStr(num: Int): String {

            return if (num < 10) "0$num" else num.toString()
        }
    }


}

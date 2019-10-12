package jp.co.solxyz.fleeksorm.widget.popwin

interface OnDateIntervalPickedListener {

    fun onDateIntervalCompleted(
        startYear : Int,endYear : Int,startMonth : Int,endMonth : Int,
        dateDesc : String
    )

    fun onDateError()
}
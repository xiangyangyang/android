package jp.co.solxyz.fleeksorm.widget.popwin

interface OnDatePickedListener {
    /**
     * @param dateDesc YYYY/MM/DD
     */
    fun onDatePickCompleted(
        year: Int, month: Int, day: Int,
        dateDesc: String
    )

    fun onDateError()
}
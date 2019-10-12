package jp.co.solxyz.fleeksorm.utils

import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private fun getDeviceTimeOfYMD() : Array<Int>{
        return arrayOf(
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DATE))

    }

    private fun transformDateToLong(dateString : String) : Long{
        return SimpleDateFormat("yyyy/MM/dd").parse(dateString).time
    }

    private fun transformDateFromStringToIntArray(dateString : String) : Array<Int>{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = transformDateToLong(dateString)
        return arrayOf(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE))
    }

//    fun showDatePicker(
//        context : Context,
//        date : String?,
//        employeeDetailEditDatePickerHandler: EmployeeDetailEditDatePickerHandler
//    ){
//        var dateArray : Array<Int> = if(date.isNullOrEmpty()){
//            getDeviceTimeOfYMD()
//        }else{
//            transformDateFromStringToIntArray(date)
//        }
//        val datePickerDialog = DatePickerDialog(context,
//            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
//                employeeDetailEditDatePickerHandler.onConfirm(year,month + 1,day,"$year/${format2LenStr(month + 1)}/${format2LenStr(day)}")
//            },dateArray[0],dateArray[1],dateArray[2])
//        datePickerDialog.setOnCancelListener {
//            employeeDetailEditDatePickerHandler.onCancel()
//        }
//        datePickerDialog.show()
//    }

    /**
     * Transform int to String with prefix "0" if less than 10
     * @param num
     */
    fun format2LenStr(num: Int): String {

        return if (num < 10) "0$num" else num.toString()
    }
}
package jp.co.solxyz.fleeksorm.utils


import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object FileUtil {
    private val rootFolderPath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "FleeksormPhoto"

    fun createImageFile(isCrop: Boolean = false): File? {
            var rootFile = File(rootFolderPath + File.separator + "capture")
            if (!rootFile.exists())
                rootFile.mkdirs()
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = if (isCrop) "IMG_${timeStamp}_CROP.jpg" else "IMG_$timeStamp.jpg"
            return File(rootFile.absolutePath + File.separator + fileName)
    }
}
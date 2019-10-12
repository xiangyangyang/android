package jp.co.solxyz.fleeksorm.utils

import android.Manifest
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker
import android.util.Base64
import android.widget.Toast
import com.chai.xiangyang.gridstickerheaderlist.R
import java.io.ByteArrayOutputStream
import java.io.File

class PhotoUtil(
        private val activity: Activity?,
        private val fragment: Fragment?,
        private val pictureSelectListener: PictureSelectListener
) {

    companion object {
        const val REQUEST_CODE_ALBUM = 1001
        const val REQUEST_CODE_CAPTURE_CROP = 1002
        const val REQUEST_CODE_PERMISSION = 1003
        var imageCropFile: File? = null
    }

    interface PictureSelectListener {
        fun onPictureSelect(image: String)

        fun onPictureError(e: Exception)
    }

    //システムアルバムを開く
    private fun gotoGallery() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if(fragment == null){
            activity?.startActivityForResult(intent, REQUEST_CODE_ALBUM)
        }else{
            fragment.startActivityForResult(intent, REQUEST_CODE_ALBUM)
        }

    }
    fun selectPhoto() {
        if (checkPermission()) {
            gotoGallery()
        }
    }

    //切り取り
    private fun gotoCrop(sourceUri: Uri) {
        imageCropFile = FileUtil.createImageFile(true)
        imageCropFile?.let {
            val intent = Intent("com.android.camera.action.CROP")
            intent.putExtra("crop", "true")
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            intent.putExtra("outputX", 500)
            intent.putExtra("outputY", 500)
            intent.putExtra("scale ", true)
            intent.putExtra("return-data", false)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.setDataAndType(sourceUri, "image/*")

                var imgCropUri = Uri.fromFile(it)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgCropUri)
            } else {
                intent.setDataAndType(sourceUri, "image/*")
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(it))
            }
            if(fragment == null){
                activity?.startActivityForResult(intent, REQUEST_CODE_CAPTURE_CROP)
            }else{
                fragment.startActivityForResult(intent, REQUEST_CODE_CAPTURE_CROP)
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_ALBUM -> {
                    data?.data?.let {
                        gotoCrop(it)
                        return
                    }
                    pictureSelectListener.onPictureError(NullPointerException((if (fragment == null) activity else fragment.activity)?.getString(R.string.app_name)))

                }

                REQUEST_CODE_CAPTURE_CROP -> {
                    imageCropFile?.absolutePath?.let {
                        val base64String = getBase64FromBitmap(getBitmapFromFile(it))
                        if(base64String == "-1"){
                            pictureSelectListener.onPictureError(NullPointerException((if (fragment == null) activity else fragment.activity)?.getString(R.string.app_name)))
                        }else{
                            pictureSelectListener.onPictureSelect(base64String)
                        }
                        return
                    }
                    pictureSelectListener.onPictureError(NullPointerException((if (fragment == null) activity else fragment.activity)?.getString(R.string.app_name)))
                }

            }
        }
    }

    private fun checkPermission(): Boolean {
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (!checkSelfPermission((if (fragment == null) activity!! else fragment.activity as Activity), Manifest.permission.WRITE_EXTERNAL_STORAGE) || !checkSelfPermission((if (fragment == null) activity!! else fragment.activity as Activity), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions((if (fragment == null) activity!! else fragment.activity as Activity), permissions, REQUEST_CODE_PERMISSION)
            return false
        }

        return true
    }

    private fun checkSelfPermission(activity: Activity, permission: String): Boolean {
        val sdkVersion = activity.applicationInfo.targetSdkVersion
        var ret = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ret = if (sdkVersion >= Build.VERSION_CODES.M) { activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            } else {
                PermissionChecker.checkSelfPermission(activity, permission) == PermissionChecker.PERMISSION_GRANTED
            }
        }
        return ret
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> {
                var isGrant = true
                for (index in 1 until grantResults.size) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        isGrant = false
                    }
                }
                if (isGrant) {
                    gotoGallery()
                } else {
                    Toast.makeText((if (fragment == null) activity else fragment as Context), (if (fragment == null) activity else fragment as Context)?.getString(R.string.app_name), Toast.LENGTH_SHORT).show()
                    val intent = getSettingIntent((if (fragment == null) activity!! else fragment as Context))
                    (if (fragment == null) activity else fragment as Context)?.startActivity(intent)
                }
            }
        }
    }

    private fun getSettingIntent(context: Context): Intent {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            localIntent.data = Uri.fromParts("package", context.packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.action = Intent.ACTION_VIEW
            localIntent.setClassName(
                "com.android.settings",
                "com.android.settings.InstalledAppDetails"
            )
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.packageName)
        }
        return localIntent
    }

    //get bitmap
    private fun getBitmapFromFile(path: String): Bitmap {
        return BitmapFactory.decodeFile(path)
    }

    private fun getBase64FromBitmap(bitmap: Bitmap): String {

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val bytes = baos.toByteArray()
        if(bytes.size > 2 * 1024 * 1024){
            return "-1"
        }
        val baseString = Base64.encodeToString(bytes, Base64.NO_WRAP)
        return "data:image/png;base64,$baseString"
    }

}
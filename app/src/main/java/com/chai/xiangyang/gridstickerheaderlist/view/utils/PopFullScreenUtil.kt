package jp.co.solxyz.fleeksorm.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.View

object PopFullScreenUtil {

    /**
     * デバイス情報を取得
     */
    private val deviceInfo: String
        get() {
            val brand = Build.BRAND
            if (TextUtils.isEmpty(brand)) return "navigationbar_is_min"

            return if (brand.equals("HUAWEI", ignoreCase = true)) {
                "navigationbar_is_min"
            } else if (brand.equals("XIAOMI", ignoreCase = true)) {
                "force_fsg_nav_bar"
            } else if (brand.equals("VIVO", ignoreCase = true)) {
                "navigation_gesture_on"
            } else if (brand.equals("OPPO", ignoreCase = true)) {
                "navigation_gesture_on"
            } else {
                "navigationbar_is_min"
            }
        }

    /**
     * 仮想キーの高さを取得
     *1.フルスクリーンで
     *1.1フルスクリーンスイッチをオンにする-0に戻る
     *1.2フルスクリーンスイッチをオフにする-フルスクリーンでない処理を実行する
     *2.フルスクリーンではない
     *2.1仮想キーがない-0に戻る
     *2.1仮想キー隠し→0を返します
     *2.2仮想キーが存在し、非表示-仮想キーの実際の高さを返します
     */
    fun getNavigationBarHeightIfRoom(context: Context): Int {
        return if (navigationGestureEnabled(context)) {
            0
        } else getCurrentNavigationBarHeight(context as Activity)
    }

    /**
     *フルスクリーン（フルスクリーンスイッチをオンするかどうか0はオフします。1はオンします。）
     * @param context
     * @return
     */
    private fun navigationGestureEnabled(context: Context): Boolean {
        val `val` = Settings.Global.getInt(context.contentResolver, deviceInfo, 0)
        return `val` != 0
    }

    /**
     *非フルスクリーンでの仮想キーの実際の高さ（非表示後の高さは0）
     * @param activity
     * @return
     */
    private fun getCurrentNavigationBarHeight(activity: Activity): Int {
        return if (isNavigationBarShown(activity)) {
            getNavigationBarHeight(activity)
        } else {
            0
        }
    }

    /**
     *非全面的なスクリーンの下で仮想ボタンは開けますか
     * @param activity
     * @return
     */
    private fun isNavigationBarShown(activity: Activity): Boolean {
        val view = activity.findViewById<View>(android.R.id.navigationBarBackground) ?: return false
        val visible = view.visibility
        return !(visible == View.GONE || visible == View.INVISIBLE)
    }

    /**
     * 非フルスクリーンでの仮想キーの高さ(非表示)
     * @param context
     * @return
     */
    private fun getNavigationBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}

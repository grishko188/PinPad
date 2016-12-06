package com.grishko188.pinlibrary.utils;

import android.content.Context;
import android.os.Build;

/**
 * Created by Unreal Mojo
 *
 * @author Grishko Nikita
 *         on 05.10.2016.
 */
public class Utils {

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static float px2dp(Context ctx, float px) {
        return px / ctx.getResources().getDisplayMetrics().density;
    }

    public static float dp2px(Context ctx, float dp) {
        return dp * ctx.getResources().getDisplayMetrics().density;
    }

    public static int addColorTransparency(int color) {
        return (color & 0x00FFFFFF) | 0x40000000;
    }
}

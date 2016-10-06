package com.grishko188.pinlibrary.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

/**
 * Created by Unreal Mojo
 *
 * @author Grishko Nikita
 *         on 05.10.2016.
 */
public class Utils {

    public static float px2dp(Context ctx, float px) {
        return px / ctx.getResources().getDisplayMetrics().density;
    }

    public static float dp2px(Context ctx, float dp) {
        return dp * ctx.getResources().getDisplayMetrics().density;
    }

    public static int addColorTransparency(int color) {
        return (color & 0x00FFFFFF) | 0x40000000;
    }


    public static Drawable tintDrawable(@DrawableRes int drawableRes, int color, Resources resources) {
        Drawable original = resources.getDrawable(drawableRes);
        if (original != null)
            original.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return original;
    }

}

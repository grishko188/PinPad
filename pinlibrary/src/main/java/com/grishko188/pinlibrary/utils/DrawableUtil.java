package com.grishko188.pinlibrary.utils;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.support.annotation.DrawableRes;

import com.grishko188.pinlibrary.R;

/**
 * Created by Unreal Mojo
 *
 * @author Grishko Nikita
 *         on 07.10.2016.
 */
public class DrawableUtil {


    public static Drawable tintDrawable(@DrawableRes int drawableRes, int color, Resources resources) {
        Drawable original = resources.getDrawable(drawableRes);
        if (original != null)
            original.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return original;
    }


    public static Drawable tintBackgroundDrawable(@DrawableRes int drawableRes, int color, Resources resources, Mask mask) {
        Drawable original = resources.getDrawable(drawableRes);
        if (original != null)
            original.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        if (Utils.isLollipop()) {
            return new RippleDrawable(ColorStateList.valueOf(color), original, resources.getDrawable(mask.getResId()));
        }
        return original;
    }

    public enum Mask {
        ROUND(R.drawable.round_mask), SQUARE(R.drawable.square_mask);

        @DrawableRes
        private int resId;

        Mask(int resId) {
            this.resId = resId;
        }

        private int getResId() {
            return resId;
        }
    }
}

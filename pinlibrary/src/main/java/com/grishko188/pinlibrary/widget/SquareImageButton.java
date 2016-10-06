package com.grishko188.pinlibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Unreal Mojo
 *
 * @author Grishko Nikita
 *         on 06.10.2016.
 */
public class SquareImageButton extends ImageView {

    public SquareImageButton(Context context) {
        super(context);
    }

    public SquareImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = this.getMeasuredWidth();
        int measuredHeight = this.getMeasuredHeight();

        if (measuredWidth > measuredHeight) {
            this.setMeasuredDimension(measuredWidth, measuredWidth);
        } else {
            this.setMeasuredDimension(measuredHeight, measuredHeight);
        }
    }
}

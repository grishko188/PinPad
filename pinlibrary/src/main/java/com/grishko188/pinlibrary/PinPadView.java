package com.grishko188.pinlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grishko188.pinlibrary.configuration.ConfigurationBuilder;
import com.grishko188.pinlibrary.utils.DrawableUtil;
import com.grishko188.pinlibrary.utils.Utils;

/**
 * Created by Unreal Mojo
 *
 * @author Grishko Nikita
 *         on 05.10.2016.
 */
public class PinPadView extends RelativeLayout {


    private PinCodeField mPinField;
    private KeyboardView mKeyboard;
    private TextView mPinTitle;
    private TextView mForgotPin;
    private Button mSkip;

    private int mColor;
    private int mKeyboardFormStyle;
    private int mKeyboardTextSize;


    private PinPadUsageMode mUsageMode;

    private ConfigurationBuilder mCurrentConfiguration;

    public PinPadView(Context context) {
        super(context);
        init();
        applyUIConfiguration();
    }

    public PinPadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initAttrs(attrs);
        applyUIConfiguration();
    }

    public PinPadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttrs(attrs);
        applyUIConfiguration();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PinPadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        initAttrs(attrs);
        applyUIConfiguration();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.PinPadView);

            if (array != null) {
                mColor = array.getColor(R.styleable.PinPadView_ppv_color, getResources().getColor(R.color.dark));
                mKeyboardFormStyle = array.getInt(R.styleable.PinPadView_ppv_keyboard_form_style, 0);
                mKeyboardTextSize = array.getDimensionPixelSize(R.styleable.PinPadView_ppv_buttons_text_size, (int) Utils.dp2px(getContext(), 20));
                array.recycle();
            }
        }
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_pin_pad, this);

        mPinField = (PinCodeField) findViewById(R.id.pin_code_field);
        mKeyboard = (KeyboardView) findViewById(R.id.keyboard);
        mPinTitle = (TextView) findViewById(R.id.pin_description);
        mForgotPin = (TextView) findViewById(R.id.btn_forgot_pin);
        mSkip = (Button) findViewById(R.id.skip);

        mKeyboard.attach(mPinField);
    }

    public void setUpNewConfiguration(ConfigurationBuilder builder) {
        this.mCurrentConfiguration = builder;
        applyConfiguration();
    }

    private void applyUIConfiguration() {
        mPinField.setColor(mColor);
        mKeyboard.setKeyboardColor(mColor);
        mKeyboard.setFormStyle(mKeyboardFormStyle);
        mKeyboard.setButtonsTextSize(mKeyboardTextSize);

        mSkip.setBackgroundDrawable(DrawableUtil.tintBackgroundDrawable(R.drawable.selector_btn_skip, mColor, getResources(), DrawableUtil.Mask.SQUARE));
        mSkip.setTextColor(mColor);
        mForgotPin.setBackgroundDrawable(DrawableUtil.tintBackgroundDrawable(R.drawable.selector_default_white_no_borders, mColor, getResources(), DrawableUtil.Mask.SQUARE));
        mForgotPin.setPaintFlags(mForgotPin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mForgotPin.setTextColor(mColor);
        mPinTitle.setTextColor(mColor);
    }

    private void applyConfiguration() {

    }


    public enum PinPadUsageMode {

        ENTER,
        SETUP;

        private static PinPadUsageMode fromAttrs(int key) {
            switch (key) {
                case 0:
                    return ENTER;
                default:
                    return SETUP;
            }
        }
    }
}

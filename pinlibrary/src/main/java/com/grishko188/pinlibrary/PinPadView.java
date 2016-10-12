package com.grishko188.pinlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grishko188.pinlibrary.configuration.Configuration;
import com.grishko188.pinlibrary.fingerprint.FingerprintUiHelper;
import com.grishko188.pinlibrary.interfaces.OnPinCodeListener;
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
    private int mMaxLength;
    private float mSize;
    private int mPinStyle;
    private int mMaxTryCount;
    private int mFillColor;
    private int mLetterSpacing;

    private Configuration mCurrentConfiguration;

    private FingerprintUiHelper mFingerprintUiHelper;

    public PinPadView(Context context) {
        super(context);
        init();
        applyUIStyle();
    }

    public PinPadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initAttrs(attrs);
        applyUIStyle();
    }

    public PinPadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttrs(attrs);
        applyUIStyle();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PinPadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        initAttrs(attrs);
        applyUIStyle();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.PinPadView);

            if (array != null) {
                mColor = array.getColor(R.styleable.PinPadView_ppv_color, getResources().getColor(R.color.dark));
                mKeyboardFormStyle = array.getInt(R.styleable.PinPadView_ppv_keyboard_form_style, 0);
                mKeyboardTextSize = array.getDimensionPixelSize(R.styleable.PinPadView_ppv_buttons_text_size, (int) Utils.dp2px(getContext(), 20));

                mSize = array.getDimensionPixelSize(R.styleable.PinPadView_ppv_size, (int) Utils.dp2px(getContext(), 24));
                mMaxLength = array.getInteger(R.styleable.PinPadView_ppv_max_len, 4);
                mPinStyle = array.getInt(R.styleable.PinPadView_ppv_empty_char_style, 0);

                mMaxTryCount = array.getInteger(R.styleable.PinPadView_ppv_max_try, 4);
                mLetterSpacing = array.getDimensionPixelSize(R.styleable.PinPadView_ppv_letter_spacing
                        , (int) Utils.dp2px(getContext(), 5));
                mFillColor = array.getColor(R.styleable.PinPadView_ppv_fill_color, Utils.addColorTransparency(mColor));
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

    public void setUpNewConfiguration(Configuration configuration) {
        this.mCurrentConfiguration = configuration;
        applyConfiguration();
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
        applyUIStyle();
    }

    public int getKeyboardTextSize() {
        return mKeyboardTextSize;
    }

    public void setKeyboardTextSize(int keyboardTextSize) {
        this.mKeyboardTextSize = keyboardTextSize;
        applyUIStyle();
    }

    public int getMaxLength() {
        return mMaxLength;
    }

    public void setMaxLength(int maxLength) {
        this.mMaxLength = maxLength;
        applyUIStyle();
    }

    public float getSize() {
        return mSize;
    }

    public void setSize(float size) {
        this.mSize = size;
        applyUIStyle();
    }

    public int getMaxTryCount() {
        return mMaxTryCount;
    }

    public void setMaxTryCount(int maxTryCount) {
        this.mMaxTryCount = maxTryCount;
        applyUIStyle();
    }

    public int getEmptyCharFillColor() {
        return mFillColor;
    }

    public void setEmptyCharFillColor(int fillColor) {
        this.mFillColor = fillColor;
        applyUIStyle();
    }

    public int getLetterSpacing() {
        return mLetterSpacing;
    }

    public void setLetterSpacing(int letterSpacing) {
        this.mLetterSpacing = letterSpacing;
        applyUIStyle();
    }

    public void onResume() {
        if (mFingerprintUiHelper != null)
            mFingerprintUiHelper.startListening(getConfig().getCryptoObject());
    }

    public void onPause() {
        if (mFingerprintUiHelper != null)
            mFingerprintUiHelper.stopListening();
    }

    public void reset() {
        mPinField.reset();
    }

    private void applyUIStyle() {

        mPinField.setColor(mColor);
        mPinField.setMaxLength(mMaxLength);
        mPinField.setStyle(mPinStyle);
        mPinField.setSize(mSize);
        mPinField.setFillColor(mFillColor);
        mPinField.setMaxTryCount(mMaxTryCount);
        mPinField.setPinLetterSpacing(mLetterSpacing);

        mKeyboard.setKeyboardColor(mColor);
        mKeyboard.setFormStyle(mKeyboardFormStyle);
        mKeyboard.setButtonsTextSize(mKeyboardTextSize);

        mSkip.setBackgroundDrawable(DrawableUtil.tintBackgroundDrawable(R.drawable.selector_btn_skip, mColor, getResources(), DrawableUtil.Mask.SQUARE_WITH_CORNERS));
        mSkip.setTextColor(mColor);
        mForgotPin.setBackgroundDrawable(DrawableUtil.tintBackgroundDrawable(R.drawable.selector_default_white_no_borders, mColor, getResources(), DrawableUtil.Mask.SQUARE));
        mForgotPin.setPaintFlags(mForgotPin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mForgotPin.setTextColor(mColor);
        mPinTitle.setTextColor(mColor);
    }

    private void applyConfiguration() {

        if (mCurrentConfiguration == null)
            return;

        buildTitle();
        buildHelpButtons();
        buildForMode();
    }

    private void buildTitle() {
        mPinTitle.setVisibility(getConfig().isShowPinCodeTitle() ? VISIBLE : INVISIBLE);
        if (!TextUtils.isEmpty(getConfig().getPinCodeTitle())) {
            mPinTitle.setText(getConfig().getPinCodeTitle());
        }
    }

    private void buildHelpButtons() {

        mForgotPin.setVisibility(getConfig().isShowForgotButton() ? VISIBLE : INVISIBLE);
        if (!TextUtils.isEmpty(getConfig().getPinCodeForgotButtonTitle())) {
            mForgotPin.setText(getConfig().getPinCodeForgotButtonTitle());
        }
        mForgotPin.setOnClickListener(mForgotButtonClickListener);

        mSkip.setVisibility(getConfig().isShowSkipButton() ? VISIBLE : INVISIBLE);
        if (!TextUtils.isEmpty(getConfig().getSkipButtonTitle())) {
            mSkip.setText(getConfig().getSkipButtonTitle());
        }
        mSkip.setOnClickListener(mSkipButtonClickListener);
    }

    private void buildForMode() {
        if (getConfig().getMode() == PinPadUsageMode.SETUP) {
            mPinField.setOnPinCodeListener(new SetupPinCodeListener());
            mKeyboard.setFingerprintEnable(false);
        } else {
            mPinField.setOnPinCodeListener(getConfig().getPinCodeListener());
            buildFingerprint();
        }
    }

    private void buildFingerprint() {
        if (getConfig().isFingerprintEnable()) {
            FingerprintUiHelper.FingerprintUiHelperBuilder mFingerprintUiHelperBuilder = new FingerprintUiHelper.FingerprintUiHelperBuilder(getContext());
            mFingerprintUiHelper = mFingerprintUiHelperBuilder.build(mKeyboard, getConfig().getFingerprintAuthListener());
            mKeyboard.setFingerprintEnable(mFingerprintUiHelper.isFingerprintAuthAvailable());
        }
    }

    private Configuration getConfig() {
        return mCurrentConfiguration;
    }


    private OnClickListener mForgotButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getConfig() != null && getConfig().getHelpButtonsListener() != null) {
                getConfig().getHelpButtonsListener().onForgotPinCode(view);
            }
        }
    };

    private OnClickListener mSkipButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getConfig() != null
                    && getConfig().getHelpButtonsListener() != null) {
                getConfig().getHelpButtonsListener().onSkip(view);
            }
        }
    };


    private class SetupPinCodeListener implements OnPinCodeListener {

        private String pinCode;
        private boolean isConfirmation;

        @Override
        public void onPinEntered(String correctPinCode) {
            if (!isConfirmation) {
                showPinConfirmation();
            } else {
                if (getConfig().getSetupPinCodeListener() != null) {
                    getConfig().getSetupPinCodeListener().onSuccess(correctPinCode);
                }
            }
        }

        @Override
        public void onPinError(int triesLeft) {
            resetPinView();
            if (getConfig().getSetupPinCodeListener() != null)
                getConfig().getSetupPinCodeListener().onFail();
            pinCode = null;
        }

        @Override
        public void onPinEnterFail() {
            //unused in this case
        }

        @Override
        public boolean verifyPinCode(String input) {
            isConfirmation = !TextUtils.isEmpty(pinCode);
            if (!isConfirmation) {
                pinCode = input;
                return true;
            }
            return pinCode.equalsIgnoreCase(input);
        }

        public void showPinConfirmation() {
            mPinField.setText(null);
            if (TextUtils.isEmpty(getConfig().getConfirmPinCodeTitle())) {
                mPinTitle.setText(R.string.title_confirm_your_pin_code);
            } else {
                mPinTitle.setText(getConfig().getConfirmPinCodeTitle());
            }
        }

        public void resetPinView() {
            if (TextUtils.isEmpty(getConfig().getPinCodeTitle())) {
                mPinTitle.setText(R.string.title_create_pin_code);
            } else {
                mPinTitle.setText(getConfig().getPinCodeTitle());
            }
            mPinField.reset();
        }
    }


    public enum PinPadUsageMode {
        ENTER, SETUP
    }
}

package com.grishko188.pinlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.grishko188.pinlibrary.interfaces.OnPinCodeListener;
import com.grishko188.pinlibrary.utils.Utils;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by Unreal Mojo
 *
 * @author Grishko Nikita
 *         on 27/08/14.
 */
public class PinCodeField extends EditText {

    private static final char mEmptyChar = '_';
    private static final char mFilledChar = '*';

    private static final int DEFAULT_TRIES_COUNT = 3;
    private static final int DEFAULT_MAX_LENGTH = 4;
    private static final int DEFAULT_COLOR = android.R.color.white;
    private static final int DEFAULT_LETTER_SPACING = 5; //dp will be converted to pixels
    private static final int DEFAULT_SIZE = 25; //dp will be converted to pixels
    private static final int DEFAULT_STROKE_WIDTH = 1;//dp will be converted to pixels


    private char[] mChars = new char[]{'_', '_', '_', '_'};

    private int mLetterSpacing;
    private float mSize, mRadius;
    private int mMaxLength;
    private int mMaxTryCount;
    private int mColor;
    private int mFillColor;
    private int mStrokeWidth;
    private PinCodeFieldStyle mStyle;


    private int mTryCount = 0;

    private OnPinCodeListener mOnPinCodeListener;

    public PinCodeField(Context context) {
        this(context, null);
    }

    public PinCodeField(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinCodeField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setSingleLine();
        setLongClickable(false);
        setCursorVisible(false);

        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.PinCodeField);
            initAttrs(attributes);
        }
        init();
        addTextWatcher();
    }

    private void initAttrs(TypedArray array) {

        if (array != null) {

            mMaxLength = array.getInt(R.styleable.PinCodeField_pcf_max_len, DEFAULT_MAX_LENGTH);
            mLetterSpacing = array.getDimensionPixelSize(R.styleable.PinCodeField_pcf_letter_spacing
                    , (int) Utils.dp2px(getContext(), DEFAULT_LETTER_SPACING));
            mSize = array.getDimensionPixelSize(R.styleable.PinCodeField_pcf_size, (int) Utils.dp2px(getContext(), DEFAULT_SIZE));
            mMaxTryCount = array.getInteger(R.styleable.PinCodeField_pcf_max_try, DEFAULT_TRIES_COUNT);
            mStrokeWidth = array.getDimensionPixelSize(R.styleable.PinCodeField_pcf_stroke_width, (int) Utils.dp2px(getContext(), DEFAULT_STROKE_WIDTH));

            int defaultColor = getResources().getColor(DEFAULT_COLOR);
            mColor = array.getColor(R.styleable.PinCodeField_pcf_color, defaultColor);
            mFillColor = array.getColor(R.styleable.PinCodeField_pcf_fill_color, Utils.addColorTransparency(defaultColor));
            mStyle = PinCodeFieldStyle.fromAttrs(array.getInteger(R.styleable.PinCodeField_pcf_style, 1));
            array.recycle();
        }
    }

    private void init() {
        mChars = new char[mMaxLength];
        Arrays.fill(mChars, mEmptyChar);

        mRadius = (float) (mSize * 0.85) / 2;

        setFilters(new InputFilter[]{createNumberInputFilter(), new InputFilter.LengthFilter(mMaxLength)});
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        getPaint().setAntiAlias(true);
        getPaint().setFilterBitmap(true);
    }

    private void addTextWatcher() {
        removeTextChangedListener(mTextWatcher);
        addTextChangedListener(mTextWatcher);
    }

    /**
     * Setup new draw style - one from enum {@link PinCodeFieldStyle}
     * <br/><li/> {@link PinCodeFieldStyle#STROKE} - an empty symbol will be drawn like a transparent circle with a stroke.
     * <br/><li/> {@link PinCodeFieldStyle#FILL} - an empty symbol will be drawn like a fill circle.
     * The color of circle's body is a transparent version of main pin code field color or color which set by {@link PinCodeField#setFillColor(int)}
     * <p/>
     * <br/><br/> Default value is {@link PinCodeFieldStyle#STROKE}
     */
    public void setStyle(PinCodeFieldStyle style) {
        this.mStyle = style;
    }


    public void setStyle(int style) {
        setStyle(PinCodeFieldStyle.fromAttrs(style));
    }

    /**
     * @return - current {@link PinCodeFieldStyle}
     */
    public PinCodeFieldStyle getStyle() {
        return mStyle;
    }

    /**
     * Setup color for {@link PinCodeField}
     */
    public void setColor(int color) {
        this.mColor = color;
        invalidate();
    }

    /**
     * @return - current color
     */
    public int getColor() {
        return mColor;
    }

    /**
     * Setup fill color for empty char. This color will be used only for {@link PinCodeFieldStyle#FILL}
     */
    public void setFillColor(@ColorRes int color) {
        this.mFillColor = color;
    }

    /**
     * @return - current fill color
     */
    public int getFillColor() {
        return mFillColor;
    }

    /**
     * @return - number of mas tries
     */
    public int getMaxTryCount() {
        return mMaxTryCount;
    }

    /**
     * Setup max number of tries
     */
    public void setMaxTryCount(int mMaxTryCount) {
        this.mMaxTryCount = mMaxTryCount;
    }

    /**
     * @return - current callback
     */
    public OnPinCodeListener getOnPinCodeListener() {
        return mOnPinCodeListener;
    }

    /**
     * Setup callback to interact with PinCodeField
     * <br/> Implement four methods:
     * <br/><li/>{@link OnPinCodeListener#onPinEntered(String)} - calls when user enter correct pin code
     * <br/><li/>{@link OnPinCodeListener#verifyPinCode(String)} - implements validation of entered PIN code
     * <br/><li/>{@link OnPinCodeListener#onPinError(int)} - calls when for current pin code method <b>verifyPinCode(String)</b> returns <b>false</b>. The argument passed to the method the number of remaining attempts
     * <br/><li/>{@link OnPinCodeListener#onPinEnterFail()} -  calls when the number of attempts exceeds the specified maximum number of attempts. In this case we recommend to reset pin code and delete all stored user data, to prevent password guessing
     */
    public void setOnPinCodeListener(OnPinCodeListener mOnPinCodeListener) {
        this.mOnPinCodeListener = mOnPinCodeListener;
    }

    /**
     * Set length of pin code
     */
    public void setMaxLength(int maxLength) {
        this.mMaxLength = maxLength;
        init();
        invalidate();
    }

    /**
     * @return - pin code length
     */
    public int getMaxLength() {
        return mMaxLength;
    }

    /**
     * Set size of pin code field
     */
    public void setSize(float size) {
        this.mSize = size;
        init();
        invalidate();
    }

    /**
     * @return - field size
     */
    public float getSize() {
        return mSize;
    }

    /**
     * Set spacing between dots in pin code field in pixels
     */
    public void setPinLetterSpacing(int letterSpacing) {
        this.mLetterSpacing = letterSpacing;
        init();
        invalidate();
    }

    /**
     * @return - spacing between dots in pin code field in pixels
     */
    public int getPinLetterSpacing() {
        return mLetterSpacing;
    }

    /**
     * Reset pin code state. Clear text and set tries count to 0
     */
    public void reset() {
        setText(null);
        mTryCount = 0;
    }

    @Override
    public void onSelectionChanged(int start, int end) {
        CharSequence text = getText();
        if (text != null) {
            if (start != text.length() || end != text.length()) {
                setSelection(text.length(), text.length());
                return;
            }
        }
        super.onSelectionChanged(start, end);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        char[] chars = new char[this.mChars.length];
        Arrays.fill(chars, mEmptyChar);

        int labelWidth = (int) (chars.length * mSize);

        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.EXACTLY: {
                int width = MeasureSpec.getSize(widthMeasureSpec);
                mLetterSpacing = (width - labelWidth - getPaddingLeft() - getPaddingRight()) / (chars.length - 1);
            }
            break;
            case MeasureSpec.UNSPECIFIED: {
                int spacingWidth = mLetterSpacing * (chars.length - 1);
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(labelWidth + spacingWidth + getPaddingLeft() + getPaddingRight(), MeasureSpec.EXACTLY);
            }
            break;
            case MeasureSpec.AT_MOST: {
                int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
                int padding = getPaddingLeft() + getPaddingRight();
                int spacingWidth = mLetterSpacing * (chars.length - 1);
                if (labelWidth + spacingWidth + padding > maxWidth) {
                    mLetterSpacing = (maxWidth - labelWidth - padding) / (chars.length - 1);
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
                } else {
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(labelWidth + spacingWidth + padding, MeasureSpec.EXACTLY);
                }
            }
            break;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (mSize * 2 + getPaddingTop() + getPaddingBottom()), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        int cursor = length();
        for (int i = 0; i < mChars.length; i++) {
            mChars[i] = i < cursor ? mFilledChar : mEmptyChar;
        }
        int x = (int) (getPaddingLeft() + mSize / 2);
        int y = getHeight() / 2;
        for (char aChar : mChars) {
            if (aChar == mFilledChar) {
                getPaint().setStyle(Paint.Style.FILL);
                getPaint().setColor(mColor);
            } else {
                if (mStyle == PinCodeFieldStyle.STROKE) {
                    getPaint().setStyle(Paint.Style.STROKE);
                    getPaint().setColor(mColor);
                    getPaint().setStrokeWidth(mStrokeWidth);
                } else {
                    getPaint().setStyle(Paint.Style.FILL);
                    getPaint().setColor(mFillColor);
                }
            }
            canvas.drawCircle(x, y, mRadius, getPaint());
            x += mSize;
            x += mLetterSpacing;
        }
    }

    private void shakeIt() {
        Animation shake = new TranslateAnimation(0, 30, 0, 0);
        shake.setInterpolator(new CycleInterpolator(3));
        shake.setDuration(300);
        startAnimation(shake);
    }

    private InputFilter createNumberInputFilter() {
        return new InputFilter() {
            private final Pattern NO_NUMBER_PATTERN = Pattern.compile("\\D+"); // No number

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                return NO_NUMBER_PATTERN.matcher(source).replaceAll(""); // Remove unsupported chars
            }
        };
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(final Editable s) {
            if (s.length() == mMaxLength) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mOnPinCodeListener == null)
                            return;
                        if (mOnPinCodeListener.verifyPinCode(s.toString())) {
                            mOnPinCodeListener.onPinEntered(s.toString());
                        } else {
                            shakeIt();
                            setText(null);
                            if (++mTryCount < mMaxTryCount) {
                                mOnPinCodeListener.onPinError(mMaxTryCount - mTryCount);
                            } else {
                                mTryCount = 0;
                                mOnPinCodeListener.onPinEnterFail();
                            }
                        }
                    }
                }, 100);
            }
        }
    };

    public enum PinCodeFieldStyle {
        FILL, STROKE;

        private static PinCodeFieldStyle fromAttrs(int key) {
            switch (key) {
                case 0:
                    return FILL;
                default:
                    return STROKE;
            }
        }
    }
}

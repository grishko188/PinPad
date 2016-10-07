package com.grishko188.pinlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.grishko188.pinlibrary.utils.DrawableUtil;
import com.grishko188.pinlibrary.utils.Utils;
import com.grishko188.pinlibrary.widget.SquareButton;
import com.grishko188.pinlibrary.widget.SquareImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Unreal Mojo
 *
 * @author Grishko Nikita
 *         on 06.10.2016.
 */
public class KeyboardView extends LinearLayout {

    private static final int DEFAULT_STYLE = 0;
    private static final int LIGHT_COLOR = android.R.color.white;
    private static final int DARK_COLOR = R.color.dark;

    private KeyboardColorStyle mColorStyle = KeyboardColorStyle.fromAttrs(DEFAULT_STYLE);
    private KeyboardFormStyle mFormStyle = KeyboardFormStyle.fromAttrs(DEFAULT_STYLE);

    private int mKeyboardCustomColor;
    private int mButtonTextSize;
    private boolean isFingerprintEnable = false;

    private List<SquareButton> mNumberButtons;
    private SquareImageButton mBackspaceButton;
    private SquareImageButton mFingerprintButton;
    private EditText mInputForKeyboard;

    private List<OnButtonsClickListener> mOnClickListeners = new ArrayList<>();

    public KeyboardView(Context context) {
        super(context);
        init();
        applyStyle();
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initAttrs(attrs);
        applyStyle();
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttrs(attrs);
        applyStyle();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        initAttrs(attrs);
        applyStyle();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_keyboard, this);
        initKeyboardButtons();
        initListeners();
        invalidateFingerprintButton();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.KeyboardView);

            if (array != null) {
                mFormStyle = KeyboardFormStyle.fromAttrs(array.getInteger(R.styleable.KeyboardView_kv_form_style, DEFAULT_STYLE));
                mColorStyle = KeyboardColorStyle.fromAttrs(array.getInteger(R.styleable.KeyboardView_kv_color_style, DEFAULT_STYLE));
                if (mColorStyle == KeyboardColorStyle.CUSTOM) {
                    mKeyboardCustomColor = array.getColor(R.styleable.KeyboardView_kv_custom_color, -1);
                    if (mKeyboardCustomColor == -1) {
                        mColorStyle = KeyboardColorStyle.LIGHT;
                    }
                }
                mButtonTextSize = array.getDimensionPixelSize(R.styleable.KeyboardView_kv_button_text_size, (int) Utils.dp2px(getContext(), 16));
                array.recycle();
            }
        }
    }

    private void initKeyboardButtons() {
        mNumberButtons = new ArrayList<>();
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_0));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_1));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_2));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_3));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_4));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_5));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_6));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_7));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_8));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_9));

        mBackspaceButton = (SquareImageButton) findViewById(R.id.backspace);
        mFingerprintButton = (SquareImageButton) findViewById(R.id.fingerprint);
    }

    private void initListeners() {
        for (SquareButton button : mNumberButtons) {
            button.setOnClickListener(mButtonsClickListener);
        }
        mBackspaceButton.setOnClickListener(mButtonsClickListener);
    }

    /**
     * Attach any {@link EditText} to work with Keyboard
     * <br/> When EditText is attached it becomes not focusable to prevent standard keyboard appearing.
     */
    public void attach(EditText editText) {
        this.mInputForKeyboard = editText;
        this.mInputForKeyboard.setFocusable(false);
        this.mInputForKeyboard.setFocusableInTouchMode(false);
    }

    /**
     * Register a {@link OnButtonsClickListener} to be invoked when some button of the keyboard is clicking.
     */
    public void addOnClickListener(OnButtonsClickListener clickListener) {
        mOnClickListeners.add(clickListener);
    }

    /**
     * Remove a {@link OnButtonsClickListener};
     */
    public void removeOnClickListener(OnButtonsClickListener clickListener) {
        mOnClickListeners.remove(clickListener);
    }

    /**
     * Clear all {@link OnButtonsClickListener};
     */
    public void removeAllClickListeners() {
        mOnClickListeners.clear();
    }

    /**
     * Display success icon in fingerprint container
     */
    @RequiresApi(23)
    public void showFingerprintSuccess() {
        if (!isFingerprintEnable)
            return;
        mFingerprintButton.setImageResource(R.drawable.ic_fingerprint_success);
    }

    /**
     * Display error icon in fingerprint container
     */
    @RequiresApi(23)
    public void showFingerprintFail() {
        if (!isFingerprintEnable)
            return;
        mFingerprintButton.setImageResource(R.drawable.ic_fingerprint_error);
    }

    /**
     * Enable or disable fingerprint UI.
     * <br/> Actually hide or show fingerprint icon at the bottom and enable methods to display error or success state.
     */
    @RequiresApi(23)
    public void setFingerprintEnable(boolean isEnable) {
        this.isFingerprintEnable = isEnable;
        invalidateFingerprintButton();
    }

    /**
     * Display default fingerprint icon
     */
    @RequiresApi(23)
    public void resetFingerprintState() {
        if (!isFingerprintEnable)
            return;
        mFingerprintButton.setImageDrawable(DrawableUtil.tintDrawable(R.drawable.ic_fingerprint_white_24dp, getColor(), getResources()));
    }

    /**
     * @return - current color style
     */
    public KeyboardColorStyle getColorStyle() {
        return mColorStyle;
    }

    /**
     * Setup new color style - one from enum {@link KeyboardColorStyle}
     * <br/><li/> {@link KeyboardColorStyle#LIGHT} - #ffffff color for all controls
     * <br/><li/> {@link KeyboardColorStyle#DARK} - #5b5b5b color for all controls
     * <br/><li/> {@link KeyboardColorStyle#CUSTOM} - enable feature to use custom color which set with {@link KeyboardView#setKeyboardCustomColor(int)}
     * <p>
     * <br/><br/> Default value is {@link KeyboardColorStyle#LIGHT}
     */
    public void setColorStyle(KeyboardColorStyle mColorStyle) {
        this.mColorStyle = mColorStyle;
        applyStyle();
    }

    /**
     * @return - current form style
     */
    public KeyboardFormStyle getFormStyle() {
        return mFormStyle;
    }

    /**
     * Setup new form style - one from enum {@link KeyboardFormStyle}
     * <br/><li/> {@link KeyboardFormStyle#ROUND} - round buttons with 1dp stroke.
     * <br/><li/> {@link KeyboardFormStyle#ROUND_NO_BORDERS} - round buttons without stroke
     * <br/><li/> {@link KeyboardFormStyle#SQUARE} - square buttons with 0.5dp stroke
     * <br/><li/> {@link KeyboardFormStyle#SQUARE_NO_BORDERS} - square buttons without stroke
     * <p>
     * <br/> All styles have general UI rules:
     * <br/><li/> Buttons are transparent
     * <br/><li/> Have buttons strokes or not depend on selected style
     * <br/><li/> For API version > 21, Buttons have ripple effect. Color of ripple effect depends on {@link KeyboardColorStyle} and {@link KeyboardView#setKeyboardCustomColor(int)} for {@link KeyboardColorStyle#CUSTOM}
     * <br/><li/> For API version < 21, Buttons have simple state list selector. Color of pressed state depends on {@link KeyboardColorStyle} and {@link KeyboardView#setKeyboardCustomColor(int)} for {@link KeyboardColorStyle#CUSTOM}, and will have some transparency}
     * <br/><br/> Default value is {@link KeyboardFormStyle#ROUND}
     */
    public void setFormStyle(KeyboardFormStyle mFormStyle) {
        this.mFormStyle = mFormStyle;
        applyStyle();
    }

    /**
     * @return - keyboard custom color resource id
     */
    @ColorRes
    public int getKeyboardCustomColor() {
        return mKeyboardCustomColor;
    }

    /**
     * Setup custom color resource id. The changes will be applied only for {@link KeyboardColorStyle#CUSTOM}
     */
    public void setKeyboardCustomColor(@ColorRes int keyboardCustomColor) {
        this.mKeyboardCustomColor = keyboardCustomColor;
        applyStyle();
    }

    private OnClickListener mButtonsClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.backspace) {
                if (mInputForKeyboard != null) {
                    int length = mInputForKeyboard.getText().length();
                    if (length > 0) {
                        mInputForKeyboard.getText().delete(length - 1, length);
                    }
                }

                if (!mOnClickListeners.isEmpty()) {
                    for (OnButtonsClickListener listener : mOnClickListeners) {
                        listener.onBackspaceClick(view);
                    }
                }
            } else {
                if (mInputForKeyboard != null)
                    mInputForKeyboard.append(((SquareButton) view).getText());

                if (!mOnClickListeners.isEmpty()) {
                    for (OnButtonsClickListener listener : mOnClickListeners) {
                        listener.onNumButtonClick(view, Integer.parseInt(((SquareButton) view).getText().toString()));
                    }
                }
            }
        }
    };

    private void applyStyle() {
        switch (mFormStyle) {
            case ROUND:
                applyRoundTheme();
                break;
            case SQUARE:
                applySquareTheme();
                break;
            case SQUARE_NO_BORDERS:
                applySquareNoBoundsTheme();
                break;
            case ROUND_NO_BORDERS:
                applyRoundNoBordersTheme();
                break;
        }
    }


    private void applyRoundTheme() {
        int color = getColor();
        Drawable numButtonsBackground = DrawableUtil.tintBackgroundDrawable(R.drawable.selector_default_white_round, color, getResources(), DrawableUtil.Mask.ROUND);
        Drawable backspaceButtonBackground = DrawableUtil.tintBackgroundDrawable(R.drawable.selector_white_round_no_borders, color, getResources(), DrawableUtil.Mask.ROUND);

        setButtonTheme(color, numButtonsBackground, backspaceButtonBackground, null, (int) Utils.dp2px(getContext(), 8));
    }

    private void applyRoundNoBordersTheme() {
        int color = getColor();
        Drawable buttonsBackground = DrawableUtil.tintBackgroundDrawable(R.drawable.selector_white_round_no_borders, color, getResources(), DrawableUtil.Mask.ROUND);

        setButtonTheme(color, buttonsBackground, buttonsBackground, null, (int) Utils.dp2px(getContext(), 8));
    }

    private void applySquareTheme() {
        int color = getColor();
        Drawable numButtonsBackground = DrawableUtil.tintBackgroundDrawable(R.drawable.selector_default_white, color, getResources(), DrawableUtil.Mask.SQUARE);
        Drawable backspaceButtonBackground = DrawableUtil.tintBackgroundDrawable(R.drawable.selector_default_white_no_borders, color, getResources(), DrawableUtil.Mask.SQUARE);
        Drawable fingerprintButtonBackground = DrawableUtil.tintBackgroundDrawable(R.drawable.bg_white_square, color, getResources(), DrawableUtil.Mask.SQUARE);

        setButtonTheme(color, numButtonsBackground, backspaceButtonBackground, fingerprintButtonBackground, 0);
    }

    private void applySquareNoBoundsTheme() {
        int color = getColor();
        Drawable buttonsBackground = DrawableUtil.tintBackgroundDrawable(R.drawable.selector_default_white_no_borders, color, getResources(), DrawableUtil.Mask.SQUARE);

        setButtonTheme(color, buttonsBackground, buttonsBackground, null, 0);
    }


    private void setButtonTheme(int color, Drawable numButtonsBackground, Drawable backspaceButtonBackground, Drawable fingerprintButtonBackground, int margin) {

        for (SquareButton squareButton : mNumberButtons) {
            squareButton.setTextColor(color);
            squareButton.setBackground(numButtonsBackground.getConstantState().newDrawable()); //workaround for very very strange issue!!
            ((LinearLayout.LayoutParams) squareButton.getLayoutParams()).setMargins(margin, margin, margin, margin);
            squareButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, mButtonTextSize);
        }

        ((LinearLayout.LayoutParams) mBackspaceButton.getLayoutParams()).setMargins(margin, margin, margin, margin);
        mBackspaceButton.setBackground(backspaceButtonBackground);
        mBackspaceButton.setImageDrawable(DrawableUtil.tintDrawable(R.drawable.ic_backspace_white_24dp, color, getResources()));

        ((LinearLayout.LayoutParams) mFingerprintButton.getLayoutParams()).setMargins(margin, margin, margin, margin);
        mFingerprintButton.setBackground(fingerprintButtonBackground);
        mFingerprintButton.setImageDrawable(DrawableUtil.tintDrawable(R.drawable.ic_fingerprint_white_24dp, color, getResources()));
    }


    private int getColor() {
        switch (mColorStyle) {
            case LIGHT:
                return getResources().getColor(LIGHT_COLOR);
            case DARK:
                return getResources().getColor(DARK_COLOR);
            case CUSTOM:
                return mKeyboardCustomColor;
            default:
                return getResources().getColor(LIGHT_COLOR);
        }
    }

    private void invalidateFingerprintButton() {
        mFingerprintButton.setVisibility(isFingerprintEnable ? VISIBLE : INVISIBLE);
    }


    public enum KeyboardColorStyle {

        LIGHT, DARK, CUSTOM;

        private static KeyboardColorStyle fromAttrs(int key) {
            switch (key) {
                case 1:
                    return DARK;
                case 2:
                    return CUSTOM;
                default:
                    return LIGHT;
            }
        }
    }

    public enum KeyboardFormStyle {
        ROUND, SQUARE, SQUARE_NO_BORDERS, ROUND_NO_BORDERS;

        private static KeyboardFormStyle fromAttrs(int key) {
            switch (key) {
                case 1:
                    return SQUARE;
                case 2:
                    return SQUARE_NO_BORDERS;
                case 3:
                    return ROUND_NO_BORDERS;
                default:
                    return ROUND;
            }
        }
    }

    public interface OnButtonsClickListener {

        void onNumButtonClick(View v, int number);

        void onBackspaceClick(View v);
    }
}

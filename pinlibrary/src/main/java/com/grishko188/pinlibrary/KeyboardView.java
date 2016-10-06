package com.grishko188.pinlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

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
public class KeyboardView extends LinearLayout implements View.OnClickListener {

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
            button.setOnClickListener(this);
        }
        mBackspaceButton.setOnClickListener(this);
    }


    public void attach(EditText editText) {
        this.mInputForKeyboard = editText;
        this.mInputForKeyboard.setFocusable(false);
        this.mInputForKeyboard.setFocusableInTouchMode(false);
    }

    public KeyboardColorStyle getColorStyle() {
        return mColorStyle;
    }

    public void setColorStyle(KeyboardColorStyle mColorStyle) {
        this.mColorStyle = mColorStyle;
        applyStyle();
    }

    public KeyboardFormStyle getFormStyle() {
        return mFormStyle;
    }

    public void setFormStyle(KeyboardFormStyle mFormStyle) {
        this.mFormStyle = mFormStyle;
        applyStyle();
    }

    public int getKeyboardCustomColor() {
        return mKeyboardCustomColor;
    }

    public void setKeyboardCustomColor(int mKeyboardCustomColor) {
        this.mKeyboardCustomColor = mKeyboardCustomColor;
        applyStyle();
    }

    public void setFingerprintEnable(boolean isEnable) {
        this.isFingerprintEnable = isEnable;
        invalidateFingerprintButton();
    }

    @Override
    public void onClick(View view) {
        if (mInputForKeyboard == null)
            return;

        if (view.getId() == R.id.backspace) {
            int length = mInputForKeyboard.getText().length();
            if (length > 0) {
                mInputForKeyboard.getText().delete(length - 1, length);
            }
        } else {
            mInputForKeyboard.append(((SquareButton) view).getText());
        }

    }

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
        }
    }


    private void applyRoundTheme() {
        int color = getColor();
        Drawable numButtonsBackground = Utils.tintDrawable(R.drawable.selector_default_white_round, color, getResources());
        Drawable backspaceButtonBackground = Utils.tintDrawable(R.drawable.selector_white_round_no_borders, color, getResources());

        setButtonTheme(color, numButtonsBackground, backspaceButtonBackground, null, (int) Utils.dp2px(getContext(), 8));
    }


    private void applySquareTheme() {
        int color = getColor();
        Drawable numButtonsBackground = Utils.tintDrawable(R.drawable.selector_default_white, color, getResources());
        Drawable backspaceButtonBackground = Utils.tintDrawable(R.drawable.selector_default_white, color, getResources());
        Drawable fingerprintButtonBackground = Utils.tintDrawable(R.drawable.bg_white_square, color, getResources());

        setButtonTheme(color, numButtonsBackground, backspaceButtonBackground, fingerprintButtonBackground, 0);
    }

    private void applySquareNoBoundsTheme() {
        int color = getColor();
        Drawable buttonsBackground = Utils.tintDrawable(R.drawable.selector_default_white_no_borders, color, getResources());

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
        mBackspaceButton.setImageDrawable(Utils.tintDrawable(R.drawable.ic_backspace_white_24dp, color, getResources()));

        ((LinearLayout.LayoutParams) mFingerprintButton.getLayoutParams()).setMargins(margin, margin, margin, margin);
        mFingerprintButton.setBackground(fingerprintButtonBackground);
        mFingerprintButton.setImageDrawable(Utils.tintDrawable(R.drawable.ic_fingerprint_white_24dp, color, getResources()));
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
        ROUND, SQUARE, SQUARE_NO_BORDERS;

        private static KeyboardFormStyle fromAttrs(int key) {
            switch (key) {
                case 1:
                    return SQUARE;
                case 2:
                    return SQUARE_NO_BORDERS;
                default:
                    return ROUND;
            }
        }
    }
}

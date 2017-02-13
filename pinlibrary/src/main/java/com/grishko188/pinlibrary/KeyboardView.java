package com.grishko188.pinlibrary;

import android.animation.Animator;
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
import android.view.ViewAnimationUtils;
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

    private KeyboardFormStyle mFormStyle = KeyboardFormStyle.fromAttrs(DEFAULT_STYLE);

    private int mKeyboardColor;
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
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.KeyboardView);

            if (array != null) {
                mFormStyle = KeyboardFormStyle.fromAttrs(array.getInteger(R.styleable.KeyboardView_kv_form_style, DEFAULT_STYLE));
                mKeyboardColor = array.getColor(R.styleable.KeyboardView_kv_color, -1);
                if (mKeyboardColor == -1) {
                    mKeyboardColor = getResources().getColor(LIGHT_COLOR);
                }
                mButtonTextSize = array.getDimensionPixelSize(R.styleable.KeyboardView_kv_button_text_size, (int) Utils.dp2px(getContext(), 16));
                array.recycle();
            }
        }
    }

    private void initKeyboardButtons() {
        mNumberButtons = new ArrayList<>();

        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_1));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_2));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_3));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_4));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_5));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_6));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_7));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_8));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_9));
        mNumberButtons.add((SquareButton) findViewById(R.id.pin_code_button_0));

        mBackspaceButton = (SquareImageButton) findViewById(R.id.backspace);
        mFingerprintButton = (SquareImageButton) findViewById(R.id.fingerprint);

        mFingerprintButton.setVisibility(INVISIBLE);
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
        switchIconWithAnimation(mFingerprintButton, getResources().getDrawable(R.drawable.ic_fingerprint_success));
    }

    /**
     * Display error icon in fingerprint container
     */
    @RequiresApi(23)
    public void showFingerprintFail() {
        if (!isFingerprintEnable)
            return;
        switchIconWithAnimation(mFingerprintButton, getResources().getDrawable(R.drawable.ic_fingerprint_error));
    }

    /**
     * Display fingerprint icon with animation
     */
    @RequiresApi(23)
    public void resetFingerprintState() {
        if (!isFingerprintEnable)
            return;
        switchIconWithAnimation(mFingerprintButton, DrawableUtil.tintDrawable(R.drawable.ic_fingerprint_white_24dp, mKeyboardColor, getResources()));
    }

    /**
     * Enable or disable fingerprint UI.
     * <br/> Actually hide or show fingerprint icon at the bottom and enable methods to display error or success state.
     */
    @RequiresApi(23)
    public void setFingerprintEnable(boolean isEnable) {
        this.isFingerprintEnable = isEnable;
    }

    /**
     * Display default fingerprint icon
     */
    @RequiresApi(23)
    public void startFingerprintListen() {
        if (!isFingerprintEnable)
            return;
        mFingerprintButton.setVisibility(VISIBLE);
        mFingerprintButton.setImageDrawable(DrawableUtil.tintDrawable(R.drawable.ic_fingerprint_white_24dp, mKeyboardColor, getResources()));
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
     * <p/>
     * <br/> All styles have general UI rules:
     * <br/><li/> Buttons are transparent
     * <br/><li/> Have buttons strokes or not depend on selected style
     * <br/><li/> For API version > 21, Buttons have ripple effect. Color of ripple effect depends on {@link KeyboardView#setKeyboardColor(int)}
     * <br/><li/> For API version < 21, Buttons have simple state list selector. Color of pressed state depends on {@link KeyboardView#setKeyboardColor(int)}  and will have some transparency}
     * <br/><br/> Default value is {@link KeyboardFormStyle#ROUND}
     */
    public void setFormStyle(KeyboardFormStyle mFormStyle) {
        this.mFormStyle = mFormStyle;
        applyStyle();
    }

    public void setFormStyle(int mFormStyle) {
        setFormStyle(KeyboardFormStyle.fromAttrs(mFormStyle));
    }

    /**
     * @return - keyboard custom color resource id
     */
    @ColorRes
    public int getKeyboardColor() {
        return mKeyboardColor;
    }

    /**
     * Setup custom color resource id.
     */
    public void setKeyboardColor(@ColorRes int keyboardCustomColor) {
        this.mKeyboardColor = keyboardCustomColor;
        applyStyle();
    }

    /**
     * Set text size for num buttons in pixel
     */
    public void setButtonsTextSize(int textSize) {
        this.mButtonTextSize = textSize;
        applyStyle();
    }

    /**
     * @return -text size for num buttons in pixel
     */
    public int getButtonsTextSize() {
        return mButtonTextSize;
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
        List<Drawable> numButtonsBackground = new ArrayList<>(mNumberButtons.size());
        for (int i = 0; i < mNumberButtons.size(); i++) {
            numButtonsBackground.add(DrawableUtil.tintBackgroundDrawable(R.drawable.selector_default_white_round, mKeyboardColor, getResources(), DrawableUtil.Mask.ROUND));
        }

        Drawable backspaceButtonBackground = DrawableUtil.tintBackgroundDrawable(R.drawable.selector_white_round_no_borders, mKeyboardColor, getResources(), DrawableUtil.Mask.ROUND);

        setButtonTheme(mKeyboardColor, numButtonsBackground, backspaceButtonBackground, null, (int) Utils.dp2px(getContext(), 8));
    }

    private void applyRoundNoBordersTheme() {
        List<Drawable> buttonsBackground = new ArrayList<>(mNumberButtons.size());
        for (int i = 0; i < mNumberButtons.size(); i++) {
            buttonsBackground.add(DrawableUtil.tintBackgroundDrawable(R.drawable.selector_white_round_no_borders, mKeyboardColor, getResources(), DrawableUtil.Mask.ROUND));
        }

        Drawable backspaceButtonBackground = DrawableUtil.tintBackgroundDrawable(R.drawable.selector_white_round_no_borders, mKeyboardColor, getResources(), DrawableUtil.Mask.ROUND);

        setButtonTheme(mKeyboardColor, buttonsBackground, backspaceButtonBackground, null, (int) Utils.dp2px(getContext(), 8));
    }

    private void applySquareTheme() {
        List<Drawable> numButtonsBackground = new ArrayList<>(mNumberButtons.size());
        for (int i = 0; i < mNumberButtons.size(); i++) {
            numButtonsBackground.add(DrawableUtil.tintBackgroundDrawable(R.drawable.selector_default_white, mKeyboardColor, getResources(), DrawableUtil.Mask.SQUARE));
        }

        Drawable backspaceButtonBackground = DrawableUtil.tintBackgroundDrawable(R.drawable.selector_default_white_no_borders, mKeyboardColor, getResources(), DrawableUtil.Mask.SQUARE);
        Drawable fingerprintButtonBackground = DrawableUtil.tintBackgroundDrawable(R.drawable.bg_white_square, mKeyboardColor, getResources(), DrawableUtil.Mask.SQUARE);

        setButtonTheme(mKeyboardColor, numButtonsBackground, backspaceButtonBackground, fingerprintButtonBackground, 0);
    }

    private void applySquareNoBoundsTheme() {
        List<Drawable> buttonsBackground = new ArrayList<>(mNumberButtons.size());
        for (int i = 0; i < mNumberButtons.size(); i++) {
            buttonsBackground.add(DrawableUtil.tintBackgroundDrawable(R.drawable.selector_default_white_no_borders, mKeyboardColor, getResources(), DrawableUtil.Mask.SQUARE));
        }

        Drawable backspaceButtonBackground = DrawableUtil.tintBackgroundDrawable(R.drawable.selector_default_white_no_borders, mKeyboardColor, getResources(), DrawableUtil.Mask.SQUARE);

        setButtonTheme(mKeyboardColor, buttonsBackground, backspaceButtonBackground, null, 0);
    }


    private void setButtonTheme(int color, List<Drawable> numButtonsBackground, Drawable backspaceButtonBackground, Drawable fingerprintButtonBackground, int margin) {

        for (SquareButton squareButton : mNumberButtons) {
            squareButton.setTextColor(color);
            squareButton.setBackground(numButtonsBackground.get(mNumberButtons.indexOf(squareButton))); //workaround for very very strange issue!!

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

    @RequiresApi(23)
    private void switchIconWithAnimation(final SquareImageButton view, Drawable newIcon) {
        view.setVisibility(INVISIBLE);
        view.setImageDrawable(newIcon);
        final int width = mFingerprintButton.getWidth();
        final int height = mFingerprintButton.getHeight();
        float maxRadius = (float) Math.sqrt(width * width / 4 + height * height / 4);
        Animator reveal = ViewAnimationUtils.createCircularReveal(mFingerprintButton, width / 2, height / 2, 0, maxRadius);
        reveal.setDuration(450);
        reveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        reveal.start();
    }

    public interface OnButtonsClickListener {

        void onNumButtonClick(View v, int number);

        void onBackspaceClick(View v);
    }
}

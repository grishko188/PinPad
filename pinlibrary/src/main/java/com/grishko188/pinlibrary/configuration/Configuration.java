package com.grishko188.pinlibrary.configuration;

import android.content.Context;
import android.support.annotation.StringRes;

import com.grishko188.pinlibrary.PinPadView;
import com.grishko188.pinlibrary.interfaces.OnFingerprintAuthListener;
import com.grishko188.pinlibrary.interfaces.OnHelpButtonsClickListener;
import com.grishko188.pinlibrary.interfaces.OnPinCodeListener;

import javax.crypto.Cipher;

/**
 * Created by Unreal Mojo
 *
 * @author Grishko Nikita
 *         on 10.10.2016.
 */
public class Configuration {

    private PinPadView.PinPadUsageMode mMode;

    private String mPinCodeTitle;
    private String mConfirmPinCodeTitle;
    private String mPinCodeForgotButtonTitle;
    private String mSkipButtonTitle;

    private boolean mIsShowSkipButton;
    private boolean mIsShowPinCodeTitle;
    private boolean mIsShowForgotButton;

    private boolean isFingerprintEnable;

    private Cipher mCryptoObject;
    private OnPinCodeListener mPinCodeListener;
    private OnFingerprintAuthListener mFingerprintAuthListener;
    private OnHelpButtonsClickListener mHelpButtonsListener;
    private Context mContext;

    private static Configuration INSTANCE;


    public static Configuration withContext(Context context) {
        if (INSTANCE == null)
            INSTANCE = new Configuration();
        INSTANCE.mContext = context;
        return INSTANCE;
    }


    public Configuration withUsageMode(PinPadView.PinPadUsageMode usageMode) {
        this.mMode = usageMode;
        return this;
    }

    public Configuration withPinCodeTitle(@StringRes int title) {
        return withPinCodeTitle(mContext.getString(title));
    }

    public Configuration withPinCodeTitle(String title) {
        this.mPinCodeTitle = title;
        this.mIsShowPinCodeTitle = true;
        return this;
    }

    public Configuration withConfirmPinCodeTitle(@StringRes int title) {
        return withConfirmPinCodeTitle(mContext.getString(title));
    }

    public Configuration withConfirmPinCodeTitle(String title) {
        this.mConfirmPinCodeTitle = title;
        return this;
    }

    public Configuration withForgotPinCodeTitle(@StringRes int title) {
        return withForgotPinCodeTitle(mContext.getString(title));
    }

    public Configuration withForgotPinCodeTitle(String title) {
        this.mPinCodeForgotButtonTitle = title;
        this.mIsShowForgotButton = true;
        return this;
    }

    public Configuration withSkipButtonTitle(@StringRes int title) {
        return withSkipButtonTitle(mContext.getString(title));
    }

    public Configuration withSkipButtonTitle(String title) {
        this.mSkipButtonTitle = title;
        this.mIsShowSkipButton = true;
        return this;
    }

    public Configuration showSkipButton(boolean isShow) {
        this.mIsShowSkipButton = isShow;
        return this;
    }

    public Configuration showPinTitle(boolean isShow) {
        this.mIsShowPinCodeTitle = isShow;
        return this;
    }

    public Configuration showForgotButton(boolean isShow) {
        this.mIsShowForgotButton = isShow;
        return this;
    }

    public Configuration withFigerprint(boolean isFingerprintEnable) {
        this.isFingerprintEnable = isFingerprintEnable;
        return this;
    }

    public Configuration withCryptoObject(Cipher cryptoObject) {
        this.mCryptoObject = cryptoObject;
        return this;
    }

    public Configuration withFingerprintAuthListener(OnFingerprintAuthListener listener) {
        this.mFingerprintAuthListener = listener;
        return this;
    }

    public Configuration withPinCodeListener(OnPinCodeListener listener) {
        this.mPinCodeListener = listener;
        return this;
    }

    public Configuration withHelpButtonsListener(OnHelpButtonsClickListener listener) {
        this.mHelpButtonsListener = listener;
        return this;
    }

    public void build(PinPadView view) {
        view.setUpNewConfiguration(this);
    }


    public PinPadView.PinPadUsageMode getMode() {
        return mMode;
    }

    public String getPinCodeTitle() {
        return mPinCodeTitle;
    }

    public String getPinCodeForgotButtonTitle() {
        return mPinCodeForgotButtonTitle;
    }

    public String getSkipButtonTitle() {
        return mSkipButtonTitle;
    }

    public boolean isShowSkipButton() {
        return mIsShowSkipButton;
    }

    public boolean isShowPinCodeTitle() {
        return mIsShowPinCodeTitle;
    }

    public boolean isShowForgotButton() {
        return mIsShowForgotButton;
    }

    public boolean isFingerprintEnable() {
        return isFingerprintEnable;
    }

    public Cipher getmCrytoObject() {
        return mCryptoObject;
    }

    public OnPinCodeListener getPinCodeListener() {
        return mPinCodeListener;
    }

    public OnFingerprintAuthListener getFingerpintAuthListener() {
        return mFingerprintAuthListener;
    }

    public OnHelpButtonsClickListener getHelpButtonsListener() {
        return mHelpButtonsListener;
    }
}

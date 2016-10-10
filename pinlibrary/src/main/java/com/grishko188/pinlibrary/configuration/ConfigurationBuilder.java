package com.grishko188.pinlibrary.configuration;

import android.content.Context;
import android.support.annotation.ColorRes;
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
public class ConfigurationBuilder {

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

    private static ConfigurationBuilder INSTANCE;


    public static ConfigurationBuilder withContext(Context context) {
        if (INSTANCE == null)
            INSTANCE = new ConfigurationBuilder();
        INSTANCE.mContext = context;
        return INSTANCE;
    }


    public ConfigurationBuilder withUsageMode(PinPadView.PinPadUsageMode usageMode) {
        this.mMode = usageMode;
        return this;
    }

    public ConfigurationBuilder withPinCodeTitle(@StringRes int title) {
        return withPinCodeTitle(mContext.getString(title));
    }

    public ConfigurationBuilder withPinCodeTitle(String title) {
        this.mPinCodeTitle = title;
        this.mIsShowPinCodeTitle = true;
        return this;
    }

    public ConfigurationBuilder withConfirmPinCodeTitle(@StringRes int title) {
        return withConfirmPinCodeTitle(mContext.getString(title));
    }

    public ConfigurationBuilder withConfirmPinCodeTitle(String title) {
        this.mConfirmPinCodeTitle = title;
        return this;
    }

    public ConfigurationBuilder withForgotPinCodeTitle(@StringRes int title) {
        return withForgotPinCodeTitle(mContext.getString(title));
    }

    public ConfigurationBuilder withForgotPinCodeTitle(String title) {
        this.mPinCodeForgotButtonTitle = title;
        this.mIsShowForgotButton = true;
        return this;
    }

    public ConfigurationBuilder withSkipButtonTitle(@StringRes int title) {
        return withSkipButtonTitle(mContext.getString(title));
    }

    public ConfigurationBuilder withSkipButtonTitle(String title) {
        this.mSkipButtonTitle = title;
        this.mIsShowSkipButton = true;
        return this;
    }

    public ConfigurationBuilder showSkipButton(boolean isShow) {
        this.mIsShowSkipButton = isShow;
        return this;
    }

    public ConfigurationBuilder showPinTitle(boolean isShow) {
        this.mIsShowPinCodeTitle = isShow;
        return this;
    }

    public ConfigurationBuilder showForgotButton(boolean isShow) {
        this.mIsShowForgotButton = isShow;
        return this;
    }

    public ConfigurationBuilder withFigerprint(boolean isFingerprintEnable) {
        this.isFingerprintEnable = isFingerprintEnable;
        return this;
    }

    public ConfigurationBuilder withCryptoObject(Cipher cryptoObject) {
        this.mCryptoObject = cryptoObject;
        return this;
    }

    public ConfigurationBuilder withFingerprintAuthListener(OnFingerprintAuthListener listener) {
        this.mFingerprintAuthListener = listener;
        return this;
    }

    public ConfigurationBuilder withPinCodeListener(OnPinCodeListener listener) {
        this.mPinCodeListener = listener;
        return this;
    }

    public ConfigurationBuilder withHelpButtonsListener(OnHelpButtonsClickListener listener) {
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

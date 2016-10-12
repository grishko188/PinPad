package com.grishko188.pinlibrary.configuration;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import com.grishko188.pinlibrary.PinPadView;
import com.grishko188.pinlibrary.interfaces.OnFingerprintAuthListener;
import com.grishko188.pinlibrary.interfaces.OnHelpButtonsClickListener;
import com.grishko188.pinlibrary.interfaces.OnPinCodeListener;
import com.grishko188.pinlibrary.interfaces.OnSetupPinCodeListener;

/**
 * Created by Unreal Mojo
 *
 * @author Grishko Nikita
 *         on 10.10.2016.
 */
public class Configuration {

    private PinPadView.PinPadUsageMode mode = PinPadView.PinPadUsageMode.ENTER;

    private String pinCodeTitle;
    private String confirmPinCodeTitle;
    private String pinCodeForgotButtonTitle;
    private String skipButtonTitle;

    private boolean showSkipButton = mode != PinPadView.PinPadUsageMode.ENTER;
    private boolean showPinCodeTitle = true;
    private boolean showForgotButton = mode == PinPadView.PinPadUsageMode.ENTER;

    private boolean isFingerprintEnable;

    private FingerprintManagerCompat.CryptoObject cryptoObject;
    private OnPinCodeListener pinCodeListener;
    private OnFingerprintAuthListener fingerprintAuthListener;
    private OnHelpButtonsClickListener helpButtonsListener;
    private OnSetupPinCodeListener setupPinCodeListener;
    private Context context;

    private static ConfigurationBuilder singleton;

    public static ConfigurationBuilder withContext(Context context) {
        if (singleton == null)
            singleton = new ConfigurationBuilder(context);
        return singleton;
    }

    public PinPadView.PinPadUsageMode getMode() {
        return mode;
    }

    public String getPinCodeTitle() {
        return pinCodeTitle;
    }

    public String getPinCodeForgotButtonTitle() {
        return pinCodeForgotButtonTitle;
    }

    public String getSkipButtonTitle() {
        return skipButtonTitle;
    }

    public boolean isShowSkipButton() {
        return showSkipButton;
    }

    public boolean isShowPinCodeTitle() {
        return showPinCodeTitle;
    }

    public boolean isShowForgotButton() {
        return showForgotButton;
    }

    public boolean isFingerprintEnable() {
        return isFingerprintEnable;
    }

    public FingerprintManagerCompat.CryptoObject getCryptoObject() {
        return cryptoObject;
    }

    public OnPinCodeListener getPinCodeListener() {
        return pinCodeListener;
    }

    public OnFingerprintAuthListener getFingerprintAuthListener() {
        return fingerprintAuthListener;
    }

    public OnHelpButtonsClickListener getHelpButtonsListener() {
        return helpButtonsListener;
    }

    public String getConfirmPinCodeTitle() {
        return confirmPinCodeTitle;
    }

    public OnSetupPinCodeListener getSetupPinCodeListener() {
        return setupPinCodeListener;
    }


    public static class ConfigurationBuilder {

        private Configuration resultConfiguration;

        public ConfigurationBuilder(Context context) {
            resultConfiguration = new Configuration();
            resultConfiguration.context = context;
        }

        public ConfigurationBuilder mode(PinPadView.PinPadUsageMode usageMode) {
            resultConfiguration.mode = usageMode;
            return this;
        }

        public ConfigurationBuilder withPinCodeTitle(@StringRes int title) {
            return withPinCodeTitle(resultConfiguration.context.getString(title));
        }

        public ConfigurationBuilder withPinCodeTitle(String title) {
            resultConfiguration.pinCodeTitle = title;
            resultConfiguration.showPinCodeTitle = true;
            return this;
        }

        public ConfigurationBuilder withConfirmPinCodeTitle(@StringRes int title) {
            return withConfirmPinCodeTitle(resultConfiguration.context.getString(title));
        }

        public ConfigurationBuilder withConfirmPinCodeTitle(String title) {
            resultConfiguration.confirmPinCodeTitle = title;
            return this;
        }

        public ConfigurationBuilder withForgotPinCodeTitle(@StringRes int title) {
            return withForgotPinCodeTitle(resultConfiguration.context.getString(title));
        }

        public ConfigurationBuilder withForgotPinCodeTitle(String title) {
            resultConfiguration.pinCodeForgotButtonTitle = title;
            resultConfiguration.showForgotButton = true;
            return this;
        }

        public ConfigurationBuilder withSkipButtonTitle(@StringRes int title) {
            return withSkipButtonTitle(resultConfiguration.context.getString(title));
        }

        public ConfigurationBuilder withSkipButtonTitle(String title) {
            resultConfiguration.skipButtonTitle = title;
            resultConfiguration.showSkipButton = true;
            return this;
        }

        public ConfigurationBuilder showSkipButton(boolean isShow) {
            resultConfiguration.showSkipButton = isShow;
            return this;
        }

        public ConfigurationBuilder showPinTitle(boolean isShow) {
            resultConfiguration.showPinCodeTitle = isShow;
            return this;
        }

        public ConfigurationBuilder showForgotButton(boolean isShow) {
            resultConfiguration.showForgotButton = isShow;
            return this;
        }

        public ConfigurationBuilder useFingerprint(boolean isFingerprintEnable) {
            resultConfiguration.isFingerprintEnable = isFingerprintEnable;
            return this;
        }

        public ConfigurationBuilder withCryptoObject(FingerprintManagerCompat.CryptoObject cryptoObject) {
            resultConfiguration.cryptoObject = cryptoObject;
            return this;
        }

        public ConfigurationBuilder setFingerprintAuthListener(OnFingerprintAuthListener listener) {
            resultConfiguration.fingerprintAuthListener = listener;
            return this;
        }

        public ConfigurationBuilder setPinCodeListener(OnPinCodeListener listener) {
            resultConfiguration.pinCodeListener = listener;
            return this;
        }

        public ConfigurationBuilder setHelpButtonsListener(OnHelpButtonsClickListener listener) {
            resultConfiguration.helpButtonsListener = listener;
            return this;
        }

        public ConfigurationBuilder setSetupPinCodeListener(OnSetupPinCodeListener listener) {
            resultConfiguration.setupPinCodeListener = listener;
            return this;
        }

        public void build(PinPadView view) {
            view.setUpNewConfiguration(resultConfiguration);
        }

    }
}

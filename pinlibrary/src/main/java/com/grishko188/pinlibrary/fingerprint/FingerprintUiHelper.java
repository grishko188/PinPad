

package com.grishko188.pinlibrary.fingerprint;

import android.content.Context;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.widget.Toast;

import com.grishko188.pinlibrary.KeyboardView;
import com.grishko188.pinlibrary.R;
import com.grishko188.pinlibrary.interfaces.OnFingerprintAuthListener;


/**
 * Small helper class to manage text/icon around fingerprint authentication UI.
 */

@SuppressWarnings("NewApi")
public class FingerprintUiHelper extends FingerprintManagerCompat.AuthenticationCallback {

    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private static final long SUCCESS_DELAY_MILLIS = 1300;

    private final FingerprintManagerCompat mFingerprintManager;
    private final KeyboardView mKeyboard;
    private final OnFingerprintAuthListener mCallback;
    private CancellationSignal mCancellationSignal;

    private boolean mSelfCancelled;

    /**
     * Builder class for {@link FingerprintUiHelper}
     */
    public static class FingerprintUiHelperBuilder {
        private final FingerprintManagerCompat mFingerPrintManager;

        public FingerprintUiHelperBuilder(Context context) {
            mFingerPrintManager = FingerprintManagerCompat.from(context);
        }

        public FingerprintUiHelper build(KeyboardView keyboardView, OnFingerprintAuthListener callback) {
            return new FingerprintUiHelper(mFingerPrintManager, keyboardView, callback);
        }
    }

    /**
     * Constructor for {@link FingerprintUiHelper}. This method is expected to be called from
     * only the {@link FingerprintUiHelperBuilder} class.
     */
    private FingerprintUiHelper(FingerprintManagerCompat fingerprintManager, KeyboardView keyboardView, OnFingerprintAuthListener callback) {
        mFingerprintManager = fingerprintManager;
        mKeyboard = keyboardView;
        mCallback = callback;
    }

    public boolean isFingerprintAuthAvailable() {
        return mFingerprintManager.isHardwareDetected()
                && mFingerprintManager.hasEnrolledFingerprints();
    }

    public void startListening(FingerprintManagerCompat.CryptoObject cryptoObject) {
        if (!isFingerprintAuthAvailable()) {
            return;
        }
        mCancellationSignal = new CancellationSignal();
        mSelfCancelled = false;
        mFingerprintManager.authenticate(cryptoObject, 0, mCancellationSignal, this, null);
        mKeyboard.startFingerprintListen();
    }

    public void stopListening() {
        if (mCancellationSignal != null) {
            mSelfCancelled = true;
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        if (!mSelfCancelled) {
            showError(errString.toString());
            mKeyboard.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mCallback != null)
                        mCallback.onError();
                }
            }, ERROR_TIMEOUT_MILLIS);
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        showError(helpString.toString());
        Toast.makeText(mKeyboard.getContext(), helpString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
        showError(mKeyboard.getResources().getString(R.string.fingerprint_not_recognized));
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        mKeyboard.showFingerprintSuccess();
        mKeyboard.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null)
                    mCallback.onAuthenticated();
            }
        }, SUCCESS_DELAY_MILLIS);
    }

    private void showError(String errString) {
        Toast.makeText(mKeyboard.getContext(), errString, Toast.LENGTH_SHORT).show();
        mKeyboard.showFingerprintFail();
        mKeyboard.removeCallbacks(mResetErrorRunnable);
        mKeyboard.postDelayed(mResetErrorRunnable, ERROR_TIMEOUT_MILLIS);
    }

    private Runnable mResetErrorRunnable = new Runnable() {
        @Override
        public void run() {
            mKeyboard.resetFingerprintState();
        }
    };

}

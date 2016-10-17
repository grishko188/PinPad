package com.grishko188.pinpad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.grishko188.pinlibrary.PinPadView;
import com.grishko188.pinlibrary.configuration.Configuration;
import com.grishko188.pinlibrary.interfaces.OnFingerprintAuthListener;
import com.grishko188.pinlibrary.interfaces.OnHelpButtonsClickListener;
import com.grishko188.pinlibrary.interfaces.OnPinCodeListener;

public class MainActivity extends AppCompatActivity {
    PinPadView mPinPad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPinPad = (PinPadView) findViewById(R.id.pin_pad);

        Configuration.withContext(this)
                .mode(PinPadView.PinPadUsageMode.ENTER)
                .setPinCodeListener(new OnPinCodeListener() {
                    @Override
                    public void onPinEntered(String correctPinCode) {
                        showToast("Success: " + correctPinCode);
                    }

                    @Override
                    public void onPinError(int triesLeft) {
                        showToast("Pin Error, tries left: " + triesLeft);
                    }

                    @Override
                    public void onPinEnterFail() {
                        showToast("FAIL!");
                    }

                    @Override
                    public boolean verifyPinCode(String input) {
                        return "1111".equalsIgnoreCase(input);
                    }
                }).setHelpButtonsListener(new OnHelpButtonsClickListener() {
            @Override
            public void onForgotPinCode(View v) {
                showToast("Forgot");
            }

            @Override
            public void onSkip(View v) {
                showToast("Skip");
            }
        }).showSkipButton(true)
                .useFingerprint(true)
                .setFingerprintAuthListener(new OnFingerprintAuthListener() {
                    @Override
                    public void onAuthenticated() {
                        showToast("Fingerprint onAuthenticated");
                    }

                    @Override
                    public void onError() {
                        showToast("Fingerprint Error");
                    }
                }).build(mPinPad);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPinPad.startFingerprintScanner(); //Call if want to work with fingerprint
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPinPad.stopFingerprintScanner(); //Call if want to work with fingerprint
    }

    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}

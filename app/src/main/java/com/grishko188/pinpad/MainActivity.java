package com.grishko188.pinpad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.grishko188.pinlibrary.PinPadView;
import com.grishko188.pinlibrary.configuration.Configuration;
import com.grishko188.pinlibrary.interfaces.OnHelpButtonsClickListener;
import com.grishko188.pinlibrary.interfaces.OnSetupPinCodeListener;

public class MainActivity extends AppCompatActivity {
    PinPadView mPinPad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPinPad = (PinPadView) findViewById(R.id.pin_pad);

        Configuration.withContext(this)
                .mode(PinPadView.PinPadUsageMode.SETUP)
                .setSetupPinCodeListener(new OnSetupPinCodeListener() {
                    @Override
                    public void onSuccess(String pinCode) {
                        Toast.makeText(MainActivity.this, "Success " + pinCode, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(MainActivity.this, "FAIL ", Toast.LENGTH_SHORT).show();
                    }
                }).setHelpButtonsListener(new OnHelpButtonsClickListener() {
            @Override
            public void onForgotPinCode(View v) {
                Toast.makeText(MainActivity.this, "F!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSkip(View v) {
                Toast.makeText(MainActivity.this, "Skip", Toast.LENGTH_SHORT).show();
            }
        }).showSkipButton(true).build(mPinPad);

    }
}

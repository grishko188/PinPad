package com.grishko188.pinpad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.grishko188.pinlibrary.KeyboardView;
import com.grishko188.pinlibrary.PinCodeField;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PinCodeField field = (PinCodeField) findViewById(R.id.pin_code);
        KeyboardView keyboardView = (KeyboardView) findViewById(R.id.key_board);
        keyboardView.attach(field);
    }
}

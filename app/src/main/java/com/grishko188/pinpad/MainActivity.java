package com.grishko188.pinpad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.grishko188.pinlibrary.KeyboardView;
import com.grishko188.pinlibrary.PinCodeField;
import com.grishko188.pinlibrary.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

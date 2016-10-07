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
        PinCodeField field = (PinCodeField) findViewById(R.id.pin_code);
        final KeyboardView keyboardView = (KeyboardView) findViewById(R.id.key_board);
        keyboardView.attach(field);

        keyboardView.addOnClickListener(new KeyboardView.OnButtonsClickListener() {
            @Override
            public void onNumButtonClick(View v, int number) {
                Log.d("AA", "Click NumButton " + number);
            }

            @Override
            public void onBackspaceClick(View v) {
                Log.d("AA", "Click backspace");
            }
        });

        keyboardView.setFingerprintEnable(Utils.isLollipop());

    }
}

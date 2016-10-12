# PinPad
PinPad view for android.
![Alt text](https://github.com/grishko188/PinPad/blob/master/screenshot/Screenshot_1.png?raw=true "Screen shot") ![Alt text](https://github.com/grishko188/PinPad/blob/master/screenshot/Screenshot_2.png?raw=true "Screen shot")
<br/>
<br/>
Highly customizable PinPad view for android applications:
<br><li>Contains completely finished Pin Pad UI for your application
<br><li>Supports fingerprint authentication
<br><li>KeyboardView and PinCodeField can be used alone to build your own unique interface
<br><li>Different styles for keyboard directly from the box
<br><li>No encryption/decryption logic, no secure storage - **ONLY UI**
<br/><br/>
#### PinPadView Usage:
<br/>1) Define your PinPadView in layout:
```
<com.grishko188.pinlibrary.PinPadView
    android:id="@+id/pin_pad"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```
<br/>2) Initialize singleton configuration 
```
        Configuration.withContext(this)
                .mode(PinPadView.PinPadUsageMode.ENTER)
                ...
                .build(mPinPad);
```
or create new instance as shown below: 
```
 Configuration.ConfigurationBuilder builder = new Configuration.ConfigurationBuilder(this);
 
        builder.mode(PinPadView.PinPadUsageMode.SETUP);
        builder.setSetupPinCodeListener(new OnSetupPinCodeListener() {
            @Override
            public void onSuccess(String pinCode) {
                
            }

            @Override
            public void onFail() {

            }
        });
        ...
        builder.build(mPinPad);
```
<br/>3) Enable fingerprint
```
        Configuration.withContext(this)
                .mode(PinPadView.PinPadUsageMode.ENTER)
                .useFingerprint(true)
                .withCryptoObject(new FingerprintManagerCompat.CryptoObject(mCipher))
                ...
                .build(mPimPad);
```
<br/>3) Start / stop listening fingerprint
```
    @Override
    protected void onResume() {
        super.onResume();
        mPinPad.onResume(); 
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPinPad.onPause();
    }
```
<br/><br/>
#### PinPadView Customization:
<br/>1)UI with xml attributes:
```
<attr name="ppv_color" format="color" />
<attr name="ppv_buttons_text_size" format="dimension|reference" />
<attr name="ppv_keyboard_form_style">
     <enum name="round" value="0" />
     <enum name="square" value="1" />
     <enum name="square_no_borders" value="2" />
     <enum name="round_no_borders" value="3" />
</attr>
<attr name="ppv_empty_char_style">
    <enum name="stroke" value="1" />
    <enum name="fill" value="0" />
</attr>
<attr name="ppv_max_len" format="integer|reference" />
<attr name="ppv_size" format="dimension|reference" />
<attr name="ppv_letter_spacing" format="dimension|reference" />
<attr name="ppv_fill_color" format="color" />
<attr name="ppv_max_try" format="integer" />
```
<br/>2) UI from Java code:
```
 public void setColor(int color) 
 public void setKeyboardTextSize(int keyboardTextSize)
 public void setMaxLength(int maxLength)
 public void setSize(float size)
 public void setMaxTryCount(int maxTryCount)
 public void setEmptyCharFillColor(int fillColor)
 public void setLetterSpacing(int letterSpacing)
```
######## README and Sample poject still in progress
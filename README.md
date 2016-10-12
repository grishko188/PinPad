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
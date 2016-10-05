package com.grishko188.pinlibrary.interfaces;

/**
 * Created by Unreal Mojo
 *
 * @author Grishko Nikita
 *         on 05.10.2016.
 */
public interface OnPinCodeListener {

    void onPinEntered(String correctPinCode);

    void onPinError(int triesLeft);

    void onPinEnterFail();

    boolean verifyPinCode(String input);

}

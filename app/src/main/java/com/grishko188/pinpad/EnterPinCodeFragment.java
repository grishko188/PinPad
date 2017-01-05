package com.grishko188.pinpad;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grishko188.pinlibrary.PinPadView;
import com.grishko188.pinlibrary.configuration.Configuration;
import com.grishko188.pinlibrary.interfaces.OnHelpButtonsClickListener;
import com.grishko188.pinlibrary.interfaces.OnPinCodeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Unreal Mojo
 *
 * @author Grishko Nikita
 *         on 05.01.2017.
 */

public class EnterPinCodeFragment extends Fragment {

    @BindView(R.id.pin_pad)
    PinPadView mPinPad;

    public static EnterPinCodeFragment getInstance() {
        return new EnterPinCodeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_enter_pincode, null);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Configuration.withContext(getActivity())
                .mode(PinPadView.PinPadUsageMode.ENTER)
                .showSkipButton(false)
                .showForgotButton(true)
                .withForgotPinCodeTitle(R.string.title_forgot_pin)
                .setHelpButtonsListener(new OnHelpButtonsClickListener() {
                    @Override
                    public void onForgotPinCode(View v) {
                        Utils.showDialog(getActivity(), getString(R.string.pin_code_enter_help));
                    }

                    @Override
                    public void onSkip(View v) {
                    }
                })
                .setPinCodeListener(new OnPinCodeListener() {
                    @Override
                    public void onPinEntered(String correctPinCode) {
                        Utils.showDialog(getActivity(), getString(R.string.success));
                        mPinPad.reset();
                    }

                    @Override
                    public void onPinError(int triesLeft) {
                        Utils.showToast(getActivity(), getString(R.string.tries_left, triesLeft));
                    }

                    @Override
                    public void onPinEnterFail() {
                        Utils.showDialog(getActivity(), getString(R.string.fail));
                        mPinPad.reset();
                    }

                    @Override
                    public boolean verifyPinCode(String input) {
                        return "1111".equalsIgnoreCase(input);
                    }
                })
                .build(mPinPad);
    }
}

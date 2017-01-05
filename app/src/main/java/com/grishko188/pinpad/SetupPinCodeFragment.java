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
import com.grishko188.pinlibrary.interfaces.OnSetupPinCodeListener;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Unreal Mojo
 *
 * @author Grishko Nikita
 *         on 05.01.2017.
 */

public class SetupPinCodeFragment extends Fragment {

    @BindView(R.id.pin_pad)
    PinPadView mPinPad;

    public static SetupPinCodeFragment getInstance() {
        return new SetupPinCodeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_setup_pincode, null);
        ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Configuration.withContext(getActivity())
                .mode(PinPadView.PinPadUsageMode.SETUP)
                .showSkipButton(true)
                .showForgotButton(true)
                .withForgotPinCodeTitle(R.string.what_is_it)
                .setHelpButtonsListener(new OnHelpButtonsClickListener() {
                    @Override
                    public void onForgotPinCode(View v) {
                        Utils.showDialog(getActivity(), getString(R.string.setup_pin_faq));
                    }

                    @Override
                    public void onSkip(View v) {
                        Utils.showToast(getActivity(), "Skip PinCode setup");
                    }
                })
                .setSetupPinCodeListener(new OnSetupPinCodeListener() {
                    @Override
                    public void onSuccess(String pinCode) {
                        Utils.showDialog(getActivity(), getString(R.string.pin_code_setup_success, pinCode));
                    }

                    @Override
                    public void onFail() {
                        Utils.showDialog(getActivity(), getString(R.string.invalid_pin_code));
                    }
                })
                .build(mPinPad);
    }
}

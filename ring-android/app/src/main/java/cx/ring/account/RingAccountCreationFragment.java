/*
 *  Copyright (C) 2004-2018 Savoir-faire Linux Inc.
 *
 *  Author: Adrien Béraud <adrien.beraud@savoirfairelinux.com>
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package cx.ring.account;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import cx.ring.R;
import cx.ring.dependencyinjection.RingInjectionComponent;
import cx.ring.model.Account;
import cx.ring.mvp.BaseSupportFragment;
import cx.ring.mvp.AccountCreationModel;
import cx.ring.utils.RegisteredNameFilter;
import io.reactivex.Observable;

public class RingAccountCreationFragment extends BaseSupportFragment<RingAccountCreationPresenter> implements RingAccountCreationView {

    public static final String KEY_RING_ACCOUNT = "RING_ACCOUNT";

    @BindView(R.id.switch_ring_username)
    protected Switch mUsernameSwitch;

    @BindView(R.id.ring_username_txt_box)
    protected TextInputLayout mUsernameTxtBox;

    @BindView(R.id.ring_username)
    protected EditText mUsernameTxt;

    @BindView(R.id.ring_password_switch)
    protected Switch mPasswordSwitch;

    @BindView(R.id.ring_password_box)
    protected ViewGroup mPasswordBox;

    @BindView(R.id.ring_password_txt_box)
    protected TextInputLayout mPasswordTxtBox;

    @BindView(R.id.ring_password_repeat_txt_box)
    protected TextInputLayout mPasswordRepeatTxtBox;

    @BindView(R.id.ring_username_box)
    protected ViewGroup mUsernameBox;

    @BindView(R.id.switch_ring_push)
    protected Switch mPushSwitch;

    @BindView(R.id.create_account)
    protected Button mCreateAccountButton;

    public static RingAccountCreationFragment newInstance(AccountCreationModelImpl ringAccountViewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_RING_ACCOUNT, ringAccountViewModel);
        RingAccountCreationFragment fragment = new RingAccountCreationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.frag_acc_ring_create;
    }

    @Override
    public void injectFragment(RingInjectionComponent component) {
        component.inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRetainInstance(true);

        ButterKnife.bind(this, view);
        mUsernameTxt.setFilters(new InputFilter[]{new RegisteredNameFilter()});

        AccountCreationModelImpl ringAccountViewModel = getArguments().getParcelable(KEY_RING_ACCOUNT);
        presenter.init(ringAccountViewModel);
        presenter.setPush(mPushSwitch.isChecked());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUsernameBox.getVisibility() == View.VISIBLE) {
            mUsernameTxt.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mUsernameTxt, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @OnCheckedChanged(R.id.ring_password_switch)
    public void onPasswordCheckedChanged(boolean isChecked) {
        mPasswordBox.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @OnCheckedChanged(R.id.switch_ring_push)
    public void onPushCheckedChanged(boolean isChecked) {
        presenter.setPush(isChecked);
    }

    @OnCheckedChanged(R.id.switch_ring_username)
    public void onCheckedChanged(boolean isChecked) {
        presenter.ringCheckChanged(isChecked);
    }

    @OnClick(R.id.create_account)
    public void onCreateAccountButtonClick() {
        presenter.createAccount();
    }

    @OnTextChanged(value = R.id.ring_username, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onUsernameChanged() {
        mUsernameTxt.setError(null);
    }

    @OnTextChanged(value = R.id.ring_password, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void afterPasswordChanged(Editable txt) {
        presenter.passwordChanged(txt.toString());
    }

    @OnTextChanged(value = R.id.ring_password_repeat, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void afterPasswordConfirmChanged(Editable txt) {
        presenter.passwordConfirmChanged(txt.toString());
    }

    @OnEditorAction(value = R.id.ring_password_repeat)
    public boolean onPasswordConfirmDone(int keyCode) {
        if (keyCode == EditorInfo.IME_ACTION_DONE) {
            presenter.createAccount();
        }
        return false;
    }

    @OnTextChanged(value = R.id.ring_username, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterUsernameChanged(Editable txt) {
        presenter.userNameChanged(txt.toString());
    }

    @Override
    public void enableTextError() {
        getActivity().runOnUiThread(() -> {
            mUsernameTxtBox.setErrorEnabled(true);
            mUsernameTxtBox.setError(getString(R.string.looking_for_username_availability));

        });
    }

    @Override
    public void disableTextError() {
        getActivity().runOnUiThread(() -> {
            mUsernameTxtBox.setErrorEnabled(false);
            mUsernameTxtBox.setError(null);
        });
    }

    @Override
    public void showExistingNameError() {
        getActivity().runOnUiThread(() -> {
            mUsernameTxtBox.setErrorEnabled(true);
            mUsernameTxtBox.setError(getString(R.string.username_already_taken));
        });
    }

    @Override
    public void showInvalidNameError() {
        getActivity().runOnUiThread(() -> {
            mUsernameTxtBox.setErrorEnabled(true);
            mUsernameTxtBox.setError(getString(R.string.invalid_username));
        });
    }

    @Override
    public void showInvalidPasswordError(final boolean display) {
        getActivity().runOnUiThread(() -> {
            if (display) {
                mPasswordTxtBox.setError(getString(R.string.error_password_char_count));
            } else {
                mPasswordTxtBox.setError(null);
            }
        });
    }

    @Override
    public void showNonMatchingPasswordError(final boolean display) {
        getActivity().runOnUiThread(() -> {
            if (display) {
                mPasswordRepeatTxtBox.setError(getString(R.string.error_passwords_not_equals));
            } else {
                mPasswordRepeatTxtBox.setError(null);
            }
        });
    }

    @Override
    public void displayUsernameBox(final boolean display) {
        getActivity().runOnUiThread(() -> mUsernameBox.setVisibility(display ? View.VISIBLE : View.GONE));
    }

    @Override
    public void enableNextButton(final boolean enabled) {
        getActivity().runOnUiThread(() -> mCreateAccountButton.setEnabled(enabled));
    }

    @Override
    public void goToAccountCreation(AccountCreationModel accountCreationModel) {
        Activity wizardActivity = getActivity();
        if (wizardActivity instanceof AccountWizardActivity) {
            AccountWizardActivity wizard = (AccountWizardActivity) wizardActivity;
            wizard.createAccount(accountCreationModel);
        }
    }
}

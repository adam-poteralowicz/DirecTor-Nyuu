package com.apap.director.client.presentation.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apap.director.client.App;
import com.apap.director.client.R;
import com.apap.director.client.data.exception.DuplicateException;
import com.apap.director.client.presentation.ui.register.contract.RegisterContract;
import com.apap.director.client.presentation.ui.register.di.component.DaggerRegisterContractComponent;
import com.apap.director.client.presentation.ui.register.di.module.RegisterContractModule;
import com.apap.director.client.presentation.ui.register.presenter.RegisterPresenter;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewAccountActivity extends Activity implements Validator.ValidationListener, RegisterContract.View {

    private final String RESULT_EXTRA = "result";

    @Length(min = 1)
    @BindView(R.id.contactNameEditText)
    EditText accountNameEditText;

    @BindView(R.id.saveAccButton)
    Button saveButton;

    @Inject
    RegisterPresenter presenter;

    private Validator validator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_acc_view);

        ButterKnife.bind(this);

        saveButton.setEnabled(false);

        setUpInjection();
        setUpValidator();

        accountNameEditText.setHint("ACCOUNT NAME");
    }

    @OnClick(R.id.saveAccButton)
    public void saveAccount(View view) {
        String accountName = String.valueOf(accountNameEditText.getText());

        presenter.signUp(accountName);
    }

    private void setUpValidator() {
        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.validate();
    }

    private void setUpInjection() {
        DaggerRegisterContractComponent.builder()
                .mainComponent(((App) getApplication()).getComponent())
                .registerContractModule(new RegisterContractModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onValidationSucceeded() {
        Log.v(NewAccountActivity.class.getSimpleName(), "Name validation succeeded");
        saveButton.setEnabled(true);
        saveButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        saveButton.setEnabled(false);
    }

    @Override
    public void handleException(Throwable throwable) {
        if(throwable instanceof DuplicateException) {
            Snackbar.make(getCurrentFocus(), throwable.getMessage(), Snackbar.LENGTH_LONG)
                    .show();
        }

        Log.getStackTraceString(throwable);
    }

    @Override
    public void handleSuccess() {
        Log.v(NewAccountActivity.class.getSimpleName(), "Registered");

        Intent returnIntent = new Intent();
        returnIntent.putExtra(RESULT_EXTRA, true);
        setResult(Activity.RESULT_OK,returnIntent);

        finish();
    }

}

package com.quascenta.petersroad.Utils.Validators;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by AKSHAY on 12/22/2016.
 */

public class SameValueValidator extends Validator {
    private final EditText otherEditText;

    public SameValueValidator(EditText otherEditText, String errorMessage) {
        super(errorMessage);
        this.otherEditText = otherEditText;
    }

    @Override
    public boolean isValid(String x) {
        return false;
    }

    @Override
    public boolean isValid(EditText editText) {
        return TextUtils.equals(editText.getText(), otherEditText.getText());
    }
}
package com.quascenta.petersroad.Utils.Validators;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by AKSHAY on 12/22/2016.
 */

public class NumericValidator extends Validator {
    public NumericValidator(String _customErrorMessage) {
        super(_customErrorMessage);
    }

    @Override
    public boolean isValid(String x) {
        return false;
    }

    public boolean isValid(EditText et) {
        return TextUtils.isDigitsOnly(et.getText());
    }
}

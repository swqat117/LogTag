package com.quascenta.petersroad.Utils.Validators;

import android.widget.EditText;

/**
 * Created by AKSHAY on 12/22/2016.
 */

public class NotValidator extends Validator {

    private Validator validator;
    public NotValidator(String errorMessage, Validator _v) {
        super(errorMessage);
        validator = _v;
    }

    @Override
    public boolean isValid(String x) {
        return false;
    }

    public boolean isValid(EditText et) {
        return !validator.isValid(et);
    }

}

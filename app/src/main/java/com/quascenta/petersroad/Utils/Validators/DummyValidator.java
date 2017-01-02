package com.quascenta.petersroad.Utils.Validators;

import android.widget.EditText;

/**
 * Created by AKSHAY on 12/22/2016.
 */

public class DummyValidator extends Validator {
    public DummyValidator() {
        super(null);
    }

    @Override
    public boolean isValid(String x) {
        return false;
    }

    public boolean isValid(EditText et) {
        return true;
    }
}

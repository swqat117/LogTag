package com.quascenta.petersroad.Utils.Validators;

import android.widget.EditText;

/**
 * Created by AKSHAY on 12/22/2016.
 */

public abstract class DigitLengthRangeValidator extends Validator {
    private int min, max;

    public DigitLengthRangeValidator(String message, int min, int max) {
        super(message);
        this.min = min;
        this.max = max;
    }

    public boolean isValid(EditText et) {
        int length = et.getText().toString().length();
        return length >= min && length < max;
    }

}
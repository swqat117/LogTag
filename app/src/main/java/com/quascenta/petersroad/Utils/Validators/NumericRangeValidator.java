package com.quascenta.petersroad.Utils.Validators;

import android.widget.EditText;

/**
 * Created by AKSHAY on 12/22/2016.
 */

public class NumericRangeValidator extends Validator {
    private int min, max;

    public NumericRangeValidator(String _customErrorMessage, int min, int max) {
        super(_customErrorMessage);
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isValid(String x) {
        return false;
    }

    public boolean isValid(EditText et) {
        try {
            int value = Integer.parseInt(et.getText().toString());
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

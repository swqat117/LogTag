package com.quascenta.petersroad.Utils.Validators;

import android.widget.EditText;

import java.util.regex.Pattern;



/**
 * Created by AKSHAY on 12/22/2016.
 */

public class PatternValidator extends Validator {
    private Pattern pattern;

    public PatternValidator(String _customErrorMessage, Pattern _pattern) {
        super(_customErrorMessage);
        if (_pattern == null) throw new IllegalArgumentException("_pattern must not be null");
        pattern = _pattern;
    }

    @Override
    public boolean isValid(String x) {
        return false;
    }

    public boolean isValid(EditText et) {
        return pattern.matcher(et.getText()).matches();
    }

}
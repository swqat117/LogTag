package com.quascenta.petersroad.Utils.Validators;

import android.widget.EditText;

/**
 * Created by AKSHAY on 12/22/2016.
 */

public class OrValidator extends MultiValidator {

    public OrValidator(String message, Validator... validators) {
        super(message, validators);
    }

    @Override
    public boolean isValid(String x) {
        return false;
    }

    public boolean isValid(EditText et) {
        //TODO: What if we've no validators ?
        for (Validator v : validators) {
            if (v.isValid(et)) {
                return true; // Remember :) We're acting like an || operator.
            }
        }
        return false;
    }

}


package com.quascenta.petersroad.Utils.Validators;

import android.widget.EditText;

/**
 * Created by AKSHAY on 12/22/2016.
 */

public class AndValidator extends MultiValidator {
    public AndValidator(Validator... validators) {
        super(null, validators);
    }

    public AndValidator() {
        super(null);
    }

    @Override
    public boolean isValid(String x) {
        return false;
    }

    public boolean isValid(EditText et) {
        for (Validator v : validators) {
            if (!v.isValid(et)) {
                this.errorMessage = v.getErrorMessage();
                return false; // Remember :) We're acting like an || operator.
            }
        }
        return true;
    }
}

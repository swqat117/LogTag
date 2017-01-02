package com.quascenta.petersroad.Utils.Validators;

import android.widget.EditText;

/**
 * Created by AKSHAY on 12/22/2016.
 */

public abstract class Validator {
    protected String errorMessage;

    public Validator(String _customErrorMessage) {
        errorMessage = _customErrorMessage;
    }

    /**
     * Should check if the String  is valid.
     *
     * @param x the edittext under evaluation
     *
     * @return true if the edittext is valid, false otherwise
     */
    public abstract boolean isValid(String x);

    /**
     * Should check if the edittext is valid
     *
     * @param et the editext is under evaluation
     * @return true if the edittext is valid , else false
     */

    public abstract boolean isValid(EditText et);

    public boolean hasErrorMessage() {
        return errorMessage != null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
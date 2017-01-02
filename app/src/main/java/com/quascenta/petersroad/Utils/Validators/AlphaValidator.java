package com.quascenta.petersroad.Utils.Validators;

/**
 * Created by AKSHAY on 12/22/2016.
 */

public class AlphaValidator extends RegexpValidator {
    public AlphaValidator(String message) {
        super(message, "[A-z\u00C0-\u00ff \\./-\\?]*");
    }}

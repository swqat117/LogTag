package com.quascenta.petersroad.Utils.Validators;

import android.os.Build;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by AKSHAY on 12/22/2016.
 */

public class DomainValidator extends PatternValidator {
    public DomainValidator(String _customErrorMessage) {
        //TODO: Fix the pattern for api level < 8
        super(_customErrorMessage, Build.VERSION.SDK_INT >= 8 ? Patterns.DOMAIN_NAME : Pattern.compile(".*"));
    }
}
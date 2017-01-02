package com.quascenta.petersroad.Utils.Validators;

import java.util.regex.Pattern;

/**
 * Created by AKSHAY on 12/22/2016.
 */
public class RegexpValidator extends PatternValidator {
    public RegexpValidator(String message, String _regexp) {
        super(message, Pattern.compile(_regexp));
    }
}
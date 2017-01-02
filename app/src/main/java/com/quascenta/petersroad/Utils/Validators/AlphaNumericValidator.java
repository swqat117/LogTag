package com.quascenta.petersroad.Utils.Validators;

/**
 * Created by AKSHAY on 12/22/2016.
 */

public class AlphaNumericValidator extends RegexpValidator {

     public AlphaNumericValidator(String message){

         super(message, "[a-zA-Z0-9\u00C0-\u00FF \\./-\\?]*");

     }
}

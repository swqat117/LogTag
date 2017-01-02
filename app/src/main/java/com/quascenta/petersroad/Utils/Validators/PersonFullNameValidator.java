package com.quascenta.petersroad.Utils.Validators;

/**
 * Created by AKSHAY on 12/22/2016.
 */



    public class PersonFullNameValidator extends RegexpValidator {
        public PersonFullNameValidator(String message) {
            // will allow people with hyphens in his name or surname. Supports also unicode
            super(message, "[\\p{L}- ]+");
        }

}

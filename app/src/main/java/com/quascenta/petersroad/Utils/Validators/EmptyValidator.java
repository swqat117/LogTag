package com.quascenta.petersroad.Utils.Validators;

/**
 * Created by AKSHAY on 12/22/2016.
 */
import android.text.TextUtils;
import android.widget.EditText;

/**
 * A simple validator that validates the field only if the field is not empty.
 *
 * @author Andrea Baccega <me@andreabaccega.com>
 */
public class EmptyValidator extends Validator {
    public EmptyValidator(String message) {
        super(message);
    }

    @Override
    public boolean isValid(String x) {
        return false;
    }

    public boolean isValid(EditText et) {
        return TextUtils.getTrimmedLength(et.getText()) > 0;
    }
}
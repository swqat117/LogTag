package com.quascenta.petersroad.Utils;

/**
 * Created by AKSHAY on 11/11/2016.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CSVAnnotation {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CSVSetter {
        String info();
    }}


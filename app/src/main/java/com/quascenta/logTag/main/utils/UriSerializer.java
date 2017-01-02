package com.quascenta.logTag.main.utils;

import android.net.Uri;

import com.google.android.gms.plus.model.people.Person;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by AKSHAY on 1/2/2017.
 */

public class UriSerializer implements JsonSerializer<Uri> {


    @Override
    public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
    }



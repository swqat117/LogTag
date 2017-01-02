package com.quascenta.petersroad.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Context;

/**
 * Created by AKSHAY on 11/11/2016.
 */

public class CSVReader {

    private static final String SEP = ",";


    public static <T> List<Object> readFile(Context context, File file, Class<T> cl) throws IOException, IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException {

        List<Object> list = new ArrayList<Object>();

        List<Method> methods = new ArrayList<Method>();

        for (int i=0; i<cl.getDeclaredMethods().length; i++) {
            try {
                if (cl.getDeclaredMethods()[i].getName().startsWith("set")) {
                    methods.add(cl.getDeclaredMethods()[i]);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        FileInputStream in = new FileInputStream(file);

        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String line = null;
        boolean firstLine = true;

        List<String> labelsName = new ArrayList<String>();

        while ((br.readLine())!= null) {
            T object = null;
            StringTokenizer st = new StringTokenizer(line, SEP);
            if (st.countTokens()>0) {
                int count = 0;
                object = cl.newInstance();
                while (st.hasMoreElements()) {
                    String elt = (String) st.nextElement();
                    if (firstLine) {
                        int indexSEP = elt.indexOf("_");
                        if (indexSEP > -1 && indexSEP<elt.length()-2) {
                            //String stringAfterSEP = elt.substring(indexSEP+1, indexSEP+2).toUpperCase() + elt.substring(indexSEP+2).toLowerCase();
                            //elt = elt.substring(0,1).toUpperCase() + elt.substring(1,indexSEP) + stringAfterSEP;
                            labelsName.add(elt);
                        }
                        else {
                            labelsName.add(elt); //.substring(0,1).toUpperCase() + elt.substring(1,elt.length()).toLowerCase());
                        }
                    }
                    else {
                        String label = labelsName.get(count++);
                        for (Method method : methods) {
                            if (/*method.getName().endsWith(*/method.getAnnotation(CSVAnnotation.CSVSetter.class).info().equals(label)) {
                                method.invoke(object, elt);
                                break;
                            }
                        }
                    }
                }
                list.add(object);
            }
            firstLine = false;
        }
        return list;
    }

}
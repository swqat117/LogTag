package com.quascenta.petersroad.Utils;

import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by AKSHAY on 11/11/2016.
 */

public class Utils {



       public static ArrayList<ArrayList<String>> creater(File file){
            BufferedReader crunchifyBuffer = null;
           ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
            try {
                String crunchifyLine;
                crunchifyBuffer = new BufferedReader(new FileReader(file.getAbsolutePath()));
               // How to read file in java line by line?
                while ((crunchifyLine = crunchifyBuffer.readLine()) != null) {

                    data.add(crunchifyCSVtoArrayList(crunchifyLine));
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (crunchifyBuffer != null) crunchifyBuffer.close();
                } catch (IOException crunchifyException) {
                    crunchifyException.printStackTrace();
                }
            }
           return data;
        }

        // Utility which converts CSV to ArrayList using Split Operation
        public static ArrayList<String> crunchifyCSVtoArrayList(String crunchifyCSV) {
            ArrayList<String> crunchifyResult = new ArrayList<String>();

            if (crunchifyCSV != null) {
                String[] splitData = crunchifyCSV.split("\\s*,\\s*");
                for (int i = 0; i < splitData.length; i++) {
                    if (!(splitData[i] == null) || !(splitData[i].length() == 0)) {
                        crunchifyResult.add(splitData[i].trim());
                    }
                }
            }

            return crunchifyResult;
        }



    public static String toString(ByteArrayInputStream is) {
        int size = is.available();
        char[] theChars = new char[size];
        byte[] bytes    = new byte[size];

        is.read(bytes, 0, size);
        for (int i = 0; i < size;)
            theChars[i] = (char)(bytes[i++]&0xff);

        return new String(theChars);
    }


    public String sortData(String x){

        return x;
    }

}

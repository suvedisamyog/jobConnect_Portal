package com.example.jobconnect.Validations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatData {
    private static final SimpleDateFormat INPUT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat INPUT_FORMAT_TYPE2 = new SimpleDateFormat("yyyy-MM-dd ");
    private static final SimpleDateFormat OUTPUT_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static String formatDate(String dateString) {
        try {
            Date date = INPUT_FORMAT.parse(dateString);
            return OUTPUT_FORMAT.format(date);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return dateString;
    }
    public static String formatDateType(String dateString) {
        try {
            Date date = INPUT_FORMAT_TYPE2.parse(dateString);
            return OUTPUT_FORMAT.format(date);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return dateString;
    }

    public  static String formatType(String inputData){

        if(inputData.equals("Select One")){
            return "Not Mentioned";
        }else{
            return inputData;
        }
    }

    public static String reDefine(String inputData) {
                if(inputData.equals("Entry-Level(0 years)")){
                    return "0-1 Y";
                } else if (inputData.equals("Junior (1–3 years)")) {
                    return "1-3 Y";
                } else if (inputData.equals("Mid–Level (3–5 years)")) {
                    return "3-5 Y";
                } else if (inputData.equals("Senior (5–7 years)")) {
                    return "5-7 Y";
                }else if(inputData.equals("Executive/Expert(7+ years)")){
                    return "7+";
                }else{
                    return inputData;
                }

    }
}

package com.udacity.stockhawk.chart_helpers;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by jamie on 2/12/17.
 */

public class StockXAxesFormater implements IAxisValueFormatter {


    private final LinkedHashMap<Integer, String> stockDateIndex;

    public StockXAxesFormater(LinkedHashMap<Integer, String> stockDateIndex) {
        this.stockDateIndex = stockDateIndex;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        String stringValue = this.stockDateIndex.get((int)value);
        String[] parseString = stringValue.split("/");
        String formatedDate = parseString[0]+"/"+parseString[1];
        return formatedDate;
    }

    private String getMonth(String stringValue) {
        int monthInt = Integer.parseInt(stringValue);
        String month = "";
        switch (monthInt){
            case 1:
                month="Jan";
                break;
            case 2:
                month="Feb";
                break;
            case 3:
                month="Mar";
                break;
            case 4:
                month="Apr";
                break;
            case 5:
                month="May";
                break;
            case 6:
                month="Jun";
                break;
            case 7:
                month="Jul";
                break;
            case 8:
                month="Aug";
                break;
            case 9:
                month="Sep";
                break;
            case 10:
                month="Oct";
                break;
            case 11:
                month="Nov";
                break;
            case 12:
                month="Dec";
                break;
            default:
                break;
        }
        return month;
    }
}

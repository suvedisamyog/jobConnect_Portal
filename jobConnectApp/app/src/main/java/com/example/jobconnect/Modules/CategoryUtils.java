package com.example.jobconnect.Modules;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.jobconnect.R;

public class CategoryUtils {
    public static void updateCategorySpinner(String selectedIndustry, Spinner categorySpinner, View view) {
        ArrayAdapter<CharSequence> categoryAdapter;
        Context context = view.getContext();

        switch (selectedIndustry) {
            case "Information Technology(IT)":
                categoryAdapter = ArrayAdapter.createFromResource(context,
                        R.array.it_categories, android.R.layout.simple_spinner_item);
                break;
            case "Healthcare":
                categoryAdapter = ArrayAdapter.createFromResource(context,
                        R.array.healthcare_categories, android.R.layout.simple_spinner_item);
                break;
            case "Finance/Banking":
                categoryAdapter = ArrayAdapter.createFromResource(context,
                        R.array.finance_categories, android.R.layout.simple_spinner_item);
                break;
            case "Education":
                categoryAdapter = ArrayAdapter.createFromResource(context,
                        R.array.education_categories, android.R.layout.simple_spinner_item);
                break;
            case "Manufacturing":
                categoryAdapter = ArrayAdapter.createFromResource(context,
                        R.array.manufacturing_categories, android.R.layout.simple_spinner_item);
                break;
            case "Retail":
                categoryAdapter = ArrayAdapter.createFromResource(context,
                        R.array.retail_categories, android.R.layout.simple_spinner_item);
                break;
            case "Marketing/Advertising":
                categoryAdapter = ArrayAdapter.createFromResource(context,
                        R.array.marketing_categories, android.R.layout.simple_spinner_item);
                break;
            case "Hospitality/Tourism":
                categoryAdapter = ArrayAdapter.createFromResource(context,
                        R.array.hospitality_categories, android.R.layout.simple_spinner_item);
                break;
            case "Engineering":
                categoryAdapter = ArrayAdapter.createFromResource(context,
                        R.array.engineering_categories, android.R.layout.simple_spinner_item);
                break;
            case "Media/Entertainment":
                categoryAdapter = ArrayAdapter.createFromResource(context,
                        R.array.media_categories, android.R.layout.simple_spinner_item);
                break;
            default:
                categoryAdapter = ArrayAdapter.createFromResource(context,
                        R.array.default_categories, android.R.layout.simple_spinner_item);
                break;
        }

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
    }
}


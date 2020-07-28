package com.keshavoneml.oneml_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;

import java.text.DecimalFormat;

public class BMI extends AppCompatActivity {


    Button btn;
    Spinner Sweight, Sheight;
    EditText weight, height, result;

    TableRow tableRow;
    EditText resultWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Sweight = findViewById(R.id.SpinnerWeight);
        Sheight = findViewById(R.id.SpinnerHeight);
        weight = findViewById(R.id.TxtWeight);
        height = findViewById(R.id.TxtHeight);
        result = findViewById(R.id.TxtResult);

        resultWords = findViewById(R.id.TxtResultWords);

        tableRow = findViewById(R.id.tableRow);

        btn = findViewById(R.id.BtnCalculate);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsValid()){
                    Float weight = ConvertWeight();
                    Float height = ConvertHeight();

                    DecimalFormat df = new DecimalFormat("#.##");

                    tableRow.setVisibility(View.VISIBLE);
                    resultWords.setVisibility(View.VISIBLE);

                    Float res = Result(weight , height);
                    result.setText(df.format(res));


                    String bmiLabel = "You are ";
                    if (Float.compare(res, 15f) <= 0) {
                        bmiLabel += "very severely underweight";
                    } else if (Float.compare(res, 15f) > 0  &&  Float.compare(res, 16f) <= 0) {
                        bmiLabel += "severely underweight";
                    } else if (Float.compare(res, 16f) > 0  &&  Float.compare(res, 18.5f) <= 0) {
                        bmiLabel += "underweight";
                    } else if (Float.compare(res, 18.5f) > 0  &&  Float.compare(res, 25f) <= 0) {
                        bmiLabel += "normal";
                    } else if (Float.compare(res, 25f) > 0  &&  Float.compare(res, 30f) <= 0) {
                        bmiLabel += "overweight";
                    } else if (Float.compare(res, 30f) > 0  &&  Float.compare(res, 35f) <= 0) {
                        bmiLabel += "obese class-1";
                    } else if (Float.compare(res, 35f) > 0  &&  Float.compare(res, 40f) <= 0) {
                        bmiLabel += "obese class-2";
                    } else {
                        bmiLabel += "obese class-3";
                    }

                    resultWords.setText(bmiLabel);


                }
            }
        });
    }


    private boolean IsValid() {
        boolean result = true;

        if (weight.getText().toString().trim().length() == 0){
            weight.setError("Enter your weight");
            return false;
        }

        if (height.getText().toString().trim().length() == 0){
            height.setError("Enter your height");
            return false;
        }
        return result;
    }


    private Float ConvertWeight() {
        Float result;
        String unit = Sweight.getSelectedItem().toString();

        switch (unit){
            case "kg":
                Float weightResult = Float.parseFloat(weight.getText().toString());
                result = weightResult * 2.2f;
                break;
            case "lbs":
                result = Float.parseFloat(weight.getText().toString());
                break;
            default:
                result = 0.0f;
        }
        return result;
    }


    private Float ConvertHeight() {
        Float result;
        Float heightResult;
        String unit = Sheight.getSelectedItem().toString();

        switch (unit){
            case "ft":
                heightResult = Float.parseFloat(height.getText().toString());
                result = heightResult * 12.0f;
                break;
            case "cm":
                heightResult = Float.parseFloat(height.getText().toString());
                result = heightResult * 0.3937f;
                break;
            default:
                result = 0.0f;
        }
        return result;
    }



    private Float Result(Float weight, Float height) {
        Float result;
        Float resultWeight;
        Float resultHeight;

        resultWeight = weight * 0.45f;
        resultHeight = height * 0.025f;

        resultHeight = resultHeight * resultHeight;

        result = resultWeight/resultHeight;

        return result;
    }

}
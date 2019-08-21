package com.homebrewforlife.sharkydart.anyonecanfish;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Trip;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TripDetailsSheet extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener {
    private final String LOG_TAG = "fart.TripDetailSheet";
    private TripSheetListener mListener;
    private EditText etName;
    private EditText etDesc;
    private TextView etStart;
    private TextView etEnd;
    private EditText etLat;
    private EditText etLon;
    private Context mContext;
    public boolean dateStart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.trips_sheet, container, false);
        mContext = v.getContext();
        dateStart = false;
        final DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, this, 2019, 8, 21);

        etName = v.findViewById(R.id.etTripName);
        etDesc = v.findViewById(R.id.etTripDesc);
        etStart = v.findViewById(R.id.etTripStart);
        etEnd = v.findViewById(R.id.etTripEnd);
        etLat = v.findViewById(R.id.etTripLat);
        etLon = v.findViewById(R.id.etTripLon);

        etStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateStart = true;
                datePickerDialog.show();
            }
        });
        etEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateStart = false;
                datePickerDialog.show();
            }
        });

//        Button btnGps;

        Button btnAdd = v.findViewById(R.id.btnAddTrip);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.getText().length() > 0) {
                    try {
                        Fire_Trip newTrip = new Fire_Trip();
                        newTrip.setName(etName.getText().toString());
                        newTrip.setDesc(etDesc.getText().toString());
                        Log.d(LOG_TAG,"name and desc found and set");
//                        newTrip.setDateStart(dateToTimestamp());
//                        newTrip.setDateEnd(new Timestamp(Date.valueOf(etEnd.getText().toString())));
                        Log.d(LOG_TAG,"dates found and set");
//                        newTrip.setLatitude(Double.parseDouble(etLat.getText().toString()));
//                        newTrip.setLongitude(Double.parseDouble(etLon.getText().toString()));
                        Log.d(LOG_TAG,"GPS coords found and set");
                        clearViews();

                        mListener.onAddTripBtnClicked(newTrip);
                        dismiss();
                    }catch(NullPointerException e){
                        e.printStackTrace();
                    }
                }
                else
                    Toast.makeText(mContext, "Please Name the Trip", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

/*
    private Timestamp dateToTimestamp(Date myDate){
        DateFormat formatter ;
        Date date ;
        formatter = new SimpleDateFormat("dd-MM-yyyy");
        date = (Date)formatter.parse(str_date);
        java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
*/
/*
        // take the Date and put in a calendar object
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        // put into a Timestamp
        java.sql.Timestamp stamp = new java.sql.Timestamp(cal.getTimeInMillis());
        // get the JDBC-formatted string
        String jdbcTime = stamp.toString();
*//*

        return new Timestamp(Date.valueOf(jdbcTime));
    }
*/

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if(dateStart)
            Log.d(LOG_TAG, "START_DATE - year: " + year + " month: " + month + " day: " + day);
        else
            Log.d(LOG_TAG, "END_DATE - year: " + year + " month: " + month + " day: " + day);
    }

    public interface TripSheetListener{
        void onAddTripBtnClicked(Fire_Trip theTrip);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mListener = (TripSheetListener)context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " must implement TripSheetListener.");
        }
    }

    private void clearViews(){
        etName.setText("");
        etDesc.setText("");
        etStart.setText("");
        etEnd.setText("");
        etLat.setText("");
        etLon.setText("");
    }
}

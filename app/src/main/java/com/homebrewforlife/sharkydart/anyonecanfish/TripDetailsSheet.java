package com.homebrewforlife.sharkydart.anyonecanfish;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.homebrewforlife.sharkydart.anyonecanfish.models.Fire_Trip;

import java.sql.Date;

import javax.xml.datatype.Duration;

public class TripDetailsSheet extends BottomSheetDialogFragment {
    private TripSheetListener mListener;
    private EditText etName;
    private EditText etDesc;
    private EditText etStart;
    private EditText etEnd;
    private EditText etLat;
    private EditText etLon;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.trips_sheet, container, false);
        mContext = v.getContext();

        etName = v.findViewById(R.id.etTripName);
        etDesc = v.findViewById(R.id.etTripDesc);
        etStart = v.findViewById(R.id.etTripStart);
        etEnd = v.findViewById(R.id.etTripEnd);
        etLat = v.findViewById(R.id.etTripLat);
        etLon = v.findViewById(R.id.etTripLon);

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
                        newTrip.setDateStart(new Timestamp(Date.valueOf(etStart.getText().toString())));
                        newTrip.setDateEnd(new Timestamp(Date.valueOf(etEnd.getText().toString())));
                        newTrip.setLatitude(Double.parseDouble(etLat.getText().toString()));
                        newTrip.setLongitude(Double.parseDouble(etLon.getText().toString()));
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
    private void clearViews(){
        etName.setText("");
        etDesc.setText("");
        etStart.setText("");
        etEnd.setText("");
        etLat.setText("");
        etLon.setText("");
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
}

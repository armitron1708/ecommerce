package com.example.oops.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oops.Prevalent.Prevalent;
import com.example.oops.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText nameEdittxt,phoneEdittxt,addressEdittxt,cityEdittxt;
    private Button confirmorderbtn;
    private String totalAmount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        confirmorderbtn=findViewById(R.id.confirm_final_order_btn);
        nameEdittxt=findViewById(R.id.shipment_name);
        phoneEdittxt=findViewById(R.id.shipment_phone);
        addressEdittxt=findViewById(R.id.shipment_address);
        cityEdittxt=findViewById(R.id.shipment_city);
        totalAmount=getIntent().getStringExtra("Total Price");
        Toast.makeText(this, totalAmount, Toast.LENGTH_SHORT).show();
        confirmorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });

    }

    private void Check() {
        if(TextUtils.isEmpty(nameEdittxt.getText().toString()))
        {
            Toast.makeText(this, "provide name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEdittxt.getText().toString()))
        {
            Toast.makeText(this, "provide phone number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEdittxt.getText().toString()))
        {
            Toast.makeText(this, "provide address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityEdittxt.getText().toString()))
        {
            Toast.makeText(this, "provide city", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String saveCurrentDate,saveCurrentTime;
        Calendar calForDate= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentDate.format(calForDate.getTime());

        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentonlineUser.getName());
        HashMap<String,Object> ordersMap= new HashMap<>();
        ordersMap.put("total amount",totalAmount);
        ordersMap.put("name",nameEdittxt.getText().toString());
        ordersMap.put("phone",phoneEdittxt.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);
        ordersMap.put("address",addressEdittxt.getText().toString());
        ordersMap.put("city",cityEdittxt.getText().toString());
        ordersMap.put("state","not shipped");
        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        FirebaseDatabase.getInstance().getReference().child("Cart List")
                                .child("User View")
                                .child(Prevalent.currentonlineUser.getName())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(ConfirmFinalOrderActivity.this, "order placed", Toast.LENGTH_SHORT).show();
                                            Intent intent =new Intent(getApplicationContext(),CustomerHomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                    }
            }
        });


    }
}
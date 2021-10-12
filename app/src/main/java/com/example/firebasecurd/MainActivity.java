package com.example.firebasecurd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    Dialog dialog;

    RecyclerAdapter recyclerAdapter;
    DatabaseReference databaseReference;


    public ArrayList<MainModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        recyclerView = findViewById(R.id.recyclerView);
        dialog = new Dialog(this);
        floatingActionButton.setOnClickListener(v -> addData());

        showAllData();
        UpdateData();

    }

    private void UpdateData() {


    }

    private void showAllData() {
//        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
//                .setQuery(FirebaseDatabase.getInstance().getReference().child("users"), MainModel.class)
//                .build();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("M", snapshot.getValue().toString());
                arrayList = new ArrayList<>();
                recyclerAdapter = new RecyclerAdapter(arrayList);

                for (DataSnapshot dsp : snapshot.getChildren()){
                    MainModel model = dsp.getValue(MainModel.class);
//                    Log.d("M", dsp.getValue().toString());
                    arrayList.add(model);
                }
                recyclerAdapter.notifyDataSetChanged();
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void addData() {
        dialog.setContentView(R.layout.entry_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView close;
        EditText name, email, contact;
        Button sendBtn;

        close = dialog.findViewById(R.id.close);
        name = dialog.findViewById(R.id.nameEt);
        email = dialog.findViewById(R.id.emailEt);
        contact = dialog.findViewById(R.id.contactEt);
        sendBtn = dialog.findViewById(R.id.button);

        sendBtn.setOnClickListener(v -> {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").push();
//            Log.d("ID", ref.getRef().getKey());
            MainModel model = new MainModel(ref.getRef().getKey(), name.getText().toString(), email.getText().toString(), contact.getText().toString());

            ref.setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(MainActivity.this, "Add Data Successfully", Toast.LENGTH_SHORT).show();
                    name.setText("");
                    email.setText("");
                    contact.setText("");
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Something is wrong please try again!!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
package com.example.firebasecurd;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.telephony.VisualVoicemailService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.database.snapshot.ChildKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<MainModel> arrayList;


    public RecyclerAdapter(ArrayList<MainModel> arrayList) {
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainModel model = arrayList.get(position);
        holder.id.setText(model.getId());
        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        holder.contact.setText(model.getContact());


        //Update Button Click Events Functions//
        holder.editBtn.setOnClickListener(v -> {
            Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.edit_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ImageView close;

            close = dialog.findViewById(R.id.close);
            close.setOnClickListener(v1 -> dialog.dismiss());

            EditText update_name, update_email, update_contact;

            update_name = dialog.findViewById(R.id.UnameEt);
            update_email = dialog.findViewById(R.id.UemailEt);
            update_contact = dialog.findViewById(R.id.UcontactEt);

            update_name.setText(model.getName());
            update_email.setText(model.getEmail());
            update_contact.setText(model.getContact());


            Button updateBtn;
            updateBtn = dialog.findViewById(R.id.updateBtn);
            updateBtn.setOnClickListener(v1 -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", model.getId());
                map.put("name", update_name.getText().toString());
                map.put("email", update_email.getText().toString());
                map.put("contact", update_contact.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(model.getId()).updateChildren(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v1.getContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v1.getContext(), "Something is wrong!!", Toast.LENGTH_SHORT).show();
                            }
                        });

            });

            dialog.show();
        });

        //Delete Button Click Events Functions//
        holder.deleteBtn.setOnClickListener(v -> {
            Dialog dialog = new Dialog(v.getContext());
            dialog.setContentView(R.layout.delete_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button yesBtn, NoBtn;
            yesBtn = dialog.findViewById(R.id.yesBtn);
            NoBtn = dialog.findViewById(R.id.cancelBtn);

            yesBtn.setOnClickListener(v2 -> {
                FirebaseDatabase.getInstance().getReference().child("users").child(model.getId()).removeValue();
                Toast.makeText(v2.getContext(), "Data is Deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

            NoBtn.setOnClickListener(v2 -> {
                Toast.makeText(v2.getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

            dialog.show();
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, editBtn, deleteBtn;
        TextView id, name, email, contact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profile);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            contact = itemView.findViewById(R.id.contact);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

        }
    }

}

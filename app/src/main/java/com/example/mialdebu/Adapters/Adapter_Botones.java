package com.example.mialdebu.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mialdebu.Models.Alumno;
import com.example.mialdebu.R;

import java.io.File;
import java.util.List;

public class Adapter_Botones  extends RecyclerView.Adapter<Adapter_Botones.MyViewHolder> {

    Context mContext;

    String[] ListDivsiion;




    public Adapter_Botones(Context mContext ) {
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        View row = LayoutInflater.from(mContext).inflate(R.layout.model_button, viewGroup,false);


        MyViewHolder holder = new MyViewHolder(row, new MyViewHolder.MyClickListener(){
            @Override
            public void onEdit(int p) {

                String s;
                s = ListDivsiion[p];


            }

        });

        return holder;
    }





    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Button rowsBtn;

        String[] your_array = mContext.getResources().getStringArray(R.array.div);
        String mdivision = your_array[i];
        ListDivsiion = your_array;
        rowsBtn = myViewHolder.itemView.findViewById(R.id.btn_model);

        rowsBtn.setText(mdivision);


    }



    @Override
    public int getItemCount() {
        return ListDivsiion.length;
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        protected Button rowBtn;

        MyClickListener listener;


        public MyViewHolder(@NonNull View itemView, MyClickListener listener) {
            super(itemView);

            rowBtn = itemView.findViewById(R.id.btn_model );


            this.listener =  listener;


            rowBtn.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            listener.onEdit(this.getLayoutPosition());

        }

        public interface MyClickListener {
            void onEdit(int p);
        }
    }

    private void OpenPdf(File pocalFile) {


        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(pocalFile),"application/pdf");
        target.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent intent= new Intent(Intent.createChooser(target, "Open pdf").addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        try {
            mContext.startActivity( intent );
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }
}

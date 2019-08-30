package com.example.mialdebu.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mialdebu.Models.Comunicado;
import com.example.mialdebu.R;

import java.util.List;

public class Adapter_Comunicados extends RecyclerView.Adapter<Adapter_Comunicados.ViewHolderComunicado> {

     Context mContext;
    List<Comunicado> ListComunicados;


    public Adapter_Comunicados(Context mContext, List<Comunicado> MList)
    {
        this.mContext = mContext;
        this.ListComunicados = MList;

    }


    @NonNull
    @Override
    public ViewHolderComunicado onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.comunicado_item,viewGroup,false);

        ViewHolderComunicado Holder = new ViewHolderComunicado(row);
    

        return Holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderComunicado viewHolderComunicado, int i) {
        
        viewHolderComunicado.AsignarComunicado(ListComunicados.get(i));
    }




    @Override
    public int getItemCount() {
        if(ListComunicados!=null)
        return ListComunicados.size();
        else return 0;
    }

    public class ViewHolderComunicado extends RecyclerView.ViewHolder {
        TextView textView ;
        TextView Time;
        TextView SubidoPor;

        public ViewHolderComunicado(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.TV_comunicados);
            Time = itemView.findViewById(R.id.ComunTime);
            SubidoPor = itemView.findViewById(R.id.ComunSubida);

        }
        public void AsignarComunicado(Comunicado comunicado) {
            textView.setText(comunicado.getComunicado());
            Time.setText(Time.getText()+comunicado.getTime());
            SubidoPor.setText (SubidoPor.getText()+ comunicado.getSubidoPor ());

        }

    }
}

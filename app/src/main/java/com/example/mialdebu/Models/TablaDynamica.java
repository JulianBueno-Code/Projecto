package com.example.mialdebu.Models;


import android.content.Context;

import android.view.Gravity;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class TablaDynamica {
    private TableLayout tableLayout;
    Context context;
    private  String[]Header;
    private ArrayList<String[]> DAta;
    private TableRow tableRow;
    private TextView textCell;
    private int IndexC,IndexR;
    private boolean Multicolor = false;
    private int firstColor;
    private int secondColor;
    private  int TextColor;

    public TablaDynamica (TableLayout tableLayout, Context context)
    {
        this.tableLayout = tableLayout;
        this.context = context;

    }
    public void  addHeader (String[]Header){
        this.Header = Header;
        createHeader();

    }

    public void  addData (ArrayList<String[]> data){
        DAta = data;
        createDataTable();
    }

    private void newRow()
    {
        tableRow = new TableRow(context);

    }

    private void newcell()
    {
        textCell = new TextView(context);
        textCell.setGravity(Gravity.CENTER);
        textCell.setTextSize(25);
    }

    private void createHeader()
    {
        IndexC = 0;
        newRow();
        while (IndexC<Header.length){
            newcell();
            textCell.setText(Header[IndexC++]);
            tableRow.addView(textCell,newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }
    private void createDataTable(){
        String info;
        for(IndexR=1;IndexR<=Header.length; IndexR++){
            newRow();
            for(IndexC=0;IndexC<=Header.length;IndexC++)
            {
                newcell();
                String[] row = DAta.get(IndexR-1);
                info =(IndexC<row.length)?row[IndexC]:"";
                textCell.setText(info);
                tableRow.addView(textCell,newTableRowParams());
            }
            tableLayout.addView(tableRow);

        }

    }
    public void lineColor(int Color){
        IndexR =0;
        while (IndexR< DAta.size()){
            getRow(IndexR++).setBackgroundColor(Color);
        }
    }

    public  void addItem(String[] item){
        String Info;
        DAta.add(item);
        IndexC = 0;
        newRow();
        while
        (IndexC<Header.length ){
            newcell();
            Info=(IndexC<item.length)?item[IndexC++]:"";
            textCell.setText(Info);
            tableRow.addView(textCell,newTableRowParams());

        }
        tableLayout.addView(tableRow,DAta.size()-1);
        reColoring();
    }

    private TableRow getRow(int index){
        return (TableRow)tableLayout.getChildAt(index);

    }

    public void SetColorData(int color){
        TextColor = color;
        for(IndexR=1;IndexR<=Header.length; IndexR++){

            for(IndexC=0;IndexC<=Header.length;IndexC++)

                getCell(IndexR,IndexC).setTextColor(TextColor);
        }

    }

    public void SetColorTextHeader(int color){
        IndexC =0;
        while(IndexC<Header.length)
            getCell(0,IndexC++).setTextColor(color);


    }

    public void backgroundHeader(int color)
    {

        IndexC = 0;

        while
        (IndexC<Header.length ){

            textCell = getCell(0,IndexC++);
            textCell.setBackgroundColor(color);
        }

    }

    public void backgroundData(int firstColor,int secondColor)
    {
        for(IndexR=1;IndexR<=Header.length; IndexR++){
            Multicolor =! Multicolor;
            for(IndexC=0;IndexC<=Header.length;IndexC++)
            {

                textCell = getCell(IndexR,IndexC);
                textCell.setBackgroundColor((Multicolor)?firstColor:secondColor);
            }


        }
        this.firstColor = firstColor;
        this.secondColor = secondColor;
    }


    public void reColoring()
    {

        IndexC = 0;
        Multicolor=!Multicolor;
        while
        (IndexC<Header.length ){

            textCell = getCell(DAta.size()-1,IndexC++);
            textCell.setBackgroundColor((Multicolor)?firstColor:secondColor);
            textCell.setTextColor(TextColor);
        }
        tableLayout.addView(tableRow);
    }


    private TextView getCell(int rowindex,int columnIndex){
        tableRow=getRow(rowindex);
        return (TextView) tableRow.getChildAt(columnIndex);

    }


    private TableRow.LayoutParams newTableRowParams(){

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1,1,1,1);
        params.weight = 1;
        return params;
    }
}

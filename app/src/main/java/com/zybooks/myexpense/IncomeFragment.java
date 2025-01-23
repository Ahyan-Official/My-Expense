package com.zybooks.myexpense;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zybooks.myexpense.database.DataHelper;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class IncomeFragment extends Fragment {


    String[] incomeList, IdIncome;
    ListView ListViewIncome;
    protected Cursor cursor;
    DataHelper dataHelper;
    Button Add;


    public IncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_income, container, false);

        // Initial ListView
        ListViewIncome = v.findViewById(R.id.listViewIncome);
        Add = v.findViewById(R.id.Add);

        dataHelper = new DataHelper(getActivity());

        new MyAsynch().execute();

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showNewCategoryDialog(v);
            }
        });

        return v;
    }


    /**
     * start asyntask onCreate and Data Changed
     */
    private class MyAsynch extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... Strings) {
            // run time intensive task in separate thread
            GetData();
            return null;
        }

        protected void onPostExecute(String result) {
            // Give the data to you adapter from here,instead of the place where you gave it earlier
            LoadListView();
        }
    }

    /**
     * Load Data From Database Category
     */
    public void GetData (){
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM category_income", null);
        // Initial Array Size
        incomeList = new String[cursor.getCount()];
        IdIncome = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            // Get Id
            IdIncome[cc] = cursor.getString(0).toString();
            // Get Item Name
            incomeList[cc] = cursor.getString(1).toString();
        }
    }

    /**
     * Load Data From Database & Set Into ListView
     */
    public void LoadListView() {

        MyAdapter adapter = new MyAdapter(getActivity(), incomeList);
        ListViewIncome.setAdapter(adapter);
        // Set On ClickListener on each Item
        ListViewIncome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get item clicked ID
                final String idn = IdIncome[position];
                // Get item clicked Name
                final String nama = incomeList[position];
                // Set Option when item clicked
                final CharSequence[] dialogitem = {"Edit Category"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Options");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                // User clicked the Update Option
                                showEditDialog(idn, nama);
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        ((ArrayAdapter) ListViewIncome.getAdapter()).notifyDataSetInvalidated();
    }

    /**
     * Adapter for Set Data ListView as CardView
     */
    private class MyAdapter extends ArrayAdapter {
        String list_nama[];
        Activity activity;

        public MyAdapter(Activity activity, String[] list_nama) {
            super(activity, R.layout.item_category, list_nama);
            this.activity = activity;
            this.list_nama = list_nama;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) activity.getLayoutInflater();
            View v = inflater.inflate(R.layout.item_category, null);
            TextView des;
            TextView category;
            ImageView imageView;

            //casting widget id
            category = v.findViewById(R.id.namecategory);
            imageView = v.findViewById(R.id.list_avatar);
            des = v.findViewById(R.id.des);

            imageView.setImageDrawable(getResources().getDrawable(R.drawable.add_circular_32));
            des.setText(list_nama[position]);
            category.setText("Income");

            return v;
        }
    }

    /**
     * Form Dialog for Update Category
     */
    public void showEditDialog(final String idParam, final String namaParam) {
        // Initial Layout to Inflate the dialogbox
        LayoutInflater mainLayout = LayoutInflater.from(getActivity());
        View mView = mainLayout.inflate(R.layout.dialog_edit_category, null);
        final EditText editCategory = mView.findViewById(R.id.userInputDialog);
        editCategory.setText(namaParam);
        // Build the Dialog
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(false)
                // User Click the Save Button
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // Empty Field
                        if (editCategory.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Category Name is Required", Toast.LENGTH_LONG).show();
                        }
                        // Not Empty Field
                        else {
                            SQLiteDatabase dbRead = dataHelper.getReadableDatabase();
                            cursor = dbRead.rawQuery("SELECT * FROM category_income WHERE type = '" + editCategory.getText().toString() + "'", null);
                            int a = cursor.getCount();
                            // Name Already Used
                            if (a > 0){
                                Toast.makeText(getContext(), "Category " + namaParam + " Already available !", Toast.LENGTH_LONG).show();
                            }
                            // Do Update
                            else {
                                SQLiteDatabase dbWrite = dataHelper.getWritableDatabase();
                                dbWrite.execSQL("UPDATE category_income SET type = '"+ editCategory.getText().toString() + "' WHERE id ='" + idParam + "'");
                                new MyAsynch().execute();
                                Toast.makeText(getContext(), "Edit " + namaParam + " Become " + editCategory.getText().toString() + " Succeed", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                })
                // User Click the Cancel Button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        // Show the Dialog
        alertDialogAndroid.show();
    }

    /**
     * Form Dialog for New Category
     */
    public void showNewCategoryDialog(View v) {

        LayoutInflater mainLayout = LayoutInflater.from(getActivity());
        View mView = mainLayout.inflate(R.layout.dialog_add_category, null);
        final EditText newCategory = mView.findViewById(R.id.userInputDialog);
        // Build the Dialog
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(false)
                // User Click the Save Button
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // Empty Field
                        if (newCategory.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Category Name is Required", Toast.LENGTH_LONG).show();
                        }
                        // Not Empty Field
                        else {
                            SQLiteDatabase dbRead = dataHelper.getReadableDatabase();
                            cursor = dbRead.rawQuery("SELECT * FROM category_income WHERE type = '" + newCategory.getText().toString() + "'", null);
                            int a = cursor.getCount();
                            // Name Already Used
                            if (a > 0){
                                Toast.makeText(getContext(), "Category " + newCategory.getText().toString() + " Sudah Ada !", Toast.LENGTH_LONG).show();
                            }
                            // Do Insert
                            else {
                                SQLiteDatabase dbWrite = dataHelper.getWritableDatabase();
                                dbWrite.execSQL("INSERT INTO 'category_income' ('type') VALUES ('"+ newCategory.getText().toString() +"') ");
                                new MyAsynch().execute();
                                Toast.makeText(getContext(), "Successfully Added Category " + newCategory.getText().toString(), Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                })
                // User Click the Cancel Button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        // Show the Dialog
        alertDialogAndroid.show();
    }




}
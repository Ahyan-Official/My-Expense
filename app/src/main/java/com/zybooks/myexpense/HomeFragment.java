package com.zybooks.myexpense;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.support.annotation.NonNull;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.zybooks.myexpense.database.DataHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    String[] IdIncome, IdCategoryIncome, CategoryIncome, DescriptionIncome, AmountIncome;
    String[] IdExpense, IdCategoryExpense, CategoryExpense, DescriptionExpense, AmountExpense;
    ListView ListViewIncome, ListViewExpense;


    // For Spinner Data
    Spinner spinCategoryIncome, spinCategoryExpense;
    String[] SpinIdCategoryIncome, SpinNameCategoryIncome, SpinIdCategoryExpense, SpinNameCategoryExpense;
    int SELECTED_CATEGORY_ID_INCOME = 0;
    int SELECTED_CATEGORY_ID_EXPENSE = 0;

    // FAB
    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2;

    // Database
    protected Cursor cursor, cursorExp, cursorRev;
    DataHelper dataHelper;

    // Get Today Date
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String datetop = df.format(Calendar.getInstance().getTime());
    int AdaDataIncome, AdaDataExpense;
    TextView tvValueExpense, tvValueIncome, tvBalance;
    LinearLayout linearLayout, footer , tvIncome , tvExpense;


    public HomeFragment() {
        // Required empty public constructor
    }

  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View v = inflater.inflate(R.layout.fragment_home, container, false);


        materialDesignFAM = v.findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = v.findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = v.findViewById(R.id.material_design_floating_action_menu_item2);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                showNewRevenueDialog();
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked
                showNewExpenseDialog();
            }
        });

        tvIncome = v.findViewById(R.id.tvIncome);
        tvExpense = v.findViewById(R.id.tvExpense);
        tvValueIncome = v.findViewById(R.id.tvValueIncome);
        tvValueExpense = v.findViewById(R.id.tvValueExpense);
        linearLayout = v.findViewById(R.id.emptyview);
        footer = v.findViewById(R.id.footer);
        tvBalance = v.findViewById(R.id.currentBalance);

        // Initial ListView expensen
        ListViewExpense = v.findViewById(R.id.ListExpense);
        // Initial ListView Income
        ListViewIncome = v.findViewById(R.id.ListIncome);

        dataHelper = new DataHelper(getActivity());

        new MyAsynch().execute();
        
        
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
            GetDataExpense();
            GetDataRevenue();

            return null;
        }

        protected void onPostExecute(String result) {
            // Give the data to you adapter from here,instead of the place where you gave it earlier
            LoadListView();
        }
    }

    /**
     * Load Data From Database Expense And Revenue
     */
    public void GetBalance(){
        int TotalIncomeBalance=0, TotalExpenseBalance = 0, Bal = 0;

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        cursorRev = db.rawQuery("select * from income", null);
        cursorExp = db.rawQuery("select * from expense", null);

        cursorRev.moveToFirst();
        for (int cc = 0; cc < cursorRev.getCount(); cc++) {
            cursorRev.moveToPosition(cc);
            // Get Item Detail
            TotalIncomeBalance += Integer.parseInt(cursorRev.getString(3).toString());
        }

        cursorExp.moveToFirst();
        for (int cc = 0; cc < cursorExp.getCount(); cc++) {
            cursorExp.moveToPosition(cc);
            // Get Item Detail
            TotalExpenseBalance += Integer.parseInt(cursorExp.getString(3).toString());
        }

        Bal = TotalIncomeBalance - TotalExpenseBalance;
        tvBalance.setText("$ "+String.valueOf(Bal));
        // Set Income dan expense Total
        tvValueIncome.setText("$ " + String.valueOf(TotalIncomeBalance));
        tvValueExpense.setText("$ " + String.valueOf(TotalExpenseBalance));
    }

    /**
     * Load ListView as CardView Expense and Revenue Set Update And Delete when User Clicked Each Item
     */
    public void LoadListView() {

        GetBalance();

        MyAdapterRevenue adapterIncome = new MyAdapterRevenue(getActivity(), DescriptionIncome, CategoryIncome, AmountIncome);
        ListViewIncome.setAdapter(adapterIncome);
        // Set On ClickListener on each Item
        ListViewIncome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get item clicked ID
                final String idIncome = IdIncome[position];
                // Get item clicked Desc
                final String desIncome = DescriptionIncome[position];
                // Get item clicked Amount
                final String amountIncome = AmountIncome[position];
                // Get item clicked Category
                final String categoryIncome = CategoryIncome[position];
                // Set Option when item clicked
                final CharSequence[] dialogitem = {"Edit Income", "Delete Income"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Options");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                // User clicked the Update Option
                                showEditDialogRevenue(idIncome, categoryIncome, desIncome, amountIncome);
                                break;
                            case 1:
                                // User clicked the Delete Option
                                SQLiteDatabase dbWrite = dataHelper.getWritableDatabase();
                                dbWrite.execSQL("delete from income where id = '"+idIncome+"'");
                                new MyAsynch().execute();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });

        MyAdapterExpense adapterExpense = new MyAdapterExpense(getActivity(), DescriptionExpense, CategoryExpense, AmountExpense);
        ListViewExpense.setAdapter(adapterExpense);
        // Set On ClickListener on each Item
        ListViewExpense.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get item clicked ID
                final String idExpense = IdExpense[position];
                // Get item clicked Desc
                final String desExpense = DescriptionExpense[position];
                // Get item clicked Amount
                final String amountExpense = AmountExpense[position];
                // Get item clicked Category
                final String categoryExpense = CategoryExpense[position];
                // Set Option when item clicked
                final CharSequence[] dialogitem = {"Edit Expense", "Delete Expense"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Options");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                // User clicked the Update Option
                                showEditDialogExpense(idExpense, categoryExpense, desExpense, amountExpense);
                                break;
                            case 1:
                                SQLiteDatabase dbWrite = dataHelper.getWritableDatabase();
                                dbWrite.execSQL("delete from expense where id = '"+idExpense+"'");
                                new MyAsynch().execute();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });

        // Set Layout
        linearLayout.setVisibility(View.GONE);
        if (AdaDataIncome < 1 && AdaDataExpense < 1){
            linearLayout.setVisibility(View.VISIBLE);
            ListViewIncome.setVisibility(View.GONE);
            ListViewExpense.setVisibility(View.GONE);
//            footer.setVisibility(View.GONE);
        } else if (AdaDataIncome > 0 && AdaDataExpense < 1){
            linearLayout.setVisibility(View.GONE);
            ListViewIncome.setVisibility(View.VISIBLE);
            ListViewExpense.setVisibility(View.GONE);

        } else if (AdaDataExpense > 0 && AdaDataIncome < 1){
            linearLayout.setVisibility(View.GONE);
            ListViewExpense.setVisibility(View.VISIBLE);
            ListViewIncome.setVisibility(View.GONE);

        } else {

            linearLayout.setVisibility(View.GONE);
            ListViewIncome.setVisibility(View.VISIBLE);
            ListViewExpense.setVisibility(View.VISIBLE);
        }

        ListUtils.setDynamicHeight(ListViewIncome);
        ListUtils.setDynamicHeight(ListViewExpense);

        ((ArrayAdapter) ListViewExpense.getAdapter()).notifyDataSetInvalidated();
        ((ArrayAdapter) ListViewIncome.getAdapter()).notifyDataSetInvalidated();
    }

    /**
     * Form Dialog for Edit Revenue
     */
    private void showEditDialogRevenue(final String idIncome, String categoryIncome, String desIncome, String amountIncome){
        // Initial Layout to Inflate the dialogbox
        LayoutInflater mainLayout = LayoutInflater.from(getActivity());
        View mView = mainLayout.inflate(R.layout.dialog_edit_income, null);
        final EditText amount = mView.findViewById(R.id.inputAmount);
        final EditText notes = mView.findViewById(R.id.inputDesk);
        final TextView category = mView.findViewById(R.id.category);

        amount.setText(amountIncome);
        notes.setText(desIncome);
        category.setText(categoryIncome);
        // Build the Dialog
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(false)
                // User Click the Save Button
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // Empty Field
                        if (amount.getText().toString().isEmpty() || notes.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Fill all details !", Toast.LENGTH_LONG).show();
                        }
                        // Not Empty Field
                        else {
                            boolean amIValid = false;
                            try {
                                Integer.parseInt(amount.getText().toString());
                                //is a valid integer!
                                amIValid = true;
                            } catch (NumberFormatException e) {

                            }
                            if (amIValid){
                                SQLiteDatabase dbWrite = dataHelper.getWritableDatabase();
                                dbWrite.execSQL("UPDATE 'income' SET description = '"+notes.getText().toString()+"' , amount = '"+amount.getText().toString()+"' where id = '"+idIncome+"'");
                                Toast.makeText(getContext(), "Successfully", Toast.LENGTH_LONG).show();
                                new MyAsynch().execute();
                            }
                            else {
                                Toast.makeText(getContext(), "Failed to Update Income", Toast.LENGTH_LONG).show();
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
     * Form Dialog for Edit Revenue
     */
    private void showEditDialogExpense(final String idExpense, String categoryExpense, String desExpense, String amountExpense){

        LayoutInflater mainLayout = LayoutInflater.from(getActivity());
        View mView = mainLayout.inflate(R.layout.dialog_edit_expense, null);
        final EditText amount = mView.findViewById(R.id.inputAmount);
        final EditText catatan = mView.findViewById(R.id.inputDesk);
        final TextView category = mView.findViewById(R.id.category);

        amount.setText(amountExpense);
        catatan.setText(desExpense);
        category.setText(categoryExpense);
        // Build the Dialog
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(false)
                // User Click the Save Button
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // Empty Field
                        if (amount.getText().toString().isEmpty() || catatan.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Successfully !", Toast.LENGTH_LONG).show();
                        }
                        // Not Empty Field
                        else {
                            boolean amIValid = false;
                            try {
                                Integer.parseInt(amount.getText().toString());
                                //is a valid integer!
                                amIValid = true;
                            } catch (NumberFormatException e) {

                            }
                            if (amIValid){
                                SQLiteDatabase dbWrite = dataHelper.getWritableDatabase();
                                dbWrite.execSQL("UPDATE 'expense' SET description = '"+catatan.getText().toString()+"' , amount = '"+amount.getText().toString()+"' where id = '"+idExpense+"'");
                                Toast.makeText(getContext(), "Successfully Update", Toast.LENGTH_LONG).show();
                                new MyAsynch().execute();
                            }
                            else {
                                Toast.makeText(getContext(), "Failed to Update ", Toast.LENGTH_LONG).show();
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
     * Adapter for Set Data ListView as CardView Revenue
     */
    private class MyAdapterRevenue extends ArrayAdapter {
        String list_des[];
        String list_category[];
        String list_amount[];
        Activity activity;

        public MyAdapterRevenue(Activity mainActivity, String[] list_des, String list_category[], String list_amount[]) {
            super(mainActivity, R.layout.item_income, list_des);
            activity = mainActivity;
            this.list_des = list_des;
            this.list_category = list_category;
            this.list_amount = list_amount;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) activity.getLayoutInflater();
            View v = inflater.inflate(R.layout.item_income, null);
            TextView des;
            TextView category;
            TextView amount;
            TextView date;
            ImageView imageView;

            //casting widget id
            des = v.findViewById(R.id.des);
            category = v.findViewById(R.id.namecategory);
            amount = v.findViewById(R.id.amount);
            date = v.findViewById(R.id.date);
            imageView = v.findViewById(R.id.list_avatar);

            // set data kedalam cardview
            des.setText(list_des[position]);
            category.setText(list_category[position]);
            amount.setText("$ "+ list_amount[position]);
            date.setText(datetop);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.add_circular_32));

            return v;
        }
    }

    /**
     * Adapter for Set Data ListView as CardView Revenue
     */
    private class MyAdapterExpense extends ArrayAdapter {
        String list_des[];
        String list_category[];
        String list_amount[];
        Activity activity;

        public MyAdapterExpense(Activity mainActivity, String[] list_des, String list_category[], String list_amount[]) {
            super(mainActivity, R.layout.item_expense, list_des);
            activity = mainActivity;
            this.list_des = list_des;
            this.list_category = list_category;
            this.list_amount = list_amount;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) activity.getLayoutInflater();
            View v = inflater.inflate(R.layout.item_expense, null);
            TextView des;
            TextView category;
            TextView amount;
            TextView date;
            ImageView imageView;

            des = v.findViewById(R.id.des);
            category = v.findViewById(R.id.namecategory);
            amount = v.findViewById(R.id.amount);
            date = v.findViewById(R.id.date);
            imageView = v.findViewById(R.id.list_avatar);

            des.setText(list_des[position]);
            category.setText(list_category[position]);
            amount.setText("$ "+ list_amount[position]);
            date.setText(datetop);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.minus_circular_32));

            return v;
        }
    }

    /**
     * Load Data From Database Expense
     */
    public void GetDataExpense (){
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from expense inner join category_expense where expense.idcategory = category_expense.id  and expense.date = '"+ datetop +"'", null);
        AdaDataExpense = cursor.getCount();;
        // Initial Array Size
        int totalData = cursor.getCount();
        IdExpense = new String[totalData];
        IdCategoryExpense = new String[totalData];
        CategoryExpense = new String[totalData];
        DescriptionExpense = new String[totalData];
        AmountExpense = new String[totalData];

        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            // Get Item Detail
            IdExpense[cc]         = cursor.getString(0).toString();
            IdCategoryExpense[cc] = cursor.getString(1).toString();
            DescriptionExpense[cc]  = cursor.getString(2).toString();
            AmountExpense[cc]     = cursor.getString(3).toString();
            CategoryExpense[cc]   = cursor.getString(6).toString();
        }
    }

    /**
     * Load Data From Database Revenue
     */
    public void GetDataRevenue (){
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        cursor = db.rawQuery("select * from income inner join category_income where income.idcategory = category_income.id and income.date = '"+ datetop +"'", null);
        AdaDataIncome = cursor.getCount();

        // Initial Array Size
        int totalData = cursor.getCount();
        IdIncome = new String[totalData];
        IdCategoryIncome = new String[totalData];
        CategoryIncome = new String[totalData];
        DescriptionIncome = new String[totalData];
        AmountIncome = new String[totalData];

        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            // Get Item Detail
            IdIncome[cc]         = cursor.getString(0).toString();
            IdCategoryIncome[cc] = cursor.getString(1).toString();
            DescriptionIncome[cc]  = cursor.getString(2).toString();
            AmountIncome[cc]     = cursor.getString(3).toString();
            CategoryIncome[cc]   = cursor.getString(6).toString();
        }
    }

    /**
     * Load Data From Database Expense Category
     */
    public void GetCategoryExpense (){
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM category_expense", null);
        // Initial Array Size
        SpinIdCategoryExpense = new String[cursor.getCount()];
        SpinNameCategoryExpense = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            // Get Id
            SpinIdCategoryExpense[cc] = cursor.getString(0).toString();
            // Get Item Name
            SpinNameCategoryExpense[cc] = cursor.getString(1).toString();
        }
    }

    /**
     * Load Data From Database Revenue Category
     */
    public void GetCategoryRevenue (){
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM category_income", null);
        // Initial Array Size
        SpinIdCategoryIncome = new String[cursor.getCount()];
        SpinNameCategoryIncome = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            // Get Id
            SpinIdCategoryIncome[cc] = cursor.getString(0).toString();
            // Get Item Name
            SpinNameCategoryIncome[cc] = cursor.getString(1).toString();
        }
    }


    public void loadKategoriExpense() {
        GetCategoryExpense();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SpinNameCategoryExpense);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinCategoryExpense.setAdapter(dataAdapter);
        spinCategoryExpense.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECTED_CATEGORY_ID_EXPENSE = Integer.parseInt(SpinIdCategoryExpense[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    public void loadKategoriRevenue() {
        GetCategoryRevenue();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, SpinNameCategoryIncome);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinCategoryIncome.setAdapter(dataAdapter);
        spinCategoryIncome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SELECTED_CATEGORY_ID_INCOME = Integer.parseInt(SpinIdCategoryIncome[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Form Dialog for New Expense
     */
    public void showNewExpenseDialog() {
        LayoutInflater mainLayout = LayoutInflater.from(getActivity());
        View mView = mainLayout.inflate(R.layout.dialog_add_expense, null);
        final EditText amount = mView.findViewById(R.id.inputAmount);
        final EditText des = mView.findViewById(R.id.inputDesk);
        spinCategoryExpense = mView.findViewById(R.id.spinner);
        loadKategoriExpense();
        // Build the Dialog
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(false)
                // User Click the Save Button
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // Empty Field
                        if (amount.getText().toString().isEmpty() || des.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Successfully !", Toast.LENGTH_LONG).show();
                        }
                        // Not Empty Field
                        else {
                            boolean amIValid = false;
                            try {
                                Integer.parseInt(amount.getText().toString());
                                //is a valid integer!
                                amIValid = true;
                            } catch (NumberFormatException e) {

                            }
                            if (amIValid){
                                SQLiteDatabase dbWrite = dataHelper.getWritableDatabase();
                                dbWrite.execSQL("INSERT INTO 'expense' ('idcategory', 'description', 'amount', 'date') VALUES ('"+ SELECTED_CATEGORY_ID_EXPENSE +"', '"+des.getText().toString()+"', '"+amount.getText().toString()+"', '"+ datetop +"') ");
                                Toast.makeText(getContext(), "Successfully ", Toast.LENGTH_LONG).show();
                                new MyAsynch().execute();
                            }
                            else {
                                Toast.makeText(getContext(), "Failed ", Toast.LENGTH_LONG).show();
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
     * Form Dialog for New Revenue
     */
    public void showNewRevenueDialog() {
        LayoutInflater mainLayout = LayoutInflater.from(getActivity());
        View mView = mainLayout.inflate(R.layout.dialog_add_income, null);
        final EditText amount = mView.findViewById(R.id.inputAmount);
        final EditText des = mView.findViewById(R.id.inputDesk);
        spinCategoryIncome = mView.findViewById(R.id.spinnerCategoryIncome);
        loadKategoriRevenue();
        // Build the Dialog
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(false)
                // User Click the Save Button
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // Empty Field
                        if (amount.getText().toString().isEmpty() || des.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Successfully !", Toast.LENGTH_LONG).show();
                        }
                        // Not Empty Field
                        else {
                            boolean amIValid = false;
                            try {
                                Integer.parseInt(amount.getText().toString());
                                //is a valid integer!
                                amIValid = true;
                            } catch (NumberFormatException e) {

                            }
                            if (amIValid){
                                SQLiteDatabase dbWrite = dataHelper.getWritableDatabase();
                                dbWrite.execSQL("INSERT INTO 'income' ('idcategory', 'description', 'amount', 'date') VALUES ('"+ SELECTED_CATEGORY_ID_INCOME +"', '"+des.getText().toString()+"', '"+amount.getText().toString()+"', '"+ datetop +"') ");
                                Toast.makeText(getContext(), "Succeeded ", Toast.LENGTH_LONG).show();
                                new MyAsynch().execute();
                            }
                            else {
                                Toast.makeText(getContext(), "Failed ", Toast.LENGTH_LONG).show();
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


    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

   




}
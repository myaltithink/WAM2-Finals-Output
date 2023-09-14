package wam2.finals.filevault;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PasswordListing extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<PasswordObj> files;
    private ActionBar actionBar;

    private DatabaseHandler db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        recyclerView = findViewById(R.id.recycler_view);
        db = new DatabaseHandler(PasswordListing.this);
        files = new ArrayList<>();
        actionBar = getSupportActionBar();

        actionBar.setTitle("Passwords");

        TextView noData = findViewById(R.id.no_data);

        FloatingActionButton fab = findViewById(R.id.add_file);
        fab.setOnClickListener(v -> {
            //add new pass
            AlertDialog.Builder builder = new AlertDialog.Builder(PasswordListing.this);
            builder.setTitle("Add Password");
            View view = getLayoutInflater().inflate(R.layout.add_password, null);
            builder.setView(view);
            builder.setPositiveButton("Save", (dialogInterface, i) -> {
                EditText website, email, password;
                website = view.findViewById(R.id.website);
                email = view.findViewById(R.id.email);
                password = view.findViewById(R.id.password);

                boolean res = db.insertNewPass(website.getText().toString(), email.getText().toString(), password.getText().toString());

                if (res){
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PasswordListing.this, PasswordListing.class));
                }else {
                    Toast.makeText(this, "Couldn't Save", Toast.LENGTH_SHORT).show();
                }

            });
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            builder.create().show();
        });

        Cursor cursor = db.getAllFiles();
        if (cursor.getCount() != 0) {
            noData.setVisibility(View.GONE);
            while (cursor.moveToNext()){
                files.add(
                        new PasswordObj(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3)
                        )
                );
            }
        }
    }

    private void attachAdapter(){
        adapter = new RecyclerViewAdapter(PasswordListing.this, files);
        recyclerView.setLayoutManager(new LinearLayoutManager(PasswordListing.this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        MenuItem deleteAll = menu.findItem(R.id.delete_all);
        deleteAll.setOnMenuItemClickListener(item -> {
            db.clearFileDatabase();
            Toast.makeText(this, "Passwords has been cleared", Toast.LENGTH_LONG).show();
            startActivity(new Intent(PasswordListing.this, PasswordListing.class));
            return true;
        });

        MenuItem updateCode = menu.findItem(R.id.update_code);
        updateCode.setOnMenuItemClickListener(item -> {
                    View pinSetup = getLayoutInflater().inflate(R.layout.setup_pin, null);

            pinSetup.findViewById(R.id.setup_pin).setVisibility(View.GONE);
            LinearLayout edit = pinSetup.findViewById(R.id.update_pin);
            edit.setVisibility(View.VISIBLE);

            AlertDialog.Builder builder = new AlertDialog.Builder(PasswordListing.this);
            builder.setTitle("Update Vault Pin");
            builder.setView(pinSetup);
            builder.setPositiveButton("Save", (dialog, which) -> {
                EditText oldPin, newPin;
                oldPin = pinSetup.findViewById(R.id.old_pin);
                newPin = pinSetup.findViewById(R.id.new_pin);

                if (db.checkVaultCode(oldPin.getText().toString())){
                    if (db.updateCode(newPin.getText().toString())){
                        Toast.makeText(this, "Vault Pin has been updated", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else {
                        Toast.makeText(this, "Failed to update Vault Pin", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Old Pin did not match", Toast.LENGTH_SHORT).show();
                }

            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
               dialog.dismiss();
            });
            builder.create().show();

            return true;
        });
        attachAdapter();
        return super.onCreateOptionsMenu(menu);
    }
}

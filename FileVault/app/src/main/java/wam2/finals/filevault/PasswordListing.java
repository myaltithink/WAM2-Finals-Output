package wam2.finals.filevault;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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

        actionBar.setTitle("Files");

        TextView noData = findViewById(R.id.no_data);

        FloatingActionButton fab = findViewById(R.id.add_file);
        fab.setOnClickListener(v -> {
            //add new pass
        });

        Cursor cursor = db.getAllFiles();
        if (cursor.getCount() != 0) {
            noData.setVisibility(View.GONE);
            while (cursor.moveToNext()){
                files.add(
                        new PasswordObj(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2)
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
            Toast.makeText(this, "File Database has been cleared", Toast.LENGTH_LONG).show();
            startActivity(new Intent(PasswordListing.this, PasswordListing.class));
            return true;
        });
        attachAdapter();
        return super.onCreateOptionsMenu(menu);
    }
}

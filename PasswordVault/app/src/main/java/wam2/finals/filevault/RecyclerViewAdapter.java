package wam2.finals.filevault;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<PasswordObj> passwords;
    private ArrayList<PasswordObj> displayPasswords;

    private DatabaseHandler db;

    public RecyclerViewAdapter(Context context, ArrayList<PasswordObj> files) {
        this.context = context;
        this.passwords = files;
        this.db = new DatabaseHandler(context);
        this.displayPasswords = new ArrayList<>(files);
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        PasswordObj passwordData = displayPasswords.get(position);
        holder.website.setText(passwordData.getWebsite());

        holder.fileContainer.setOnClickListener(v -> {
            PasswordObj item = displayPasswords.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Password Info");
            View view = LayoutInflater.from(context).inflate(R.layout.add_password, null);
            EditText website, email, password;
            website = view.findViewById(R.id.website);
            email = view.findViewById(R.id.email);
            password = view.findViewById(R.id.password);

            website.setText(item.getWebsite(), TextView.BufferType.EDITABLE);
            email.setText(item.getEmail(), TextView.BufferType.EDITABLE);
            password.setText(item.getPassword(), TextView.BufferType.EDITABLE);

            Button delete = view.findViewById(R.id.delete);
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(view1 -> {
                if (db.deletePassword(item.getID() + "")){
                    Toast.makeText(context, "Delete Succefully", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, PasswordListing.class));
                }else {
                    Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setView(view);

            builder.setPositiveButton("Save", (dialogInterface, i) -> {
                item.setEmail(email.getText().toString());
                item.setWebsite(website.getText().toString());
                item.setPassword(password.getText().toString());
                db.updatePassword(item);
                context.startActivity(new Intent(context, PasswordListing.class));
            });

            builder.setNegativeButton("Close", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            builder.create().show();

        });
    }

    @Override
    public int getItemCount() {
        return displayPasswords.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PasswordObj> filteredItems = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredItems.addAll(passwords);
            }else {
                String pattern = constraint.toString().trim();

                for (PasswordObj password : passwords) {
                    if (password.getWebsite().contains(pattern)){
                        filteredItems.add(password);
                    }
                }
            }
            FilterResults result = new FilterResults();
            result.values = filteredItems;
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            displayPasswords.clear();
            displayPasswords.addAll((Collection<? extends PasswordObj>) results.values);
            notifyDataSetChanged();
        }
    };


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView website;
        RelativeLayout fileContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            website = itemView.findViewById(R.id.website);
            fileContainer = itemView.findViewById(R.id.file_container);
        }
    }

}

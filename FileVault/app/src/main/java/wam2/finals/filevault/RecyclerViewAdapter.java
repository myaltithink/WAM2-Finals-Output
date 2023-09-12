package wam2.finals.filevault;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<PasswordObj> files;
    private ArrayList<PasswordObj> displayFiles;

    public RecyclerViewAdapter(Context context, ArrayList<PasswordObj> files) {
        this.context = context;
        this.files = files;
        this.displayFiles = new ArrayList<>(files);
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        PasswordObj fileData = displayFiles.get(position);
        String fileName = fileData.getFileName() + "." + fileData.getExtension();
        holder.fileName.setText(fileName);
        holder.dateAdded.setText(fileData.getDateAdded());

        holder.fileContainer.setOnClickListener(v -> {
            Toast.makeText(context, "item at position " + position + " was selected", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
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
                filteredItems.addAll(files);
            }else {
                String pattern = constraint.toString().trim();

                for (PasswordObj file : files) {
                    if (file.getFileName().contains(pattern) || file.getDateAdded().contains(pattern)){
                        filteredItems.add(file);
                    }
                }
            }
            FilterResults result = new FilterResults();
            result.values = filteredItems;
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            displayFiles.clear();
            displayFiles.addAll((Collection<? extends PasswordObj>) results.values);
            notifyDataSetChanged();
        }
    };


    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView fileName, dateAdded;
        RelativeLayout fileContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.file_name);
            dateAdded = itemView.findViewById(R.id.date_added);
            fileContainer = itemView.findViewById(R.id.file_container);
        }
    }

}

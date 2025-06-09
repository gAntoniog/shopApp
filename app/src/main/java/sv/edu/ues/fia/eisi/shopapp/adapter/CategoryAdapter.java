package sv.edu.ues.fia.eisi.shopapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import sv.edu.ues.fia.eisi.shopapp.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> categoryList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String categoryName);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public CategoryAdapter(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Usamos el layout item_category.xml que creamos
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        String currentCategory = categoryList.get(position);
        holder.categoryNameTextView.setText(currentCategory);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentCategory);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameTextView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
        }
    }

    public void updateCategories(List<String> newCategories) {
        this.categoryList.clear();
        this.categoryList.addAll(newCategories);
        notifyDataSetChanged();
    }
}

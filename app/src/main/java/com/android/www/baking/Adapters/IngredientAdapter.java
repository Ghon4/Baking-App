package com.android.www.baking.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.www.baking.model.Ingredient;
import com.android.www.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> mIngredient;

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {

        Ingredient ingredient = mIngredient.get(position);

        holder.tvQuantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.tvMeasure.setText(ingredient.getMeasure());
        holder.tvingredient.setText(ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        if (mIngredient == null) {
            return 0;
        }

        return mIngredient.size();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.mIngredient = ingredients;

        if (mIngredient != null) {
            notifyDataSetChanged();
        }
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredient_quantity)
        TextView tvQuantity;

        @BindView(R.id.tv_ingredient_measure)
        TextView tvMeasure;

        @BindView(R.id.tv_ingredient)
        TextView tvingredient;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

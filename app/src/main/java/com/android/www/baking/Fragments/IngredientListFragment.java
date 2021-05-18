package com.android.www.baking.Fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.www.baking.Adapters.IngredientAdapter;
import com.android.www.baking.Constants;
import com.android.www.baking.model.Ingredient;
import com.android.www.baking.model.Recipe;
import com.android.www.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;




public class IngredientListFragment extends Fragment {


    private IngredientAdapter mIngredientAdapter;


    @BindView(R.id.rv_recipe_ingredient)
    RecyclerView mRecyclerView;

    private Unbinder unbinder;


    public IngredientListFragment() {
    }

    public static IngredientListFragment newInstance(Recipe recipe) {

        Bundle args = new Bundle();
        args.putParcelable(Constants.RECIPE_INGREDIENT_LIST, recipe);

        IngredientListFragment fragment = new IngredientListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mIngredientAdapter = new IngredientAdapter();

        mRecyclerView.setAdapter(mIngredientAdapter);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(Constants.BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }

        Bundle bundle = getArguments();

        if (bundle != null) {
            Recipe recipe = bundle.getParcelable(Constants.RECIPE_INGREDIENT_LIST);

            if (recipe != null) {
                List<Ingredient> ingredients = recipe.getIngredients();
                mIngredientAdapter.setIngredients(ingredients);
            }
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.BUNDLE_RECYCLER_LAYOUT,
                mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

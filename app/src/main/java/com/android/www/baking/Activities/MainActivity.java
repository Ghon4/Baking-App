package com.android.www.baking.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.www.baking.Adapters.RecipeAdapter;
import com.android.www.baking.Constants;
import com.android.www.baking.model.Recipe;
import com.android.www.baking.utils.NetworkUtils;
import com.android.www.bakingapp.R;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Recipe>>, RecipeAdapter.OnRecipeClickListener{





    @BindView(R.id.rv_main_recipe)
    RecyclerView mRecyclerView;

    private RecipeAdapter mRecipeAdapter;

    @BindView(R.id.pb_loading)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        GridLayoutManager layoutManager = new GridLayoutManager(
                this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecipeAdapter = new RecipeAdapter(this, this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        getSupportLoaderManager().initLoader(Constants.RECIPE_LOADER_ID, null, this);

    }

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        return new RecipeAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if (data != null) {
            mRecipeAdapter.setRecipes(data);
        } else {
            Toast.makeText(this, R.string.error_msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {

    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        Intent openDetailActivity = new Intent(this, DetailActivity.class);
        openDetailActivity.putExtra(Constants.RECIPE_INTENT_EXTRA, recipe);

        startActivity(openDetailActivity);
    }



    private static class RecipeAsyncTaskLoader extends AsyncTaskLoader<List<Recipe>> {

        private List<Recipe> mRecipes;

        private WeakReference<MainActivity> mMainActivityWeakReference;

        public RecipeAsyncTaskLoader(MainActivity mainActivity) {
            super(mainActivity);
            this.mMainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (mRecipes != null) {
                deliverResult(mRecipes);
            } else {
                if (mMainActivityWeakReference.get() != null) {
                    mMainActivityWeakReference.get().mProgressBar.setVisibility(View.VISIBLE);
                }
                forceLoad();
            }
        }

        @Override
        public List<Recipe> loadInBackground() {

            URL requestUrl = NetworkUtils.buildUrl(getContext()
                    .getResources().getString(R.string.request_url));

            if (requestUrl != null) {
                String recipesJsonResponse = NetworkUtils.getJsonResponseFromHttpUrl(requestUrl);
                if (recipesJsonResponse != null) {
                    return NetworkUtils.getRecipeFromJson(recipesJsonResponse);
                }
            }
            return null;
        }

        @Override
        public void deliverResult(List<Recipe> data) {
            this.mRecipes = data;
            super.deliverResult(data);
        }
    }


}

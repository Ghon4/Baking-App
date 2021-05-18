package com.android.www.baking.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.www.baking.Adapters.RecipeAdapter;
import com.android.www.baking.Constants;
import com.android.www.baking.model.Ingredient;
import com.android.www.baking.model.Recipe;
import com.android.www.baking.utils.NetworkUtils;
import com.android.www.bakingapp.R;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetConfigurationActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Recipe>>, RecipeAdapter.OnRecipeClickListener {


    @BindView(R.id.rv_widget_recipe)
    RecyclerView mRecView;

    @BindView(R.id.pb_widget_loading_indicator)
    ProgressBar bar;

    private RecipeAdapter mRecipeAdapter;

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private AppWidgetManager mAppWidgetManger;
    private RemoteViews mRemoteViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configuration);
        ButterKnife.bind(this);

        setResult(RESULT_CANCELED);

        mAppWidgetManger = AppWidgetManager.getInstance(this);

        mRemoteViews = new RemoteViews(this.getPackageName(), R.layout.ingredient_list_widget);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        GridLayoutManager layoutManager = new GridLayoutManager(
                this, 2);
        mRecView.setLayoutManager(layoutManager);

        mRecView.setHasFixedSize(true);

        mRecipeAdapter = new RecipeAdapter(this, this);
        mRecView.setAdapter(mRecipeAdapter);

        getSupportLoaderManager().initLoader(Constants.WIDGET_LOADER_ID, null, this);
    }


    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new RecipeAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> data) {
        bar.setVisibility(View.INVISIBLE);
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

        List<Ingredient> ingredients = recipe.getIngredients();

        StringBuilder builder = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            builder.append("- ")
                    .append(ingredient.getIngredient())
                    .append(" ")
                    .append(ingredient.getMeasure())
                    .append(" ")
                    .append(ingredient.getQuantity())
                    .append("\n");
        }

        mRemoteViews.setTextViewText(R.id.tv_all_ingredient, builder.toString());


        mAppWidgetManger.updateAppWidget(mAppWidgetId, mRemoteViews);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

        setResult(RESULT_OK, resultValue);
        finish();


    }

    private static class RecipeAsyncTaskLoader extends AsyncTaskLoader<List<Recipe>> {

        private List<Recipe> mRecipes;

        private WeakReference<WidgetConfigurationActivity> mWidgetActivityWeakReference;

        public RecipeAsyncTaskLoader(WidgetConfigurationActivity widgetConfigurationActivity) {
            super(widgetConfigurationActivity);
            this.mWidgetActivityWeakReference = new WeakReference<>(widgetConfigurationActivity);
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (mRecipes != null) {
                deliverResult(mRecipes);
            } else {
                if (mWidgetActivityWeakReference.get() != null) {
                    mWidgetActivityWeakReference.get().bar
                            .setVisibility(View.VISIBLE);
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

package com.android.www.baking.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.www.baking.Constants;
import com.android.www.baking.Fragments.DetailStepFragment;
import com.android.www.baking.Fragments.IngredientListFragment;
import com.android.www.baking.Fragments.StepListFragment;
import com.android.www.baking.model.Recipe;
import com.android.www.baking.model.Step;
import com.android.www.bakingapp.R;


public class DetailActivity extends AppCompatActivity
        implements StepListFragment.OnListFragmentListener {

    private int Position = 0;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();

        if (findViewById(R.id.detail_linearLayout) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {


                if (intent != null) {

                    Recipe recipe = intent.getParcelableExtra(Constants.RECIPE_INTENT_EXTRA);
                    StepListFragment stepListFragment = StepListFragment.newInstance(recipe);

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.step_list_container, stepListFragment)
                            .commit();

                    Step step = recipe.getSteps().get(Position);
                    DetailStepFragment detailStepFragment = DetailStepFragment.newInstance(step);

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.detail_step_container, detailStepFragment)
                            .commit();

                    IngredientListFragment ingredientListFragment = IngredientListFragment.newInstance(recipe);

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.ingredient_list_container, ingredientListFragment)
                            .commit();
                }
            }
        } else {
            mTwoPane = false;
            if (savedInstanceState == null) {

                if (intent != null) {

                    Recipe recipe = intent.getParcelableExtra(Constants.RECIPE_INTENT_EXTRA);
                    StepListFragment stepListFragment = StepListFragment.newInstance(recipe);

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.step_list_container, stepListFragment)
                            .commit();

                    IngredientListFragment ingredientListFragment = IngredientListFragment.newInstance(recipe);

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.ingredient_list_container, ingredientListFragment)
                            .commit();
                }
            }
        }

    }

    @Override
    public void onListFragmentItemClicked(int itemListFragmentPosition) {
        Position = itemListFragmentPosition;
        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra(Constants.RECIPE_INTENT_EXTRA);

        Step step = recipe.getSteps().get(Position);
        if (mTwoPane) {

            DetailStepFragment detailStepFragment = DetailStepFragment.newInstance(step);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_step_container, detailStepFragment)
                    .commit();

        } else {
            Intent openStepDetails = new Intent(this, StepDetailsActivity.class);
            openStepDetails.putExtra(Constants.DETAILS_RECIPE, recipe);
            openStepDetails.putExtra(Constants.DETAILS_STEP, step);

            startActivity(openStepDetails);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

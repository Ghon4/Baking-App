package com.android.www.baking.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.www.baking.Constants;
import com.android.www.baking.Fragments.DetailStepFragment;
import com.android.www.bakingapp.R;

import com.android.www.baking.model.Step;

public class StepDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();

        if (savedInstanceState == null) {


            if (intent != null) {

                Step step = intent.getParcelableExtra(Constants.DETAILS_STEP);
                DetailStepFragment detailStepFragment = DetailStepFragment.newInstance(step);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detail_step_container, detailStepFragment)
                        .commit();

            }
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

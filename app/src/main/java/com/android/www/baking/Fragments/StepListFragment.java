package com.android.www.baking.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.www.baking.Adapters.StepAdapter;
import com.android.www.baking.Constants;
import com.android.www.baking.model.Recipe;
import com.android.www.baking.model.Step;
import com.android.www.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;




public class StepListFragment extends Fragment implements StepAdapter.OnStepItemClickListener {


    @BindView(R.id.rv_recipe_steps)
    RecyclerView mRecyclerView;

    private StepAdapter mStepListAdapter;

    private Unbinder unbinder;

    private OnListFragmentListener mOnListFragmentListener;

    public interface OnListFragmentListener {
        void onListFragmentItemClicked(int itemListFragmentPosition);
    }

    public StepListFragment() {
    }

    public static StepListFragment newInstance(Recipe recipe) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE_STEP_LIST, recipe);

        StepListFragment fragment = new StepListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnListFragmentListener = (OnListFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement OnListFragmentListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_list, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mStepListAdapter = new StepAdapter(this);

        mRecyclerView.setAdapter(mStepListAdapter);

        if (savedInstanceState != null) {
            Parcelable recyclerViewState = savedInstanceState.getParcelable(Constants.RECYCLER_POSITION_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {

            Recipe recipe = bundle.getParcelable(Constants.RECIPE_STEP_LIST);

            if (recipe != null) {
                List<Step> steps = recipe.getSteps();
                mStepListAdapter.setSteps(steps);
            }
        }

        return rootView;
    }

    @Override
    public void onStepItemClicked(int itemPosition) {
        mOnListFragmentListener.onListFragmentItemClicked(itemPosition);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable parcelable = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(Constants.RECYCLER_POSITION_STATE, parcelable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

package com.android.www.baking.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.www.baking.model.Step;
import com.android.www.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepListViewHolder> {

    private List<Step> mStep;

    private OnStepItemClickListener mOnStepItemClickListener;

    public interface OnStepItemClickListener {
        void onStepItemClicked(int itemPosition);
    }

    public StepAdapter(OnStepItemClickListener stepItemClickListener) {
        this.mOnStepItemClickListener = stepItemClickListener;
    }

    @NonNull
    @Override
    public StepListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_item, parent, false);
        return new StepListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StepListViewHolder holder, int position) {
        Step step = mStep.get(position);

        holder.tvShortDescription.setText(step.getShortDescription());

    }

    @Override
    public int getItemCount() {

        if (mStep == null){
            return 0;
        }

        return mStep.size();
    }

    public void setSteps(List<Step> steps) {
        this.mStep = steps;

        if (mStep != null) {
            notifyDataSetChanged();
        }
    }

    public class StepListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_step_short_description)
        TextView tvShortDescription;

        public StepListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getAdapterPosition();

            mOnStepItemClickListener.onStepItemClicked(itemPosition);
        }
    }
}

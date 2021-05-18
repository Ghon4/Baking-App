package com.android.www.baking.Fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.www.baking.Constants;
import com.android.www.baking.model.Step;
import com.android.www.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class DetailStepFragment extends Fragment {

    @BindView(R.id.tv_step_description)
    TextView tvDes;
    @BindView(R.id.tv_step_short_description)
    TextView tvShortDes;
    @BindView(R.id.player_view)
    PlayerView mPlayerView;


    private SimpleExoPlayer mSimpleExoPlayer;

    private boolean mPlayWhenReady = false;
    private int mCurrentWindow = 0;
    private long mPosition = 0;

    private Unbinder unbinder;
    private String mVideoUrl;

    public DetailStepFragment() {
    }

    public static DetailStepFragment newInstance(Step step) {

        Bundle args = new Bundle();
        args.putParcelable(Constants.STEP_LIST_ITEM, step);

        DetailStepFragment fragment = new DetailStepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_step, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            mPlayWhenReady = savedInstanceState.getBoolean(Constants.PLAY_STATE_KEY);
            mCurrentWindow = savedInstanceState.getInt(Constants.WINDOW_INDEX_KEY);
            mPosition = savedInstanceState.getLong(Constants.CURRENT_POSITION_KEY);
            mVideoUrl = savedInstanceState.getString(Constants.VIDEO_URL_KEY);

        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            Step step = bundle.getParcelable(Constants.STEP_LIST_ITEM);

            if (step != null) {
                tvShortDes.setText(step.getShortDescription());
                tvDes.setText(step.getDescription());
                mVideoUrl = step.getVideoURL();


            }
        }

        return rootView;
    }

    private void initializePlayer() {
        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mPlayerView.setPlayer(mSimpleExoPlayer);

        mSimpleExoPlayer.setPlayWhenReady(mPlayWhenReady);
        mSimpleExoPlayer.seekTo(mCurrentWindow, mPosition);


        Uri uri = Uri.parse(mVideoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        mSimpleExoPlayer.prepare(mediaSource, false, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("BakingApp"))
                .createMediaSource(uri);
    }

    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mSimpleExoPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.PLAY_STATE_KEY, mPlayWhenReady);
        outState.putInt(Constants.WINDOW_INDEX_KEY, mCurrentWindow);
        outState.putLong(Constants.CURRENT_POSITION_KEY, mPosition);

        outState.putString(Constants.VIDEO_URL_KEY, mVideoUrl);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayWhenReady = mSimpleExoPlayer.getPlayWhenReady();
        mCurrentWindow = mSimpleExoPlayer.getCurrentWindowIndex();
        mPosition = mSimpleExoPlayer.getCurrentPosition();

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

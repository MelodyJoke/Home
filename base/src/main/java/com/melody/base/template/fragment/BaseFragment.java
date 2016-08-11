package com.melody.base.template.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

/**
 * description: base fragment
 * author: Melody
 * date: 2016/7/9
 * version: 0.0.0.2
 * <p>
 * 0.0.0.1: base fragment.
 * <p>
 * 0.0.0.2: modify some methods to match methods in
 * {@link com.melody.base.template.activity.BaseActivity}
 *
 * @see HandlerFragment may be a better choice if you need to solve hanlder leak problem.
 */
@SuppressWarnings("WeakerAccess, unused")
public abstract class BaseFragment extends Fragment {

    /**
     * context reference
     */
    protected Context mContext;

    /**
     * root layout
     */
    protected View mLayoutView;

    /**
     * onFragmentInteractionListener
     */
    protected OnFragmentInteractionListener mListener;

    public BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getBundle(getArguments());
    }

    protected abstract void getBundle(@NotNull Bundle bundle);

    protected abstract void initViews();

    protected abstract void bindListeners();

    public void onInteraction(Uri uri) {
        if (mListener != null) mListener.onFragmentInteraction(uri);
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener)
            mListener = (OnFragmentInteractionListener) context;
        else throw new RuntimeException(context.toString()
                + " must implement OnFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    protected void setContentView(LayoutInflater inflater, @LayoutRes int layoutRes,
                                  ViewGroup parent) {
        mLayoutView = inflater.inflate(layoutRes, parent, false);
    }

    protected View findViewById(@IdRes int id) {
        return mLayoutView.findViewById(id);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

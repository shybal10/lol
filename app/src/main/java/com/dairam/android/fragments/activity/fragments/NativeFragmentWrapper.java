package com.dairam.android.fragments.activity.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by siva on 6/6/2016.
 */
public class NativeFragmentWrapper extends android.support.v4.app.Fragment{
    private final LoginFragment nativeFragment;

    public NativeFragmentWrapper(LoginFragment nativeFragment) {
        this.nativeFragment = nativeFragment;
    }

    public NativeFragmentWrapper(){

        nativeFragment = null;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        nativeFragment.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        nativeFragment.onActivityResult(requestCode, resultCode, data);
    }
}
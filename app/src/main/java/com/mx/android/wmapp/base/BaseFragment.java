package com.mx.android.wmapp.base;

import android.support.v4.app.Fragment;
import android.view.View;

public class BaseFragment extends Fragment {

    public View findviewbyid(int id) {
        return getView().findViewById(id);
    }
}

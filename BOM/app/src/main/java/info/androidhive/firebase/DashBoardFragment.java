package info.androidhive.firebase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Subbareddy on 5/5/17.
 */

public class DashBoardFragment  extends Fragment {

    private Toolbar toolbar;

    private FragmentTransaction fragmentTransaction = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setHasOptionsMenu(true);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Dash Board");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();


        return view;
    }
}

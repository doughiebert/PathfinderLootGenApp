package mereologic.net.pathfinderlootgen;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.List;

/**
     * A placeholder fragment containing a simple view.
     */
    public class ParameterizerFragment extends Fragment implements View.OnClickListener {

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            if (!(activity instanceof Callbacks)) {
                throw new IllegalStateException("hosting activity must implement ParameterizerFragment.Callbacks");
            }
            callbackReceiver = (Callbacks) activity;
        }

        @Override
        public void onDetach() {
            callbackReceiver = NULL_RECEIVER;
            super.onDetach();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_parameterizer, container, false);

            addLevelOptions(rootView);
            addRollListener(rootView);

            return rootView;
        }

        private void addRollListener(View rootView) {
            rootView.findViewById(R.id.roll_button).setOnClickListener(this);
        }

        @Override
        public void onClick(View button) {
            Spinner levelSpinner = (Spinner) getView().findViewById(R.id.level_spinner);
            Integer level = (Integer) levelSpinner.getSelectedItem();
            callbackReceiver.onRoll(level);
        }

        private void addLevelOptions(View rootView) {
            ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(rootView.getContext(), android.R.layout.simple_spinner_item, oneUpTo(20));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ((Spinner) rootView.findViewById(R.id.level_spinner)).setAdapter(adapter);
        }

        private List<Integer> oneUpTo(int max) {
            List<Integer> levels = new ArrayList<Integer>(max);
            for (int i = 0; i < max; i++) {
                levels.add(i + 1);
            }
            return levels;
        }

        private static final Callbacks NULL_RECEIVER = new Callbacks() {
            @Override
            public void onRoll(int level) {
            }
        };

        private Callbacks callbackReceiver = NULL_RECEIVER;

        interface Callbacks {
            void onRoll(int level);
        }
    }
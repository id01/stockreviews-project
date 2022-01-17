package one.id0.stockreviews;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.fragment.NavHostFragment;

import one.id0.stockreviews.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        MiniStonkInfo msi = MiniStonkInfo.newInstance("AAPL", 3.2f, 0.1f, 100.55f, 1.1f);
        View msiView = msi.onCreateView(inflater, container, savedInstanceState);
        binding.stonkView.addView(msiView);
        msiView.setOnClickListener(v->{
            ((MainActivity)getActivity()).setViewingReviews(true);
            Log.println(Log.VERBOSE, "CLICKED", "CLICKED");
            NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_firstFragment_to_viewReviewsFragment, msi.getParams());
        });

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

/*        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
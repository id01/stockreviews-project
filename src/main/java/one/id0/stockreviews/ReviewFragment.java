package one.id0.stockreviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import one.id0.stockreviews.databinding.FragmentReviewBinding;

public class ReviewFragment extends Fragment {
    private FragmentReviewBinding binding;
    private String owner, content;
    private float rating;
    private long date;

    public ReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewReviewsFragment.
     */
    public static ReviewFragment newInstance(String owner, String content, float rating, long date) {
        ReviewFragment fragment = new ReviewFragment();
        fragment.setParams(owner, content, rating, date);
        return fragment;
    }

    public void setParams(String owner, String content, float rating, long date) {
        this.owner = owner;
        this.content = content;
        this.rating = rating;
        this.date = date;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentReviewBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_reviews, container, false);
    }
}
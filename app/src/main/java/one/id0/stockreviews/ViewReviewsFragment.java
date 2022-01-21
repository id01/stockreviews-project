package one.id0.stockreviews;

import static android.content.ContentValues.TAG;
import static android.view.View.INVISIBLE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import one.id0.stockreviews.databinding.FragmentMiniStonkInfoBinding;
import one.id0.stockreviews.databinding.FragmentViewReviewsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewReviewsFragment extends Fragment {
    private FragmentViewReviewsBinding binding;
    private Map<String, Review> allReviews;
    private String stockName;

    LayoutInflater inflater;
    ViewGroup container;

    public ViewReviewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewReviewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewReviewsFragment newInstance(FirebaseFirestore db) {
        ViewReviewsFragment fragment = new ViewReviewsFragment();
        return fragment;
    }

    public void fetchData(FirebaseFirestore db) {
        if (getArguments() != null) {
            stockName = getArguments().getString("Name");
            float stars = getArguments().getFloat("Stars");
            float starChange = getArguments().getFloat("StarChange");
            float value = getArguments().getFloat("Value");
            float valueChange = getArguments().getFloat("ValueChange");
            try {
                db.collection("User/Ticker/" + stockName)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                HashMap<String, Review> out = new HashMap<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> data = document.getData();
                                    Log.d(TAG, document.getId() + " => " + data);
                                    String owner = (String) data.get("owner");
                                    String content = (String) data.get("content");
                                    Double rating = (Double) data.get("rating");
                                    Long date = (Long) data.get("date");
                                    out.put(owner, new Review(owner, content, rating.floatValue(), date.longValue()));
                                }
                                allReviews = out;
                                Log.w(TAG, "Parsed Review.");
                                if (binding != null) {
                                    showReviews();
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        });
            } catch (NullPointerException e) { // Stock has no reviews yet
                allReviews = null;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            stockName = getArguments().getString("Name");
            float stars = getArguments().getFloat("Stars");
            float starChange = getArguments().getFloat("StarChange");
            float value = getArguments().getFloat("Value");
            float valueChange = getArguments().getFloat("ValueChange");
            FirebaseFirestore db = ((MainActivity)getActivity()).getDB();
//            try {
                db.collection("User/Ticker/" + stockName)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                HashMap<String, Review> out = new HashMap<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Map<String, Object> data = document.getData();
                                    Log.d(TAG, document.getId() + " => " + data);
                                    String owner = (String) data.get("owner");
                                    String content = (String) data.get("content");
                                    Double rating = (Double) data.get("rating");
                                    Long date = (Long) data.get("date");
                                    out.put(owner, new Review(owner, content, rating.floatValue(), date.longValue()));
                                }
                                allReviews = out;
                                Log.w(TAG, "Parsed Review.");
                                if (binding != null) {
                                    showReviews();
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        });
//            } catch (NullPointerException e) { // Stock has no reviews yet
//                allReviews = null;
//            }
        }
    }

    public String getStockName() {
        return stockName;
    }

    public void showReviews() {
        if (allReviews != null && !allReviews.isEmpty()) {
            Log.v(TAG, allReviews.toString());
            for (Review review : allReviews.values()) {
                ReviewFragment rf = ReviewFragment.newInstance(review.owner, review.content, review.rating, review.date);
                binding.reviewsView.addView(rf.onCreateView(inflater, container, new Bundle()));
            }
            binding.noReviewsYetText.setVisibility(View.INVISIBLE);
        } else {
            binding.noReviewsYetText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentViewReviewsBinding.inflate(inflater, container, false);
        this.inflater = inflater;
        this.container = container;

        showReviews();

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}
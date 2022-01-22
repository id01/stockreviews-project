package one.id0.stockreviews;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import one.id0.stockreviews.databinding.FragmentMiniStonkInfoBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MiniStonkInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MiniStonkInfo extends Fragment {

    private FragmentMiniStonkInfoBinding binding;
    private String name;
    private float stars, starChange, value, valueChange;

    public MiniStonkInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name the name of the stock
     * @param stars the star rating of the stock
     * @param starChange the recent change in star rating of the stock
     * @param value the value of the stock
     * @param valueChange the recent change in value of the stock
     * @return A new instance of fragment mini_stonk_info.
     */
    public static MiniStonkInfo newInstance(String name, float stars, float starChange, float value, float valueChange) {
        MiniStonkInfo fragment = new MiniStonkInfo();
        fragment.setParams(name, stars, starChange, value, valueChange);
        return fragment;
    }

    // Sets parameters
    public void setParams(String name, float stars, float starChange, float value, float valueChange) {
        this.name = name;
        this.stars = stars;
        this.starChange = starChange;
        this.value = value;
        this.valueChange = valueChange;
    }

    // Gets parameters
    public Bundle getParams() {
        Bundle b = new Bundle();
        b.putString("Name", this.name);
        b.putFloat("Stars", this.stars);
        b.putFloat("StarChange", this.starChange);
        b.putFloat("Value", this.value);
        b.putFloat("ValueChange", this.valueChange);
        return b;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMiniStonkInfoBinding.inflate(inflater, container, false);
        binding.nameText.setText(name);
//        binding.priceText.setText(String.format("$%.2f (%.2f%%)", value, valueChange));
        binding.ratingBar.setRating(stars);
//        binding.ratingChangeText.setText(String.format("(%.2f)", starChange));
        return binding.getRoot();
    }
}
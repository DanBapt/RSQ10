package com.example.rsq.Pump;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rsq.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class PumpActivity extends AppCompatActivity {

    private List<String> participantNames = new ArrayList<>();
    public void updateParticipantNames(List<String> names) {
        this.participantNames = names;
    }

    public List<String> getParticipantNames() {
        return this.participantNames;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defibrillator);

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        PagerAdapter pagerAdapter = new PagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Utilisez TabLayoutMediator pour relier TabLayout et ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Paramètres");
                        break;
                    case 1:
                        tab.setText("Évaluation");
                        break;
                    case 2:
                        tab.setText("Résultats");
                        break;
                }
            }
        }).attach();
    }

    // Utilisez FragmentStateAdapter au lieu de FragmentPagerAdapter
    public class PagerAdapter extends FragmentStateAdapter {

        public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new PumpParametresFragment();
                case 1:
                    return new PumpEvaluationFragment();
                case 2:
                    return new PumpResultsFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 3;  // Nous avons trois onglets maintenant
        }
    }
}

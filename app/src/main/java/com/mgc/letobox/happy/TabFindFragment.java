package com.mgc.letobox.happy;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leto.game.base.util.DensityUtil;
import com.mgc.letobox.happy.find.ui.ArticleListFragment;
import com.mgc.letobox.happy.circle.ui.CircleFragment;
import com.mgc.letobox.happy.find.ui.TopNewsFragment;
import com.mgc.letobox.happy.view.ColorBar;
import com.mgc.letobox.happy.view.NoScrollViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by zhaozhihui on 2019-09-06
 **/
public class TabFindFragment extends BaseFragment {
    private NoScrollViewPager homePager;
    private List<Fragment> fragments;
    private IndicatorViewPager indicatorViewPager;

    @Keep
    public static TabFindFragment newInstance() {
        TabFindFragment fragment = new TabFindFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_find, container, false);

        // find views
        homePager = view.findViewById(R.id.home_pager);

        // set up top pager
        homePager.setScroll(false);
        homePager.setOffscreenPageLimit(3);
        ScrollIndicatorView scrollIndicatorView = view.findViewById(R.id.indicator);
        scrollIndicatorView.setOnTransitionListener(new OnTransitionTextListener());
        scrollIndicatorView.setScrollBar(new ColorBar(getActivity(), DensityUtil.dip2px(getContext(), 28), DensityUtil.dip2px(getContext(), 4)));

        // set up indicator pager
        indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, homePager);
        indicatorViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        // add top fragments
        fragments = new ArrayList<>();
        fragments.add(TopNewsFragment.newInstance());
        fragments.add(ArticleListFragment.getInstance(ArticleListFragment.TYPE_FOLLOW));
        fragments.add(CircleFragment.newInstance());

        return view;
    }

    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private String[] titleName = getContext().getResources().getStringArray(R.array.find_tab);

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return titleName.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.tab_view, container, false);
            }
            TextView textView = (TextView) convertView;
            int padding = DensityUtil.dip2px(getContext(), 15);
            textView.setPadding(padding, 0, padding, 0);
            textView.setText(titleName[position]);
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_UNCHANGED;
        }
    }
}

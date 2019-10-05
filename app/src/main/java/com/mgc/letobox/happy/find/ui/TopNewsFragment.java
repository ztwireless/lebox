package com.mgc.letobox.happy.find.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.view.tablayout.SlidingTabLayout;
import com.leto.game.base.view.tablayout.listener.OnTabSelectListener;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.model.NewsCategory;
import com.mgc.letobox.happy.find.util.FindApiUtil;
import com.mgc.letobox.happy.find.util.MgctUtil;
import com.mgc.letobox.happy.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class TopNewsFragment extends Fragment {
	// views
	SlidingTabLayout indicator;
	NoScrollViewPager homePager;

	// fragments
	private ArrayList<Fragment> fragments;

	// model
	private List<NewsCategory> mCategoryList = new ArrayList<NewsCategory>();

	public static TopNewsFragment newInstance() {
		TopNewsFragment fragment = new TopNewsFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.find_fragment_top_news, container, false);

		// find views
		homePager = view.findViewById(R.id.home_pager);
		indicator = view.findViewById(R.id.indicator);

		homePager.setScroll(true);
		fragments = new ArrayList<>();

		loadLocalCategory();
		int size = mCategoryList.size();
		for (int i = 0; i < size; i++) {
			fragments.add(ArticleListFragment.getInstance(ArticleListFragment.TYPE_TOP, mCategoryList.get(i).getId()));
		}

		homePager.setOffscreenPageLimit(7);
		homePager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
		homePager.setCurrentItem(0,false);
		homePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				indicator.setCurrentTab(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		indicator.setTabSpaceEqual(false);
		indicator.setTabPadding(10);
		indicator.setTextBold(SlidingTabLayout.TEXT_BOLD_NONE);
		indicator.setTextSelectColor(0xff476efe);
		indicator.setTextUnselectColor(Color.BLACK);
		indicator.setTextsize(16);
		indicator.setViewPager(homePager);
		indicator.setOnTabSelectListener(new OnTabSelectListener() {
			@Override
			public void onTabSelect(int position) {
				homePager.setCurrentItem(position);
			}

			@Override
			public void onTabReselect(int position) {

			}
		});

		// return
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// if no category, load
		if(mCategoryList.isEmpty()) {
			doGetNewsCategory();
		}
	}

	private void doGetNewsCategory() {
		Context ctx = getContext();
		FindApiUtil.getNewsCategory(ctx, new HttpCallbackDecode<List<NewsCategory>>(ctx, null, new TypeToken<List<NewsCategory>>(){}.getType()) {
			@Override
			public void onDataSuccess(List<NewsCategory> data) {
				if(data != null) {
					mCategoryList.clear();
					mCategoryList.addAll(data);
					homePager.getAdapter().notifyDataSetChanged();
					indicator.notifyDataSetChanged();
				}
			}
		});
	}

	private void loadLocalCategory(){
		List<NewsCategory> categories = new ArrayList<>();
		try {
			categories = MgctUtil.loadNewsCategory(getActivity());
			if (null != categories && categories.size() > 0) {
				mCategoryList.clear();
				mCategoryList.addAll(categories);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class MyPagerAdapter extends FragmentPagerAdapter {
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mCategoryList.get(position).getTitle();
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}
	}
}

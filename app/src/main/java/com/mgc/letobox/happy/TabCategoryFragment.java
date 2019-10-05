package com.mgc.letobox.happy;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ledong.lib.minigame.AllCategoryFragment;

/**
 * Create by zhaozhihui on 2019-09-06
 **/
public class TabCategoryFragment extends BaseFragment {

    TextView tv_title;

    @Keep
    public static TabCategoryFragment newInstance() {
        TabCategoryFragment fragment = new TabCategoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_list, container, false);


        tv_title = view.findViewById(R.id.tv_title);

        tv_title.setText("分类");

        // install content fragment
        Fragment fragment = AllCategoryFragment.getInstance(null, 0);
        getChildFragmentManager().beginTransaction()
                .add(R.id.home_content, fragment)
                .commit();

        return view;
    }

}

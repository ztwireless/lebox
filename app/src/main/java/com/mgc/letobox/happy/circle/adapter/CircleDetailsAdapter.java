package com.mgc.letobox.happy.circle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.GlideUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.circle.bean.CircleTieZiListResponse;
import com.mgc.letobox.happy.circle.holder.FootViewHolder;
import com.mgc.letobox.happy.find.FindConst;
import com.mgc.letobox.happy.find.ui.KOLActivitiy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2018/7/6.
 */

public class CircleDetailsAdapter extends SwipeRefreshAdaptor implements IDataAdapter<List<CircleTieZiListResponse>> {

    /**
     * 纯文字布局(文章、广告)
     */
    public static final int TEXT_NEWS = 1;
    /**
     * 三张图片布局(文章、广告)
     */
    public static final int THREE_PICS_NEWS = 2;
    /**
     * 右侧小图布局(1.小图新闻)
     */
    public static final int RIGHT_PIC_NEWS = 3;
    /**
     * 居中大图布局(1.单图文章；2.单图广告；3.视频，中间显示播放图标，右侧显示时长)
     */
    public static final int CENTER_SINGLE_PIC_NEWS = 4;
    /**
     * 居中大图布局(4.组图，右侧显示组图数目)
     */
    public static final int CENTER_PICS = 5;
    /**
     * 居中大图布局(3.视频，中间显示播放图标，右侧显示时长)
     */
    public static final int CENTER_SINGLE_VIDEO_NEWS = 6;
    /**
     * 右侧小图布局(2.视频类型，右下角显示视频时长)
     */
    public static final int RIGHT_VIDEO_NEWS = 7;

    private Context context;


    private List<CircleTieZiListResponse> list = new ArrayList<>();

    private CircleDetailsClickListener listener;

    public CircleDetailsAdapter(Context context, List<CircleTieZiListResponse> list, CircleDetailsClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_FOOTER) {
            View foot_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_footer, parent, false);
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        } else if (viewType == TEXT_NEWS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weibo_text_news, parent, false);
            return new MyViewHolderOne(view);
        } else if (viewType == CENTER_SINGLE_PIC_NEWS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weibo_center_pic, parent, false);
            return new MyViewHolderTwo(view);
        } else if (viewType == RIGHT_PIC_NEWS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weibo_pic_video, parent, false);
            return new MyViewHolderThree(view);
        } else if (viewType == RIGHT_VIDEO_NEWS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weibo_pic_video, parent, false);
            return new MyViewHolderThree(view);
        } else if (viewType == THREE_PICS_NEWS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weibo_three_pics, parent, false);
            return new MyViewHolderFour(view);
        }
        return null;
    }

    public void itemInsert(int pos, CircleTieZiListResponse response, CircleDetailsAdapter mAdapter) {
        if (mAdapter != null && list != null && list.size() != 0) {
            if (pos <= list.size()) {
                list.remove(pos);
                list.add(pos, response);
                mAdapter.notifyItemChanged(pos);
            }
        }
    }

    public void deleteItem(int pos, CircleDetailsAdapter mAdapter) {
        if (mAdapter != null && list != null && list.size() != 0) {
            if (pos <= list.size()) {
                list.remove(pos);
                mAdapter.notifyItemRemoved(pos);
            }
        }
    }

    public void delete(int pos, CircleDetailsAdapter mAdapter) {
        if (mAdapter != null && list != null && list.size() != 0) {
            list.remove(pos);
            mAdapter.notifyItemRemoved(pos);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_tv.setText("上拉加载更多");
                    footViewHolder.view.setVisibility(View.GONE);
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                    footViewHolder.view.setVisibility(View.VISIBLE);
                    break;
                case NO_MORE_DATA:
                    footViewHolder.foot_view_item_tv.setText("暂无更多数据");
                    footViewHolder.view.setVisibility(View.GONE);
                    break;
            }
        } else if (holder instanceof MyViewHolderOne) {
            final CircleTieZiListResponse news = list.get(position);
            final MyViewHolderOne holder1 = (MyViewHolderOne) holder;
            holder1.tv_title.setText(news.getTitle());
            GlideUtil.loadRoundedCorner(context,
                news.getKol().getCover_pic(),
                holder1.iv_avatar,
                20,
                R.mipmap.default_avatar);
            holder1.tv_name.setText(news.getKol().getNickname());
            holder1.tv_time.setText(news.getDate());
            holder1.tv_comment_num.setText(news.getComment());
            if (news.getKol().getIsfollow() == 0) { // 未关注
                holder1.cb_follow.setChecked(false);
            } else { // 已关注
                holder1.cb_follow.setChecked(true);
            }

            if (news.getPost_id() == Integer.valueOf(LoginManager.getMemId(context))) {
                ((MyViewHolderOne) holder).imageView_click.setVisibility(View.VISIBLE);
            } else {
                ((MyViewHolderOne) holder).imageView_click.setVisibility(View.GONE);
            }

            if (LoginManager.getMemId(context) != null) {
                // 判断如果是用户自己就不显示关注按钮
                if (news.getKol().getId() == Integer.valueOf(LoginManager.getMemId(context))) {
                    holder1.cb_follow.setVisibility(View.GONE);
                } else {
                    holder1.cb_follow.setVisibility(View.VISIBLE);
                }
            } else {
                holder1.cb_follow.setVisibility(View.GONE);
            }

            holder1.iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    KOLActivitiy.startActivity(context, news.getKol().getId());
                }
            });

            holder1.cb_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onCheckBox(holder1.cb_follow.isChecked(), holder1.cb_follow, news.getKol().getId(), position);
                    }
                }
            });

            if (news.getIs_edit() == 0 && news.getIs_delete() == 0) {
                ((MyViewHolderOne) holder).imageView_click.setVisibility(View.GONE);
            } else {
                ((MyViewHolderOne) holder).imageView_click.setVisibility(View.VISIBLE);
            }

            ((MyViewHolderOne) holder).imageView_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onShowCircle(((MyViewHolderOne) holder).imageView_click, position, news.getIs_edit(), news.getIs_delete(), news);
                    }
                }
            });

            holder1.ll_weibo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(position, news);
                }
            });
        } else if (holder instanceof MyViewHolderTwo) {
            final MyViewHolderTwo holder1 = (MyViewHolderTwo) holder;
            final CircleTieZiListResponse news = list.get(position);
            if (news.getTemplate_id() == CENTER_SINGLE_VIDEO_NEWS) {
                TextView tvBottomRight = ((MyViewHolderTwo) holder).tv_bottom_right;
                ((MyViewHolderTwo) holder).iv_play.setVisibility(View.VISIBLE);
                tvBottomRight.setVisibility(View.VISIBLE);
                holder1.ll_bottom_right.setVisibility(View.VISIBLE);
                tvBottomRight.setCompoundDrawables(null, null, null, null);//去除TextView左侧图标
                if (null != news.getPics() && news.getPics().size() > 0) {
                    //中间图片使用视频大图
                    Glide.with(((MyViewHolderTwo) holder).iv_img.getContext()).load(news.getPics().get(0).getUrl()).into(((MyViewHolderTwo) holder).iv_img);
                }
            } else {
                holder1.ll_bottom_right.setVisibility(View.GONE);
                ((MyViewHolderTwo) holder).iv_play.setVisibility(View.GONE);
                ((MyViewHolderTwo) holder).tv_bottom_right.setCompoundDrawables(null, null, null, null);//去除TextView左侧图标
                ((MyViewHolderTwo) holder).tv_bottom_right.setVisibility(View.GONE);
                Glide.with(((MyViewHolderTwo) holder).iv_img.getContext()).load(news.getPics().get(0).getUrl()).into(((MyViewHolderTwo) holder).iv_img);

            }

            holder1.tv_title.setText(news.getTitle());
            GlideUtil.loadRoundedCorner(context,
                news.getKol().getCover_pic(),
                holder1.iv_avatar,
                20,
                R.mipmap.default_avatar);
            holder1.tv_name.setText(news.getKol().getNickname());
            holder1.tv_time.setText(news.getDate());
            holder1.tv_comment_num.setText(news.getComment());
            if (news.getKol().getIsfollow() == 0) { // 未关注
                holder1.cb_follow.setChecked(false);
            } else { // 已关注
                holder1.cb_follow.setChecked(true);
            }
            if (LoginManager.getMemId(context) != null) {
                // 判断如果是用户自己就不显示关注按钮
                if (news.getKol().getId() == Integer.valueOf(LoginManager.getMemId(context))) {
                    holder1.cb_follow.setVisibility(View.GONE);
                } else {
                    holder1.cb_follow.setVisibility(View.VISIBLE);
                }
            } else {
                holder1.cb_follow.setVisibility(View.GONE);
            }

            holder1.iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    KOLActivitiy.startActivity(context, news.getKol().getId());
                }
            });

            holder1.cb_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onCheckBox(holder1.cb_follow.isChecked(), holder1.cb_follow, news.getKol().getId(), position);
                    }
                }
            });

            if (news.getIs_edit() == 0 && news.getIs_delete() == 0) {
                ((MyViewHolderTwo) holder).imageView_click.setVisibility(View.GONE);
            } else {
                ((MyViewHolderTwo) holder).imageView_click.setVisibility(View.VISIBLE);
            }

            ((MyViewHolderTwo) holder).imageView_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onShowCircle(((MyViewHolderTwo) holder).imageView_click, position, news.getIs_edit(), news.getIs_delete(), news);
                    }
                }
            });

            ((MyViewHolderTwo) holder).ll_weibo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(position, news);
                }
            });
        } else if (holder instanceof MyViewHolderThree) {
            final CircleTieZiListResponse news = list.get(position);
            final MyViewHolderThree holder1 = (MyViewHolderThree) holder;

            int screen_width = BaseAppUtil.getDeviceWidth(holder1.ll_weibo.getContext());
            int image_width = (screen_width - 2 * DensityUtil.dip2px(holder1.ll_weibo.getContext(), 10)) / 3;
            int image_height = (screen_width - 2 * DensityUtil.dip2px(holder1.ll_weibo.getContext(), 10)) / 4;

            RelativeLayout rl_right = holder1.rl_right;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl_right.getLayoutParams();
            params.height = image_height;
            params.width = image_width;
            rl_right.setLayoutParams(params);

            if (news.getTemplate_id() == RIGHT_VIDEO_NEWS) {
                ((MyViewHolderThree) holder).iv_play.setVisibility(View.VISIBLE);
                ((MyViewHolderThree) holder).tv_duration.setVisibility(View.VISIBLE);
                ((MyViewHolderThree) holder).ll_duration.setVisibility(View.VISIBLE);
                if (null != news.getPics() && news.getPics().size() > 0) {
                    //中间图片使用视频大图
                    GlideUtil.loadRoundedCorner(context,
                        news.getPics().get(0).getUrl()+ String.format(FindConst.image_url_with_size, 640, 420),
                        ((MyViewHolderThree) holder).iv_img,
                        4,
                        R.mipmap.default_image_2);
                }
            } else {
                ((MyViewHolderThree) holder).iv_play.setVisibility(View.GONE);
                ((MyViewHolderThree) holder).tv_duration.setVisibility(View.GONE);
                ((MyViewHolderThree) holder).ll_duration.setVisibility(View.GONE);

                if (null != news.getPics() && news.getPics().size() > 0) {
                    GlideUtil.loadRoundedCorner(context,
                        news.getPics().get(0).getUrl()+ String.format(FindConst.image_url_with_size, 640, 420),
                        ((MyViewHolderThree) holder).iv_img,
                        4,
                        R.mipmap.default_image_2);
                }
            }

            holder1.tv_title.setText(news.getTitle());
            Glide.with(context).load(news.getKol().getCover_pic()).into(holder1.iv_avatar);
            holder1.tv_name.setText(news.getKol().getNickname());
            holder1.tv_time.setText(news.getDate());
            holder1.tv_comment_num.setText(news.getComment());
            if (news.getKol().getIsfollow() == 0) { // 未关注
                holder1.cb_follow.setChecked(false);
            } else { // 已关注
                holder1.cb_follow.setChecked(true);
            }

            if (LoginManager.getMemId(context) != null) {
                // 判断如果是用户自己就不显示关注按钮
                if (news.getKol().getId() == Integer.valueOf(LoginManager.getMemId(context))) {
                    holder1.cb_follow.setVisibility(View.GONE);
                } else {
                    holder1.cb_follow.setVisibility(View.VISIBLE);
                }
            } else {
                holder1.cb_follow.setVisibility(View.GONE);
            }

            holder1.iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    KOLActivitiy.startActivity(context, news.getKol().getId());
                }
            });

            holder1.cb_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onCheckBox(holder1.cb_follow.isChecked(), holder1.cb_follow, news.getKol().getId(), position);
                    }
                }
            });

            if (news.getIs_edit() == 0 && news.getIs_delete() == 0) {
                ((MyViewHolderThree) holder).imageView_click.setVisibility(View.GONE);
            } else {
                ((MyViewHolderThree) holder).imageView_click.setVisibility(View.VISIBLE);
            }
            ((MyViewHolderThree) holder).imageView_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onShowCircle(((MyViewHolderThree) holder).imageView_click, position, news.getIs_edit(), news.getIs_delete(), news);
                    }
                }
            });

            ((MyViewHolderThree) holder).ll_weibo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(position, news);
                }
            });
        } else if (holder instanceof MyViewHolderFour) {
            final CircleTieZiListResponse news = list.get(position);

            final MyViewHolderFour holder1 = (MyViewHolderFour) holder;

            int screen_width = BaseAppUtil.getDeviceWidth(holder1.ll_weibo.getContext());
            int image_width = (screen_width - 2 * DensityUtil.dip2px(holder1.ll_weibo.getContext(), 10)) / 3;
            int image_height = (screen_width - 2 * DensityUtil.dip2px(holder1.ll_weibo.getContext(), 10)) / 4;

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder1.iv_img1.getLayoutParams();
            params.height = image_height;
            params.width = image_width;

            ImageView imgView1 = holder1.iv_img1;
            imgView1.setVisibility(View.VISIBLE);
            imgView1.setLayoutParams(params);
            GlideUtil.loadRoundedCorner(context,
                news.getPics().get(0).getUrl()+ String.format(FindConst.image_url_with_size, 640, 420),
                imgView1,
                4,
                R.mipmap.default_image_2);

            if (news.getPics().size() > 1) {
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) holder1.iv_img1.getLayoutParams();
                params2.height = image_height;
                params2.width = image_width;
                params2.leftMargin = DensityUtil.dip2px(holder1.ll_weibo.getContext(), 5);
                params2.rightMargin = DensityUtil.dip2px(holder1.ll_weibo.getContext(), 5);
                holder1.iv_img2.setLayoutParams(params2);
                holder1.iv_img2.setVisibility(View.VISIBLE);
                GlideUtil.loadRoundedCorner(context,
                    news.getPics().get(1).getUrl()+ String.format(FindConst.image_url_with_size, 640, 420),
                    holder1.iv_img2,
                    4,
                    R.mipmap.default_image_2);
            }
            if (news.getPics().size() > 2) {
                holder1.iv_img3.setLayoutParams(params);
                holder1.iv_img3.setVisibility(View.VISIBLE);
                GlideUtil.loadRoundedCorner(context,
                    news.getPics().get(2).getUrl()+ String.format(FindConst.image_url_with_size, 640, 420),
                    holder1.iv_img3,
                    4,
                    R.mipmap.default_image_2);
            }

            holder1.tv_title.setText(news.getTitle());
            Glide.with(context).load(news.getKol().getCover_pic()).into(holder1.iv_avatar);
            holder1.tv_name.setText(news.getKol().getNickname());
            holder1.tv_time.setText(news.getDate());
            holder1.tv_comment_num.setText(news.getComment());
            if (news.getKol().getIsfollow() == 0) { // 未关注
                holder1.cb_follow.setChecked(false);
            } else { // 已关注
                holder1.cb_follow.setChecked(true);
            }

            if (LoginManager.getMemId(context) != null) {
                // 判断如果是用户自己就不显示关注按钮
                if (news.getKol().getId() == Integer.valueOf(LoginManager.getMemId(context))) {
                    holder1.cb_follow.setVisibility(View.GONE);
                } else {
                    holder1.cb_follow.setVisibility(View.VISIBLE);
                }
            } else {
                holder1.cb_follow.setVisibility(View.GONE);
            }

            holder1.iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    KOLActivitiy.startActivity(context, news.getKol().getId());
                }
            });

            holder1.cb_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onCheckBox(holder1.cb_follow.isChecked(), holder1.cb_follow, news.getKol().getId(), position);
                    }
                }
            });

            if (news.getIs_edit() == 0 && news.getIs_delete() == 0) {
                ((MyViewHolderFour) holder).imageView_click.setVisibility(View.GONE);
            } else {
                ((MyViewHolderFour) holder).imageView_click.setVisibility(View.VISIBLE);
            }
            ((MyViewHolderFour) holder).imageView_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onShowCircle(((MyViewHolderFour) holder).imageView_click, position, news.getIs_edit(), news.getIs_delete(), news);
                    }
                }
            });
            ((MyViewHolderFour) holder).ll_weibo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(position, news);
                }
            });
        }
    }

    @Override
    public void notifyDataChanged(List<CircleTieZiListResponse> gameBeans, boolean isRefresh) {
        if (isRefresh) {
            list.clear();
        }
        list.addAll(gameBeans);
        notifyDataSetChanged();
    }

    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }

    @Override
    public List<CircleTieZiListResponse> getData() {
        return list;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }


    @Override
    public int getItemCount() {
        if (list == null)
            return 1;
        return list.size() + 1;
    }

    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            if (list.get(position).getTemplate_id() == TEXT_NEWS) {
                return TEXT_NEWS;
            } else if (list.get(position).getTemplate_id() == CENTER_SINGLE_PIC_NEWS) {
                return CENTER_SINGLE_PIC_NEWS;
            } else if (list.get(position).getTemplate_id() == RIGHT_VIDEO_NEWS) {
                return RIGHT_VIDEO_NEWS;
            } else if (list.get(position).getTemplate_id() == THREE_PICS_NEWS) {
                return THREE_PICS_NEWS;
            } else if (list.get(position).getTemplate_id() == RIGHT_PIC_NEWS) {
                return RIGHT_PIC_NEWS;
            }
        }
        return TYPE_FOOTER;
    }

    public class MyViewHolderOne extends RecyclerView.ViewHolder {

        private LinearLayout ll_weibo;
        private ImageView imageView_click;
        private ImageView iv_avatar;
        private TextView tv_name;
        private TextView tv_time;
        private CheckBox cb_follow;
        private TextView tv_title;
        private TextView tv_comment_num;

        public MyViewHolderOne(View itemView) {
            super(itemView);
            ll_weibo = itemView.findViewById(R.id.ll_weibo);
            imageView_click = itemView.findViewById(R.id.imageView_click);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_title = itemView.findViewById(R.id.tv_title);
            cb_follow = itemView.findViewById(R.id.cb_follow);
            tv_comment_num = itemView.findViewById(R.id.tv_comment_num);
            iv_avatar.setBackgroundResource(R.mipmap.default_avatar);
            View viewLine = itemView.findViewById(R.id.viewLine);
            viewLine.setVisibility(View.VISIBLE);
        }
    }

    public class MyViewHolderTwo extends RecyclerView.ViewHolder {

        public TextView tv_bottom_right;
        public ImageView iv_play;
        public ImageView iv_img;
        private LinearLayout ll_weibo;
        private ImageView imageView_click;
        private ImageView iv_avatar;
        private TextView tv_name;
        private TextView tv_time;
        private CheckBox cb_follow;
        private TextView tv_title;
        private TextView tv_comment_num;
        private LinearLayout ll_bottom_right;

        public MyViewHolderTwo(View itemView) {
            super(itemView);
            tv_bottom_right = itemView.findViewById(R.id.tv_bottom_right);
            iv_play = itemView.findViewById(R.id.iv_play);
            iv_img = itemView.findViewById(R.id.iv_img);
            ll_weibo = itemView.findViewById(R.id.ll_weibo);
            imageView_click = itemView.findViewById(R.id.imageView_click);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_title = itemView.findViewById(R.id.tv_title);
            cb_follow = itemView.findViewById(R.id.cb_follow);
            tv_comment_num = itemView.findViewById(R.id.tv_comment_num);
            ll_bottom_right = itemView.findViewById(R.id.ll_bottom_right);
            iv_avatar.setBackgroundResource(R.mipmap.default_avatar);
            View viewLine = itemView.findViewById(R.id.viewLine);
            viewLine.setVisibility(View.VISIBLE);
        }
    }

    public class MyViewHolderThree extends RecyclerView.ViewHolder {

        private LinearLayout ll_weibo;
        private ImageView imageView_click;
        public ImageView iv_img;
        public ImageView iv_play;
        private ImageView iv_avatar;
        private TextView tv_name;
        private TextView tv_time;
        private CheckBox cb_follow;
        private TextView tv_title;
        private TextView tv_comment_num;
        private TextView tv_duration;
        private LinearLayout ll_duration;
        private RelativeLayout rl_right;

        public MyViewHolderThree(View itemView) {
            super(itemView);
            ll_weibo = itemView.findViewById(R.id.ll_weibo);
            iv_play = itemView.findViewById(R.id.iv_play);
            iv_img = itemView.findViewById(R.id.iv_img);
            imageView_click = itemView.findViewById(R.id.imageView_click);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            cb_follow = itemView.findViewById(R.id.cb_follow);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_comment_num = itemView.findViewById(R.id.tv_comment_num);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            ll_duration = itemView.findViewById(R.id.ll_duration);
            rl_right = itemView.findViewById(R.id.rl_right);
            iv_avatar.setBackgroundResource(R.mipmap.default_avatar);
            View viewLine = itemView.findViewById(R.id.viewLine);
            viewLine.setVisibility(View.VISIBLE);
        }
    }

    public class MyViewHolderFour extends RecyclerView.ViewHolder {

        public ImageView iv_img1;
        public ImageView iv_img2;
        public ImageView iv_img3;
        public LinearLayout ll_weibo;
        private ImageView imageView_click;
        private ImageView iv_avatar;
        private TextView tv_name;
        private TextView tv_time;
        private CheckBox cb_follow;
        private TextView tv_title;
        private TextView tv_comment_num;

        public MyViewHolderFour(View itemView) {
            super(itemView);
            iv_img1 = itemView.findViewById(R.id.iv_img1);
            iv_img2 = itemView.findViewById(R.id.iv_img2);
            iv_img3 = itemView.findViewById(R.id.iv_img3);
            ll_weibo = itemView.findViewById(R.id.ll_weibo);
            imageView_click = itemView.findViewById(R.id.imageView_click);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            cb_follow = itemView.findViewById(R.id.cb_follow);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_comment_num = itemView.findViewById(R.id.tv_comment_num);
            iv_avatar.setBackgroundResource(R.mipmap.default_avatar);
            View viewLine = itemView.findViewById(R.id.viewLine);
            viewLine.setVisibility(View.VISIBLE);
        }
    }

    public interface CircleDetailsClickListener {
        void onClick(int pos, CircleTieZiListResponse news);

        void onShowCircle(ImageView imageView, int pos, int editext, int detele, CircleTieZiListResponse response);

        void onCheckBox(boolean isCheck, CheckBox cb_follow, int userId, int pos);
    }
}

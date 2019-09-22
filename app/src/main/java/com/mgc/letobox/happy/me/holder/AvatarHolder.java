package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leto.game.base.util.GlideUtil;
import com.leto.game.base.util.MResource;

public class AvatarHolder extends CommonViewHolder<Pair<String, String>> {
	private TextView _nameLabel;
	private ImageView _avatarView;


	public static AvatarHolder create(Context ctx, ViewGroup parent) {
		// load game row, and leave a gap so that next column can be seen
		View view = LayoutInflater.from(ctx)
			.inflate(MResource.getIdByName(ctx, "R.layout.lebox_list_item_avatar"), parent, false);
		return new AvatarHolder(view);
	}

	public AvatarHolder(View view) {
		super(view);

		Context ctx = view.getContext();
		_nameLabel = view.findViewById(MResource.getIdByName(ctx, "R.id.name"));
		_avatarView = view.findViewById(MResource.getIdByName(ctx, "R.id.avatar"));
	}

	@Override
	public void onBind(Pair<String, String> model, int position) {
		// name
		_nameLabel.setText(model.first);

		// avatar
		GlideUtil.loadCircle(itemView.getContext(), model.second, _avatarView);
	}
}
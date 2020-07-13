package com.mgc.letobox.happy.find.dialog;

import android.app.Dialog;
import android.widget.EditText;

import com.mgc.leto.game.base.view.StarBar;

public interface FillDialogCallBack {
	void textViewCreate(Dialog dialog, StarBar starBar, EditText contentEditer, EditText titleEditer);

	void selectPicture();

	void cancel();
}
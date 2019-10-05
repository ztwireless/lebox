package com.mgc.letobox.happy.find.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.leto.game.base.http.HttpParamsBuild;
import com.leto.game.base.util.DialogUtil;
import com.leto.game.base.util.ToastUtil;
import com.leto.game.base.view.StarBar;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.bean.CommentBean;
import com.mgc.letobox.happy.find.bean.PostImage;
import com.mgc.letobox.happy.find.util.FindApi;
import com.mgc.letobox.happy.find.util.RxVolleyUtil;
import com.mgc.letobox.happy.find.util.Util;
import com.mgc.letobox.happy.find.view.KeyboardLayout;
import com.mgc.letobox.happy.find.view.richedittext.RichEditText;
import com.mgc.letobox.happy.find.view.richedittext.span.FakeImageSpan;
import com.mgc.letobox.happy.find.view.richedittext.utils.RichTextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by DELL on 2018/7/3.
 */

public class RichEditDialog {

    private static Toast mToast;
    FillDialogCallBack fillDialogCallBack;
    Dialog dialog;
    KeyboardLayout root_layout;
    RelativeLayout ll_title;
    RichEditText editText;
    EditText etTitle;
    TextView tv_title, tv_number;
    ImageView iv_picture;
    StarBar starBar;
    Context mContext;
    int type;

    String mContent;

    int title_max_length = 30;

    List<String> processing = new ArrayList<>();
    Map<String, String> mFileList = new HashMap<String, String>();
    boolean is_ok;

    private static final int check_upload_status = 0;
    private static final int upload_image = 1;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case check_upload_status:

                    if (processing.size() > 0) {
                        mHandler.sendEmptyMessageDelayed(check_upload_status, 1000);
                    } else {

                        mContent = editText.getMgcRichText(mFileList);
                        fillDialogCallBack.textViewCreate(dialog, starBar, editText, etTitle);
                    }
                    break;
                case upload_image:
                    String path = (String) msg.obj;
                    uploadImage(path);
                    break;
            }
            return false;
        }
    });

    // type- 0:游戏评分；1:评论；2:发稿；3：发贴
    public void fillDialog(Context context, final int type, final FillDialogCallBack fillDialogCallBack) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed())
                return;
        }
        this.mContext = context;
        if (dialog != null) {
            dialog.dismiss();
        }
        is_ok = false;
        this.fillDialogCallBack = fillDialogCallBack;
        this.type = type;
        dialog = new Dialog(context, R.style.CornersDialogStyle);
        View mView = LayoutInflater.from(context).inflate(R.layout.rich_edit_dialog, null);
        root_layout = mView.findViewById(R.id.root_layout);
        ll_title = mView.findViewById(R.id.ll_title);
        tv_title = mView.findViewById(R.id.tv_title);
        tv_number = mView.findViewById(R.id.tv_number);
        etTitle = mView.findViewById(R.id.et_title);
        ImageView imageBack = mView.findViewById(R.id.imageBack);
        iv_picture = mView.findViewById(R.id.iv_picture);
        TextView textViewCreate = mView.findViewById(R.id.textViewCreate);
        starBar = mView.findViewById(R.id.starBar);
        starBar.setIntegerMark(true);
        editText = mView.findViewById(R.id.editText);
        TextView textView_bar = mView.findViewById(R.id.textView_bar);

        if (type == 0) {
            starBar.setVisibility(View.VISIBLE);
            textView_bar.setVisibility(View.VISIBLE);
            ll_title.setVisibility(View.GONE);
            tv_title.setText("写评论");
        } else if (type == 1) {
            starBar.setVisibility(View.GONE);
            textView_bar.setVisibility(View.GONE);
            ll_title.setVisibility(View.GONE);
            tv_title.setText("写评论");
        } else if (type == 2) {
            starBar.setVisibility(View.GONE);
            textView_bar.setVisibility(View.GONE);
            ll_title.setVisibility(View.VISIBLE);
            tv_title.setText("文章投稿");
        } else if (type == 3) {
            starBar.setVisibility(View.GONE);
            textView_bar.setVisibility(View.GONE);
            ll_title.setVisibility(View.VISIBLE);
            tv_title.setText("发布帖子");
        }


        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(mView);
        dialog.show();

        Window dialogWin = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWin.getAttributes();
/**
 Dialog的width和height默认值为WRAP_CONTENT，正是因为如此，当屏幕中有足够的空间时，Dialog是不会被压缩的
 但是设置width和height为MATCH_PARENT的代价是无法设置gravity的值，这就无法调整Dialog中内容的位置，Dialog的内容会显示在屏幕左上角位置不过可以通过Padding来调节Dialog内容的位置。
 **/
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWin.setAttributes(lp);
//
//        ChangeBounds changeBounds = new ChangeBounds();
//        changeBounds.setDuration(100);
//        TransitionManager.beginDelayedTransition(root_layout, changeBounds);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        textViewCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Util.isFastClick()) return;

                if (type == 2 || type == 3) {
                    if (TextUtils.isEmpty(etTitle.getText().toString())) {
                        ToastUtil.s(mContext, "请输入标题");
                        return;
                    }
                } else if (type == 0) {
                    if (starBar.getStarMark() == 0) {
                        ToastUtil.s(mContext, "请为游戏打分！");
                        return;
                    }
                }
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    ToastUtil.s(mContext, "请输入内容");
                    return;
                }
                DialogUtil.showDialog(mContext, "正在发布....");
                if (processing.size() > 0) {
                    mHandler.sendEmptyMessageDelayed(check_upload_status, 1000);
                } else {
                    mContent = editText.getMgcRichText(mFileList);
                    fillDialogCallBack.textViewCreate(dialog, starBar, editText, etTitle);
                }
            }
        });
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputStr = editable.toString().trim();
                byte[] bytes = inputStr.getBytes();
                if (inputStr.length() > title_max_length) {
                    //Toast.makeText(context, "超过规定字符数", Toast.LENGTH_SHORT).show();
                    //Log.i("str", inputStr);
                    //取前15个字节
                    byte[] newBytes = new byte[title_max_length];
                    for (int i = 0; i < title_max_length; i++) {
                        newBytes[i] = bytes[i];
                    }
                    String newStr = new String(newBytes);
                    etTitle.setText(newStr);
                    //将光标定位到最后
                    Selection.setSelection(etTitle.getEditableText(), newStr.length());

                } else {
                    tv_number.setText(String.format("( %d / 30 )", inputStr.length()));
                }
            }
        });

        iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillDialogCallBack.selectPicture();
            }
        });
    }

    public void setContent(List<CommentBean> comments) {
        if (comments == null) return;

//        for (int i=0; i<comments.size(); i++){
//            if(comments.get(i).getDisplayType().equalsIgnoreCase(MgcFormatParse.TYPE_TEXT)){
//                editText.addText(comments.get(i).getEditorialCopy());
//            }else if(comments.get(i).getDisplayType().equalsIgnoreCase(MgcFormatParse.TYPE_INLINE_IMAGE)){
//                editText.addImage(comments.get(i).getArtwork().getUrl());
//            }
//        }
        String content = RichTextUtils.convertMgcToRichText(comments);
        editText.setRichText(content);

        FakeImageSpan[] imageSpans = editText.getFakeImageSpans();
        if (imageSpans == null || imageSpans.length == 0) {
            return;
        }
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (final FakeImageSpan imageSpan : imageSpans) {
            final String src = imageSpan.getValue();
            if (src.startsWith("http")) {
                // web images
                new AsyncTask<String, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(String... params) {
                        try {
                            InputStream is = new URL(src).openStream();
                            return BitmapFactory.decodeStream(is);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        if (bitmap == null) {
                            return;
                        }
                        editText.replaceDownloadedImage(imageSpan, bitmap, src);
                    }
                }.executeOnExecutor(executorService, src);
            } else {
                // local images
                editText.replaceLocalImage(imageSpan, src);
            }
        }
    }

    public void setTitle(String title) {

        if (title != null) {
            etTitle.setText(title);
        }
    }


    public String getContent() {
        return mContent;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight(Context context) {
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    public interface FillDialogCallBack {
        void textViewCreate(Dialog dialog, StarBar starBar, EditText contentEditer, EditText titleEditer);

        void selectPicture();

        void cancel();
    }

    public void bindImage(String path) {
        if (editText.getFakeImageSpans().length > 9) {
            ToastUtil.s(mContext, "最多插入九张图片");
            return;
        }
        Log.d("Bind Image", "start....");
        editText.addImage(path);
        Log.d("Bind Image", "end....");
        processing.add(path);

        Message msg = new Message();
        msg.what = upload_image;
        msg.obj =  path;
        mHandler.sendMessageDelayed(msg, 200);
        //uploadImage(path);
//        MyTask task = new MyTask();
//        task.execute(path);
    }

    public void bindImage(String localpath, String path) {
        editText.addImage(localpath, path);
    }

    // type- 0:游戏评分； 1:发稿|发贴；2:评论
    public void fillRatingDialog(Context context, final int type, Boolean enableChange, int score, final FillDialogCallBack fillDialogCallBack) {
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isDestroyed())
                return;
        }
        this.mContext = context;
        if (dialog != null) {
            dialog.dismiss();
        }
        is_ok = false;
        this.fillDialogCallBack = fillDialogCallBack;
        this.type = type;
        final Dialog dialog = new Dialog(context, R.style.CornersDialogStyle);
        View mView = LayoutInflater.from(context).inflate(R.layout.rich_edit_dialog, null);
        root_layout = mView.findViewById(R.id.root_layout);
        ll_title = mView.findViewById(R.id.ll_title);
        etTitle = mView.findViewById(R.id.et_title);
        tv_title = mView.findViewById(R.id.tv_title);
        tv_number = mView.findViewById(R.id.tv_number);
        ImageView imageBack = mView.findViewById(R.id.imageBack);
        TextView textViewCreate = mView.findViewById(R.id.textViewCreate);
        iv_picture = mView.findViewById(R.id.iv_picture);
        starBar = mView.findViewById(R.id.starBar);
        starBar.setIntegerMark(true);
        editText = mView.findViewById(R.id.editText);
        TextView textView_bar = mView.findViewById(R.id.textView_bar);

        if (type == 0) {
            starBar.setVisibility(View.VISIBLE);
            textView_bar.setVisibility(View.VISIBLE);
            ll_title.setVisibility(View.GONE);
            tv_title.setText("写评论");
            starBar.setMark(score);
            if (!enableChange) {
                starBar.setIsIndicator(true);
            }
        } else if (type == 1) {
            starBar.setVisibility(View.GONE);
            textView_bar.setVisibility(View.GONE);
            ll_title.setVisibility(View.GONE);
            tv_title.setText("写评论");
        } else if (type == 2) {
            starBar.setVisibility(View.GONE);
            textView_bar.setVisibility(View.GONE);
            ll_title.setVisibility(View.VISIBLE);
            tv_title.setText("文章投稿");
        } else if (type == 3) {
            starBar.setVisibility(View.GONE);
            textView_bar.setVisibility(View.GONE);
            ll_title.setVisibility(View.VISIBLE);
            tv_title.setText("发布帖子");
        }


        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(mView);
        dialog.show();

        Window dialogWin = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWin.getAttributes();
/**
 Dialog的width和height默认值为WRAP_CONTENT，正是因为如此，当屏幕中有足够的空间时，Dialog是不会被压缩的
 但是设置width和height为MATCH_PARENT的代价是无法设置gravity的值，这就无法调整Dialog中内容的位置，Dialog的内容会显示在屏幕左上角位置不过可以通过Padding来调节Dialog内容的位置。
 **/
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWin.setAttributes(lp);


        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillDialogCallBack.cancel();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        textViewCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Util.isFastClick()) return;
                if (type == 2 || type == 3) {
                    if (TextUtils.isEmpty(etTitle.getText().toString())) {
                        ToastUtil.s(mContext, "请输入标题");
                        return;
                    }
                } else if (type == 0) {
                    if (starBar.getStarMark() == 0) {
                        ToastUtil.s(mContext, "请为游戏打分！");
                        return;
                    }
                }

                if (TextUtils.isEmpty(editText.getText().toString())) {
                    ToastUtil.s(mContext, "请输入内容");
                    return;
                }
                DialogUtil.showDialog(mContext, "正在发布....");
                is_ok = true;
                if (processing.size() > 0) {
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                } else {
                    mContent = editText.getMgcRichText(mFileList);
                    fillDialogCallBack.textViewCreate(dialog, starBar, editText, etTitle);
                }

            }
        });
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inputStr = editable.toString().trim();
                byte[] bytes = inputStr.getBytes();
                if (bytes.length > title_max_length) {
                    //Toast.makeText(context, "超过规定字符数", Toast.LENGTH_SHORT).show();
                    //Log.i("str", inputStr);
                    //取前15个字节
                    byte[] newBytes = new byte[title_max_length];
                    for (int i = 0; i < title_max_length; i++) {
                        newBytes[i] = bytes[i];
                    }
                    String newStr = new String(newBytes);
                    etTitle.setText(newStr);
                    //将光标定位到最后
                    Selection.setSelection(etTitle.getEditableText(), newStr.length());

                } else {
                    tv_number.setText(String.format("( %d / 30", bytes.length));
                }
            }
        });

//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                fillDialogCallBack.editText(editText);
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                fillDialogCallBack.editText(editText);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                fillDialogCallBack.editText(editText);
//            }
//        });

        iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillDialogCallBack.selectPicture();
            }
        });
    }

    // type- 0:游戏评分；1:评论；2:发稿；3：发贴
    public void uploadImage(final String path) {
        HttpParams params = HttpParamsBuild.getCustomHttpParams(mContext);
        if (type == 1 || type == 0) {
            params.put("type", 4);
        } else if (type == 2) {
            params.put("type", 3);
        } else if (type == 3) {
            params.put("type", 2);
        }
//        String tmpPath = AppConfig.app_dir+"/" + System.currentTimeMillis()+".jpg";
//        Bitmap bitmap = ImageUtil.compressImage(path);
//        try {
//            ImageUtil.storeImage(bitmap, tmpPath);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        params.put("portrait[]", new File(path));

        new RxVolleyUtil().post(FindApi.postImage(), params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Log.d("MgcUpload", "receive result....");
                Gson gson = new Gson();
                PostImage postImage = gson.fromJson(t, PostImage.class);
                if (Integer.valueOf(postImage.getCode()) == 200) {
                    String[] data = postImage.getData();
                    if (data != null && data.length > 0) {
                        mFileList.put(path, data[0]);
                        processing.remove(path);
                    }
                }
//                if(bitmap !=null){
//                    bitmap.recycle();
//
//                }
                Log.d("MgcUpload", "finish.");
            }

            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
            }
        });
        Log.d("MgcUpload", "begining....");
    }

    public void clear() {
        processing.clear();
        mFileList.clear();
        mContent = "";
        is_ok = false;
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}

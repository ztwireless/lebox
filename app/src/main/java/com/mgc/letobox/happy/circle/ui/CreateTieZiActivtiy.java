package com.mgc.letobox.happy.circle.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.DialogUtil;
import com.leto.game.base.util.StatusBarUtil;
import com.leto.game.base.util.ToastUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.circle.bean.CreateTieZiReponse;
import com.mgc.letobox.happy.circle.view.SpaceGridItemDecoration;
import com.mgc.letobox.happy.find.FindConst;
import com.mgc.letobox.happy.find.adapter.SelectedImageAdapter;
import com.mgc.letobox.happy.find.bean.PostImage;
import com.mgc.letobox.happy.find.intf.OnItemClickListener;
import com.mgc.letobox.happy.find.model.Image;
import com.mgc.letobox.happy.find.ui.SelectImageActivity;
import com.mgc.letobox.happy.find.util.FindApi;
import com.mgc.letobox.happy.find.util.FindApiUtil;
import com.mgc.letobox.happy.find.util.MgctUtil;
import com.mgc.letobox.happy.find.util.RxVolleyUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class CreateTieZiActivtiy extends AppCompatActivity implements View.OnClickListener {

    TextView tv_title;
    RelativeLayout rl_left;
    TextView tv_right;
    EditText et_title;
    EditText et_content;
    ImageView iv_add;
	RecyclerView recyclerView;

    private SelectedImageAdapter mImageAdapter;
    private ImageView imgChahao;
    private List<Image> selectImages;//选中的图片列表
    private List<Image> uploadImages = new ArrayList<>();//待上传的图片列表

    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果


    private static final int REQUEST_CODE_CAMERA_PERMISSION = 2001;
    private static final int REQUEST_CODE_GALLERY_PERMISSION = 2002;


    private int mGroupId = 0;

    private File tempFile;

    private final int UPLOAD_IMAGE = 1;
    private final int UPLOAD_CONTENT = 3;

    private String title = "";
    private String content = "";
    private String attach_id = "";
    private List<String> covers = new ArrayList<>();
    private boolean isOk = false; // 是否请求成功
    public static boolean isReWard = true; // 是否有奖励
    public static boolean isReWardOk = false; // 跳转帖子奖励
    private CreateTieZiReponse tieZiData;

    public static void start(Context context,int type) {
        Intent intent = new Intent(context.getApplicationContext(), CreateTieZiActivtiy.class);
        intent.putExtra(FindConst.EXTRA_ARTICLE_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_publish);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.white));
        }

        // find views
        tv_title = findViewById(R.id.tv_title);
        rl_left = findViewById(R.id.rl_left);
        tv_right = findViewById(R.id.tv_right);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        iv_add = findViewById(R.id.iv_add);
        recyclerView = findViewById(R.id.rv_image);

        isReWard = true;
        isReWardOk = false;
        if (null != getIntent()) {
            mGroupId = getIntent().getIntExtra(FindConst.EXTRA_ARTICLE_TYPE,0);
            tv_title.setText("发布帖子");
        }
        et_content.setHint("帖子内容");
        rl_left.setOnClickListener(this);

        iv_add.setOnClickListener(this);

        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("发布");

        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
//                if(length<=report_content_max_length){
//                    repair_report_content_status.setText("" + length +"/" + report_content_max_length);
//                }else{
//                    int start = repair_report_content.getSelectionStart();
//                    int end = repair_report_content.getSelectionEnd();
//                    s.delete(start - 1, end);
//                    int tempSelection = start;
//                    repair_report_content.setText(s);
//                    repair_report_content.setSelection(tempSelection);
//                }
            }
        });

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = et_title.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    ToastUtil.s(CreateTieZiActivtiy.this, "请填写标题");
                    return;
                }
                content = et_content.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.s(CreateTieZiActivtiy.this, "请填写内容");
                    return;
                }
                if (content.length() < 10) {
                    ToastUtil.s(CreateTieZiActivtiy.this, "内容不少于10字");
                    return;
                }
                DialogUtil.showDialog(CreateTieZiActivtiy.this,true,"正在上传中......");
                if (selectImages != null && selectImages.size() != 0) {
                    postImage();
                } else {
                    uploadContent();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left:
                finish();
                break;

            case R.id.iv_add:
                showDialog();
                break;

            default:
                break;
        }
    }

    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (isOk) { // 请求成功返回刷新
            EventBus.getDefault().post(tieZiData);
        }
        isReWard = false;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case FindConst.SELECT_PHOTO_RESULT:
                if (selectImages == null) {
                    selectImages = new ArrayList<>();
                }
                selectImages.clear();

                List<Image> images = (List<Image>) data.getSerializableExtra(FindConst.SELECT_PHOTO_LIST);
                selectImages.addAll(images);
                if (selectImages.size() > 0) {
                    iv_add.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    initRecyclerView();
                } else {
                    iv_add.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                //repair_report_img_status.setText("" +selectImages.size()+"/"+report_img_max_length);
                break;
//            case IntentContant.SELECT_VIDEO_RESULT:
//                if (selectImages == null) {
//                    selectImages = new ArrayList<>();
//                }
//                selectVideo = (Image) data.getSerializableExtra(IntentConstant.SELECT_VIDEO);
//                selectImages.add(selectVideo);
//                if (selectImages.size() > 0) {
//                    repair_report_img1.setVisibility(View.GONE);
//                    recyclerView.setVisibility(View.VISIBLE);
//                    initRecyclerView();
//                } else {
//                    repair_report_img1.setVisibility(View.VISIBLE);
//                    recyclerView.setVisibility(View.GONE);
//                }
//                break;

            default:
                break;
        }
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (MgctUtil.haveSDCard()) {
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
//                this.iv_image.setImageBitmap(bitmap);
            }
            try {
                // 将临时文件删除
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initRecyclerView() {
        if (mImageAdapter == null) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.addItemDecoration(new SpaceGridItemDecoration((int) DensityUtil.dip2px(this, 1)));
            mImageAdapter = new SelectedImageAdapter(this, selectImages, (View) iv_add, 9);
            recyclerView.setAdapter(mImageAdapter);
            recyclerView.setItemAnimator(null);

            mImageAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (position == selectImages.size()) {
                        Intent intent = new Intent();
                        intent.putExtra(FindConst.EXTRA_IMAGE_NUMBER, 9);
                        intent.setClass(CreateTieZiActivtiy.this, SelectImageActivity.class);
                        if (selectImages != null) {
                            intent.putExtra(FindConst.SELECT_PHOTO_LIST, (Serializable) selectImages);
                        }
                        startActivityForResult(intent, 0);
                    }
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
        } else {
            mImageAdapter.notifyDataSetChanged();
        }
    }

    private void showDialog() {
        if (EasyPermissions.hasPermissions(CreateTieZiActivtiy.this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            Intent intent = new Intent();
            intent.setClass(CreateTieZiActivtiy.this, SelectImageActivity.class);
            if (selectImages != null && selectImages.size() > 0) {
                intent.putExtra(FindConst.SELECT_PHOTO_LIST, (Serializable) selectImages);
            }
            intent.putExtra(FindConst.EXTRA_IMAGE_NUMBER, 9);
            startActivityForResult(intent, 0);
        } else {
            EasyPermissions.requestPermissions(CreateTieZiActivtiy.this, "需要存储和照相机权限",
                    REQUEST_CODE_CAMERA_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        }
    }

    public void uploadContent() {
        if (CreateTieZiActivtiy.this == null) {
            return;
        }

        FindApiUtil.putPost(this, title, content, mGroupId, attach_id, new HttpCallbackDecode<CreateTieZiReponse>(this, null) {
            @Override
            public void onDataSuccess(CreateTieZiReponse data) {
                if (data != null) {
                    isOk = true;
                    if(tv_right != null) {
                        tv_right.setClickable(true);
                    }
                    if (isReWard) {
                        if (CreateTieZiActivtiy.this != null) {
                            isReWardOk = true;
                            ArticleDetailsActivity.startActivity(CreateTieZiActivtiy.this, data);
                            tieZiData = data;
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                if(tv_right != null) {
                    tv_right.setClickable(true);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                DialogUtil.dismissDialog();
            }
        });
    }

    public void postImage() {
        isOk = false;
        tv_right.setClickable(false);
        HttpParams params = HttpParamsBuild.getCustomHttpParams(CreateTieZiActivtiy.this);
        params.put("type", 2);
        if (selectImages != null && selectImages.size() != 0) {
            for (int i = 0; i < selectImages.size();i++) {
                params.put("portrait[]", new File(selectImages.get(i).getPath()));
            }
        }

        new RxVolleyUtil().post(FindApi.postImage(),params,new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Gson gson = new Gson();
                PostImage postImage = gson.fromJson(t, PostImage.class);
                if (Integer.valueOf(postImage.getCode()) == 200) {
                    String[] data = postImage.getData();
                    if (data != null) {
                        for (String s : data) {
                            attach_id = attach_id + s + ",";
                        }
                        uploadContent();
                    } else {
                        if(tv_right != null) {
                            tv_right.setClickable(true);
                        }
                        DialogUtil.dismissDialog();
                    }
                }
            }
            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
                if(tv_right != null) {
                    tv_right.setClickable(true);
                }
                DialogUtil.dismissDialog();
            }
            @Override
            public void onFailure(int errorNo, String strMsg, String completionInfo) {
                super.onFailure(errorNo, strMsg, completionInfo);
                if(tv_right != null) {
                    tv_right.setClickable(true);
                }
                DialogUtil.dismissDialog();
            }
        });
    }
}

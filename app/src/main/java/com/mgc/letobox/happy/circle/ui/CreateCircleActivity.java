package com.mgc.letobox.happy.circle.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.leto.game.base.db.LoginControl;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.http.HttpParamsBuild;
import com.leto.game.base.http.SdkConstant;
import com.leto.game.base.util.DeviceUtil;
import com.leto.game.base.util.DialogUtil;
import com.leto.game.base.util.GlideUtil;
import com.leto.game.base.util.ToastUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.circle.CircleConst;
import com.mgc.letobox.happy.find.FindConst;
import com.mgc.letobox.happy.find.bean.PostImage;
import com.mgc.letobox.happy.find.model.Image;
import com.mgc.letobox.happy.find.ui.SelectImageActivity;
import com.mgc.letobox.happy.find.util.FindApi;
import com.mgc.letobox.happy.find.util.FindApiUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by DELL on 2018/7/10.
 */

public class CreateCircleActivity extends AppCompatActivity {
    TextView textViewCreate;
    ImageView imageBack;
    EditText editText_circle_name;
    EditText editText_miaoshu;
    TextView textView_header;
    ImageView imageView_header;
    TextView textView_Background;
    ImageView imageView_Background;
    RelativeLayout layout_left;
    RelativeLayout layout_right;

    private String leftUrl = "";
    private String rightUrl = "";
    private String urlLeft = "";
    private String urlRight = "";
    private boolean isCreate = false; // 判断是否创建成功

    private static final int REQUEST_CODE_CAMERA_PERMISSION = 2001;
    private static final int REQUEST_CODE_GALLERY_PERMISSION = 2002;

    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_ALBUM = 2;
    public static final int REQUEST_CROP = 3;

    public static final String IMAGE_UNSPECIFIED = "image/*";

    private String tempfilePath;

    public static void startActivity(Context mContext) {
        Intent mIntent = new Intent(mContext, CreateCircleActivity.class);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_create_activity);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));

        // find views
        textViewCreate = findViewById(R.id.textViewCreate);
        imageBack = findViewById(R.id.imageBack);
        editText_circle_name = findViewById(R.id.editText_circle_name);
        editText_miaoshu = findViewById(R.id.editText_miaoshu);
        textView_header = findViewById(R.id.textView_header);
        imageView_header = findViewById(R.id.imageView_header);
        textView_Background = findViewById(R.id.textView_Background);
        imageView_Background = findViewById(R.id.imageView_Background);
        layout_left = findViewById(R.id.layout_left);
        layout_right = findViewById(R.id.layout_right);

        initOnClick();
    }

    private void initOnClick() {
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        layout_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooseImage();
//                Intent intent = new Intent();
//                intent.setClass(CreateCircleActivity.this, SelectImageActivity.class);
//                intent.putExtra(IntentContant.EXTRA_IMAGE_NUMBER, 1);
//                startActivityForResult(intent, 0);
            }
        });

        layout_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(CreateCircleActivity.this, SelectImageActivity.class);
                intent.putExtra(FindConst.EXTRA_IMAGE_NUMBER, 1);
                startActivityForResult(intent, 0);
            }
        });

        textViewCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LoginControl.isLogin()) {
                    return;
                } else if (editText_circle_name.getText() == null || editText_circle_name.getText().toString().isEmpty()) {
                    ToastUtil.s(CreateCircleActivity.this,"请添加圈子名称!");
                    return;
                } else if (editText_miaoshu.getText() == null || editText_miaoshu.getText().toString().isEmpty()) {
                    ToastUtil.s(CreateCircleActivity.this,"请添加圈子描述!");
                    return;
                } else if (leftUrl == null || leftUrl.isEmpty()) {
                    ToastUtil.s(CreateCircleActivity.this,"请添加圈子头像!");
                    return;
                } else if (rightUrl == null || rightUrl.isEmpty()) {
                    ToastUtil.s(CreateCircleActivity.this,"请添加圈子背景图片!");
                    return;
                }
                initLife();
            }
        });
    }

    private void showChooseImage() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("选择图片");
        ab.setItems(new String[]{"拍照", "从相册选择", "取消"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        if (EasyPermissions.hasPermissions(CreateCircleActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
                            selectCamera();
                        } else {
                            EasyPermissions.requestPermissions(CreateCircleActivity.this, "需要存储和照相机权限",
                                    REQUEST_CODE_CAMERA_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                        }

                        break;
                    case 1:
                        if (EasyPermissions.hasPermissions(CreateCircleActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            selectAlbum();
                        } else {
                            EasyPermissions.requestPermissions(CreateCircleActivity.this, "需要存储权限",
                                    REQUEST_CODE_GALLERY_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                        break;
                }
            }
        });
        ab.show();
    }

    private void selectAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(albumIntent, REQUEST_ALBUM);
    }

    private void selectCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri mImageCaptureUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是7.0android系统
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, tempfilePath);
            mImageCaptureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            mImageCaptureUri = Uri.fromFile(new File(tempfilePath));
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isCreate) { // 请求成功返回刷新
            EventBus.getDefault().post(CircleConst.CREATE_CIRCLE);
        }
    }

    private void initCreate() {
        if (urlLeft == null || urlLeft.isEmpty()) {
            ToastUtil.s(CreateCircleActivity.this,"图片未上传成功，请重试!");
            return;
        } else if (rightUrl == null || rightUrl.isEmpty()) {
            ToastUtil.s(CreateCircleActivity.this,"图片未上传成功，请重试!");
            return;
        }
        FindApiUtil.createCircle(this, editText_circle_name.getText().toString(), editText_miaoshu.getText().toString(), urlLeft, urlRight, new HttpCallbackDecode<String>(this, null) {
            @Override
            public void onDataSuccess(String data) {
                if (textViewCreate != null) {
                    textViewCreate.setClickable(true);
                }
                if (CreateCircleActivity.this != null) {
                    isCreate = true;
                    ToastUtil.s(CreateCircleActivity.this, "创建成功。");
                    finish();
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                if (textViewCreate != null) {
                    textViewCreate.setClickable(true);
                }
                if (CreateCircleActivity.this != null) {
                    ToastUtil.s(CreateCircleActivity.this, msg);
                }
            }
        });
    }

    private void initLife() {
        textViewCreate.setClickable(false);
        DialogUtil.showDialog(CreateCircleActivity.this, true, "上传中......");
        HttpParams params = HttpParamsBuild.getCustomHttpParams(CreateCircleActivity.this);
        params.put("type", 2);
        params.put("portrait[]", new File(leftUrl));

        RxVolley.post(FindApi.postImage(), params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Gson gson = new Gson();
                PostImage postImage = gson.fromJson(t, PostImage.class);
                if (Integer.valueOf(postImage.getCode()) == 200) {
                    String[] data = postImage.getData();
                    if (data != null) {
                        urlLeft = data[0];
                        initRight();
                    }
                } else {
                    if (textViewCreate != null) {
                        textViewCreate.setClickable(true);
                    }
                    if (CreateCircleActivity.this != null) {
                        ToastUtil.s(CreateCircleActivity.this, postImage.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
                DialogUtil.dismissDialog();
                if (textViewCreate != null) {
                    textViewCreate.setClickable(true);
                }
                if (CreateCircleActivity.this != null) {
                    ToastUtil.s(CreateCircleActivity.this, "网络异常");
                }
            }
        });
    }

    private void initRight() {
        HttpParams params = new HttpParams();
        params.put("app_id", SdkConstant.MGC_APPID);
        params.put("client_id", SdkConstant.MGC_CLIENTID);
        params.put("from", SdkConstant.FROM);
        params.put("agentgame", SdkConstant.MGC_AGENT);
        params.put("user_token", SdkConstant.userToken);
        long timestamp = System.currentTimeMillis() + SdkConstant.SERVER_TIME_INTERVAL;
        params.put("timestamp", String.valueOf(timestamp));
        params.put("device.device_id", DeviceUtil.getDeviceId(CreateCircleActivity.this));
        params.put("device.userua", DeviceUtil.getUserUa(CreateCircleActivity.this));
        params.put("packagename", SdkConstant.packageName);
        params.put("type", 2);
        params.put("portrait[]", new File(rightUrl));

        RxVolley.post(FindApi.postImage(), params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                Gson gson = new Gson();
                PostImage postImage = gson.fromJson(t, PostImage.class);
                if (Integer.valueOf(postImage.getCode()) == 200) {
                    String[] data = postImage.getData();
                    if (data != null) {
                        urlRight = data[0];
                        initCreate();
                    }
                } else {
                    if (textViewCreate != null) {
                        textViewCreate.setClickable(true);
                    }
                    if (CreateCircleActivity.this != null) {
                        ToastUtil.s(CreateCircleActivity.this, postImage.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
                DialogUtil.dismissDialog();
                if (textViewCreate != null) {
                    textViewCreate.setClickable(true);
                }
                if (CreateCircleActivity.this != null) {
                    ToastUtil.s(CreateCircleActivity.this,"网络异常");
                }
            }
        });
    }

    private void cropImage(Uri uri) {
        tempfilePath = Environment.getExternalStorageDirectory() + File.separator + System.currentTimeMillis() + ".jpg";
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tempfilePath)));
        startActivityForResult(intent, REQUEST_CROP);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (FindConst.SELECT_PHOTO_RESULT == resultCode) {
            List<Image> images = (List<Image>) data.getSerializableExtra(FindConst.SELECT_PHOTO_LIST);
            if (images != null && images.size() != 0) {
                String path = images.get(0).getPath();
                rightUrl = path;
                textView_Background.setVisibility(View.GONE);
                GlideUtil.load(CreateCircleActivity.this, rightUrl, imageView_Background);
                return;
//            if (isLeft) {
//                if (images != null && images.size() != 0) {
//                    String path = images.get(0).getPath();
//                    textView_header.setVisibility(View.GONE);
//                    leftUrl = path;
//                    Glide.with(mContext).load(leftUrl).into(imageView_header);
//                }
//            } else {
            }
        }

        if (RESULT_OK != resultCode) {
            return;
        }

        switch (requestCode) {
            case REQUEST_CAMERA:
                cropImage(Uri.fromFile(new File(tempfilePath)));
                break;
            case REQUEST_ALBUM:
                Uri uri = data.getData();
                if (uri != null) {
                    cropImage(uri);
                }
                break;
            case REQUEST_CROP:
                leftUrl = tempfilePath;
                textView_header.setVisibility(View.GONE);
                GlideUtil.load(this, leftUrl, imageView_header);
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (resultCode) {
//            case IntentContant.SELECT_PHOTO_RESULT:
//                List<Image> images = (List<Image>) data.getSerializableExtra(IntentContant.SELECT_PHOTO_LIST);
//                if (isLeft) {
//                    if (images != null && images.size() != 0) {
//                        String path = images.get(0).getPath();
//                        textView_header.setVisibility(View.GONE);
//                        leftUrl = path;
//                        Glide.with(mContext).load(leftUrl).into(imageView_header);
//                    }
//                } else {
//                    if (images != null && images.size() != 0) {
//                        String path = images.get(0).getPath();
//                        textView_Background.setVisibility(View.GONE);
//                        rightUrl = path;
//                        Glide.with(mContext).load(rightUrl).into(imageView_Background);
//                    }
//                }
//            default:
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}

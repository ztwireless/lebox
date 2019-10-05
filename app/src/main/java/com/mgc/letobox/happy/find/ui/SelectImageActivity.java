package com.mgc.letobox.happy.find.ui;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.ToastUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.circle.view.SpaceGridItemDecoration;
import com.mgc.letobox.happy.find.FindConst;
import com.mgc.letobox.happy.find.adapter.SelectImageAdapter;
import com.mgc.letobox.happy.find.intf.OnItemClickListener;
import com.mgc.letobox.happy.find.model.Image;
import com.mgc.letobox.happy.find.model.ImageFolder;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 图片选择
 */
public class SelectImageActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tv_title;
    TextView tv_right;


    private ImageView imgBack;
    //private Button btnDone;
    private RecyclerView mContentView;
    private SelectImageAdapter mImageAdapter;
    private ArrayList<Image> images,videos;
    private LoaderCallBackListener mLoadListener = new LoaderCallBackListener();
    private VideoLoaderListener videoLoaderListener = new VideoLoaderListener();
    private List<Image> selectImages;//选中的视频或图片
    private String mCamImageName;//新拍照片名
    private String savePath = "";
    private int selectCount;//已选数量


    private int maxSize = 3;

    private static final int REQUEST_CODE_CAMERA_PERMISSION = 2001;
    private static final int REQUEST_CODE_GALLERY_PERMISSION = 2002;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);

        // find views
        tv_title = findViewById(R.id.tv_title);
        tv_right = findViewById(R.id.tv_right);

        Intent intent = getIntent();
        selectImages = (List<Image>) intent.getSerializableExtra(FindConst.SELECT_PHOTO_LIST);
        if(selectImages!=null&&selectImages.size()>0){
            selectCount = selectImages.size();
        }
        maxSize = intent.getIntExtra(FindConst.EXTRA_IMAGE_NUMBER,3);


        findViewById(R.id.rl_left).setOnClickListener(this);

        tv_title.setText("选择图片");

        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("完成("+selectCount+")");

        mContentView = findViewById(R.id.rv_image);

        //imgBack.setOnClickListener(this);
        tv_right.setOnClickListener(this);

        initLoader();
    }

    protected void initWidget() {
        if(selectImages!=null&&selectImages.size()>0){
            for(int i=0;i<images.size();i++){
                for(int j=0;j<selectImages.size();j++){
                    if(selectImages.get(j).getId()==images.get(i).getId()){
                        images.get(i).setSelect(true);
                    }
                }
            }
        }

        if(videos!=null&&videos.size()>0){
            images.addAll(videos);
        }
        if(mImageAdapter==null){
            mContentView.setLayoutManager(new GridLayoutManager(this, 4));
            mContentView.addItemDecoration(new SpaceGridItemDecoration((int) DensityUtil.dip2px(this,1)));
            mImageAdapter = new SelectImageAdapter(this,images);
            mContentView.setAdapter(mImageAdapter);
            mContentView.setItemAnimator(null);
            mImageAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(position==0){

                        if (EasyPermissions.hasPermissions(SelectImageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
                            openCamera();
                        } else {
                            EasyPermissions.requestPermissions(SelectImageActivity.this, "需要存储和照相机权限",
                                    REQUEST_CODE_CAMERA_PERMISSION,  Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                        }

                    }else{
                        Image image = images.get(position-1);
                        if(image.getType()==0){//图片
                            if(image.isSelect()){
                                image.setSelect(false);
                                selectCount--;
                            }else{
                                if(selectCount < maxSize){
                                    image.setSelect(true);
                                    selectCount++;
                                }else{
                                    ToastUtil.s(SelectImageActivity.this,"最多选"+maxSize+"个");
                                }
                            }
                            tv_right.setText("完成("+selectCount+")");
                            mImageAdapter.notifyItemChanged(position);
                        }else if(image.getType()==1){//视频
                            if(selectCount>0){
                                ToastUtil.s(SelectImageActivity.this,"不能同时选择图片或视频");
                                return;
                            }
                            Intent intent = new Intent();
                            intent.putExtra(FindConst.SELECT_VIDEO,image);
                            setResult(FindConst.SELECT_VIDEO_RESULT,intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
        }else{
            mImageAdapter.notifyDataSetChanged();
        }

    }

    /**
     *
     *
     *
     *
     * 打开相机
     */
    private void openCamera() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (TextUtils.isEmpty(savePath)) {
            Toast.makeText(this, "无法保存照片，请检查SD卡是否挂载", Toast.LENGTH_LONG).show();
            return;
        }

        mCamImageName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";// 照片命名
        File out = new File(savePath, mCamImageName);
        Uri uri = Uri.fromFile(out);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent,
                0x03);
    }

    private void initLoader() {
        getSupportLoaderManager().initLoader(0,null,mLoadListener);
    }

    private void initVideoLoader() {
        getSupportLoaderManager().initLoader(1,null,videoLoaderListener);
    }

    /**
     * 拍照完成通知系统添加照片
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 0x03 && mCamImageName != null) {
            Uri localUri = Uri.fromFile(new File(savePath + mCamImageName));
            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
            sendBroadcast(localIntent);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.rl_left:
                //点击返回上一界面
                onBackPressed();
                break;
            case R.id.tv_right:
                if(selectImages == null){
                    selectImages = new ArrayList<>();
                }
                selectImages.clear();
                if(images!=null){
                    for(int i=0;i<images.size();i++){
                        if(images.get(i).isSelect()){
                            selectImages.add(images.get(i));
                        }
                    }
                }
                intent.putExtra(FindConst.SELECT_PHOTO_LIST,(Serializable) selectImages);
                setResult(FindConst.SELECT_PHOTO_RESULT,intent);
                finish();
                break;
            default:
                break;
        }
    }

    private class LoaderCallBackListener implements LoaderManager.LoaderCallbacks<Cursor> {
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.MINI_THUMB_MAGIC,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == 0) {
                //数据库光标加载器
                return new CursorLoader(SelectImageActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
            if(data!=null){
                if(images==null){
                    images = new ArrayList<>();
                }
                images.clear();
                final List<ImageFolder> imageFolders = new ArrayList<>();

                final ImageFolder defaultFolder = new ImageFolder();
                defaultFolder.setName("全部照片");
                defaultFolder.setPath("");
                imageFolders.add(defaultFolder);

                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int id = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
//                        String thumbPath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
//                        String bucket = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                        if(!new File(path).canRead()){
                            continue;
                        }

                        Image image = new Image();
                        image.setPath(path);
                        image.setName(name);
                        image.setDate(dateTime);
                        image.setId(id);
//                        image.setThumbPath(thumbPath);
//                        image.setFolderName(bucket);
                        images.add(image);
                        //如果是新拍的照片
                        if (mCamImageName != null && mCamImageName.equals(image.getName())&&selectCount<maxSize) {
                            image.setSelect(true);
                            selectCount++;
                            tv_right.setText("完成("+selectCount+")");
                        }
                    } while (data.moveToNext());
                }
                initWidget();
            }

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    private class VideoLoaderListener implements LoaderManager.LoaderCallbacks<Cursor> {
        private final String[] VIDEO_PROJECTION = {
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DURATION};
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == 1) {
                //数据库光标加载器
                return new CursorLoader(SelectImageActivity.this,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION,
                        MediaStore.Video.Media.DURATION+"<?", new String[]{"100000"}, VIDEO_PROJECTION[2] + " DESC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
            if(data!=null){
                if(videos==null){
                    videos = new ArrayList<>();
                }
                videos.clear();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(VIDEO_PROJECTION[2]));
                        int id = data.getInt(data.getColumnIndexOrThrow(VIDEO_PROJECTION[3]));
                        int duration = data.getInt(data.getColumnIndexOrThrow(VIDEO_PROJECTION[4]));

                        Log.e("zh","duration======"+duration);
                        Image image = new Image();
                        image.setPath(path);
                        image.setName(name);
                        image.setDate(dateTime);
                        image.setId(id);
                        image.setDuration(duration);
                        image.setType(1);
                        videos.add(image);

                    } while (data.moveToNext());
                }
            }
            initLoader();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}

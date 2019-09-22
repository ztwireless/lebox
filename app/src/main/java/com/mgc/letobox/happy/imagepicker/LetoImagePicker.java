package com.mgc.letobox.happy.imagepicker;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Environment;

import java.io.File;
import java.lang.ref.WeakReference;

public class LetoImagePicker {
	boolean _fromAlbum;
	WeakReference<ImagePickerCallback> _callback;
	String _path;
	int _expectedWidth;
	int _expectedHeight;
	boolean _front;
	boolean _keepRatio;
	File _destFile;

	Context _ctx;

	private static LetoImagePicker _inst;

	public static LetoImagePicker getInstance(Context ctx) {
		if(_inst == null) {
			_inst = new LetoImagePicker(ctx);
		}
		return _inst;
	}

	private LetoImagePicker(Context ctx) {
		this._ctx = ctx;
	}

	public boolean hasCamera() {
		return Camera.getNumberOfCameras() > 0;
	}

	public boolean hasFrontCamera() {
		int c = Camera.getNumberOfCameras();
		for(int i = 0; i < c; i++) {
			CameraInfo cameraInfo = new CameraInfo();
			Camera.getCameraInfo(i, cameraInfo);
			if(cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT)
				return true;
		}
		return false;
	}

	public void pickFromCamera(String path, int w, int h, boolean front, boolean keepRatio, ImagePickerCallback cb) {
		_fromAlbum = false;
		_callback = new WeakReference<>(cb);
		_path = path;
		_expectedWidth = w;
		_expectedHeight = h;
		_front = front;
		_keepRatio = keepRatio;
		
		// the full path of image file
		_destFile = new File(_path);
		if(!_path.startsWith("/")) {
			File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
			_destFile = new File(storageDir, _path);
		}
		if(!_destFile.getParentFile().exists()) {
			_destFile.getParentFile().mkdirs();
		}
		
		// start a delegate activity, it will call other activity to finish camera and crop process
		Intent intent = new Intent(_ctx, ImagePickerActivity.class);
		_ctx.startActivity(intent);
	}

	public void pickFromAlbum(String path, int w, int h, boolean keepRatio, ImagePickerCallback cb) {
		_fromAlbum = true;
		_callback = new WeakReference<>(cb);
		_path = path;
		_expectedWidth = w;
		_expectedHeight = h;
		_keepRatio = keepRatio;
		
		// the full path of image file
		_destFile = new File(_path);
		if(!_path.startsWith("/")) {
			File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
			_destFile = new File(storageDir, _path);
		}
		if(!_destFile.getParentFile().exists()) {
			_destFile.getParentFile().mkdirs();
		}
		
		// start a delegate activity, it will call other activity to finish camera and crop process
		Intent intent = new Intent(_ctx, ImagePickerActivity.class);
		_ctx.startActivity(intent);
	}
	
	void onImagePicked() {
		ImagePickerCallback cb = _callback.get();
		if(cb != null) {
			cb.onImagePicked(_destFile.getAbsolutePath());
		}
	}

	void onImagePickingCancelled() {
		ImagePickerCallback cb = _callback.get();
		if(cb != null) {
			cb.onImagePickingCancelled();
		}
	}
}

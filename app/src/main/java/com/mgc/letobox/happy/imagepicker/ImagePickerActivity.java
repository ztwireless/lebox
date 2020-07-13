package com.mgc.letobox.happy.imagepicker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera.CameraInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.mgc.leto.game.base.provider.LetoFileProvider;
import com.mgc.letobox.happy.imagepicker.cropimage.CropImageIntentBuilder;

public class ImagePickerActivity extends Activity {
	private static final int REQ_CAPTURE_IMAGE = 100; 
	private static final int REQ_CROP_IMAGE = 101; 
	private static final int REQ_SELECT_PHOTO = 102;

	LetoImagePicker _imagePicker;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_imagePicker = LetoImagePicker.getInstance(this);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		if(_imagePicker._fromAlbum) {
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			startActivityForResult(intent, REQ_SELECT_PHOTO);
		} else {
			if(_imagePicker._destFile == null){
				finish();
				return;
			}
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			Uri uri = LetoFileProvider.getUriForFile(this, getPackageName() + ".leto.fileprovider", _imagePicker._destFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			if(_imagePicker._front) {
				intent.putExtra("android.intent.extras.CAMERA_FACING", CameraInfo.CAMERA_FACING_FRONT);
			}
			startActivityForResult(intent, REQ_CAPTURE_IMAGE); 
		}
	}
	
	private boolean isPNG() {
		String path = _imagePicker._destFile.getAbsolutePath();
		int lastDot = path.lastIndexOf('.');
		if(lastDot == -1) {
			return false;
		} else {
			return path.substring(lastDot + 1).equalsIgnoreCase("png");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
			case REQ_SELECT_PHOTO:
				if(resultCode == RESULT_OK) {
					Uri selectedImage = data.getData();
					Uri uri = LetoFileProvider.getUriForFile(this, getPackageName() + ".leto.fileprovider", _imagePicker._destFile);
		            CropImageIntentBuilder cropImage = new CropImageIntentBuilder(_imagePicker._expectedWidth,
						_imagePicker._expectedHeight, uri);
		            cropImage.setSourceImage(selectedImage);
		            if(isPNG()) {
						cropImage.setOutputFormat(CompressFormat.PNG.toString());
					}
		            startActivityForResult(cropImage.getIntent(this), REQ_CROP_IMAGE);
				} else {
					// callback
					_imagePicker.onImagePickingCancelled();
					
					// finish self
					finish();
				}
				break;
			case REQ_CAPTURE_IMAGE:
				if(resultCode == RESULT_OK) {
					Uri uri = LetoFileProvider.getUriForFile(this, getPackageName() + ".leto.fileprovider", _imagePicker._destFile);
		            CropImageIntentBuilder cropImage = new CropImageIntentBuilder(_imagePicker._expectedWidth,
						_imagePicker._expectedHeight, uri);
		            cropImage.setSourceImage(uri);
		            if(isPNG()) {
						cropImage.setOutputFormat(CompressFormat.PNG.toString());
					}
		            startActivityForResult(cropImage.getIntent(this), REQ_CROP_IMAGE);
				} else {
					// callback
					_imagePicker.onImagePickingCancelled();
					
					// finish self
					finish();
				}
				
				break;
			case REQ_CROP_IMAGE:
				if(resultCode == RESULT_OK) {
					_imagePicker.onImagePicked();
				} else {
					_imagePicker.onImagePickingCancelled();
				}
				
				// finish self
				finish();
				
				break;
			default:
				super.onActivityResult(requestCode, resultCode, data);
				break;
		}
	}
}

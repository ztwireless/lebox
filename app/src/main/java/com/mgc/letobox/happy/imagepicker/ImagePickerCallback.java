package com.mgc.letobox.happy.imagepicker;

public interface ImagePickerCallback {
	void onImagePicked(String file);

	void onImagePickingCancelled();
}

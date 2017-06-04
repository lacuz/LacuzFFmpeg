package com.lacuz.ffmpeg;

public interface JniProgressListener {
	public  void onProgress(int progress);
	public  void onFinish();
	public  void onError(String message);
}

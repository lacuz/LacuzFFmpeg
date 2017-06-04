package com.lacuz.ffmpeg;

import android.util.Log;

//-y -i in.mp4 -filter_complex scale=480:852,setdar=480:852 -r 30 -b 10M out.mp4
public class FFmpegCmd {

	private static FFmpegCmd instance;
	private static JniProgressListener listener;
	public static FFmpegCmd getInstance(){
		if(instance==null)instance=new FFmpegCmd();
		return instance;
	}

	public  void  exec(String[] cmds, JniProgressListener listener) {
		this.listener = listener;
		ffmpegcore(cmds.length, cmds);
	}
	public native int ffmpegcore(int argc, String[] argv);
	public  native void stop();//self stop

	static {
		System.loadLibrary("avutil-54");
		System.loadLibrary("swresample-1");
		System.loadLibrary("avcodec-56");
		System.loadLibrary("avformat-56");
		System.loadLibrary("swscale-3");
		System.loadLibrary("postproc-53");
		System.loadLibrary("avfilter-5");
		System.loadLibrary("avdevice-56");
		System.loadLibrary("ffmpeg");
	}

	public void onProgress(int progress) {
		if (listener != null)
			listener.onProgress(progress);

	}

	public void onFinish() {
		if (listener != null)
			listener.onFinish();

	}

	public void onError(String message) {
		Log.e(">>>>"," "+message);
		if (listener != null)
			listener.onError(message);
	}

}

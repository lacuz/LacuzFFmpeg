package com.lacuz.lacuzffmpeg;

import java.text.SimpleDateFormat;
import java.util.Date;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.lacuz.ffmpeg.FFmpegUtils;
import com.lacuz.ffmpeg.FileUtils;
import com.lacuz.ffmpeg.JniProgressListener;
import com.lacuz.ffmpeg.TimeUtils;
import com.lacuz.ffmpeg.VideoDeal;

public class VideoMainActivity extends Activity implements JniProgressListener {

	private static final int TRANSFER_VIDEO_CHANGE_TITLE = 3;
	private static final int TRANSFER_VIDEO_FAIL = 2;
	private static final int TRANSFER_VIDEO_PROGRESS = 1;
	private static final int TRANSFER_VIDEO_SUCCESS = 0;

	private  ProgressDialog mProgressDialog;
	private  EditText filePathET;
	private  String outfile;
	private  long startTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		outfile = Environment.getExternalStorageDirectory() + "/1/"
				+ getOutFileName();
		filePathET = (EditText) this.findViewById(R.id.editText_cmd);
		Button startButton = (Button) this.findViewById(R.id.button_start);
		Button chooseButton = (Button) this.findViewById(R.id.button_choose);
		chooseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				chooseVideo();
			}
		});

		startButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {

				try{
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							startTime = System.currentTimeMillis();
							FFmpegUtils.OutputOption outputOption = new FFmpegUtils.OutputOption(outfile);
							outputOption.width = 480;//输出视频宽，如果不设置则为原始视频宽高
							outputOption.height = FileUtils.getVideoHegith(filePathET.getText().toString(),480);//输出视频高度
							outputOption.frameRate = 30;//输出视频帧率,默认30
							outputOption.bitRate = 10;//输出视频码率,默认10
							FFmpegUtils.exec(new VideoDeal(filePathET.getText().toString()), outputOption,VideoMainActivity.this);
						}
					}).start();
					openProgressDialog();
				}catch(Exception e){
					if(mProgressDialog!=null) mProgressDialog.cancel();
					showNormalDia("转换失败，请重新尝试");
				}
			}
		});
	}

	@Override
	public void onProgress(int progress) {
		// TODO Auto-generated method stub
		Log.e("OnMessage", " ------onProgress--------" + progress);
		if(progress<100){
			Message msg = new Message();
			msg.what = TRANSFER_VIDEO_PROGRESS;
			msg.arg1 = progress;
			mhander.sendMessage(msg);
		}else{
			mhander.sendEmptyMessage(TRANSFER_VIDEO_CHANGE_TITLE);
		}

	}


	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		Log.e("OnMessage", " ------onFinish--------");
		if(mProgressDialog != null)
			mProgressDialog.cancel();

		mhander.sendEmptyMessage(TRANSFER_VIDEO_SUCCESS);
	}


	@Override
	public void onError(String message) {
		// TODO Auto-generated method stub
		if(mProgressDialog != null)
			mProgressDialog.cancel();
		mhander.sendEmptyMessage(TRANSFER_VIDEO_FAIL);

	}


	@SuppressLint("HandlerLeak")
	Handler mhander = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case TRANSFER_VIDEO_SUCCESS:
					String aSize = FileUtils.getFileOrFilesSize(outfile, 3) + "";
					String bSize = FileUtils.getFileOrFilesSize(filePathET.getText()
							.toString(), 3)
							+ "";
					String message = "转换前大小为：" + bSize + "M\n" + "转换后大小为：" + aSize + "M\n";
					showNormalDia(message
							+ TimeUtils.getDistanceTime2(startTime,
							System.currentTimeMillis()));
					break;
				case TRANSFER_VIDEO_PROGRESS:
					mProgressDialog.setProgress(msg.arg1);
					break;
				case TRANSFER_VIDEO_FAIL:
					showNormalDia("转换失败");
					break;
				case TRANSFER_VIDEO_CHANGE_TITLE:
					if(mProgressDialog !=null) mProgressDialog.setTitle("视频转换还差最后一步，请稍等。。。。");
				default:
					break;
			}

		};
	};




	@SuppressWarnings("deprecation")
	public void openProgressDialog() {
		mProgressDialog = new ProgressDialog(this);
		final int totalProgressTime = 100;
		mProgressDialog.setMessage("正在转换视频，请勿取消");
		mProgressDialog.setButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				FFmpegUtils.stop();

			}
		});
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(false);
		mProgressDialog.setMax(totalProgressTime);
		mProgressDialog.show();

	}

	/* 普通的对话框 */
	private  void showNormalDia(String message) {
		// AlertDialog.Builder normalDialog=new
		// AlertDialog.Builder(getApplicationContext());
		AlertDialog.Builder normalDia = new AlertDialog.Builder(this);
		normalDia.setTitle("提示");
		normalDia.setMessage(message);
		normalDia.setCancelable(false);
		normalDia.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		normalDia.create().show();
	}

	@SuppressLint("SimpleDateFormat")
	private String getOutFileName() {
		String filename = "hehe.mp4";
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmssSSS");
		filename = simpleDateFormat.format(date) + ".mp4";
		return filename;
	}

	// 选取图片按钮单击事件
	public void chooseVideo() {
		Intent intent = new Intent();
		/* 开启Pictures画面Type设定为image */
		// intent.setType(“image/*”);
		// intent.setType(“audio/*”); //选择音频
		intent.setType("video/*"); // 选择视频 （mp4 3gp 是android支持的视频格式）
		// intent.setType(“video/*;image/*”);//同时选择视频和图片
		/* 使用Intent.ACTION_GET_CONTENT这个Action */
		intent.setAction(Intent.ACTION_GET_CONTENT);
		/* 取得相片后返回本画面 */
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 选取图片的返回值
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Uri uriVideo = data.getData();
				Cursor cursor=this.getContentResolver().query(uriVideo, null, null, null, null);
				if (cursor.moveToNext()) {
                        /* _data：文件的绝对路径 ，_display_name：文件名 */
					String videoFilePath  = cursor.getString(cursor.getColumnIndex("_data"));
					filePathET.setText(videoFilePath);
				}
			}

		}
	}



}

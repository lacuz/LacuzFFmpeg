# LacuzFFmpeg
基于FFmpeg库ndk开发的视频处理框架，加入了视频处理进度。可进行视频压缩，其他视频剪辑、视频合成、加入图片文字logo等功能需自己看看功能实现</br>

##视频压缩
```java
FFmpegUtils.OutputOption outputOption = new FFmpegUtils.OutputOption(outfile);
outputOption.width = 480;//输出视频宽，如果不设置则为原始视频宽高
outputOption.height = FileUtils.getVideoHegith(filePathET.getText().toString(),480);
FFmpegUtils.exec(new VideoDeal(filePathET.getText().toString()), outputOption,VideoMainActivity.this);
对视频处理要注意先获取视频的分辨率，再进行等比例缩放，不然处理完的视频会变形。 
设置width和height的值时不能为奇数，不然会报错。
```

<img src="https://github.com/lacuz/LacuzFFmpeg/blob/master/photo/Screenshot_1.png" width="20%" height="20%"/>
<img src="https://github.com/lacuz/LacuzFFmpeg/blob/master/photo/Screenshot_2.png" width="20%" height="20%"/>
<img src="https://github.com/lacuz/LacuzFFmpeg/blob/master/photo/Screenshot_2.png" width="20%" height="20%"/>

						


这里感谢雷霄骅提供的库以及yangjie10930对上层的封装。

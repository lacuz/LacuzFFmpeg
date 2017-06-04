/**
 * This software is a Transcoder in Android.
 * It is transplanted from ffmpeg.c command line tools.
 *
 */

#include <string.h>
#include <jni.h>
#include <ffmpeg.h>

#ifdef ANDROID
#include <jni.h>
#include <android/log.h>
#define LOGE(format, ...)  __android_log_print(ANDROID_LOG_ERROR, "(>_<)", format, ##__VA_ARGS__)
#define LOGI(format, ...)  __android_log_print(ANDROID_LOG_INFO,  "(^_^)", format, ##__VA_ARGS__)
#else
#define LOGE(format, ...)  printf("(>_<) " format "\n", ##__VA_ARGS__)
#define LOGI(format, ...)  printf("(^_^) " format "\n", ##__VA_ARGS__)
#endif


int ffmpegmain( JNIEnv * env, jobject thiz,int argc, char **argv);

//Output FFmpeg's av_log()
void custom_log(void *ptr, int level, const char* fmt, va_list vl){

	//To TXT file

	FILE *fp=fopen("/storage/emulated/0/1/av_log.txt","a+");
	if(fp){
		vfprintf(fp,fmt,vl);
		fflush(fp);
		fclose(fp);
	}


	//To Logcat
	//LOGE(fmt, vl);
}
JNIEXPORT jint JNICALL Java_com_lacuz_ffmpeg_FFmpegCmd_ffmpegcore( JNIEnv * env, jobject thiz, jint cmdnum, jobjectArray cmdline)
{

  //FFmpeg av_log() callback
//  av_log_set_callback(custom_log);//log to local text

  int argc=cmdnum;
  char** argv=(char**)malloc(sizeof(char*)*argc);

  int i=0;
  for(i=0;i<argc;i++)
  {
    jstring string=(*env)->GetObjectArrayElement(env,cmdline,i);
    const char* tmp=(*env)->GetStringUTFChars(env,string,0);
    argv[i]=(char*)malloc(sizeof(char)*1024);
    strcpy(argv[i],tmp);
  }

  ffmpegmain(env,thiz,argc,argv);

  for(i=0;i<argc;i++){
    free(argv[i]);
  }
  free(argv);
  return 0;

}


JNIEXPORT void  JNICALL Java_com_lacuz_ffmpeg_FFmpegCmd_stop()
{
	com_java_cancel();
}

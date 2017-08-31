package com.lee.baiduvoicedemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

public class SynthesisActivity extends Activity implements SpeechSynthesizerListener {

    private EditText content;
    private Button btn;
    // 语音合成客户端
    private SpeechSynthesizer mSpeechSynthesizer;
    private String mSampleDirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    private static final String LICENSE_FILE_NAME = "temp_license.txt";
    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.synthesis);

        initialEnv();

        content = (EditText) findViewById(R.id.edt_content);
        btn = (Button) findViewById(R.id.btn_speak);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTTS();
            }
        });
    }

    // 初始化语音合成客户端并启动
    private void startTTS() {
        // 获取语音合成对象实例
        mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        // 设置context
        mSpeechSynthesizer.setContext(this);
        // 设置语音合成状态监听器
        mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 设置在线语音合成授权，需要填入从百度语音官网申请的api_key和secret_key
        mSpeechSynthesizer.setApiKey("Cj0jf48NHHTD4uQpEGYVluI0", "gL7Zf4GIg2i58xLnTnAr8bXGxye9hqYs");
        // 设置离线语音合成授权，需要填入从百度语音官网申请的app_id
        mSpeechSynthesizer.setAppId("10068368");
        // 设置语音合成文本模型文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE,  mSampleDirPath + "/voicetest/"
                + TEXT_MODEL_NAME);
        // 设置语音合成声音模型文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/voicetest/"  + SPEECH_FEMALE_MODEL_NAME);
        // 设置语音合成声音授权文件
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, mSampleDirPath + "/voicetest/"  + LICENSE_FILE_NAME);
        //发音人（在线引擎），可用参数为0,1,2,3。。。
        //（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置Mix模式的合成策略
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 获取语音合成授权信息
        AuthInfo authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);
        // 判断授权信息是否正确，如果正确则初始化语音合成器并开始语音合成，如果失败则做错误处理
        if (authInfo.isSuccess()) {
            //            mSpeechSynthesizer.initTts(TtsMode.MIX);
        } else {
            // 授权失败
            String errorMsg = authInfo.getTtsError().getDetailMessage();
            Log.i("SynthesisActivity", ">>>auth failed errorMsg: " + errorMsg);
        }
        // 引擎初始化tts接口
        mSpeechSynthesizer.initTts(TtsMode.MIX);
        //         加载离线英文资源（提供离线英文合成功能）
        //        int result = mSpeechSynthesizer.loadEnglishModel(mSampleDirPath + ENGLISH_TEXT_MODEL_NAME, 
        //                mSampleDirPath + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        int result = mSpeechSynthesizer.loadEnglishModel(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/voicetest/" + ENGLISH_TEXT_MODEL_NAME,
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/voicetest/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        Log.i("SynthesisActivity", ">>>loadEnglishModel result: " + result);

        mSpeechSynthesizer.speak(content.getText().toString().trim());
    }

    public void onError(String arg0, SpeechError arg1) {
        // 监听到出错，在此添加相关操作
        Log.d("SynthesisActivity", "onError>>>" + arg0 + arg1);
    }

    public void onSpeechFinish(String arg0) {
        // 监听到播放结束，在此添加相关操作
        Log.d("SynthesisActivity", "onSpeechFinish>>>" + arg0);
    }

    public void onSpeechProgressChanged(String arg0, int arg1) {
        // 监听到播放进度有变化，在此添加相关操作
        Log.d("SynthesisActivity", "onSpeechProgressChanged>>>" + arg0 + arg1);
    }

    public void onSpeechStart(String arg0) {
        // 监听到合成并播放开始，在此添加相关操作
        Log.d("SynthesisActivity", "onSpeechStart>>>" + arg0);
    }

    public void onSynthesizeDataArrived(String arg0, byte[] arg1, int arg2) {
        // 监听到有合成数据到达，在此添加相关操作
        Log.d("SynthesisActivity", "onSynthesizeDataArrived>>>" + arg0 + arg2);
    }

    public void onSynthesizeFinish(String arg0) {
        // 监听到合成结束，在此添加相关操作
        Log.d("SynthesisActivity", "onSynthesizeFinish>>>" + arg0);
    }

    public void onSynthesizeStart(String arg0) {
        // 监听到合成开始，在此添加相关操作
        Log.d("SynthesisActivity", "onSynthesizeStart>>>" + arg0);
    }

    private void initialEnv() {

        CopyAssetsFile copyAssetsFile = new CopyAssetsFile();
        copyAssetsFile.copyFilesFassets(this, "assets/" + SPEECH_FEMALE_MODEL_NAME,  "voicetest/" + SPEECH_FEMALE_MODEL_NAME);
        copyAssetsFile.copyFilesFassets(this, "assets/" + SPEECH_MALE_MODEL_NAME, "voicetest/" + SPEECH_MALE_MODEL_NAME);
        copyAssetsFile.copyFilesFassets(this, "assets/" + TEXT_MODEL_NAME, "voicetest/" + TEXT_MODEL_NAME);
        copyAssetsFile.copyFilesFassets(this, "assets/" + LICENSE_FILE_NAME, "voicetest/" + LICENSE_FILE_NAME);
        copyAssetsFile.copyFilesFassets(this, "assets/english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, "voicetest/"
                + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        copyAssetsFile.copyFilesFassets(this, "assets/english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, "voicetest/"
                + ENGLISH_SPEECH_MALE_MODEL_NAME);
        copyAssetsFile.copyFilesFassets(this, "assets/english/" + ENGLISH_TEXT_MODEL_NAME, "voicetest/"
                + ENGLISH_TEXT_MODEL_NAME);
    }

    @Override
    protected void onDestroy() {
        this.mSpeechSynthesizer.release();//释放资源
        super.onDestroy();
    }
}


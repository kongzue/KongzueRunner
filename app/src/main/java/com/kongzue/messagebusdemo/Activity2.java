package com.kongzue.messagebusdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialogx.util.InputInfo;
import com.kongzue.runner.ActivityRunnable;
import com.kongzue.runner.DataWatcher;
import com.kongzue.runner.DataWatchers;
import com.kongzue.runner.Runner;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.runner.SenderTarget;

public class Activity2 extends AppCompatActivity {
    
    //不放心或者需要混淆的话，请使用 @SenderTarget("bitmapResult") 来标记接收 key 的成员
    Bitmap bitmapResult;
    
    @DataWatchers({"subscriberA", "subscriberB"})
    private TextView txtSubscribeMessage;
    private ImageView imgSenderPicture;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        
        txtSubscribeMessage = findViewById(R.id.txt_subscribe_message);
        
        //如果有图像，直接显示 bitmapResult
        imgSenderPicture = findViewById(R.id.img_sender_picture);
        if (bitmapResult != null) {
            imgSenderPicture.setImageBitmap(bitmapResult);
        }
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    /**
     * 在 MainActivity 执行一个事件
     *
     * @param view 按钮
     */
    public void runOnMainActivity(View view) {
        Runner.runOnActivity("MainActivity", new ActivityRunnable() {
            @Override
            public void run(Activity activity) {
                MessageDialog.build()
                        .setTitle("提示")
                        .setMessage("这个事件来自 Activity2 创建。\n请注意此事件是立即在 MainActivity 中执行的，具体详情请查看 Logcat 日志顺序。")
                        .setOkButton("OK")
                        .show(activity);
            }
        });
        PopTip.show("已执行\n请返回 MainActivity 查看！");
    }
    
    /**
     * 在回到 MainActivity 后执行一个事件
     *
     * @param view 按钮
     */
    public void runOnMainResume(View view) {
        Runner.runOnActivity("MainActivity", new ActivityRunnable() {
            @Override
            public void run(Activity activity) {
                MessageDialog.build()
                        .setTitle("提示")
                        .setMessage("这个事件来自 Activity2 创建。\n请注意此事件是在回到 MainActivity 后执行的，具体详情请查看 Logcat 日志顺序。")
                        .setOkButton("OK")
                        .show(activity);
            }
        });
        PopTip.show("已准备就绪\n请返回 MainActivity 查看！");
    }
    
    /**
     * 为所有订阅者A推送一个消息
     *
     * @param view 按钮
     */
    public void sendTextDataA(View view) {
        InputDialog.show("为所有订阅者A推送一个消息", "请输入内容，会使 MainActivity 和 Activity2 两个界面的订阅者组件全部变更内容", "OK")
                .setInputText("Test Message")
                .setInputInfo(new InputInfo().setSelectAllText(true))
                .setOkButton(new OnInputDialogButtonClickListener<InputDialog>() {
                    @Override
                    public boolean onClick(InputDialog baseDialog, View v, String inputStr) {
                        Runner.changeData("subscriberA", inputStr);
                        PopTip.show("已完成！\n请观察本界面和上一层界面的订阅消息内容变化");
                        return false;
                    }
                });
    }
    
    /**
     * 为所有订阅者B推送一个消息
     *
     * @param view 按钮
     */
    public void sendTextDataB(View view) {
        InputDialog.show("为所有订阅者B推送一个消息", "请输入内容，会使 MainActivity 和 Activity2 两个界面的订阅者组件全部变更内容", "OK")
                .setInputText("Hello World!")
                .setInputInfo(new InputInfo().setSelectAllText(true))
                .setOkButton(new OnInputDialogButtonClickListener<InputDialog>() {
                    @Override
                    public boolean onClick(InputDialog baseDialog, View v, String inputStr) {
                        Runner.changeData("subscriberB", inputStr);
                        Runner.changeDataByTag("subscriberB", inputStr);
                        PopTip.show("已完成！\n请观察本界面和上一层界面的订阅消息内容变化");
                        return false;
                    }
                });
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //点击返回图标事件
            case android.R.id.home:
                onBackPressed();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
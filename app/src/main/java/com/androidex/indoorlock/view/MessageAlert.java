package com.androidex.indoorlock.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidex.indoorlock.R;

/**
 * Created by Administrator on 2018/3/2.
 */

public class MessageAlert extends Dialog {
    private Context context;
    private String strTitle = "";
    private String strMessage  ="";
    private MessageAlertEvent event;
    private TextView title;
    private TextView message;
    private Button cancel,confirm;

    public MessageAlert(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public MessageAlert(@NonNull Context context, int themeResId,String title,String message,MessageAlertEvent event) {
        super(context, themeResId);
        this.context = context;
        this.context = context;
        this.strTitle = title;
        this.strMessage = message;
        this.event = event;

    }

    protected MessageAlert(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_message);
        title = findViewById(R.id.title);
        if(strTitle!=null && strTitle.length()>0){
            title.setText(strTitle);
        }else{
            title.setText("消息通知");
        }
        message = findViewById(R.id.message);
        if(strMessage !=null && strMessage.length()>0){
            message.setText(strMessage);
        }
        cancel = findViewById(R.id.cancel);
        cancel.setText("取消");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(event!=null){
                    event.onCancel();
                }
                MessageAlert.this.dismiss();
            }
        });
        confirm = findViewById(R.id.confirm);
        confirm.setText("确认");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(event!=null){
                    event.onConfirm();
                }
                MessageAlert.this.dismiss();
            }
        });
    }

    public interface MessageAlertEvent{
        public void onCancel();
        public void onConfirm();
    }
}

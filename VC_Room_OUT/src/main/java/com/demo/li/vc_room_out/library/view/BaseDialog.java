package com.demo.li.vc_room_out.library.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class BaseDialog extends Dialog
{
    public BaseDialog(Context context)
    {
	super(context);
    }

    public BaseDialog(Context context, int theme)
    {
	super(context, theme);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
    {
	super(context, cancelable, cancelListener);
    }

    @Override
    public void show()
    {
	// 弹出对话框时不显示系统导航栏
	Window dialogWindow = getWindow();
	WindowManager.LayoutParams lp = dialogWindow.getAttributes();
	dialogWindow.setGravity(Gravity.CENTER);
	lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
	lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
	dialogWindow.setAttributes(lp);
	super.show();
    }
}

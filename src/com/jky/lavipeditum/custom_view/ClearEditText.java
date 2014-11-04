package com.jky.lavipeditum.custom_view;



import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jky.lavipeditum.R;

/**
 * 
 * @ClassName: ClearEditText
 * @Description: 快速清除文字的EditView
 *
 * @author o0teamo0o
 * @date 2014年10月26日 下午2:02:38
 */
public class ClearEditText extends EditText implements OnFocusChangeListener, TextWatcher {
	private Drawable mClearDrawable;  //删除按钮的引用
	private boolean hasFoucs; //控件是否有焦点
	private PopupWindow popupHeadWindow;
	private PopupWindow popupBodyWindow;

	public ClearEditText(Context context) { 
    	this(context, null); 
    } 
 
    public ClearEditText(Context context, AttributeSet attrs) { 
    	//这里构造方法也很重要，不加这个很多属性不能再XML里面定义
    	this(context, attrs, android.R.attr.editTextStyle); 
    } 
    
    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始化视图和数据
     */
	private void init() {
		//获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
		mClearDrawable = getCompoundDrawables()[2];
		if(mClearDrawable == null){
			//throw new NullPointerException("You can add drawableRight attribute in XML");
			mClearDrawable = getResources().getDrawable(R.drawable.delete);
		}
		
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
		//默认设置隐藏图标
		setClearIconVisible(false);
		//设置焦点改变监听
		setOnFocusChangeListener(this);
		//设置输入框里面内容发生改变的监听
		addTextChangedListener(this);
	}
	
	/**
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
	 * 当我们按下的位置在EditText的宽度 - 图标到控件右边的距离 - 图标的宽度 和
	 * EditText宽度 - 图标到控件右边的的距离之间我们就算点击了图标，竖直方向就没有考虑
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){
			if(getCompoundDrawables()[2] != null){
				boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < ((getWidth() - getPaddingRight())));
				if(touchable){
					this.setText("");
				}
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 设置清除图片的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * @param visible
	 */
	private void setClearIconVisible(boolean visible) {
		//判断是否输入了内容,如果输入了内容我们就把弹窗关闭
		if (visible) {
			if (popupHeadWindow != null) {
				popupHeadWindow.dismiss();
			}
			if (popupBodyWindow != null) {
				popupBodyWindow.dismiss();
			}
		}
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	/**
	 * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	 * @param v
	 * @param hasFocus
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFoucs = hasFocus;
		if(hasFocus){
			setClearIconVisible(getText().length() > 0);
		}else{
			setClearIconVisible(false);
		}
	}
	
	/**
	 * 当输入框里面内容发生变化的时候回调的方法
	 */
	@Override
	public void onTextChanged(CharSequence text, int start, int before,
			int after) {
		if(hasFoucs){
			//获取焦点设置回之前的图标
			mClearDrawable = getResources().getDrawable(R.drawable.delete);
			mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
			setClearIconVisible(text.length() > 0);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}
	
	/**
	 * 设置晃动动画
	 */
	public void setShakeAnimation(){
		this.setAnimation(shakeAnimation(5));
	}
	
	/**
	 * 
	 * @Title: setAlertDialog
	 * @Description: 显示警告对话框,该方法返回一个对话框集合,调用者可以自己cancel掉
	 * @param context
	 * @param message
	 * @return
	 */
	public List<PopupWindow> setAlertDialog(Context context, String message){
		List<PopupWindow> popupWindows = new ArrayList<PopupWindow>();
		mClearDrawable = getResources().getDrawable(R.drawable.exclamation_mark);
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
		setClearIconVisible(true);
		//箭头的图片
		ImageView popupHead = new ImageView(context);
		if (popupHeadWindow != null) {
			popupHeadWindow.dismiss();
		}
		popupHeadWindow = new PopupWindow(popupHead, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		
		popupHeadWindow.setOutsideTouchable(true);
		
		//显示内容的popup也构造出来
		View popupBody = View.inflate(context, R.layout.empty_alert_popup, null);
		TextView tv_content = (TextView) popupBody.findViewById(R.id.tv_content);
		tv_content.setText(message);
		
		if (popupBodyWindow != null) {
			popupBodyWindow.dismiss();
		}
		popupBodyWindow = new PopupWindow(popupBody, -2, -2);
		
		
		//找到具体坐标
		int[] location = new int[2];
		//
		this.getLocationInWindow(location);
		popupHead.setImageResource(R.drawable.bg_head_bottom);
		popupHeadWindow.showAtLocation(this, Gravity.NO_GRAVITY, location[0] + this.getWidth() - 30, location[1] + this.getHeight());
		popupWindows.add(popupHeadWindow);
		
		popupBodyWindow.showAtLocation(popupHead, Gravity.NO_GRAVITY, location[0]  + this.getWidth(), location[1] + this.getHeight() + 15);
		popupWindows.add(popupBodyWindow);
		return popupWindows;
	}

	/**
	 * 晃动动画
	 * @param counts 1秒晃动多少下
	 * @return
	 */
	private Animation shakeAnimation(int counts) {
		Animation trAnimation = new TranslateAnimation(0, 10, 0, 0);
		trAnimation.setInterpolator(new CycleInterpolator(counts));
		trAnimation.setDuration(1000);
		return trAnimation;
	}

}
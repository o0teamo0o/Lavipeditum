package com.jky.lavipeditum.custom_view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

/**
 * 
 * @ClassName: CustomViewPager 
 * @Description: 自定义ViewPager 意图是控制viewpager滑动
 *
 * @author o0teamo0o
 * @date 2014年10月24日 下午10:17:02 
 *
 */
public class CustomViewPager extends ViewPager {
	
	private boolean enabled;

	public CustomViewPager(Context context) {
		super(context);
		this.enabled = false;
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs); 
		this.enabled = false;
	}
	
	/**
	 * 触摸没有反应就可以了
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (this.enabled) {
            return super.onTouchEvent(ev);
        }
        return false;
	}
	
	@Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }
 
        return false;
    }
	
	/**
     * 解决ViewPager无法自适应高度的问题
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

      int height = 0;
      for(int i = 0; i < getChildCount(); i++) {
        View child = getChildAt(i);
        child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int h = child.getMeasuredHeight();
        if(h > height) height = h;
      }

      heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
	
	public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}

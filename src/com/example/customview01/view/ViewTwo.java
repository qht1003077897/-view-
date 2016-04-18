package com.example.customview01.view;

import com.example.customview01.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
@SuppressLint("ClickableViewAccessibility") public class ViewTwo extends View{
	private Paint paint;
	private Path path;
	private float x=300;
	private float y=500;
	private float xp=300;
	private float yp=500;
	private Bitmap bitmap;
	private Options options;
	private boolean jump;
	private int jumpPercent=100;
	public ViewTwo(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	public ViewTwo(Context context)
	{
		this(context, null);
	}
	public ViewTwo(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs,defStyle);
		// TODO Auto-generated constructor stub
		paint=new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(7f);
		paint.setAntiAlias(true);
	    path=new Path();
	    //这种写法，有效避免内存溢出
	    options=new Options();
	    options.inJustDecodeBounds=true;
	    bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.qqq,options);
	    //下面这句话是为了节省内存
	    options.inSampleSize=options.outHeight/90;
	    //下面计算得到缩略图（等比例放大缩小）的宽和高
	    options.outWidth=options.outWidth*90/options.outHeight;
	    options.outHeight=90;
	    options.inJustDecodeBounds=false;
	    bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.qqq,options);
	}
	protected void onDraw(Canvas canvas) {
		/***画正方形*/
		paint.reset();
		float[] pts={100,500,500,500,
				     100,100,500,100,
				     100,100,100,500,
				     500,100,500,500};
		canvas.drawLines(pts,4,12, paint);
		
		/***画贝塞尔*/
	    paint.reset();
	    //画笔颜色
	    paint.setColor(Color.RED);
	    //画出不填充的图形
	    paint.setStyle(Paint.Style.STROKE);
	    //参数越大线条越粗
	    paint.setStrokeWidth(3f);
        //必须重置path，不然会出现many曲线
	    path.reset();
	    //起点坐标
	   	path.moveTo(100, 500);
	    //x,y为辅助点坐标，500，500为终点坐标
		path.quadTo(x, y, 500, 500);
		canvas.drawPath(path, paint);
		
		/***画出辅助点，可以看出轨迹*/
		canvas.drawPoint(x, y, paint);
		/***画小人*/
		 canvas.drawBitmap(bitmap, xp-options.outWidth/2, yp-options.outHeight, paint);
		 if (jump) {  
				/***画贝塞尔*/
	            if(jumpPercent >0) {  
	            	if((yp-options.outHeight)*jumpPercent/100<=100){
	            		canvas.drawBitmap(bitmap, xp-options.outWidth/2, 100, paint);  
	            		jumpPercent -=5;  
		                xp=300;
		    			yp=500;
		                postInvalidateDelayed(50);
		                return;
	            	}
	                    canvas.drawBitmap(bitmap, xp-options.outWidth/2, (yp-options.outHeight)* jumpPercent/100, paint);  
	                    jumpPercent -=5;  
	                    xp=300;
	    		    	yp=500;
	                    postInvalidateDelayed(50);
	            }else {  
	            	    jumpPercent = 100;  
	                    jump = false;  
	            }
			}
		 invalidate();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			x=event.getX();
			y=event.getY();
			/***t的范围从0-0.5就是从起始点到终点曲线上的点坐标,
			 *  xp中100是起始点横坐标，x是辅助点的横坐标，500是终点的横坐标
			    yp中500是起始点纵坐标，y是辅助点的纵坐标，500是终点的纵坐标
			**/
			if(y<500){y=500;}
//			for(float t=0;t<=0.5;t+=0.1){
			     float t=(float) 0.5;
			     //xp,yp是贝塞尔曲线中点坐标，调节下面y的大小可以控制在拉动的时候图片距离横线的距离
		         xp = (1-t)*(1-t)* 100 + 2* t*(1-t)* x + t * t * 500;
		         yp = (1-t)*(1-t)* 500 + 2* t*(1-t)* y + t * t * 500;
//		}
			//postInvalidate()方法是在子线程中刷新View
			//invalidate()方法是在UI线程中刷新View
			invalidate();
			break;
			 //ACTION_UP为松开手势状态，让曲线回到直线状态
		case MotionEvent.ACTION_UP:
			//让曲线回到直线状态
			x=300;
			y=500;
			jump = true;
			invalidate();
		}
		return true;
	}
	
}

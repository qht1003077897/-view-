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
	    //����д������Ч�����ڴ����
	    options=new Options();
	    options.inJustDecodeBounds=true;
	    bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.qqq,options);
	    //������仰��Ϊ�˽�ʡ�ڴ�
	    options.inSampleSize=options.outHeight/90;
	    //�������õ�����ͼ���ȱ����Ŵ���С���Ŀ�͸�
	    options.outWidth=options.outWidth*90/options.outHeight;
	    options.outHeight=90;
	    options.inJustDecodeBounds=false;
	    bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.qqq,options);
	}
	protected void onDraw(Canvas canvas) {
		/***��������*/
		paint.reset();
		float[] pts={100,500,500,500,
				     100,100,500,100,
				     100,100,100,500,
				     500,100,500,500};
		canvas.drawLines(pts,4,12, paint);
		
		/***��������*/
	    paint.reset();
	    //������ɫ
	    paint.setColor(Color.RED);
	    //����������ͼ��
	    paint.setStyle(Paint.Style.STROKE);
	    //����Խ������Խ��
	    paint.setStrokeWidth(3f);
        //��������path����Ȼ�����many����
	    path.reset();
	    //�������
	   	path.moveTo(100, 500);
	    //x,yΪ���������꣬500��500Ϊ�յ�����
		path.quadTo(x, y, 500, 500);
		canvas.drawPath(path, paint);
		
		/***���������㣬���Կ����켣*/
		canvas.drawPoint(x, y, paint);
		/***��С��*/
		 canvas.drawBitmap(bitmap, xp-options.outWidth/2, yp-options.outHeight, paint);
		 if (jump) {  
				/***��������*/
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
			/***t�ķ�Χ��0-0.5���Ǵ���ʼ�㵽�յ������ϵĵ�����,
			 *  xp��100����ʼ������꣬x�Ǹ�����ĺ����꣬500���յ�ĺ�����
			    yp��500����ʼ�������꣬y�Ǹ�����������꣬500���յ��������
			**/
			if(y<500){y=500;}
//			for(float t=0;t<=0.5;t+=0.1){
			     float t=(float) 0.5;
			     //xp,yp�Ǳ����������е����꣬��������y�Ĵ�С���Կ�����������ʱ��ͼƬ������ߵľ���
		         xp = (1-t)*(1-t)* 100 + 2* t*(1-t)* x + t * t * 500;
		         yp = (1-t)*(1-t)* 500 + 2* t*(1-t)* y + t * t * 500;
//		}
			//postInvalidate()�����������߳���ˢ��View
			//invalidate()��������UI�߳���ˢ��View
			invalidate();
			break;
			 //ACTION_UPΪ�ɿ�����״̬�������߻ص�ֱ��״̬
		case MotionEvent.ACTION_UP:
			//�����߻ص�ֱ��״̬
			x=300;
			y=500;
			jump = true;
			invalidate();
		}
		return true;
	}
	
}

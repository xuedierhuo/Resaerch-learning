package Linstener;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BothListener {
	private static final int FIRSTLISTERNERHOUR = 24;
	private static final int SECONDLISTENERHOUR = 48;
	private static final long MINUTEMILLIS = 60 * 1000;
	private static final long HOURMILIS = MINUTEMILLIS * 60;
	private static final Calendar BEGINDATE = Calendar.getInstance();
	private static boolean canFistrListener = true;
	private static long secondListenerMillis = 0;
	private static Timer firstListener;
	private static Timer secondListener;
	
	static Calendar todayFirstListener, todaySecondListener;

	public static void main(String[] args) {
		init();
		toDoFirstListener();
		toDoSecondListener();
	}
	
	
	//初始化设定
	public static void init() {
		todayFirstListener = BEGINDATE;
		//将第一提醒的时间设置成今天的0点
		todayFirstListener.set(BEGINDATE.get(Calendar.YEAR), 
				BEGINDATE.get(Calendar.MONTH), 
				BEGINDATE.get(Calendar.DATE), 
				0, 0, 0);
		//将第二提醒的时间设定成1点
		todaySecondListener = todayFirstListener;
		todaySecondListener.set(Calendar.HOUR_OF_DAY, 1);
		
	}
	//设置第一个监听器
	public static void toDoFirstListener() {
		firstListener= new Timer();
		firstListener.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					if (!canFistrListener) {
						firstListener.wait();
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("第一监听器暂停失败");
				}
				System.out.println("第一监听器提醒");
			}
		}, todayFirstListener.getTime(), MINUTEMILLIS * FIRSTLISTERNERHOUR);
	}
	
	
	//设置第二个监听器
	public static void toDoSecondListener() {
		secondListener = new Timer();
		secondListener.schedule(new TimerTask() {
			
			@Override
			public void run() {
				canFistrListener = false;
				System.out.println("第二监听器提醒 ");
				beginSecondListener();
			}
		}, todaySecondListener.getTime(), MINUTEMILLIS * SECONDLISTENERHOUR);	
	}
	
	//开始执行第二个监听器的提醒
		public static void beginSecondListener() {
			Thread running = new Thread(new Runnable() {
				
				@Override
				public void run() {
					Timer secondListener = new Timer();
					secondListener.schedule(new TimerTask() {
						
						@Override
						public void run() {
							canFistrListener = true;
							firstListener.notifyAll();
						}
					}, secondListenerMillis);
				}
			});
			running.start();
		}
}

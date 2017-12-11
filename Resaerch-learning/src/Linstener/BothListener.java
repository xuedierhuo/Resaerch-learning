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
	
	
	//��ʼ���趨
	public static void init() {
		todayFirstListener = BEGINDATE;
		//����һ���ѵ�ʱ�����óɽ����0��
		todayFirstListener.set(BEGINDATE.get(Calendar.YEAR), 
				BEGINDATE.get(Calendar.MONTH), 
				BEGINDATE.get(Calendar.DATE), 
				0, 0, 0);
		//���ڶ����ѵ�ʱ���趨��1��
		todaySecondListener = todayFirstListener;
		todaySecondListener.set(Calendar.HOUR_OF_DAY, 1);
		
	}
	//���õ�һ��������
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
					throw new RuntimeException("��һ��������ͣʧ��");
				}
				System.out.println("��һ����������");
			}
		}, todayFirstListener.getTime(), MINUTEMILLIS * FIRSTLISTERNERHOUR);
	}
	
	
	//���õڶ���������
	public static void toDoSecondListener() {
		secondListener = new Timer();
		secondListener.schedule(new TimerTask() {
			
			@Override
			public void run() {
				canFistrListener = false;
				System.out.println("�ڶ����������� ");
				beginSecondListener();
			}
		}, todaySecondListener.getTime(), MINUTEMILLIS * SECONDLISTENERHOUR);	
	}
	
	//��ʼִ�еڶ���������������
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

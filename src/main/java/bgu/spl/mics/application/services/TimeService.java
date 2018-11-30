package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import java.util.Timer;
import java.util.TimerTask;


/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{
	private int speed;
	private int duration;
	private Timer timer;
	private int ticks;
	private final Object lockMain;
	public TimeService(String name,Object lockMain , int speed,int duration) {
		super(name);
		this.lockMain=lockMain;
		this.speed=speed;
		this.duration=duration;
		this.timer=new Timer();
		this.ticks=1;
	}

	@Override
	protected void initialize() {
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
                System.out.println("Global time :"+ticks);
				if(ticks<duration)
					sendBroadcast(new TickBroadcast(ticks));
				else {
					sendBroadcast(new TickBroadcast(ticks));
					sendBroadcast(new TerminationBroadcast());
					synchronized (lockMain) {
						terminate();
						timer.cancel();
						lockMain.notify();
					}
				}
				ticks++;
			}
		};


		timer.scheduleAtFixedRate(timerTask,0,speed);
	}

	public int getDuration() {
		return duration;
	}


	public int getSpeed() {
		return speed;
	}
}

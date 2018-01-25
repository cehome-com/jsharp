package jsharp.util;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class TimeCal {

    private int seconds;
    //private long  lastTime=0;
    private AtomicLong lastTime = new AtomicLong(0);
    private AtomicLong count=new AtomicLong(0);
    private long firstTime;

    public TimeCal(int seconds) {
        this(seconds, false);
    }

    public TimeCal(int seconds, boolean firstDelay) {
        this.seconds = seconds;
        this.firstTime=System.currentTimeMillis();
        if (firstDelay) {
            lastTime.set(System.currentTimeMillis());

        }
    }

    /**
     * thread safe
     * @return  is time up return run times, else return 0;
     */
    public  long isTimeUp() {
        long last = lastTime.get();
        long c=count.incrementAndGet();
        long time = (System.currentTimeMillis() - last) / 1000;
        if (time >= seconds) {
            if (lastTime.compareAndSet(last, System.currentTimeMillis())) {
                return c;
            }
        }
        return 0;
    }
    public  long getRunCount(){
        return count.get();
    }

    public static void main(String[] args) throws InterruptedException {
        final TimeCal timeCal=new TimeCal(5);
        final long t=System.currentTimeMillis();
        for(int i=0;i<20;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        long runTimes=timeCal.isTimeUp();

                        if (runTimes>0) {
                            long q= runTimes*1000/(System.currentTimeMillis()-t);
                            System.out.println(new Date().toString() + " time up. qps="+q);
                        }
                    }
                }
            }).start();
        }
        Thread.sleep(1000000);

    }

    public long getRunTime(){
        return System.currentTimeMillis()-firstTime;
    }
}
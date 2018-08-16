package stock;

import java.io.Serializable;

/** Klasa zarządzająca przedziałami czasowymi operacji kupna i sprzedaży */
public class Timer extends Thread implements Serializable {

    public static boolean stop = true;
    public static volatile boolean action = false;
    public static int sleepTime;
    public static int actionTime;
    public static Thread timerThread;

    public Timer(int actionTime, int sleepTime) {
        Timer.actionTime = actionTime;
        Timer.sleepTime = sleepTime;
        start();
    }

    @Override
    public void run() {
        super.run();
        Timer.timerThread = currentThread();

        while (true) {
            if (!Timer.stop) {

                action = true;
//                System.out.println("==========Action=========");

                for (InvestmentFund b : InvestmentFund.getFundList()){
                    b.wakeUp();
                }
                for (Investor i : Investor.getInvList()){
                    i.wakeUp();
                }

                waitExactTime(actionTime);

                if (!Timer.stop) {
                    action = false;
//                    System.out.println("==========Sleep=========");

                    for (Company c : Company.getList()){
                        c.wakeUpIncome();
                    }

                    waitExactTime(sleepTime);
                }
            } else {
                try {
                    synchronized (Timer.timerThread) {
                        wait();
                    }
                } catch (InterruptedException e) {}
            }
        }
    }

    private void waitExactTime(int exactTime) {
        try {
            sleep(exactTime);
        } catch (InterruptedException e) { }
    }

    public static void wakeUp() {
        Timer.timerThread.interrupt();
    }
}


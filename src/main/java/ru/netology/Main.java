package ru.netology;

public class Main {
    private static final int TIME_USER_WAIT = 3_000;
    private static final int TIME_TOY_WAIT = 100;
    private static final int USER_SWITCH_TOGGLE_COUNT = 5;

    private static volatile boolean isToggleSwitchedOn;

    public static void main(String[] args) throws InterruptedException {
        final Thread userThread = new Thread(getUserAction());
        userThread.start();

        final Thread toyThread = new Thread(getToyAction());
        toyThread.start();

        userThread.join();
        toyThread.interrupt();
    }

    private static Runnable getUserAction() {
        return () -> {
            for (int i = 0; i < USER_SWITCH_TOGGLE_COUNT; i++) {
                try {
                    isToggleSwitchedOn = true;
                    System.out.println("Тумблер включен");
                    Thread.sleep(TIME_USER_WAIT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @SuppressWarnings("BusyWait")
    private static Runnable getToyAction() {
        return () -> {
            while (true) {
                try {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Игрушка завершает работу");
                        return;
                    }
                    if (isToggleSwitchedOn) {
                        isToggleSwitchedOn = false;
                        System.out.println("Тумблер выключен");
                    }
                    Thread.sleep(TIME_TOY_WAIT);
                } catch (InterruptedException e) {
                    System.out.println("Игрушка завершает работу во время сна");
                    return;
                }
            }
        };
    }
}

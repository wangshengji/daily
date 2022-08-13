package com.wsj;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: wangsj
 * @Date: 2022/8/13 15:44
 */
public class OffWork {


    public static void main(String[] args) throws InterruptedException, IOException {

        String offWorkTime = "2022-08-13 17:45:00";

        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        AtomicBoolean run = new AtomicBoolean(true);
        scheduledThreadPoolExecutor.schedule(new OffWorkTask(scheduledThreadPoolExecutor, run, offWorkTime), 1, TimeUnit.SECONDS);

        while (run.get()) {Thread.sleep(0);}

        Runtime.getRuntime().exec("cmd /c mshta vbscript:msgbox(\"run...\",128,\"提示\")(window.close)");

        System.err.println("run ....");
        scheduledThreadPoolExecutor.shutdown();
        System.exit(0);
    }

    static class OffWorkTask implements Runnable {

        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

        AtomicBoolean flag;

        String time;

        public OffWorkTask(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, AtomicBoolean flag, String time) {
            this.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;
            this.flag = flag;
            this.time = time;
        }

        @Override
        public void run() {
            try {
                long cu = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long diff = simpleDateFormat.parse(time).getTime() - cu;
                if (diff <= 0) {
                    flag.set(false);
                }
                System.out.printf("当前时间: %s,  距离run: %s\n", simpleDateFormat.format(cu), getDiff(diff));
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                scheduledThreadPoolExecutor.schedule(this, 1, TimeUnit.SECONDS);
            }
        }

        private String getDiff(long diff) {
            long hour = diff / (60 * 60 * 1000) % 24;
            long minute = diff / (1000 * 60) % 60;
            long second = diff / 1000 % 60;
            return String.format("%d时%d分%d秒", hour, minute, second);
        }
    }

}

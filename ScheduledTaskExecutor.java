/*
 * Click `Run` to execute the snippet below!
 */

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.lang.Runnable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
/*
 * To execute Java, please define "static void main" on a class
 * named Solution.
 *
 * If you need more classes, simply define them inline.
 */

class Solution {
  private static class ScheduledTask extends Thread {
    // execute time in Milliseconds
    private long executeTime;
    private Runnable task;
    private String workingThreadName;
    
    ScheduledTask(long delay, Runnable task) {
      super(task);
      this.executeTime = System.currentTimeMillis() + delay;
      this.task = task;
    }
    
    public long getDelay() {
      return this.executeTime - System.currentTimeMillis();
    } 
    
    public void setWorkingThreadName(String workingThreadName) {
      this.workingThreadName = workingThreadName;
    }
    
    public void run() {
      System.out.println("Working Thread " + workingThreadName + "is working on this task!");
      task.run();
    }
  }
  
  private static class ScheduledTaskQueue {
    private final transient ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private Thread leader = null;
    
    private PriorityQueue<ScheduledTask> pq= new PriorityQueue<ScheduledTask>(11, Comparator.comparingLong(ScheduledTask::getDelay));
    ScheduledTaskQueue() {}
    
    public ScheduledTask peek() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            return pq.peek();
        } finally {
            lock.unlock();
        }
    }
      
    public boolean offer(ScheduledTask task) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            pq.offer(task);
            if (pq.peek() == task) {
                leader = null;
                condition.signal();
            }
            return true;
        } finally {
            lock.unlock();
        }
    }
    
    public ScheduledTask take() throws InterruptedException {
        final ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            for (;;) {
                ScheduledTask first = pq.peek();
                if (first == null)
                    condition.await();
                else {
                    long delay = first.getDelay();
                    if (delay <= 0)
                        return pq.poll();
                    first = null; // don't retain ref while waiting
                    if (leader != null)
                        condition.await();
                    else {
                        Thread thisThread = Thread.currentThread();
                        leader = thisThread;
                        try {
                          condition.awaitNanos(delay * 1000000);
                        } finally {
                            if (leader == thisThread)
                                leader = null;
                        }
                    }
                }
            }
        } finally {
            if (leader == null && pq.peek() != null)
                condition.signal();
            lock.unlock();
        }
    }
    
    public int size() {
      return pq.size();
    }
    
  }
  
  private static class MyThread extends Thread {
    public String name;
    public MyThread(String name) {
      this.name = name;
    }
  }
  
  private static ScheduledTaskQueue stq = new ScheduledTaskQueue();
  private static List<Thread> pool = new ArrayList<Thread>();
  
  public static void schedule(Runnable task, long delayMs) {
    ScheduledTask stk = new ScheduledTask(delayMs, task);
    stq.offer(stk);
  }
  
  public static void start(int poolSize) {
    for (int i = 0; i < poolSize; i++) {
    Thread workingThread = new MyThread("Thread" + i){
      public void run() {
                // System.out.println("Working thread: " + this.name + "started!");
        while(true) {
          try {       
            ScheduledTask stk = stq.take();
            stk.setWorkingThreadName("Working thread: " + this.name);
            stk.start();
          } catch (InterruptedException e) {
            return;
          }
        }
      }
      };
      pool.add(workingThread);
      workingThread.start();
    }
  }
  
  public static void shutdownNow() {
    for (int i = 0; i < pool.size(); i++) {
    Thread workingThread = pool.get(i);
      workingThread.interrupt();
    }
  }
  
  // private class 
  
  public static void main(String[] args) throws InterruptedException {
    Runnable thread1 = new Thread(() -> {System.out.println("Task 1 starting time" + System.currentTimeMillis());});
    Runnable thread2 = new Thread(() -> {System.out.println("Task 2 starting time" + System.currentTimeMillis());});
    Runnable thread3 = new Thread(() -> {System.out.println("Task 3 starting time" + System.currentTimeMillis());});
    
    schedule(thread1, 10000);
    schedule(thread2, 3000);
    schedule(thread3, 0);
    
    System.out.println(stq.size());
    
    
    start(4);
    try {
      Thread.sleep(20000);
    } catch (InterruptedException e) {}
    shutdownNow();
    System.out.println("Program ended!");
    
    
    
    
    // ScheduledTask sTask1 = new ScheduledTask(10000, thread1);
    // System.out.println("Current Time:" + System.currentTimeMillis());
    // sTask1.start();
//     ArrayList<String> strings = new ArrayList<String>();
//     strings.add("Hello, World!");
//     strings.add("Welcome to CoderPad.");
//     strings.add("This pad is running Java " + Runtime.version().feature());

//     for (String string : strings) {
//       System.out.println(string);
//     }
  }
}

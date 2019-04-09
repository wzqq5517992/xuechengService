/**
  * @Title: SyncThread.java 
  * @Package sy 
  * @Description: TODO(用一句话描述该文件做什么) 
  * @author fsjohnhuang
  * @date 2019年4月7日 下午11:40:40 
  * @version V1.0   
  */
package java.com.duoxiancheng;

/**
 * @Package sy 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Chen er
 * @date 2019年4月7日 下午11:40:40 
 */
public class SyncThread implements Runnable {

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        // TODO Auto-generated method stub
        if(threadName.startsWith("a")) {
            async();//异步
        }else if(threadName.startsWith("b")) {
            syncObjectBlock1();//同步代码块(当前对象锁)
        }else if(threadName.startsWith("c")) {
            syncObjectMethod1();//同步方法(非静态方法)
        }else if(threadName.startsWith("d")) {
            syncObjectBlock2();//同步代码块(类锁)
        }else if(threadName.startsWith("e")) {
            syncObjectMethod2();//同步方法(静态方法)
        }
        
    }
    
    /** 异步方法 */
    private void async() {
        try {
            System.out.println(Thread.currentThread().getName() + "async_start" + System.currentTimeMillis());
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + "async_end" + System.currentTimeMillis());
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /** 修饰同步代码块(当前对象this锁) */
    private void syncObjectBlock1() {
        System.out.println("syncObjectBlock1");
        synchronized (this) {
            try {
                System.out.println(Thread.currentThread().getName() + "syncObjectBlock1_start" + System.currentTimeMillis());
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "syncObjectBlock1_end" + System.currentTimeMillis());
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
    
    /** 修饰同步代码块(当前对象类锁) */
    private void syncObjectBlock2() {
        System.out.println("syncObjectBlock2");
        synchronized (SyncThread.class) {
            try {
                System.out.println(Thread.currentThread().getName() + "syncObjectBlock1_start" + System.currentTimeMillis());
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "syncObjectBlock1_end" + System.currentTimeMillis());
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
    
    /** 修饰非静态方法 */
    private synchronized void syncObjectMethod1() {
        System.out.println("syncObjectMethod1");
            try {
                System.out.println(Thread.currentThread().getName() + "syncObjectMethod1_start" + System.currentTimeMillis());
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "syncObjectMethod1_end" + System.currentTimeMillis());
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
    }
    
    /** 修饰静态方法 */
    private synchronized static void syncObjectMethod2() {
        System.out.println("syncObjectMethod2");
            try {
                System.out.println(Thread.currentThread().getName() + "syncObjectMethod1_start" + System.currentTimeMillis());
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "syncObjectMethod1_end" + System.currentTimeMillis());
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
    }

}

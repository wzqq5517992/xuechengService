/**
  * @Title: TestSync.java 
  * @Package sy 
  * @Description: TODO(用一句话描述该文件做什么) 
  * @author fsjohnhuang
  * @date 2019年4月7日 下午11:51:10 
  * @version V1.0   
  */
package java.com.duoxiancheng;

/**
 * @Package sy 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Chen er
 * @date 2019年4月7日 下午11:51:10 
 */
public class TestSync {
    public static void main(String[] args) {
        SyncThread syncThread = new SyncThread();
        Thread aThread1 = new Thread(syncThread,"aThread1");
        Thread aThread2 = new Thread(syncThread,"aThread2");
        Thread bThread1 = new Thread(syncThread,"bThread1");
        Thread bThread2 = new Thread(syncThread,"bThread2");
        Thread cThread1 = new Thread(syncThread,"cThread1");
        Thread cThread2 = new Thread(syncThread,"cThread2");
//        Thread dThread1 = new Thread(syncThread,"dThread1");
//        Thread dThread2 = new Thread(syncThread,"dThread2");
//        Thread eThread1 = new Thread(new SyncThread(),"eThread1");
//        Thread eThread2 = new Thread(new SyncThread(),"eThread2");
        aThread1.start();
        aThread2.start();
        bThread1.start();
        bThread2.start();
        cThread1.start();
        cThread2.start();
//        dThread1.start();
//        dThread2.start();
//        eThread1.start();
//        eThread2.start();
    }
}

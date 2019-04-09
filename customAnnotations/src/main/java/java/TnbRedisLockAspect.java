package java;

import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TnbRedisLockAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisUtil redisUtil;

    @Pointcut("@annotation(TnbRedisLock)")

    @TnbRedisLock

    public void cut() {
    }


    @Around("cut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object object = null;
        try {
            String keyName = "";
            //取方法入参的第一个参数作为redis_key
            Object[] args = joinPoint.getArgs();
            if (args != null) {
                keyName = args[0].toString();
            }
            boolean isLock = redisUtil.syncLock(keyName);
            if (isLock) {

                try {
                    //执行方法逻辑
                    object = joinPoint.proceed();
                } catch (Exception e) {
                    logger.error("", e);
                } finally {
                    redisUtil.unLock(keyName);
                }

            } else {
                // 设置失败次数计数器, 当到达10次时, 返回失败
                int failCount = 1;
                while (failCount <= 5) {
                    try {
                        Thread.sleep(100);
                        if (redisUtil.syncLock(keyName)) {
                            try {
                                //执行方法逻辑
                                object = joinPoint.proceed();
                            } catch (Exception e) {
                                logger.error("", e);
                            } finally {
                                redisUtil.unLock(keyName);
                            }
                            break;
                        } else {
                            failCount++;
                            if (failCount > 5) {
                                throw new RuntimeException("操作频繁, 请稍后再试");
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("", e);
          // object = new Object(cats.error);
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_NUll);
        }
        if (object == null) {
           // object = new ResultInfo<>(ErrorInfoEnums.error);
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_NUll);

        }

        return object;
    }

}

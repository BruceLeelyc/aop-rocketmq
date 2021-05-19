package com.example.demo.aop;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.aop.config.ErrorAnnotation;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @ClassName: AspectService
 * @Description:
 * @Author: lixl
 * @Date: 2021/4/29 17:33
 */
@Aspect
@Component
public class OperationLogAspect {

    Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    /**
     * 定义切点 @Pointcut,在注解的位置切入代码
     */
    @Pointcut("@annotation(com.example.demo.aop.config.ErrorAnnotation)")
    public void errorAspect() {}

    @Pointcut("@annotation(com.example.demo.aop.config.LoginfoAnnotation)")
    public void loginfoAspect() {}

    //定义切点函数,execution也支持模糊匹配 如execution(* com.example.demo.aop.ErrorServiceImpl.*(**))
    //切点函数参数列表类型与代理对象函数参数列表相同，这是为了方便使用参数
    @Pointcut(value = "execution(* com.example.demo..*.*(..))")
    public void point(){}

    //函数抛出异常后调用，参数e获取抛出的异常
    @AfterThrowing(pointcut = "errorAspect()",throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        // 读取session中的信息
        // session.getAttribute("xxx");

        try {
            logger.info("请求方法={}.{}", joinPoint.getTarget().getClass().getName(), joinPoint.getSignature().getName());
            logger.info("方法描述={}", getControllerMethodDescription(joinPoint));

            JSONObject jsonParams = new JSONObject();
            if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0){
                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                    Object obj = joinPoint.getArgs()[i];
                    System.out.println();
                    jsonParams.putAll(this.getJsonParams(obj));
                }
            }

            // 保存数据库
            // service.insert(xxx);
        }catch (Exception ee){
            logger.error("异常:{},{}", ee.getMessage(), ee);
        }
    }

    @AfterThrowing(pointcut = "loginfoAspect()", throwing = "e")
    public void aop(Exception e) {
        System.out.println("异常拦截 throw II:"+e);
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * @param joinPoint
     * @return
     * @throws Exception
     */
    public static String getControllerMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();//目标方法名
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method:methods) {
            if (method.getName().equals(methodName)){
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length==arguments.length){
                    description = method.getAnnotation(ErrorAnnotation.class).value();
                    break;
                }
            }
        }
        return description;
    }

    /**
     * 拼装请求参数
     * @param obj
     * @return
     * @throws Exception
     */
    private JSONObject getJsonParams(Object obj) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            boolean wrapClass = isWrapClass(obj);
            if (wrapClass) {
                logger.info("非基本类型返回.");
                return jsonObject;
            }
            Class aClass = (Class) obj.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field f : declaredFields) {
                String name = f.getName();
                Field declaredField = aClass.getDeclaredField(name);
                declaredField.setAccessible(true);
                PropertyDescriptor pd = new PropertyDescriptor(declaredField.getName(), aClass);
                Method readMethod = pd.getReadMethod();
                Object val = readMethod.invoke(obj);
                if (null != val && StringUtils.isNotBlank(val.toString())) {
                    jsonObject.put(name, val);
                    logger.info(String.valueOf(val));
                }
            }
        } catch (Exception e) {
            logger.error("转换参数异常.", e );
        }
        return jsonObject;
    }


    public static boolean isWrapClass(Object obj) {
        try {
            if(obj.getClass().getName().equals("java.lang.String")) {
                return true;
            }
            return ((Class<?>)obj.getClass().getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }
}

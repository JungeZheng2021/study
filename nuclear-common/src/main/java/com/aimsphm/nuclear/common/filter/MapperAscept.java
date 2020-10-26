package com.aimsphm.nuclear.common.filter;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.aimsphm.nuclear.common.constant.AuthConstant;
import com.aimsphm.nuclear.common.token.TokenAuthenticationService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aimsphm.nuclear.common.entity.ModelBase;
import com.aimsphm.nuclear.common.redis.RedisClient;

@Aspect
@Component
public class MapperAscept {
//	@Autowired
//	RedisClient redisClient;

    @Pointcut("execution(* com.aimsphm.nuclear.*.mapper.*.update*(..)) || execution(* com.aimsphm.nuclear.*.service.*.update*(..)) && !execution(* com.aimsphm.nuclear.common.mapper.BizAutoIncrMapper.update*(..))")
    public void daoUpdate() {
    }

    @Pointcut("execution(* com.aimsphm.nuclear.*.mapper.*.insert*(..)) || execution(* com.aimsphm.nuclear.*.service.*.save*(..))  && !execution(* com.aimsphm.nuclear.common.mapper.BizAutoIncrMapper.insert*(..))")
    public void daoCreate() {
    }

    @Before("daoCreate()")
    public void createBefore(JoinPoint joinPoint) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (!ObjectUtils.isEmpty(attributes)) {
            String userName = getUserName(attributes);

            Object[] args = joinPoint.getArgs();
            aopArgs(userName, args, joinPoint);
        } else {
            String userName = "period-algorithm";
            Object[] args = joinPoint.getArgs();
            aopArgs(userName, args, joinPoint);
        }
    }

    public String getUserName(ServletRequestAttributes attributes) {
        String userName = "";
        String authorization = attributes.getRequest().getHeader(AuthConstant.AUTH_HEADER_STRING);
        if (authorization == null) {
            authorization = "";
            userName = "period-algorithm";
        } else {
            if (attributes.getRequest() != null) {
                userName = TokenAuthenticationService.getUserName(attributes.getRequest());
            }
        }
        return userName;
    }

    @Before("daoUpdate()")
    public void updateBefore(JoinPoint joinPoint) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String userName = "";
        if (!ObjectUtils.isEmpty(attributes)) {
             userName = getUserName(attributes);
            Object[] args = joinPoint.getArgs();
            aopArgs(userName, args, joinPoint);
        } else {
            userName = "period-algorithm";
            Object[] args = joinPoint.getArgs();
            aopArgs(userName, args, joinPoint);
        }
    }

    private void aopArgs(String userName, Object[] args, JoinPoint jp) {
        for (Object arg : args) {
            if (args.length == 1 && arg instanceof List) {
                boolean isModelBaseList = false;

                isModelBaseList = isModelBaseList(jp, isModelBaseList);
                if(!isModelBaseList)
                {
                    Object obj =   ((List)args[0]).stream().findAny().orElse(null);
                    if(obj instanceof ModelBase)
                    {
                        isModelBaseList = true;
                    }
                }
                if (isModelBaseList) {
                    List<ModelBase> modelBases = (List<ModelBase>) arg;
                    for (ModelBase modelBase : modelBases) {
                        if (modelBase.getCreateOn() == null) {
                            modelBase.setCreateOn(new Date());
                        }
                        if (modelBase.getCreateBy() == null) {
                            modelBase.setCreateBy(userName);
                        }
                        modelBase.setLastUpdateOn(new Date());
                        modelBase.setLastUpdateBy(userName);
                    }
                }
            } else {
                if (arg instanceof ModelBase) {
                    ModelBase modelBase = (ModelBase) arg;
                    if (modelBase.getCreateOn() == null) {
                        modelBase.setCreateOn(new Date());
                    }
                    if (modelBase.getCreateBy() == null) {
                        modelBase.setCreateBy(userName);
                    }
                    modelBase.setLastUpdateOn(new Date());
                    modelBase.setLastUpdateBy(userName);
                }
            }
        }
    }

    private boolean isModelBaseList(JoinPoint jp, boolean isModelBaseList) {
        if ((jp.getSignature() instanceof MethodSignature)) {
            MethodSignature methodSign = (MethodSignature) jp.getSignature();
            Method method = methodSign.getMethod();

            Type[] parameters = method.getGenericParameterTypes();// 获取参数，可能是多个，所以是数组

            for (Type type : parameters) {
                if (type instanceof ParameterizedType)// 判断获取的类型是否是参数类型
                {
                    Type[] typetwos = ((ParameterizedType) type).getActualTypeArguments();
                    if (typetwos.length == 1) {
                        Class<?> strClass = null;
                        try {
                            strClass = Class.forName(typetwos[0].toString().replace("class", "").trim());
                            if (strClass.getSuperclass().equals(ModelBase.class)) {
                                isModelBaseList = true;
                            }
                        } catch (ClassNotFoundException e) {
                            isModelBaseList = false;
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
        return isModelBaseList;
    }


}

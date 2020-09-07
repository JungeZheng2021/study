package com.study.auth.config;

import com.study.auth.comm.ResponseData;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package: com.study.auth.config.core
 * @Description: <增强controller层返回值>
 * @Author: milla
 * @CreateDate: 2020/09/04 14:42
 * @UpdateUser: milla
 * @UpdateDate: 2020/09/04 14:42
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Configuration
public class InitializingAdviceDecorator implements InitializingBean {
    @Autowired
    private RequestMappingHandlerAdapter adapter;

    @Override
    public void afterPropertiesSet() {
        //获取所有的handler对象
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        //因为上面返回的是unmodifiableList，所以需要新建list处理
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList(returnValueHandlers);
        this.decorateHandlers(handlers);
        //将增强的返回值回写回去
        adapter.setReturnValueHandlers(handlers);
    }


    /**
     * 使用自定义的返回值控制类
     *
     * @param handlers
     */
    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                //找到返回值的handler并将起包装成自定义的handler
                ControllerReturnValueHandler decorator = new ControllerReturnValueHandler((RequestResponseBodyMethodProcessor) handler);
                int index = handlers.indexOf(handler);
                handlers.set(index, decorator);
                break;
            }
        }
    }

    /**
     * 自定义返回值的Handler
     * 采用装饰者模式
     */
    private class ControllerReturnValueHandler implements HandlerMethodReturnValueHandler {
        //持有一个被装饰者对象
        private HandlerMethodReturnValueHandler handler;

        ControllerReturnValueHandler(RequestResponseBodyMethodProcessor handler) {
            this.handler = handler;
        }

        @Override
        public boolean supportsReturnType(MethodParameter returnType) {
            return true;
        }

        /**
         * 增强被装饰者的功能
         *
         * @param returnValue  返回值
         * @param returnType   返回类型
         * @param mavContainer view
         * @param webRequest   请求对象
         * @throws Exception 抛出异常
         */
        @Override
        public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
            //如果已经封装了结构体就直接放行
            if (returnValue instanceof ResponseData) {
                handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
                return;
            }
            //正常返回success
            ResponseData success = ResponseData.success(returnValue);
            handler.handleReturnValue(success, returnType, mavContainer, webRequest);
        }
    }
}

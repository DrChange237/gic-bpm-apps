package com.change.gic.config;

import com.change.gic.constant.FeignHeader;
import feign.Headers;
import feign.MethodMetadata;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class FeignClientContract extends SpringMvcContract {

    @Override
    protected void processAnnotationOnClass(MethodMetadata data, Class<?> clazz) {
        super.processAnnotationOnClass(data, clazz);
        for(Annotation annotation : clazz.getAnnotations()) {
            if (annotation instanceof Headers) {
                data.template().headers(toMap(((Headers) annotation).value()));
                break;
            }

            if(annotation instanceof FeignClient) {
                data.template().header(FeignHeader.TARGET_SERVICE_NAME, ((FeignClient) annotation).name());
            }
        }
    }

    @Override
    protected void processAnnotationOnMethod(MethodMetadata data, Annotation methodAnnotation, Method method) {
        super.processAnnotationOnMethod(data, methodAnnotation, method);
        if (methodAnnotation instanceof Headers) {
            data.template().headers(toMap(((Headers) methodAnnotation).value()));
        }
    }

    private static Map<String, Collection<String>> toMap(String[] input) {
        Map<String, Collection<String>> result = new LinkedHashMap<>(input.length);
        for (String header : input) {
            int colon = header.indexOf(':');
            String name = header.substring(0, colon);
            if (!result.containsKey(name)) {
                result.put(name, new ArrayList<>(1));
            }
            result.get(name).add(header.substring(colon + 1).trim());
        }
        return result;
    }
}

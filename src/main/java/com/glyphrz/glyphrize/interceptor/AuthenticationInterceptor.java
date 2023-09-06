package com.glyphrz.glyphrize.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.glyphrz.glyphrize.annotation.PassToken;
import com.glyphrz.glyphrize.annotation.UserLoginToken;
import com.glyphrz.glyphrize.model.User;
import com.glyphrz.glyphrize.repository.UserRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;


public class AuthenticationInterceptor implements HandlerInterceptor {
    @Resource
    UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        //检查是否映射到方法，无则不拦截
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        //获取对象方法
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //方法有PassToken注解，则不拦截
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //方法有UserLoginToken注解，则需要对token进行校验
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                Cookie[] cookies = request.getCookies();
                if (cookies == null) {
                    System.out.println("无token，需要登录");
                    response.setStatus(403);
                    return false;
                }
                String token = cookies[0].getValue();
                String userName = JWT.decode(token).getAudience().get(0);
                System.out.printf("\033[31m%s 正在尝试鉴权\n\033[0m", userName);
                User user = userRepository.findByName(userName);
                if (user == null) {
                    System.out.println("用户不存在，需要登录");
                    response.setStatus(403);
                    return false;
                }
                try {
                    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException j) {
                    System.out.println("token验证失败，需要登录");
                    response.setStatus(403);
                    return false;
                }
                System.out.printf("%s 通过验证\n", userName);
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}

package com.apriverse.glyphz.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.apriverse.glyphz.annotation.PassToken;
import com.apriverse.glyphz.annotation.UserLoginToken;
import com.apriverse.glyphz.model.User;
import com.apriverse.glyphz.repository.UserRepository;
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
                //Cookies为空，代表无token
                if (cookies == null) {
                    System.out.println("无token，需要登录");
                    response.setStatus(403);
                    return false;
                }
                //解析token中的userName信息
                String token = cookies[0].getValue();
                String userName = JWT.decode(token).getAudience().get(0);
                System.out.printf("\033[31m%s 正在尝试鉴权\n\033[0m", userName);
                //查询用户是否存在
                User user = userRepository.findByName(userName);
                if (user == null) {
                    System.out.println("用户不存在，需要登录");
                    response.setStatus(403);
                    return false;
                }
                //对JWT签名进行验证
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

    //重写其它两个方法
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}

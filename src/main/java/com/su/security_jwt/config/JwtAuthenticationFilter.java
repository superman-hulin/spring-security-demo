package com.su.security_jwt.config;

import com.su.security_jwt.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: security_jwt
 * @description: 验证请求中token的jwt过滤器
 * @author: Mr.Wang
 * @create: 2020-05-03 09:35
 **/
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private MyUserDetailService myuserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader=request.getHeader("Authorization");
        if(authHeader!=null&&authHeader.startsWith("Bearer")){
            final String authToken=authHeader.substring("Bearer".length()+1);
            String userName=jwtTokenUtil.getUserNameFromToken(authToken);
            // 服务重启或单点登录进入（有token无session情况）
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = myuserDetailsService.loadUserByUsername(userName);
                // 如果token有效
                if (jwtTokenUtil.validateToken(authToken, userDetails)){
                    // request中放入用户名
                    request.setAttribute("username",userName);
                    request.setAttribute("token", authToken);
                    // 鉴权通过
                    UsernamePasswordAuthenticationToken authentication
                            = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        // 放行
        filterChain.doFilter(request,response);
    }
}

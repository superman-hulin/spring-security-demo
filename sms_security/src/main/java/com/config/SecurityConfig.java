package com.config;

import com.filter.SmsCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @program: image_security
 * @description: spring security的配置类
 *      短信验证码验证的实现
 *          我们仿照用户密码登录请求的验证流程来写我们的短信登录请求的验证，首先写一个SmsAuthenticationFilter（相当于UsernamePasswordAuthenticationFilter），
 *          来拦截短信登录请求，然后拿到请求中的手机号并封装成SmsAuthenticationToken（相当于UsernamePasswordAuthenticationToken）（未认证的），然后这个token也会
 *          传给AuthenticationManager（这个整个系统中只有一个，我们不用自己写），我们需要额外自己写一个SmsAuthenticationProvider来处理这个token（校验token
 *          中的信息），在这个校验过程中，依然调用UserDetailService，然后将手机号传给它，让它去读用户的信息并判断是否认证成功，
 *          上一部分是只验证用户的手机号信息，而不会去验证验证码是否正确（就像图形验证码的验证是在UsernamePasswordAuthenticationFilter之前单独写了个过滤器去验证验证码），
 *          所以在SmsAuthenticationFilter之前也要写一个这样的过滤器。只有这样把验证码验证的功能单独写出来，其它的需要验证码验证的模板也能共用（如支付的时候）
 * @author: Su
 * @create: 2020-08-22 09:19
 **/
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SmsCodeFilter smsCodeFilter;
    @Autowired
    private SecurityPropertiesConfig securityPropertiesConfig;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
         http.addFilterBefore(smsCodeFilter,UsernamePasswordAuthenticationFilter.class) //将图形验证码过滤器设置到前面
                .formLogin().loginPage("/authentication/require") // 代表用表单来身份认证  也可以用http.httpBasic（）的方式来身份认证（也就是 不是表单形式）
                .loginProcessingUrl("/authentication/mobile")
                .and()
                .authorizeRequests() //代表下面都是授权的配置
                .antMatchers(
                  "/authentication/require",securityPropertiesConfig.getLoginPage()
                ,"/code/*","/login.html","/register").permitAll()//这是代表这些请求无需登录
             .antMatchers(HttpMethod.GET,"/user/*").hasRole("ADMIN")//这是代表该请求不仅看是否登录还要看该用户是不是有ADMIN权限
             //权限是在userDetailService中给用户
                .anyRequest() //代表除了上面之外的任何请求
                .authenticated()
                 .and()
                .csrf().disable()
                 .apply(smsCodeAuthenticationSecurityConfig);;

    }


}

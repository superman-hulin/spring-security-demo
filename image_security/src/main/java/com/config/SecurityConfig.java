package com.config;

import com.filter.ImageCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 *    @EnableWebSecurity
 *                      用于开启spring security。为某些请求路径开启安全认证
 *                      引入WebSecurityConfiguration配置类 在这个配置类中 注入了 springSecurityFilterChain，是Spring Secuity的核心过滤器, 是请求的认证入口
 *      引入@EnableGlobalAuthentication注解
 *                        引入AuthenticationConfiguration配置类配置认证相关的核心类
 *                        该类向spring容器中注入AuthenticationManagerBuilder, 其实是使用了建造者模式, 他能建造AuthenticationManager, 是身份认证的入口
 *    @EnableGlobalMethodSecurity
 *                      Spring Security默认是禁用注解（即在controller请求方法上加过滤注解）的，通过该注解进行开启
 *            prePostEnabled=true 代表开启以下注解
 *                      @PreAuthorize 在方法调用之前, 基于表达式的计算结果来限制对方法的访问
 *                      @PostAuthorize 允许方法调用, 但是如果表达式计算结果为false, 将抛出一个安全性异常
 *                      @PostFilter 允许方法调用, 但必须按照表达式来过滤方法的结果
 *                      @PreFilter 允许方法调用, 但必须在进入方法之前过滤输入值
 *    认证处理流程说明
 *          登录请求、SecurityContextPersistenceFilter、UsernamePasswordAuthenticationFilter（将用户名和密码和请求信息封装到token）、
 *          AuthenticationManager（管理和收集provider并且选出能处理该token类型的provider（DaoAuthenticationProvider），会有多种token的）、
 *          AuthenticationProvider（加载我们自定义的UserDetailServie对象来获取UserDetails，并且对该UserDetails里面的信息作相应的检查，检查成功后会创建一个认证过的Authentication）、
 *          然后就开始走成功认证之后的处理（我们也自定义了验证成功后的处理），如果上述步骤出现了一个异常则走失败处理
 *
 *   认证结果如何在多个请求之间共享
 *          认证成功的Authentication在调用验证成功后的处理之前把成功的Authentication放到了SecurityContext以及放到了SecurityContextHolder（线程）中。
 *          请求先进入SecurityContextPersistenceFilter,检查session中是否有SecurityContext，如果有则把它放到线程中，如果没有则进入下一个
 *          过滤器链（UsernamePasswordAuthenticationFilter），当响应出去的时候，最后也是要经过SecurityContextPersistenceFilter
 *          这个时候检查线程中是否有SecurityContext（请求和响应是同一个线程），如果有的话，则放回session。这样不同请求之间就可以共享
 *   获取认证用户信息
 *          认证的用户信息放到了SecurityContextHolder中，我们可以直接使用SecurityContextHolder.getContext().getAuthentication()来获取包含了用户信息的对象
 *
  *  实现记住我功能的基本原理
  *         浏览器认证请求发给UsernamePasswordAuthenticationFilter，认证成功后会调用RemeberMeService(这个服务中有个TokenRepository)，
  *         这个服务会将token写入浏览器的cookie中，同时利用TokenRepository将token（用户名也写入，和token一一对应）写入到数据库中
  *         后面用户再次服务请求，会经过一个RememberMeAuthenticationFilter，这个过滤器的作用是读取cookie中的token然后交给RemeberMeService，
  *         RemeberMeService再去数据库中查找该token，如果有的话则取出对应的用户名，然后拿着用户名去调用我们的UserDetailService，然后获取用户的信息
  *         然后将用户信息放到当前的SecurityContext中。
  *
 * @author: Su
 * @create: 2020-08-22 09:19
 **/
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AuthenticationSuccessHandler myAuthenticationSuccessHandler; //注入自己重写的成功验证后的处理
    @Autowired
    private AuthenticationFailureHandler myAuthenticationFailureHandler;
    @Autowired
    @Qualifier("myUserDetailsService")
    private UserDetailsService myUserDetailsService;
    @Autowired
    private ImageCodeFilter imageCodeFilter;
    @Autowired
    private SecurityPropertiesConfig securityProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
         http.addFilterBefore(imageCodeFilter,UsernamePasswordAuthenticationFilter.class) //将图形验证码过滤器设置到前面
                .formLogin().loginPage("/authentication/require") // 代表用表单来身份认证  也可以用http.httpBasic（）的方式来身份认证（也就是 不是表单形式）
                .loginProcessingUrl("/userlogin")
                .successHandler(myAuthenticationSuccessHandler) //自定义验证成功后的处理
                .failureHandler(myAuthenticationFailureHandler)
                 .and()
                .userDetailsService(myUserDetailsService)//配置我们自定义的UserDetailService
                .authorizeRequests() //代表下面都是授权的配置
                .antMatchers(
                  "/authentication/require",securityProperties.getLoginPage()
                ,"/code/*","/register.html","/register").permitAll()//这是代表这些请求无需登录
             .antMatchers(HttpMethod.GET,"/user/*").hasRole("ADMIN")//这是代表该请求不仅看是否登录还要看该用户是不是有ADMIN权限
             //权限是在userDetailService中给用户
                .anyRequest() //代表除了上面之外的任何请求
                .authenticated()
                 .and()
                .csrf().disable();

    }


}

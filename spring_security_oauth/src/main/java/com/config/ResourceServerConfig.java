package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * @program: restfulrcud
 * @description: spring security oauth中的资源服务器
 *      @Configuration
 *      @EnableResourceServer//一旦写了该注解，该项目就是一个资源服务器
 *      public class ResourceServerConfig {
 *
 *      }
 *  现在想要访问该项目的rest服务，就需要先去认证服务器拿到该用户的token，然后再在请求的请求头中拿着acess_token和token_type的值去访问rest服务
  * 通过两个注解使该项目成为认证服务器和资源服务器，用户请求rest服务，不再是通过session和cookie的方式去验证了，而是通过token。
  * 但是仅有这两个注解，只是实现了标准的token认证机制（4种授权模式下去拿token），比如只能用授权模式来生成token（比如无法通过输入手机号登录时，服务器向其发放token），
  * 生成token的方式是默认的，存储token的方式是放在内存中。
  * 所以我们需要自定义一些东西，来改变上面的默认。
  *
  * Spring Security Oauth核心源码
  * 获取令牌的请求（/oauth/token）被TokenEndpoint类（相当于一个controller）处理，它会调用ClientDetailService接口（实现类是InMemory..，用来读取第三方应用的信息，因为在请求的头上都会携带第三方应用的信息）
  * 这个接口有点像之前的userDetailService，该接口根据clientId去读第三方信息相关信息封装成ClientDeatails，然后TokenRequest类会将ClientDeatails的信息和用户请求所传参数的信息一起封装，TokenRequest会去调用
  * TokenGranter接口（实现类是Composite..,该接口封装了四种授权模式不同的实现，根据请求所传参数grant_type来确定选择哪种授权模式的实现类来完成令牌生成）,生成令牌的过程中会产生OAuth2Request对象（该对象其实就是ClientDeatails和TokenRequest的信息整合）
  * 和Authentication接口（封装了当前授权用户的一些信息，这些信息是通过UserDetailService得到的），然后这两个对象会组合成OAuth2Authentication对象（包含了哪个第三方应用在请求哪个用户在授权、授权模式是什么、其它信息），
  * 这个对象会传给一个AuthorizationServerTokenServices接口的实现（DefaultTokenServices，该实现类里面还包含TokenStore（用于令牌的存取）和TokenEnhancer（令牌增强器，也就是想往令牌加什么东西）），最后会生成一个OAuth2AccessToken类型的令牌
  *
  * 重构登录方式
  * 根据spring security oauth的思想，我们只需要用它的AuthorizationServerTokenServices接口部分来生成token，而前面的从TokenEndpoint类到TokenGranter接口都是处理获取令牌的请求的，而我们是登录请求，所以将这部分流程换成登录请求被过滤器拦截，然后进行登录逻辑处理
  * 最后执行AuthenticationSuccessHandler，然后AuthenticationSuccessHandler生成OAuth2Request对象（该对象的构建还是和上面保持一致）和Authentication（登录逻辑处理中本来就会为我们封装出这个），最后的流程就和上面一样。
  *
  * 采用用户名密码登录方式获取token的流程：
  * 通过postman工具向表单登录所请求的url（userlogin）发post请求，请求头中带上clientId和clientSec，body带上用户名和密码。
  * 然后就可以拿到token
  *
  * 采用短信验证码登录方式
  * 先通过postman工具向发验证码的请求的url发get请求，拿到验证码，然后再对登录请求发post请求，请求头携带clientId信息，
  * body携带手机号和验证码的信息，就可以获取到token，但是之前短信验证码是存储在session中，现在令牌模式不用session，所以需要先解决验证码的存储问题。
  * 可以将验证码的存储放到redis中，发请求时在请求头中再带上设备id，以设备id为key，验证码为value存到redis中，后面验证码检验的请求中依然带上设备号。
  *
  * 上面的重构登录方式就是我们用自定义的认证替换掉了默认下的4种授权模式从而去拿token，而token的生成和存储依然是默认的，我们依然可以自定义
  * token的处理是在认证服务器中完成的，所以我们要去改下认证服务器配置类AuthticationServerConfig(这个类默认下只有一个注解就好)，
  *
 * @author: Mr.Wang
 * @create: 2020-03-03 14:16
 **/


//我们重构好登录成功后的处理之后，需要对资源服务器作一些配置
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
    @Autowired
    private AuthenticationSuccessHandler tokenMyAuthenticationSuccessHandler; //注入自己重写的成功验证后的处理
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();   //这个对象就是PasswordEncoder的实现类，用于我们完成密码加密和匹配密码
    }
    @Override
    public void configure(HttpSecurity http) throws Exception {
                http.formLogin()
                .successHandler(tokenMyAuthenticationSuccessHandler) //自定义验证成功后的处理
                .and()
                .authorizeRequests() //代表下面都是授权的配置
                .antMatchers("/authentication/require","login.html"
                        ,"/code/*").permitAll()
                .antMatchers("/register.html").permitAll()
                .antMatchers("/register").permitAll()
                .anyRequest() //代表任何请求
                .authenticated() //代表都需要身份认证
                .and()
                .csrf().disable();

    }
}


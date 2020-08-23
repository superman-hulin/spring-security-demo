package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * @program: restfulrcud
 * @description: social的配置类
 *  OAuth协议简介
 * 1.OAuth协议要解决的问题
 *    假设现在有个应用需要获取微信上用户的自拍数据，很明显微信只要在用户授权给该应用后才能被拿走自拍数据，一种原始的做法是将用户名和密码给这个应用，
 * 这个应用就可以拿到微信数据了，但是存在的问题有（应用可以访问微信所有的数据，用户只有修改密码才能收回授权）。
 * 所以一种好的做法是微信提供一个令牌（该令牌相当于一个token），这样就不需要用户的密码了，并且这个令牌只能访问特定的数据和令牌有时间限制。
 * OAuth协议就是一种授权协议，在第三方应用不拿到用户的密码的前提下，能够被授权访问服务提供商。
 * 2.OAuth协议中的各种角色
 *   服务提供商（provider）：提供令牌
 *   认证服务器（存在于服务提供商）：认证用户的身份并且产生令牌
 *   资源服务器（存在于服务提供商）：保存用户的资源和验证令牌（第三方应用拿到认证服务器给的令牌后拿用户的数据是发请求发往资源服务器的）
 *   资源所有者：用户
 *   第三方应用（Client）：将该用户变成自己应用的用户（本来是服务提供商的用户）
 * 3.OAuth协议运行流程
 *   首先用户访问第三方应用，然后第三方应用请求用户授权（授权第三方应用可以去服务提供商拿数据），如果用户同意授权，则第三方应用向认证服务器申请令牌（认证服务器会认证用户是否真的同意授权了）
 * 如果认证成功的话，则发放令牌，然后第三方应用拿到令牌后会向资源服务器申请资源，则资源服务器会验证该令牌，当没问题后则开放资源给第三方应用。
 * OAuth协议中的用户授权模式：授权码模式（市面上几乎都是这种）、密码模式、客户端模式和简化模式
 * 授权码模式：用户访问第三方应用，然后第三方应用将用户导向认证服务器，然后用户在认证服务器上完成用户同意授权（这种用户授权方式比上面的流程要严谨，
 * 因为是在认证服务器上同意授权而不是在第三方应用），然后返回client并携带授权码（之所以没有直接给令牌，是因为更安全），然后client的服务器收到授权码并向认证服务器申请令牌，然后认证服务器发放令牌。
 *
 * 使用Spring Social开发第三方登录
  *  OAuth协议和第三方登录的关系：假设发放令牌后，现在是拿资源服务器的用户基本信息然后再根据用户信息构建Authentication（已认证）并放入SecurityContext中。
  * 则已经实现了应用的登录。Spirng Social是将上面的所有流程全部封装起来成为了一个SocialAuthenticationFilter并加入到了Spring Security的过滤器链中
  *
  * spring Social相关的类和接口
  *   每个服务提供商都会对应一个ServiceProvider（social为我们提供了一个抽象类AbstractOAuth2ServiceProvider<>实现了服务提供商共有的东西）我们在写具体的服务提供商时继承这个抽象类。
  * 对不同的服务提供商来说，认证服务器部分对应的认证流程是一致的，只有资源服务器中提供的资源数据不同（如果是提供用户信息则是第三方登录）
  * social提供了OAuth2Operations接口（默认的实现OAuth2Template）来封装这个认证流程，提供Api（提供了抽象类AbstractOAuth2ApiBinding）接口来定义资源服务器那部分。
  * 上面三个接口是对应服务提供商的（第2个和第三个是包含在第一个里面），包含了oauth的所有流程（从将用户导向认证服务器到获取用户信息流程为止）。
  * 根据用户信息构建Authentication（已认证）并放入SecurityContext中这个流程和服务提供商就没有任何关系了，全部在我们自己的应用完成。
  * 和上面这个步骤相关的接口有Connection(提供了默认实现类OAuth2Connection，作用是封装我们前面获取到的用户信息)，这个connection是由ConnectionFactory（默认实现是OAuth2ConnectionFactory）创建的。
  * 这个工厂为了创建这个connection对象，就需要服务提供商的所有流程，所以这个工厂里面是要有ServiceProvider实例的。
  * 由于connection的数据结构是固定的，而我们从不同的服务提供商获取的用户信息（也就是api部分）的数据结构是不一样的，所以我们需要在工厂中写一个ApiAdapter来适配这两者
  * connection中的数据是服务提供商中的用户信息，我们通过一张数据表（userConnection表）来对应我们自己应用的用户和服务提供商中的用户信息。而操作这个数据表是通过UserConnectionRepository（默认实现是JdbcUsersConnectionRepository）。
  * 这个数据表我们需要自己创建（sql脚本在JdbcUsersConnectionRepository类旁边的sql文件找）。
  *
  * 社交登录的运行流程
  * SocialAuthenticationFilter拦截社交登录请求，然后调用SocialAuthenticationService（具体实现是OAuth2AuthenticationService），它再去调用我们自己写的ConnectionFactory，然后生成Connection对象，
  * 然后Authentication（SocialAuthenticationToken）会将connection封装到token中，然后Manager会选择SocialAuthenticationProvider来处理这个token，然后JdbcUsersConnectionRepository会根据connection中的用户信息去表中查出对应的我们自己应用的用户的id
  * ，然后再根据userId调用我们自己写的SocialUserDetailsService生成SocialUserDetails对象，然后再核对信息，如果无误则再封装到token中，标记为已认证。
  *
 * @author: Mr.Wang
 * @create: 2019-12-01 13:15
 **/
@Configuration
@EnableSocial //将social相关的东西启动
public class SocialConfig extends SocialConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        //connectionFactoryLocator的作用就是去查找我们的connectionFactory（系统中可能会有多个，如qq的、微信的），根据条件找出现在应该用哪个connectionFactory来构建connection的数据
        //第三个参数是帮我们把插入数据库的数据（用户比较敏感的数据，比如token等）进行加解密的工具
        return new JdbcUsersConnectionRepository(dataSource,connectionFactoryLocator,Encryptors.noOpText());//该处Encryptors.noOpText()是不作任何加解密处理，为了显示清楚
    }
    @Bean  //这个bean的作用是将social放到springSecurity过滤器链上的bean
    public SpringSocialConfigurer springSocialConfigurer(){
        return new SpringSocialConfigurer();
    }
}

package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: restfulrcud
 * @description: spring security oauth中的认证服务器
 *      @Configuration
 *      @EnableAuthorizationServer  只需要写这一个注解 就已经实现了标准的认证服务器（4种授权模式到token的生成存储）
 *      public class AuthticationServerConfig{
 *
 *      }
 *    这样我们的这个项目就是一个认证服务器了，其它第三方应用可以通过授权地址（该地址在该项目启动时的控制台中有）来访问这个认证服务器
 *    浏览器输入该认证服务器的授权地址时需要携带以下几个参数
 *      response_type=code (必要)
 *      client_id(需要授权的第三方应用的标识，默认下该项目启动时控制台中有，必要，通过这个参数该认证服务器就知道是哪个应用在请求授权)
 *      redirect_uri（可选的，该地址可以看到返回来的授权码）
 *      scope(权限，可选)
 * 如http://localhost:8080/oauth/authorize?response_type=code&client_id=hulin&redirect_uri=http://example.com&scope=all
 * 然后需要用户登录（认证服务器需要知道是哪个用户授权第三方应用并需要换取token），然后授权码会发到redirect_uri这个地址上，然后第三方应用再对获取令牌的地址发起请求（post），也需要携带一些参数。
 * 然后认证服务器会为该用户返回一个acess_token、refresh_token、acess_token的过期时间，
 * 只要同一个用户在过期时间内访问认证服务器，认证服务器发给该用户的token不会变，一旦过了过期时间，则用refresh_token发给用户
 * @author: Mr.Wang
 * @create: 2020-03-03 13:18
 **/

/*自定义token的处理，所以不再使用上面的默认（写一个TokenStoreConfig）
  token的存储默认是放在内存中的，我们把它存到redis中
*/
@Configuration
@EnableAuthorizationServer
public class AuthticationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenStore tokenStore;//引入我们自己配置的tokenstore
    @Autowired(required = false)//required = false代表可以没有，因为只有配置了后才有它
    private JwtAccessTokenConverter jwtAccessTokenConverter;
   @Autowired(required = false)
   private TokenEnhancer jwtTokenEnhancer;
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);
        //这个是配认证服务器会给哪些第三方应用发放令牌，一旦写了这个配置，配置文件中写的clientId就不起作用了
        clients.inMemory()
                .withClient("hulin")
                .secret("hulin")
                .accessTokenValiditySeconds(7200)//令牌的有效期，单位是秒
                //当令牌过期后，如果让用户重新登录获取令牌太不好了
                // 所以我们判断到令牌过期后，可以马上用刷新的令牌向/oauth/token发请求，body的参数的grant_type为refresh_token和参数refresh_token=刷新的令牌。
                //这样用户感觉不到令牌换了
                .refreshTokenValiditySeconds(28000) //刷新令牌的有效期
                .authorizedGrantTypes("password","refresh_token")//这是指针对hulin这个应用，该认证服务器所支持的授权模式有哪些,只有是这个集合里面的模式才支持
                .scopes("all") //权限的配置，该处配了all时，该clientId发请求时可以不带scope参数，如果带了，也必须是该处配置集合里面的才行
                .and()
                .withClient("xxx");//同上面一样，再多配一个可以支持的应用

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //之前获取令牌请求是由tokenendpoints类处理，endpoints就是处理令牌请求的入口点
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
        if(jwtAccessTokenConverter!=null&&jwtTokenEnhancer!=null){
            //当需要jwtTokenEnhancer扩展令牌信息时，才写下面的增强链
            TokenEnhancerChain enhancerChain=new TokenEnhancerChain();
            List<TokenEnhancer> enhancers=new ArrayList<>();
            enhancers.add(jwtTokenEnhancer);
            enhancers.add(jwtAccessTokenConverter);
            enhancerChain.setTokenEnhancers(enhancers);
            //当不需要扩展时，上面的不用写
            endpoints.tokenEnhancer(enhancerChain)//当不需要扩展时，该方法不写
                    .accessTokenConverter(jwtAccessTokenConverter);
        }
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }
}


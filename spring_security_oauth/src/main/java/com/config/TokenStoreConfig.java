package com.config;

import com.component.JwtTkoenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @program: restfulrcud
 * @description: token的存储配置
 *    JWT(json web token,json开放的令牌标准)
 *      默认的令牌生成是通过uuid生成的（不含任何有意义的信息，所有的信息是通过额外存储起来的），而jwt的令牌生成的特点是令牌本身可以包含有意义信息的，
 *      所以我们拿到令牌后进行解析后就可以知道之前需要额外存储的一些信息。
 *      另外这个令牌是具有密签的特点，不是加密，而是密钥的签名，是指如果别人修改该令牌的信息，我们是可以知道的（但是不要把敏感的信息放进去）。
 *      另外还有可扩展性，我们可以自定义往令牌中放什么信息
 * @author: Mr.Wang
 * @create: 2020-03-05 11:10
 **/
@Configuration
public class TokenStoreConfig {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Bean
    @ConditionalOnProperty(prefix = "xxx",name = "yy",havingValue = "redis")
    public TokenStore redisTokenStore(){
        return new RedisTokenStore(redisConnectionFactory);//从redis连接工厂拿到redis的连接，一旦有令牌会存放到redis中
    }

    //使用JWT
    @Configuration
    @ConditionalOnProperty(prefix = "xxx",name = "yy",havingValue = "jwt",matchIfMissing = true)
    //只有配置文件中配了xxx.yy=jwt时，该类的配置才生效，这样就不会和上面的TOkenStore冲突
    //matchIfMissing = true 代表当配置文件里没有配置xxx.yy时，下面的配置也生效
    public  static class JwtTokenConfig{
        @Bean
        public TokenStore JwtTokenStore(){
            return new JwtTokenStore(jwtAccessTokenConverter()); //这个是负责令牌的存储
        }
        @Bean
        /*负责令牌的生成(其实就是使用一个jwt增强器来对默认的令牌进行增强)
        jwt生成的令牌有如下信息
        "access_token":xxxxx,
        "token_type": "bearer",
        "expires_in": 7196,
        "scope": "all",
        "jti": "81706dca-6152-4574-9b7a-8ce2e17fe1e7"
        我们可以拿着jwt生成的access_token去jwt官网进行解析，看含有哪些信息

        */
        public JwtAccessTokenConverter jwtAccessTokenConverter(){
            JwtAccessTokenConverter accessTokenConverter=new JwtAccessTokenConverter();
            accessTokenConverter.setSigningKey("hulin");//设置密钥,发令牌的时候会用这个密钥来签名，验证令牌的时候也会用这个密钥来验签。
            return accessTokenConverter;
        }
        @Bean
        //对jwt令牌进行扩展(可选的 并不一定要扩展)
        public TokenEnhancer jwtTokenEnhancer(){
                return new JwtTkoenEnhancer();
        }
    }
}

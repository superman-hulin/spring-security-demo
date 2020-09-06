package com.component;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: restfulrcud
 * @description: 对jwt令牌进行扩展的类（当不需要扩展时，该类不用写）
 * @author: Mr.Wang
 * @create: 2020-03-05 15:43
 **/
public class JwtTkoenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String,Object> info=new HashMap<>();//map里就是我们要额外放到令牌里面的信息
        info.put("company","hulin");
        ((DefaultOAuth2AccessToken)oAuth2AccessToken).setAdditionalInformation(info);
        return oAuth2AccessToken;
        //这样我们就把我们自定义的信息也加到token里面去了，返回的令牌中会多了一个company的信息
        //我们拿着这个token去访问rest服务时，但是这个额外的信息不会被解析到Authentication中，所以我们引入解析的依赖
        //自己写代码来解析

    }

}
/*解析代码示例如下
@GetMapping("/me")
public Object getCurrentUser(Authentication user,HttpServletRequest request){
 String header =request.getHeader("Authorization");//取请求头中的Authorization参数信息
        String token= StringUtils.substringAfter(header,"bearer ");
        Claims claims=Jwts.parser().setSigningKey("hulin".getBytes("UTF-8")).parseClaimsJws(token).getBody();
        String company=(String)claims.get("company");
        System.out.println(company);
}
*/

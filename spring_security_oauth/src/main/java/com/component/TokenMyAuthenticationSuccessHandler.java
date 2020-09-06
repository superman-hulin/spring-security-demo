package com.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: restfulrcud
 * @description: 由Spring Security Oauth令牌模式重构登录成功后的处理
 * @author: Mr.Wang
 * @create: 2019-11-21 10:14
 **/
@Component(value = "tokenMyAuthenticationSuccessHandler")
//当成功处理不做根据用户配置返回格式为json还是跳转时就implements AuthenticationFailureHandler，如果加上这种用户配置判断的话就extends SavedRequestAwareAuthenticationSuccessHandler
public class TokenMyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        System.out.println("登录成功");
        //取出登录请求的请求头中的信息（clientId来生成ClientDeatails）
        String header = httpServletRequest.getHeader("Authorization");
        if (header != null && header.startsWith("Basic ")) {
            try {
                //extractAndDecodeHeader作用是把请求头中的已编码的串进行解码
                String[] tokens = extractAndDecodeHeader(header, httpServletRequest);
                assert tokens.length == 2;
                String clientId = tokens[0];
                String clientSecret=tokens[1];
                ClientDetails clientDetails=clientDetailsService.loadClientByClientId(clientId);
                if(clientDetails==null){
                    throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在"+clientId);
                }else if(!StringUtils.equals(clientDetails.getClientSecret(),clientSecret)){
                    throw new UnapprovedClientAuthenticationException("clientSecret不匹配"+clientId);
                }
                TokenRequest tokenRequest=new TokenRequest(null,clientId,clientDetails.getScope(),"custom");//grantType本来是写四种授权模式的一种，但是我们这用自定义的
                OAuth2Request oAuth2Request=tokenRequest.createOAuth2Request(clientDetails);
                OAuth2Authentication oAuth2Authentication=new OAuth2Authentication(oAuth2Request,authentication);
                OAuth2AccessToken token=authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
                //将token以json形式写回去
                httpServletResponse.getWriter().write(objectMapper.writeValueAsString(token));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {
                byte[] base64Token = header.substring(6).getBytes("UTF-8");

                byte[] decoded;
                try {
                    decoded = Base64.decode(base64Token);
                } catch (IllegalArgumentException var7) {
                    throw new BadCredentialsException("Failed to decode basic authentication token");
                }

                String token = new String(decoded,"UTF-8");
                int delim = token.indexOf(":");
                if (delim == -1) {
                    throw new BadCredentialsException("Invalid basic authentication token");
                } else {
                    return new String[]{token.substring(0, delim), token.substring(delim + 1)};
                }
            }

        }

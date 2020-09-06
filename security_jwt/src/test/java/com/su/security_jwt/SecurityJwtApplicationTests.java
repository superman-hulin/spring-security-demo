package com.su.security_jwt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityJwtApplicationTests {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    public void contextLoads() {
        String pass = "admin";
        String hashPass = passwordEncoder.encode(pass);
        System.out.println(hashPass);
        boolean f = passwordEncoder.matches("admin",hashPass);
        System.out.println(f);
    }

}

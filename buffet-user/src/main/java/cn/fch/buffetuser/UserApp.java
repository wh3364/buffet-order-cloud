package cn.fch.buffetuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author fch
 * @program BuffetOrderCloud
 * @description
 * @create 2023-03-21 20:43
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class UserApp {
    public static void main(String[] args) {
        SpringApplication.run(UserApp.class, args);
    }
}

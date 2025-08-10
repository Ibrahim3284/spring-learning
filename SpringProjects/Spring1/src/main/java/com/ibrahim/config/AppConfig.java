package com.ibrahim.config;

import com.ibrahim.Alien;
import com.ibrahim.Computer;
import com.ibrahim.Desktop;
import com.ibrahim.Laptop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan("com.ibrahim")
public class AppConfig {

//    @Bean(name = "com2")
//    @Primary
////    @Scope("prototype")
//    public Desktop desktop() {
//        return new Desktop();
//    }
//
//    @Bean
////    @Primary
//    public Laptop laptop(){
//        return new Laptop();
//    }
//
//    @Bean
//    public Alien alien(@Autowired Computer com) {
////    public Alien alien(@Qualifier("laptop") Computer com){
//        Alien obj = new Alien();
//        obj.setAge(25);
//        obj.setCom(com);
//        return obj;
//    }
}

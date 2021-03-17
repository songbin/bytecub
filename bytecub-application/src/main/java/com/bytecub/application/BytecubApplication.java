package com.bytecub.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
  * ByteCub.cn.
  * Copyright (c) 2020-2020 All Rights Reserved.
  * 
  * @author bytecub@163.com  songbin
  * @version Id: BytecubApplication.java, v 0.1 2020-12-02  Exp $$
  */
@Configuration
@SpringBootApplication(scanBasePackages = { "com.bytecub"})
@Slf4j
@MapperScan(basePackages = { "com.bytecub.mdm.dao.*"})
@EnableElasticsearchRepositories(basePackages = "com.bytecub.storage.es")
@EnableScheduling
public class BytecubApplication {
    public static void main(String[] args){
        SpringApplication.run(BytecubApplication.class, args);
    }

//    private static void parseCommand(String[] args){
//        try{
//            final Options options = new Options();
//            final Option option = new Option("p", true, "listen port");
//            final Option optionHelp = new Option("h", false, "list all command");
//            options.addOption(option);
//            options.addOption(optionHelp);
//
//            final CommandLineParser parser = new PosixParser();
//            CommandLine cmd = parser.parse(options, args);
//            if (cmd.hasOption("h")) {
//                System.out.println(options.toString());
//                System.exit(0);
//            }else if(cmd.hasOption("p")){
//                SysParamConstant.SERVER_PORT = Integer.parseInt(cmd.getOptionValue("p"));
//            }
//
//        }catch (Exception e){
//            //log.info("解析参数异常", e);
//        }

//    }
}

package com.example.backendsupportapp;

import com.example.backendsupportapp.constant.FileConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;

@SpringBootApplication
public class BackendSupportappApplication {

	public static void main(String[] args) {

		SpringApplication.run(BackendSupportappApplication.class, args);
        new File(FileConstant.USER_FOLDER).mkdirs(); // will create a folder in the url USER_FOLDER
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
}

package com.harsh.lms;

import com.harsh.lms.ui.LoginDashboard;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class LmsApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(LmsApplication.class, args);
        LoginDashboard dashboard = context.getBean(LoginDashboard.class);
        dashboard.start();
	}

}

package com.king.caesar.gamma.demo.client;

import org.springframework.context.ApplicationContext;

import com.king.caesar.gamma.Bootstrap;
import com.king.caesar.gamma.core.util.ContextHolder;
import com.king.caesar.gamma.demo.api.HelloService;

/**
 * 客户端
 * @author Administrator
 *
 */
public class Startup {
	public static void main(String[] args) {
		System.err.println("Begin to start...");
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.init();
		bootstrap.start();
		ApplicationContext context = ContextHolder.getContext();
		HelloService bean = (HelloService) context.getBean("hello");
		String response = bean.sayHello("hello!");
		System.out.println(response);
	}
}

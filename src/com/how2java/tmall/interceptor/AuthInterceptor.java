package com.how2java.tmall.interceptor;

import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.StrutsStatics;
import org.springframework.beans.factory.annotation.Autowired;

import com.how2java.tmall.pojo.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AuthInterceptor extends AbstractInterceptor {
//	@Autowired
//	OrderItemService orderItemService;

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		// TODO Auto-generated method stub
		String[] noNeedAuthPage = new String[] { "home", "checkLogin", "register", "loginAjax", "login", "product",
				"category", "search" };
		ActionContext ctx = invocation.getInvocationContext();
		HttpServletRequest request = (HttpServletRequest) ctx.get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response = (HttpServletResponse) ctx.get(StrutsStatics.HTTP_RESPONSE);
		ServletContext servletContext = (ServletContext) ctx.get(StrutsStatics.SERVLET_CONTEXT);
		String contextPath = servletContext.getContextPath();
//		System.out.println("contextPath:" + contextPath);
		String url = request.getRequestURI();
//		System.out.println("url:" + url);
		url = StringUtils.remove(url, contextPath);
		if (url.startsWith("/fore")) {
			String method = StringUtils.substringAfterLast(url, "/fore");
			if (!Arrays.asList(noNeedAuthPage).contains(method)) {
				User user = (User) ctx.getSession().get("user");
//				System.out.println("ctx-user" + user);
//				System.out.println("ctx-user+request" + request.getSession().getAttribute("user"));
				if (null == user) {
					response.sendRedirect("login.jsp");
					return null;
				}
			}
		}
//		System.out.println(
//				"request.getServletContext().getContextPath():" + request.getServletContext().getContextPath());
//		System.out.println("request.getContextPath():" + request.getContextPath());

		return invocation.invoke();
	}

}

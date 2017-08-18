package com.happy.web;

import java.io.*;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.happy.web.base.MyClientEndpoint;
import com.happy.web.base.MyClientEndpoint.onMessageListener;

@SuppressWarnings("serial")
public class InfoServlet extends HttpServlet {

	public void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {		
		Properties pro = new Properties();
		String realpath = request.getRealPath("/WEB-INF/classes");
		try {
			//读取配置文件
			FileInputStream in = new FileInputStream(realpath
					+ "/config.properties");
			pro.load(in);
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		
		//通过key获取配置文件
		final String loginPage = pro.getProperty("loginPage");
		String websocketSrv = pro.getProperty("websocketSrv");
		final String siteCode = pro.getProperty("siteCode");
		String siteKey = pro.getProperty("siteKey");

		
		String fromlogin = request.getParameter("fromlogin");
		if (null != fromlogin) {
			request.getSession().setAttribute("mainsid",
					request.getParameter("sid"));
		}

		String sessionId = (String) request.getSession().getAttribute(
				"mainsid");
		if (null == sessionId || "".equals(sessionId)) {
			response.sendRedirect(loginPage + "?site=" + siteCode
					+ "&returnto=index.jsp");
			return;
		}

		MyClientEndpoint.init(sessionId, siteCode, siteKey,
				new onMessageListener() {

					@Override
					public void onMessage(String msg) {
						JsonParser parse = new JsonParser();
						JsonObject msgJson = parse.parse(msg).getAsJsonObject();
						boolean b = msgJson.get("b").getAsBoolean();
						if (!b) {
							try {
								response.sendRedirect(loginPage + "?site=" + siteCode
										+ "&returnto=index.jsp");
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							//先转成字符串可以去掉转义字符
							String v = msgJson.get("v").getAsString();
							JsonObject user = parse.parse(v).getAsJsonObject();
							try {
								request.setAttribute("username", user.get("Name"));
								//单点登录验证-转到主系统
								request.setAttribute("container-sso", true);
								request.getRequestDispatcher("info.jsp").forward(request, response);
							} catch (ServletException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});

		MyClientEndpoint.connect(websocketSrv + "?group=sub&key=" + siteCode);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}

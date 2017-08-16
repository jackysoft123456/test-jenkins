package com.happy.web.base;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.google.gson.JsonObject;

@ClientEndpoint
public class MyClientEndpoint {

	private static CountDownLatch latch;

	private static String mSid;
	private static String mCode;
	private static String mKey;
	private static onMessageListener mListener;

	public static void init(String sessionId,String siteCode,String siteKey, onMessageListener listener) {
		mSid = sessionId;
		mCode = siteCode;
		mKey = siteKey;
		mListener = listener;
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("Connected " + session.getId());
		try {
			JsonObject obj = new JsonObject();
			obj.addProperty("m", "get");
			obj.addProperty("sid", mSid);
			obj.addProperty("k", "account");
		
			JsonObject site = new JsonObject();
			site.addProperty("code", mCode);
			site.addProperty("key", mKey);
			
			obj.add("site", site);
			
			System.out.println("onOpen " + obj.toString());
			
			if (null != session) {
				session.getBasicRemote().sendText(obj.toString());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("onMessage " + message);
		mListener.onMessage(message);
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		System.out.println(String.format("Session %s close because of %s",
				session.getId(), closeReason));
		latch.countDown();
	}

	public static void connect(String url) {
		latch = new CountDownLatch(1);

		WebSocketContainer client = ContainerProvider.getWebSocketContainer();
		try {
			client.connectToServer(MyClientEndpoint.class, new URI(url));
			latch.await();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (DeploymentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public interface onMessageListener {
		void onMessage(String message);
	}
}

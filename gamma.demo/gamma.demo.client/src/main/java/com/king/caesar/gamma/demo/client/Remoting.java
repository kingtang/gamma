package com.king.caesar.gamma.demo.client;

import com.king.caesar.gamma.core.constants.GammaConstants;
import com.king.caesar.gamma.remoting.api.ClientOption;
import com.king.caesar.gamma.remoting.api.RemotingClient;
import com.king.caesar.gamma.remoting.netty.NettyRemotingClient;
import com.king.caesar.gamma.rpc.api.message.AttachmentObject;
import com.king.caesar.gamma.rpc.api.message.AttachmentType;
import com.king.caesar.gamma.rpc.api.message.MessageType;
import com.king.caesar.gamma.rpc.message.MessageImpl;

/**
 * Hello world!
 *
 */
public class Remoting {
	public void sendMsg() {
	    ClientOption option = new ClientOption();
		RemotingClient client = new NettyRemotingClient(option);
		client.init();
		client.connect();
		try {
			System.out.println("connect successed.");

			/*
			 * for (int i = 0; i < 6; i++) { Thread t = new MyThread(client);
			 * t.start(); }
			 */

			MessageImpl msg = new MessageImpl();
			msg.getMessageHeader().setService("helloService");
			msg.getMessageHeader().setOperation("sayHello");
			msg.getMessageHeader().setGroup("default");
			msg.getMessageHeader().setVersion("1.0.0");
			Object[] request = new Object[1];
			request[0] = "LebronJames";
			msg.setPayload(request);
			msg.setMessageType(MessageType.REQUEST);
			msg.getMessageHeader().addAttachment(GammaConstants.Attachment.REQUESTID,
					new AttachmentObject(AttachmentType.REMOTE_IMPLICIT, 1L));
			msg.getMessageHeader().addAttachment(GammaConstants.Attachment.SERIALIZETYPE,
					new AttachmentObject(AttachmentType.REMOTE_IMPLICIT, Byte.parseByte("1")));
			client.sendOneway(msg);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			Thread.sleep(600000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static class MyThread extends Thread {
		private RemotingClient client;

		public MyThread(RemotingClient client) {
			this.client = client;
		}

		@Override
		public void run() {
			for (int i = 0; i < 5000; i++) {
				/*
				 * try { Thread.sleep(10); } catch (InterruptedException e) {
				 * e.printStackTrace(); }
				 */
				MessageImpl msg = new MessageImpl();
				msg.setPayload("tangyongzhe");
				msg.setMessageType(MessageType.REQUEST);
				client.sendOneway(msg);
				System.out.println(i);
			}
		}

	}
}

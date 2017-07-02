package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import fx.ServerGUI;

public class ServerConnection extends Thread {
	
	private class SendThread extends Thread {
		
		PrintWriter stream;
		
		public SendThread (PrintWriter stream) {
			this.stream = stream;
		}
		
		public void run () {
			active = true;
		}
		
		public void pushMessage (ClientMessage message) {
			stream.
		}
	}
	
	private class ReceiveThread extends Thread {
		BufferedReader clientIn = null;
		
		public void run () {
			
			if (clientIn == null) {
				try {
					clientIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			while (client.isConnected()) {
				String msg = "";
				String input;
				try {
					input = clientIn.readLine();
					
					if (!input.startsWith("#STARTOF")) {
						
					}
					
					while (!(input = clientIn.readLine()).startsWith("#ENDOF")) {
						msg += input + "\n";
					}
				} catch (IOException e) {
					e.printStackTrace();
				}		
				
				handleMessage(msg);
			}
		}
	}
	
	Semaphore mutex = new Semaphore(1);
	Socket client;
	ServerGUI gui;
	ClientManager manager;
	boolean active = false;
	
	public ServerConnection (Socket client, ClientManager manager, ServerGUI gui) {
		this.client 	= client;
		this.gui 		= gui;
		this.manager	= manager;
	}
	
	private void login (ClientMessage message) {
		if (!manager.hasClient(message.clientName)) {
			Client clnt = new Client(message.clientName, client);
			manager.registerClient(clnt);
			gui.addClient(clnt.getId(), clnt.getName());
		}
	}
	
	private void chat (ClientMessage message) {
		
	}
	
	private void whisper (ClientMessage message) {
		
	}
	
	public void handleMessage (String msg) {
		ClientMessage message = Client.parseMessage(msg);
		System.out.println(message);
		
		if (message.intent == ClientMessage.COMMAND.LOGIN) {
			login(message);
		}
		
		if (message.intent == ClientMessage.COMMAND.CHAT) {
			chat(message);
		}
		
		if (message.intent == ClientMessage.COMMAND.WHISPER) {
			whisper(message);
		}
	}
	
	@Override
	public void run () {
		new ReceiveThread().start();
		new SendThread().start();
	}
}
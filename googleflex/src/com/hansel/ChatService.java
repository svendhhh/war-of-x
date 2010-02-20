package com.hansel;

import java.util.List;

import javax.jdo.PersistenceManager;

public class ChatService {

	public List<ChatEntry> getChat() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "select from " + ChatEntry.class.getName();
		List<ChatEntry> curentChat = (List<ChatEntry>)pm.newQuery(query).execute();
		
		return curentChat;
//		StringBuilder sb = new StringBuilder();
//		for (ChatEntry chatEntry : curentChat) {
//			sb.append(chatEntry.getUser()+" : "+chatEntry.getChatText()+"\n");
//		}
//		return sb.toString();
	}
	
	public void addChat(String user, String chatText){
		ChatEntry newChat = new ChatEntry(user, chatText);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(newChat);
		pm.close();
	}
}

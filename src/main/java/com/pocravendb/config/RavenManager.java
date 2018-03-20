package com.pocravendb.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.ravendb.client.documents.DocumentStore;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.session.IDocumentSession;

@Component()
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RavenManager {
	
	private IDocumentStore store;
	
	public IDocumentStore getStore() {
		
		if(store == null) {
			store = new DocumentStore("http://127.0.0.1:7979", "poc-ravendb");
			store.initialize();
		}
		
		return store;
	}
	
	public IDocumentSession openSession() {
		return getStore().openSession();
	}
}

package com.pocravendb;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pocravendb.config.RavenManager;
import com.pocravendb.documents.Post;
import com.pocravendb.documents.Seo;

import net.ravendb.client.documents.commands.PutDocumentCommand;
import net.ravendb.client.documents.session.DocumentInfo;
import net.ravendb.client.documents.session.IDocumentSession;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostCRUDTest {

	@Autowired
	private RavenManager manager;
	
	@Test
	public void deveCriarUmPost(){
			
		try(IDocumentSession session  = manager.openSession()){
			Post  post = createPost();
			
			session.store(post.getSeo());
			session.store(post);
			
			session.saveChanges();
			
			session.close();
		}
		
	}
	@Test
	public void deveCriarUmPostBuscandoUmaSeo(){
		
		try(IDocumentSession session  = manager.openSession()){
			Post  post = createPost();
			
			post.setSeo(session.load(Seo.class, "seos/1-A"));
			
			session.store(post);
			
			session.saveChanges();
			
			session.close();
		}
		
	}
	
	@Test
	public void deveBuscarUmPostPorId() {
		try(IDocumentSession session  = manager.openSession()){
			String id = "posts/193-A";
			
			Post post =  session.load(Post.class, id);
			Assert.assertNotNull(post);
			
			
			Assert.assertNotNull(post.getId());
			Assert.assertEquals(post.getId(), id);
			
			Assert.assertNotNull(session.advanced().getLastModifiedFor(post));
			
			session.close();
		}
		
	}
	
	@Test
	public void deveBuscarUmPostComSEO() {
		
		try(IDocumentSession session  = manager.openSession()){
		
		String id = "posts/193-A";
		
		Post post =  session
				.include("seo")
				.load(Post.class, id);
		
		// Se não persistir o objeto da dependencia antes. não terá id
		//fará parte do documento, apenas agrupado
		Seo seo = session.load(Seo.class, post.getSeo().getId());
		
		Assert.assertNotNull(post);
		Assert.assertNotNull(seo);
		
		Assert.assertNotNull(seo.getId());
		
		Assert.assertNotNull(post.getId());
		Assert.assertEquals(post.getId(), id);
		
		Assert.assertNotNull(session.advanced().getLastModifiedFor(post));
		
//		session.close();
		
		}
		
	}
	
	@Test
	public void deveAlterarUmPost() {
		
		try(IDocumentSession session  = manager.openSession()){

			String author = "Rafinha Lemes";
			Post post = session.query(Post.class)
							.firstOrDefault();
			
			post.setAuthor(author);
			
			DocumentInfo info = new DocumentInfo();
			info.setCollection("posts");
			
			ObjectNode on = session.advanced().getEntityToJson().convertEntityToJson(post, info);
			
			PutDocumentCommand put = new PutDocumentCommand(post.getId(), null, on);
			session.advanced().getRequestExecutor().execute(put);
			
			
//			session.close();
		}
		
	}
	
	@Test
	public void deveAlterarUmPostFacilmente() {
		
		try(IDocumentSession session = manager.openSession()){
			String author = "Rafael-Lemes2";
			Post post = session.query(Post.class)
					.whereEquals("author", null)
					.firstOrDefault();
			
//			Post post = session.load(Post.class, id);
			//Só inclui depois de usar o store (duplica e altera)
			
			if(post != null) {
				post.setAuthor(author);
				
				session.saveChanges();
				
				Assert.assertNotNull(post.getAuthor());
			}
			//Já tentei com e sem
			//session.close();
		}
		
	}
	
	
	@Test
	public void deveExcluirUmPost() {
		IDocumentSession session  = manager.openSession();
		String id = "posts/193-A";
		
		session.delete(id);
		session.saveChanges();
		
		Post post = session.load(Post.class, id);
		Assert.assertNull(post);
		session.close();
		
		
	}
	
	
	private Post createPost() {
		
		Post  post = new Post();
		post.setTitle("Titulo");
		post.setSubtitle("Subtitulo");
		post.setDate(new Date());
		post.setDraft(false);
		post.setResume("Resumo");
		post.setTags("Rafa, HUE, BR");
		
		Seo seo = new Seo();
		seo.setTitle("Keyword Title");
		seo.setMetadescription("seo description");
		seo.setKeywordKey("keyword");
		seo.setTags("seo, key, word, keyword");
		seo.setKeywords("AUHUAH, aeae, kkk");//Seria Interessante uma lista de String? T
											//Talvez aumentaria a complexidade nas consultas
		
		post.setSeo(seo);
		
		return post;
	}
}

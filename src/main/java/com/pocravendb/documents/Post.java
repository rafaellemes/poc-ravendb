package com.pocravendb.documents;

import java.util.Date;

import lombok.Data;

public @Data class Post {
	
	private String id;
	private String titleId;
	private String title;
	private String subtitle;
	private String resume;
	private String content;
	private String tags;
	private String img;
	private String author;
	private Date date;
	private Date publishDate;
	private boolean draft;
	
	private Seo seo;
	
	
	public Post() {
		
	}
	
	
}

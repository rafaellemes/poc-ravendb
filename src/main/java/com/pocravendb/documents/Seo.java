package com.pocravendb.documents;

import lombok.Data;

public @Data class Seo {
	
	private String id;
	private String title;
	private String metadescription;
	private String keywords;
	private String keywordKey;
	private String tags;
	
	public Seo() {
	}
	
}

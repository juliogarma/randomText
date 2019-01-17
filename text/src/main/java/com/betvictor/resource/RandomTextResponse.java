package com.betvictor.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tomcat.util.buf.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RandomTextResponse {

	private String type;
	private long amount;
	private long number;
	private String text_out;
	
	public List<String> getParagraphs() {
		Pattern p = Pattern.compile("<p>(.+?)</p>", Pattern.DOTALL);
	    Matcher m = p.matcher(text_out);
	    ArrayList<String> res = new ArrayList<>();
	    while (m.find()) {
	        // get the matching group and remove full stops
	        res.add(m.group(1).replace(".", ""));
	    }
	    return res;
	}

	@Override
	public String toString() {
		return StringUtils.join(getParagraphs());
	}
}

package com.betvictor.resource;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class TextResponse implements Serializable{

	@Id
	@GeneratedValue
	private Long id;
	private String freq_word;
	private double avg_paragraph_size;
	private double avg_paragraph_processing_time;
	private long total_processing_time;

}

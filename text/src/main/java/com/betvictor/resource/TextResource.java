package com.betvictor.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betvictor.repository.TextRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/text")
@Api
public class TextResource {

	private RandomTextResource randomTextResource;
	private TextRepository textRepository;
	
	public TextResource(RandomTextResource rtr, TextRepository tr) {
		randomTextResource = rtr;
		textRepository = tr;
	}

	/**
	 * 
	 * @param p_start
	 * @param p_end
	 * @param w_count_min
	 * @param w_count_max
	 */
	@GetMapping
	@ApiOperation(value="get statistics new random text")
	public TextResponse get(int p_start, int p_end, int w_count_min, int w_count_max) {
		long totalTimeIni = System.currentTimeMillis();
		TextResponse res = new TextResponse();
		List<RandomTextResponse> responses = new ArrayList<RandomTextResponse>();
		HashMap<String, Integer> frecuencies = new HashMap<String, Integer>();
		List<Long> wordCounts = new ArrayList<Long>();
		List<Long> processingTimes = new ArrayList<Long>();
		//get one
		for (int i = p_start;i<=p_end;i++) {
			long timeIni = System.currentTimeMillis();
			RandomTextResponse rtRes = randomTextResource.getResponse(i, w_count_min, w_count_max);
			//extract wordCount
			List<String> paragraphs = rtRes.getParagraphs();
			for (String paragraph:paragraphs) {
				String[] words = paragraph.split(" ");
				//update frecuencies
				frecuencies = addWordsAndFrecuency(frecuencies, words);
				wordCounts.add((long) words.length);
			}
			//calculate request time
			processingTimes.add(System.currentTimeMillis() - timeIni);
		}
		//calculate average
		res.setAvg_paragraph_size(calculateAverage(wordCounts));
		res.setAvg_paragraph_processing_time(calculateAverage(processingTimes));
		res.setTotal_processing_time(System.currentTimeMillis() - totalTimeIni);
		res.setFreq_word(frecuencies.keySet().iterator().next());
		//add to repository
		textRepository.save(res);
		return res;
	}

	
	@GetMapping("/history")
	@ApiOperation(value="get latest 10 executions")
	public List<TextResponse> getHistory() {
		return textRepository.findAll();
	}
	
	private HashMap<String, Integer> addWordsAndFrecuency(HashMap<String, Integer> frecuencies, String[] words){
		
		for (String word:words) {
			int index = frecuencies.get(word)==null?0:frecuencies.get(word);
			frecuencies.put(word, index++);
		}
		return frecuencies;
	}
	
	private double calculateAverage(List <Long> elements) {
		  Long sum = (long) 0;
		  if(!elements.isEmpty()) {
		    for (Long element : elements) {
		        sum += element;
		    }
		    return sum / elements.size();
		  }
		  return sum;
		}
}

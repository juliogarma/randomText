package com.betvictor.resource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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
	private final String NO_WORDS_RESULT = "none";
	
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
		SortedMap<String, Integer> frecuencies = new TreeMap<String, Integer>();
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
		
		frecuencies = sortMapByValue (frecuencies);

		if (!frecuencies.isEmpty())
			res.setFreq_word(frecuencies.keySet().iterator().next());
		else
			res.setFreq_word(NO_WORDS_RESULT);
		res.setTotal_processing_time(System.currentTimeMillis() - totalTimeIni);
		//add to repository
		textRepository.save(res);
		return res;
	}

	
	@GetMapping("/history")
	@ApiOperation(value="get latest 10 executions")
	public List<TextResponse> getHistory() {
		List<TextResponse> res = textRepository.findTop10ByOrderByIdDesc();
		if (res.size()>10) {
			res = res.subList(0, 10);
		}
		return res;
	}
	
	private SortedMap<String, Integer> addWordsAndFrecuency(SortedMap<String, Integer> frecuencies, String[] words){
		
		for (String word:words) {
			int index = frecuencies.get(word.toLowerCase())==null?1:frecuencies.get(word.toLowerCase());
			frecuencies. put(word.toLowerCase(), ++index);
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
	
	
	public static TreeMap<String, Integer> sortMapByValue(Map<String, Integer> map){
		Comparator<String> comparator = new ValueComparator(map);
		TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
		result.putAll(map);
		return result;
	}
}

class ValueComparator implements Comparator {
	Map map;
 
	public ValueComparator(Map map) {
		this.map = map;
	}
 
	public int compare(Object keyA, Object keyB) {
		Comparable valueA = (Comparable) map.get(keyA);
		Comparable valueB = (Comparable) map.get(keyB);
		return valueB.compareTo(valueA);
	}
}

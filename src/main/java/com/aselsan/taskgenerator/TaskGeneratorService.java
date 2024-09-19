package com.aselsan.taskgenerator;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskGeneratorService {

	private Thread generatorThread;
	
	private AtomicBoolean generating = new AtomicBoolean(false);
	
	private final Random random = new Random();
	
	private String baseUrl = "http://localhost:8080/tasks";
	
	private RestTemplate restTemplate;
	
	public TaskGeneratorService(RestTemplate restTemplate) {
		
		this.restTemplate = restTemplate;
	}
	

	public void start(int delay, int minPriority, int maxPriority, int minDuration, int maxDuration) {
		
		if(generatorThread == null) {

			generating.set(true);
			
			generatorThread = new Thread(() -> {
				
				while(generating.get()) {
					
					int priority = random.nextInt(maxPriority - minPriority) + minPriority;
					
					int duration = random.nextInt(maxDuration - minDuration) + minDuration;
					
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			
					MultiValueMap<String, Integer> map= new LinkedMultiValueMap<String, Integer>();
					map.add("priority", priority);
					map.add("duration", duration);
					
					HttpEntity<MultiValueMap<String, Integer>> request = 
							new HttpEntity<MultiValueMap<String, Integer>>(map, headers);
					
					ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);
				
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			generatorThread.start();
		
		} else {
			
			stop();
			start(delay, minPriority, maxPriority, minDuration, maxDuration);
		}
	}
	
	public void stop() {
		
		generating.set(false);
		
		generatorThread.interrupt();
		
		try {
			generatorThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		generatorThread = null;
	}
}

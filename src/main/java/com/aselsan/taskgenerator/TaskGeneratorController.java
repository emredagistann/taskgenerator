package com.aselsan.taskgenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generator")
public class TaskGeneratorController {

	@Autowired
	TaskGeneratorService taskGeneratorService;

	@PostMapping("/start")
	public ResponseEntity<String> startGenerating(
			@RequestParam int delayInMs, 
			@RequestParam int minPriority, 
			@RequestParam int maxPriority,
			@RequestParam int minDuration,
			@RequestParam int maxDuration) {
		
		taskGeneratorService.start(delayInMs, minPriority, maxPriority, minDuration, maxDuration);
		
		return ResponseEntity.ok("Task generation started.");
	}
	
	@PostMapping("/stop")
	public ResponseEntity<String> stopGenerating() {
		
		taskGeneratorService.stop();
		
		return ResponseEntity.ok("Task generation stopped.");
	}
}

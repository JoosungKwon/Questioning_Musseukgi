package com.kwonjs.questioningmusseukgi.global.chatqpt;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;

import java.net.URI;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.kwonjs.questioningmusseukgi.global.chatqpt.dto.ChatGptRequest;
import com.kwonjs.questioningmusseukgi.global.chatqpt.dto.ChatGptResponse;
import com.kwonjs.questioningmusseukgi.global.common.config.ChatGptConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGptService {

	public static final String BEARER = "Bearer ";

	private final ChatGptConfig chatGptConfig;
	private final ChatGptClient chatGptClient;
	private final HttpHeaders headers = new HttpHeaders();

	@PostConstruct
	public void init() {
		headers.setContentType(APPLICATION_JSON);
		headers.add(AUTHORIZATION, BEARER + chatGptConfig.getApiKey());
	}

	public String sendTo(String message) {
		URI requestUrl = URI.create(chatGptConfig.getRequestUrl());
		return sendTo(requestUrl, message);
	}

	public String sendTo(URI requestUrl, String message) {
		return sendTo(headers, requestUrl, message);
	}

	// TODO: @Async 고려하기
	public String sendTo(HttpHeaders headers, URI requestUrl, String message) {

		ChatGptRequest body = buildRequest(message);

		log.debug("[Sending Request] URL:{}, Header:{}, Body:{}", requestUrl, headers, body);

		ChatGptResponse result = chatGptClient.sendByPost(requestUrl, headers, body);
		log.debug("[ChatGPT Response]: {}", result);

		return result.choices().get(0).text();
	}

	private ChatGptRequest buildRequest(String message) {
		return ChatGptRequest.builder()
			.model(chatGptConfig.getModel())
			.prompt(message)
			.maxTokens(chatGptConfig.getMaxToken())
			.temperature(chatGptConfig.getTemperature())
			.topP(chatGptConfig.getTopP())
			.build();
	}
}
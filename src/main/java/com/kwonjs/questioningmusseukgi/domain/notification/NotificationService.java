package com.kwonjs.questioningmusseukgi.domain.notification;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kwonjs.questioningmusseukgi.domain.notification.sender.service.SenderService;
import com.kwonjs.questioningmusseukgi.domain.question.model.Question;
import com.kwonjs.questioningmusseukgi.domain.user.model.User;
import com.kwonjs.questioningmusseukgi.domain.user.service.UserService;
import com.kwonjs.questioningmusseukgi.domain.question.service.QuestionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

	@Value("${service.range}") private int RANGE;

	private final UserService userService;
	private final QuestionService questionService;
	private final SenderService senderService;

	public void sendToQuestionAtTime() {
		LocalTime start = LocalTime.now();
		this.sendToQuestionAtTime(start);
	}

	public void sendToQuestionAtTime(LocalTime startTime) {
		// 1. 설정한 알림 시각에 보내야 하는 사용자 정보 가져오기 -> 구독자 정보
		// 1-1. 얼만큼의 범위의 시간에서 사용자를 식별할 것인가?
		LocalTime start = startTime;
		LocalTime finish = start.plusMinutes(RANGE);

		List<User> users = userService.getByTime(start, finish);

		log.info("users: {}", users);

		// 2. 해당 사용자에게 보낼 질문 고르기 -> 날짜 별로 설정 -> 추후에 사용자 설정에 맞출 수 있도록 확장
		for(User user : users) {
			Question question = questionService.generateBy(user);
			// 3. 해당 사용자에게 질문 보내기
			senderService.send(user, question);
		}
	}
}

package org.solen.business.practicecases;

import org.solen.controller.dto.practice.CreatePracticeRequest;
import org.solen.domain.practices.Practice;

public interface ICreatePracticeUseCase {
    Practice createPractice(CreatePracticeRequest request, Long userId);
}
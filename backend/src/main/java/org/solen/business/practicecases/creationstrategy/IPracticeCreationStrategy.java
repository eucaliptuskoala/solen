package org.solen.business.practicecases.creationstrategy;

import org.solen.controller.dto.practice.CreatePracticeRequest;
import org.solen.domain.practices.Practice;

public interface IPracticeCreationStrategy {
    Practice createPractice(CreatePracticeRequest request, Long userId);
}
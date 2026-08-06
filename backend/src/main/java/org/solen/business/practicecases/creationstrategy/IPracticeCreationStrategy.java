package org.solen.business.practicecases.creationstrategy;

import org.solen.domain.practices.Practice;

public interface IPracticeCreationStrategy {
    Practice createPractice(Long categoryId, String name, String description, Long userId);
}
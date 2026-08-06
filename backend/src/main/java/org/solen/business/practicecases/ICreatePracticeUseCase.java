package org.solen.business.practicecases;

import org.solen.domain.practices.Practice;

public interface ICreatePracticeUseCase {
    Practice createPractice(Long categoryId, String name, String description, Long userId);
}
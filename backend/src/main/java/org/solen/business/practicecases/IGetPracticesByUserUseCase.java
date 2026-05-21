package org.solen.business.practicecases;

import org.solen.domain.practices.Practice;

import java.util.List;

public interface IGetPracticesByUserUseCase {
    List<Practice> getPracticesByUser(Long userId);
}

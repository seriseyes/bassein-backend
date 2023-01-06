package com.seris.bassein.service.repository;

import com.seris.bassein.entity.BaseModel;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BaseRepository <T extends BaseModel> extends PagingAndSortingRepository<T, Long> {
}

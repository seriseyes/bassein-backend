package com.seris.bassein.service.component.user.repository;

import com.seris.bassein.entity.user.Customer;
import com.seris.bassein.enums.Status;
import com.seris.bassein.service.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends BaseRepository<Customer> {

    Optional<Customer> findFirstByRegNoOrPhone(String regNo, String phone);

    List<Customer> findAllByStatus(Status status);

    List<Customer> findAllByRegNoLikeAndStatus(String regNo, Status status);

    boolean existsByRegNo(String regNo);
}

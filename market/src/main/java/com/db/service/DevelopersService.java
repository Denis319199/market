package com.db.service;

import com.db.exception.DevelopersServiceException;
import com.db.model.Developer;
import com.db.repo.DevelopersRepo;
import com.db.utility.Utilities;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DevelopersService {
  private final DevelopersRepo developersRepo;

  @Transactional(readOnly = true)
  public List<Developer> getAllDevelopers(int page, int size) {
    return developersRepo.findAll(PageRequest.of(page, size)).getContent();
  }

  @Transactional(readOnly = true)
  public Developer findDeveloperById(int id) throws DevelopersServiceException {
    Optional<Developer> developer = developersRepo.findById(id);
    if (developer.isEmpty()) {
      throw new DevelopersServiceException(DevelopersServiceException.DEVELOPER_NOT_FOUND);
    }

    return developer.get();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Developer insertDeveloper(Developer developer) throws DevelopersServiceException {
    if (developersRepo.existsById(developer.getId())) {
      throw new DevelopersServiceException(DevelopersServiceException.DEVELOPER_ALREADY_EXISTS);
    }

    return developersRepo.save(developer);
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Developer updateDeveloper(Developer developer) throws DevelopersServiceException {
    Developer old = findDeveloperById(developer.getId());
    Utilities.merge(developer, old);
    return developersRepo.save(developer);
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteDeveloper(int id) throws DevelopersServiceException {
    try {
      developersRepo.deleteById(id);
    } catch (DataAccessException ex) {
      throw new DevelopersServiceException(ex.getMessage());
    }
  }
}

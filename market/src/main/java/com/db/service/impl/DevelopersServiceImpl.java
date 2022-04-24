package com.db.service.impl;

import com.db.client.AuthClient;
import com.db.exception.DevelopersServiceException;
import com.db.model.Developer;
import com.db.repo.DevelopersRepo;
import com.db.service.DevelopersService;
import com.db.utility.Utilities;
import com.db.utility.mapper.ModelMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Profile({"prod", "!auth-service-disabled"})
public class DevelopersServiceImpl implements DevelopersService {
  protected final DevelopersRepo developersRepo;
  protected final AuthClient authClient;
  protected final ModelMapper modelMapper;

  @Override
  @Transactional(readOnly = true)
  public List<Developer> getAllDevelopers(int page, int size) {
    return developersRepo.findAll(PageRequest.of(page, size)).getContent();
  }

  @Override
  @Transactional(readOnly = true)
  public Developer findDeveloperById(int id) throws DevelopersServiceException {
    Optional<Developer> developer = developersRepo.findById(id);
    if (developer.isEmpty()) {
      throw new DevelopersServiceException(DevelopersServiceException.DEVELOPER_NOT_FOUND);
    }

    return developer.get();
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Developer insertDeveloper(Developer developer) throws DevelopersServiceException {
    if (!authClient.checkCountriesExistence(List.of(developer.getCountryId())).get(0)) {
      throw new DevelopersServiceException(DevelopersServiceException.BAD_COUNTRY_ID);
    }

    try {
      return developersRepo.save(developer);
    } catch (DataAccessException ex) {
      throw new DevelopersServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Developer updateDeveloper(Developer developer) throws DevelopersServiceException {
    Developer old = findDeveloperById(developer.getId());

    if (Objects.nonNull(developer.getCountryId())
        && !authClient.checkCountriesExistence(List.of(developer.getCountryId())).get(0)) {
      throw new DevelopersServiceException(DevelopersServiceException.BAD_COUNTRY_ID);
    }

    modelMapper.merge(developer, old);

    try {
      return developersRepo.save(developer);
    } catch (DataAccessException ex) {
      throw new DevelopersServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteDeveloper(int id) throws DevelopersServiceException {
    try {
      developersRepo.deleteById(id);
    } catch (DataAccessException ex) {
      throw new DevelopersServiceException(ex.getMessage());
    }
  }
}

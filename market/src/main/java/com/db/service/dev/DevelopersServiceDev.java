package com.db.service.dev;

import com.db.client.AuthClient;
import com.db.exception.DevelopersServiceException;
import com.db.model.Developer;
import com.db.repo.DevelopersRepo;
import com.db.service.impl.DevelopersServiceImpl;
import com.db.utility.Utilities;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("auth-service-disabled")
public class DevelopersServiceDev extends DevelopersServiceImpl {
  public DevelopersServiceDev(DevelopersRepo developersRepo, AuthClient authClient) {
    super(developersRepo, authClient);
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Developer insertDeveloper(Developer developer) throws DevelopersServiceException {
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
    Utilities.merge(developer, old);

    try {
      return developersRepo.save(developer);
    } catch (DataAccessException ex) {
      throw new DevelopersServiceException(ex.getMessage());
    }
  }
}

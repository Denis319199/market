package com.db.service.dev;

import com.db.client.AuthClient;
import com.db.exception.UsersItemsServiceException;
import com.db.model.UsersItem;
import com.db.model.UsersItemId;
import com.db.repo.UsersItemsRepo;
import com.db.service.impl.UsersItemsServiceImpl;
import com.db.utility.Utilities;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("auth-service-disabled")
public class UsersItemsServiceDev extends UsersItemsServiceImpl {
  public UsersItemsServiceDev(UsersItemsRepo usersItemsRepo, AuthClient authClient) {
    super(usersItemsRepo, authClient);
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public UsersItem insertUsersItem(UsersItem usersItem) throws UsersItemsServiceException {
    if (usersItemsRepo.existsById(new UsersItemId(usersItem.getUserId(), usersItem.getItemId()))) {
      throw new UsersItemsServiceException(UsersItemsServiceException.USERS_ITEM_ALREADY_EXISTS);
    }

    try {
      return usersItemsRepo.save(usersItem);
    } catch (DataAccessException ex) {
      throw new UsersItemsServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public UsersItem updateUsersItem(UsersItem usersItem) throws UsersItemsServiceException {
    UsersItem old =
        findUsersItemByUsersItemId(new UsersItemId(usersItem.getUserId(), usersItem.getItemId()));

    Utilities.merge(usersItem, old);

    try {
      return usersItemsRepo.save(usersItem);
    } catch (DataAccessException ex) {
      throw new UsersItemsServiceException(ex.getMessage());
    }
  }
}

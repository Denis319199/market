package com.db.service;

import com.db.client.AuthClient;
import com.db.exception.SellingItemsServiceException;
import com.db.exception.UsersItemsServiceException;
import com.db.model.UsersItem;
import com.db.model.UsersItemId;
import com.db.repo.UsersItemsRepo;
import com.db.utility.Utilities;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersItemsService {
  private final UsersItemsRepo usersItemsRepo;
  private final AuthClient authClient;

  @Transactional(readOnly = true)
  public List<UsersItem> getAllUsersItems(int page, int size) {
    return usersItemsRepo.findAll(PageRequest.of(page, size)).getContent();
  }

  @Transactional(readOnly = true)
  public UsersItem findUsersItemByUsersItemId(UsersItemId id) throws UsersItemsServiceException {
    Optional<UsersItem> usersItem = usersItemsRepo.findById(id);

    if (usersItem.isEmpty()) {
      throw new UsersItemsServiceException(UsersItemsServiceException.USERS_ITEM_NOT_FOUND);
    }

    return usersItem.get();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public UsersItem insertUsersItem(UsersItem usersItem) throws UsersItemsServiceException {
    if (usersItemsRepo.existsById(new UsersItemId(usersItem.getUserId(), usersItem.getItemId()))) {
      throw new UsersItemsServiceException(UsersItemsServiceException.USERS_ITEM_ALREADY_EXISTS);
    }

    if (!authClient.checkExistence(List.of(usersItem.getUserId()), true).get(0)) {
      throw new UsersItemsServiceException(UsersItemsServiceException.BAD_USER_ID);
    }

    try {
      return usersItemsRepo.save(usersItem);
    } catch (DataAccessException ex) {
      throw new UsersItemsServiceException(ex.getMessage());
    }
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public UsersItem updateUsersItem(UsersItem usersItem) throws UsersItemsServiceException {
    UsersItem old =
        findUsersItemByUsersItemId(new UsersItemId(usersItem.getUserId(), usersItem.getItemId()));

    if (Objects.nonNull(usersItem.getUserId())) {
      if (!authClient.checkExistence(List.of(usersItem.getUserId()), true).get(0)) {
        throw new UsersItemsServiceException(UsersItemsServiceException.BAD_USER_ID);
      }
    }

    Utilities.merge(usersItem, old);

    try {
      return usersItemsRepo.save(usersItem);
    } catch (DataAccessException ex) {
      throw new UsersItemsServiceException(ex.getMessage());
    }
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteUsersItem(UsersItemId id) throws UsersItemsServiceException {
    try {
      usersItemsRepo.deleteById(id);
    } catch (DataAccessException ex) {
      throw new UsersItemsServiceException(ex.getMessage());
    }
  }
}

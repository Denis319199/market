package com.db.service;

import com.db.exception.UsersItemsServiceException;
import com.db.model.UsersItem;
import com.db.model.UsersItemId;
import java.util.List;

public interface UsersItemsService {
  UsersItem findUsersItemByUsersItemId(UsersItemId id) throws UsersItemsServiceException;

  UsersItem insertUsersItem(UsersItem usersItem) throws UsersItemsServiceException;

  UsersItem updateUsersItem(UsersItem usersItem) throws UsersItemsServiceException;

  void deleteUsersItem(UsersItemId id) throws UsersItemsServiceException;

  void addItemToUserWithoutUserCheck(int userId, int itemId);

  void takeItemFromUserWithoutUserCheck(int userId, int itemId) throws UsersItemsServiceException;
}

package com.db.service;

import com.db.exception.ServiceException;
import com.db.exception.UsersItemsServiceException;
import com.db.model.UsersItem;
import com.db.model.UsersItemId;
import java.util.List;

public interface UsersItemsService {
  List<UsersItem> getAllUsersItems(int page, int size);

  UsersItem findUsersItemByUsersItemId(UsersItemId id) throws UsersItemsServiceException;

  UsersItem insertUsersItem(UsersItem usersItem) throws UsersItemsServiceException;

  UsersItem updateUsersItem(UsersItem usersItem) throws UsersItemsServiceException;

  void deleteUsersItem(UsersItemId id) throws UsersItemsServiceException;

  void addItemToUser(int userId, int itemId) throws ServiceException;

  void takeItemFromUser(int userId, int itemId) throws UsersItemsServiceException, ServiceException;
}

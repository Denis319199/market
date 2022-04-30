package com.db.service;

import com.db.exception.SellingItemsServiceException;
import com.db.exception.ServiceException;
import com.db.model.SellingItem;

public interface SellingItemsService {
  SellingItem findSellingItemById(int id) throws SellingItemsServiceException;

  SellingItem insertSellingItemWithoutUserCheck(SellingItem sellingItem)
      throws SellingItemsServiceException;

  void deleteSellingItem(int id) throws SellingItemsServiceException;

  void sellItemWithoutUserCheck(SellingItem sellingItem) throws SellingItemsServiceException;

  void removeItemFromSale(int id, int sellerId) throws SellingItemsServiceException;

  void purchaseItemWithoutUserCheck(int id, int customerId)
      throws SellingItemsServiceException, ServiceException;
}

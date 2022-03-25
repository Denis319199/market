package com.db.service;

import com.db.exception.SellingItemsServiceException;
import com.db.exception.ServiceException;
import com.db.model.SellingItem;
import java.util.List;

public interface SellingItemsService {
  List<SellingItem> getAllSellingItems(int page, int size);

  SellingItem findSellingItemById(int id) throws SellingItemsServiceException;

  SellingItem insertSellingItem(SellingItem sellingItem) throws SellingItemsServiceException;

  SellingItem updateSellingItem(SellingItem sellingItem) throws SellingItemsServiceException;

  void deleteSellingItem(int id) throws SellingItemsServiceException;

  void sellItem(SellingItem sellingItem) throws SellingItemsServiceException, ServiceException;

  void removeItemFromSale(int id, int sellerId)
      throws SellingItemsServiceException, ServiceException;

  void purchaseItem(int id, int customerId) throws SellingItemsServiceException, ServiceException;

  List<SellingItem> getAllSellingItemsWithFilters(
      int page,
      int size,
      boolean users,
      int userId,
      Integer game,
      String orderBy,
      boolean ascOrder);
}

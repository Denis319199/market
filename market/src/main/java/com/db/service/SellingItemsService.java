package com.db.service;

import com.db.exception.SellingItemsServiceException;
import com.db.model.SellingItem;
import java.util.List;

public interface SellingItemsService {
  List<SellingItem> getAllSellingItems(int page, int size);

  SellingItem findSellingItemById(int id) throws SellingItemsServiceException;

  SellingItem insertSellingItem(SellingItem sellingItem) throws SellingItemsServiceException;

  SellingItem updateSellingItem(SellingItem sellingItem) throws SellingItemsServiceException;

  void deleteSellingItem(int id) throws SellingItemsServiceException;
}

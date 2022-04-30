package com.db.service;

import com.db.exception.ItemsServiceException;
import com.db.model.Item;
import com.db.model.ItemsImage;
import java.util.List;

public interface ItemsService {
  Item findItemById(int id) throws ItemsServiceException;

  Item insertItem(Item item) throws ItemsServiceException;

  Item updateItem(Item item) throws ItemsServiceException;

  void deleteItem(int id) throws ItemsServiceException;

  ItemsImage getItemsImage(int itemId) throws ItemsServiceException;

  void putItemsImage(ItemsImage image) throws ItemsServiceException;

  void deleteItemsImage(int itemId) throws ItemsServiceException;
}

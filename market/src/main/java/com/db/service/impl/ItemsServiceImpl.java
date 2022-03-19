package com.db.service.impl;

import com.db.exception.ItemsServiceException;
import com.db.model.Item;
import com.db.model.ItemsImage;
import com.db.repo.ItemsImagesRepo;
import com.db.repo.ItemsRepo;
import com.db.service.ItemsService;
import com.db.utility.Utilities;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemsServiceImpl implements ItemsService {
  private final ItemsRepo itemsRepo;
  private final ItemsImagesRepo itemsImagesRepo;

  @Transactional(readOnly = true)
  public Item findItemById(int id) throws ItemsServiceException {
    Optional<Item> item = itemsRepo.findById(id);

    if (item.isEmpty()) {
      throw new ItemsServiceException(ItemsServiceException.ITEM_NOT_FOUND);
    }

    return item.get();
  }

  @Transactional(readOnly = true)
  public List<Item> getAllItems(int page, int size) {
    return itemsRepo.findAll(PageRequest.of(page, size)).getContent();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Item insertItem(Item item) throws ItemsServiceException {
    try {
      return itemsRepo.save(item);
    } catch (DataAccessException ex) {
      throw new ItemsServiceException(ex.getMessage());
    }
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Item updateItem(Item item) throws ItemsServiceException {
    Item old = findItemById(item.getId());
    Utilities.merge(item, old);

    try {
      return itemsRepo.save(item);
    } catch (DataAccessException ex) {
      throw new ItemsServiceException(ex.getMessage());
    }
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteItem(int id) throws ItemsServiceException {
    try {
      itemsRepo.deleteById(id);
    } catch (EmptyResultDataAccessException ex) {
      throw new ItemsServiceException(ex.getMessage());
    }
  }

  @Transactional(readOnly = true)
  public ItemsImage getItemsImage(int itemId) throws ItemsServiceException {
    Optional<ItemsImage> image = itemsImagesRepo.findById(itemId);

    if (image.isEmpty()) {
      throw new ItemsServiceException(ItemsServiceException.ITEMS_IMAGE_NOT_FOUND);
    }

    return image.get();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void insertItemsImage(ItemsImage image) throws ItemsServiceException {
    if (itemsImagesRepo.existsById(image.getItemId())) {
      throw new ItemsServiceException(ItemsServiceException.ITEMS_IMAGE_ALREADY_EXISTS);
    }

    itemsImagesRepo.save(image);
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void updateItemsImage(ItemsImage image) throws ItemsServiceException {
    if (!itemsImagesRepo.existsById(image.getItemId())) {
      throw new ItemsServiceException(ItemsServiceException.ITEMS_IMAGE_NOT_FOUND);
    }

    itemsImagesRepo.save(image);
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteItemsImage(int itemId) throws ItemsServiceException {
    try {
      itemsImagesRepo.deleteById(itemId);
    } catch (EmptyResultDataAccessException ex) {
      throw new ItemsServiceException(ex.getMessage());
    }
  }
}

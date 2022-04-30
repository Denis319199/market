package com.db.service.impl;

import com.db.exception.ItemsServiceException;
import com.db.model.Item;
import com.db.model.ItemsImage;
import com.db.repo.ItemsImagesRepo;
import com.db.repo.ItemsRepo;
import com.db.service.ItemsService;
import com.db.utility.Utilities;
import com.db.utility.mapper.ModelMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
  private final ModelMapper modelMapper;

  @Override
  @Transactional(readOnly = true)
  public Item findItemById(int id) throws ItemsServiceException {
    Optional<Item> item = itemsRepo.findById(id);

    if (item.isEmpty()) {
      throw new ItemsServiceException(ItemsServiceException.ITEM_NOT_FOUND);
    }

    return item.get();
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Item insertItem(Item item) throws ItemsServiceException {
    try {
      return itemsRepo.save(item);
    } catch (DataAccessException ex) {
      throw new ItemsServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public Item updateItem(Item item) throws ItemsServiceException {
    Item old = findItemById(item.getId());
    modelMapper.merge(item, old);

    try {
      return itemsRepo.save(item);
    } catch (DataAccessException ex) {
      throw new ItemsServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteItem(int id) throws ItemsServiceException {
    try {
      itemsRepo.deleteById(id);
    } catch (DataAccessException ex) {
      throw new ItemsServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(readOnly = true)
  public ItemsImage getItemsImage(int itemId) throws ItemsServiceException {
    Optional<ItemsImage> image = itemsImagesRepo.findById(itemId);

    if (image.isEmpty()) {
      throw new ItemsServiceException(ItemsServiceException.ITEMS_IMAGE_NOT_FOUND);
    }

    return image.get();
  }

  @Override
  public void putItemsImage(ItemsImage image) throws ItemsServiceException {
    Item item = findItemById(image.getItemId());

    if (!item.getIsImagePresented()) {
      item.setIsImagePresented(true);
      itemsRepo.save(item);
    }

    try {
      itemsImagesRepo.save(image);
    } catch (DataAccessException ex) {
      throw new ItemsServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteItemsImage(int itemId) throws ItemsServiceException {
    try {
      Item item = findItemById(itemId);

      if (!item.getIsImagePresented()) {
        throw new ItemsServiceException(ItemsServiceException.ITEMS_IMAGE_NOT_FOUND);
      }

      item.setIsImagePresented(false);
      itemsRepo.save(item);

      itemsImagesRepo.deleteById(itemId);
    } catch (DataAccessException ex) {
      throw new ItemsServiceException(ex.getMessage());
    }
  }
}

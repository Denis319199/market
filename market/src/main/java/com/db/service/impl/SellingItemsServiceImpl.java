package com.db.service.impl;

import com.db.client.AuthClient;
import com.db.exception.SellingItemsServiceException;
import com.db.model.SellingItem;
import com.db.repo.SellingItemsRepo;
import com.db.service.SellingItemsService;
import com.db.utility.Utilities;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Profile({"prod", "!auth-service-disabled"})
public class SellingItemsServiceImpl implements SellingItemsService {
  protected final SellingItemsRepo sellingItemsRepo;
  protected final AuthClient authClient;

  @Override
  @Transactional(readOnly = true)
  public List<SellingItem> getAllSellingItems(int page, int size) {
    return sellingItemsRepo.findAll(PageRequest.of(page, size)).getContent();
  }

  @Override
  @Transactional(readOnly = true)
  public SellingItem findSellingItemById(int id) throws SellingItemsServiceException {
    Optional<SellingItem> sellingItem = sellingItemsRepo.findById(id);

    if (sellingItem.isEmpty()) {
      throw new SellingItemsServiceException(SellingItemsServiceException.SELLING_ITEM_NOT_FOUND);
    }

    return sellingItem.get();
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public SellingItem insertSellingItem(SellingItem sellingItem)
      throws SellingItemsServiceException {
    if (!authClient.checkUsersExistence(List.of(sellingItem.getSellerId()), true).get(0)) {
      throw new SellingItemsServiceException(SellingItemsServiceException.BAD_SELLER_ID);
    }

    try {
      return sellingItemsRepo.save(sellingItem);
    } catch (DataAccessException ex) {
      throw new SellingItemsServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public SellingItem updateSellingItem(SellingItem sellingItem)
      throws SellingItemsServiceException {
    SellingItem old = findSellingItemById(sellingItem.getId());

    if (Objects.nonNull(sellingItem.getSellerId())) {
      if (!authClient.checkUsersExistence(List.of(sellingItem.getSellerId()), true).get(0)) {
        throw new SellingItemsServiceException(SellingItemsServiceException.BAD_SELLER_ID);
      }
    }

    Utilities.merge(sellingItem, old);

    try {
      return sellingItemsRepo.save(sellingItem);
    } catch (DataAccessException ex) {
      throw new SellingItemsServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteSellingItem(int id) throws SellingItemsServiceException {
    try {
      sellingItemsRepo.deleteById(id);
    } catch (DataAccessException ex) {
      throw new SellingItemsServiceException(ex.getMessage());
    }
  }
}

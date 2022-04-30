package com.db.service.impl;

import com.db.client.AuthClient;
import com.db.exception.PurchasesServiceException;
import com.db.exception.SellingItemsServiceException;
import com.db.exception.ServiceException;
import com.db.exception.UsersItemsServiceException;
import com.db.model.Purchase;
import com.db.model.SellingItem;
import com.db.repo.SellingItemsRepo;
import com.db.service.PurchasesService;
import com.db.service.SellingItemsService;
import com.db.service.UsersItemsService;
import com.db.utility.mapper.ModelMapper;
import com.db.utility.sql.SqlQueryExecutor;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/* Any checks for user's id correctness is unnecessary, since these operations can be made by only plain user
 * authenticated, that is, their id is already known and doesn't require check.
 */
@Service
@RequiredArgsConstructor
public class SellingItemsServiceImpl implements SellingItemsService {
  protected final SellingItemsRepo sellingItemsRepo;
  protected final AuthClient authClient;
  protected final UsersItemsService usersItemsService;
  protected final PurchasesService purchasesService;
  protected final SqlQueryExecutor sqlQueryExecutor;
  protected final ModelMapper modelMapper;

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
  public SellingItem insertSellingItemWithoutUserCheck(SellingItem sellingItem)
      throws SellingItemsServiceException {
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

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void sellItemWithoutUserCheck(SellingItem sellingItem) throws SellingItemsServiceException {
    try {
      usersItemsService.takeItemFromUserWithoutUserCheck(sellingItem.getSellerId(), sellingItem.getItemId());

      insertSellingItemWithoutUserCheck(sellingItem);
    } catch (UsersItemsServiceException ex) {
      throw new SellingItemsServiceException(ex.getMessage());
    }
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void removeItemFromSale(int id, int sellerId) throws SellingItemsServiceException {
    SellingItem sellingItem = findSellingItemById(id);

    if (sellingItem.getSellerId() != sellerId) {
      throw new SellingItemsServiceException(SellingItemsServiceException.USER_IS_NOT_AN_OWNER);
    }

    usersItemsService.addItemToUserWithoutUserCheck(sellerId, sellingItem.getItemId());
    deleteSellingItem(id);
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public void purchaseItemWithoutUserCheck(int sellingItemId, int customerId)
      throws SellingItemsServiceException, ServiceException {
    SellingItem sellingItem = findSellingItemById(sellingItemId);

    if (sellingItem.getSellerId() == customerId) {
      throw new SellingItemsServiceException(
          SellingItemsServiceException.USER_CANT_BUY_THEIR_OWN_ITEM);
    }

    usersItemsService.addItemToUserWithoutUserCheck(customerId, sellingItem.getItemId());

    try {
      purchasesService.insertPurchaseWithoutUsersCheck(
          Purchase.builder()
              .sellerId(sellingItem.getSellerId())
              .customerId(customerId)
              .itemId(sellingItem.getItemId())
              .purchaseDate(LocalDate.now())
              .price(sellingItem.getPrice())
              .build());
    } catch (PurchasesServiceException ex) {
      throw new ServiceException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    deleteSellingItem(sellingItemId);
  }
}

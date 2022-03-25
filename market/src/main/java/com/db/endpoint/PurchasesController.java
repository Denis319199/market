package com.db.endpoint;

import com.db.exception.PurchasesServiceException;
import com.db.exception.ServiceException;
import com.db.model.Purchase;
import com.db.model.dto.purchase.PurchaseInsertDto;
import com.db.model.dto.purchase.PurchaseUpdateDto;
import com.db.service.PurchasesService;
import java.util.List;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
@Validated
public class PurchasesController {
  private final PurchasesService purchasesService;
  private final ModelMapper modelMapper;

  @GetMapping
  List<Purchase> getAllPurchases(int page, int size) {
    return purchasesService.getAllPurchases(page, size);
  }
}

package com.db.service;

import com.db.exception.UsersServiceException;
import com.db.model.User;
import com.db.model.UsersImage;
import com.db.repo.UsersImagesRepo;
import com.db.repo.UsersRepo;
import com.db.utility.Utilities;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersService {
  private final UsersRepo usersRepo;
  private final UsersImagesRepo usersImagesRepo;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public List<User> getUsers(int page, int size) {
    return usersRepo.findAll(PageRequest.of(page, size)).getContent();
  }

  @Transactional(readOnly = true)
  public User findUserByUsername(String username) throws UsersServiceException {
    Optional<User> user = usersRepo.findByUsername(username);

    if (user.isEmpty()) {
      throw new UsersServiceException(UsersServiceException.USER_NOT_FOUND);
    }

    return user.get();
  }

  @Transactional(readOnly = true)
  public boolean checkExistence(int id, boolean onlyEnabled) {
    if (onlyEnabled) {
      return usersRepo.existsByIdAndIsEnabled(id, true);
    } else {
      return usersRepo.existsById(id);
    }
  }

  @Transactional(readOnly = true)
  public User findUserById(int id) throws UsersServiceException {
    Optional<User> user = usersRepo.findById(id);

    if (user.isEmpty()) {
      throw new UsersServiceException(UsersServiceException.USER_NOT_FOUND);
    }

    return user.get();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public User insertUser(User user) throws UsersServiceException {
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    try {
      return usersRepo.save(user);
    } catch (DataAccessException ex) {
      throw new UsersServiceException(ex.getMessage());
    }
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public User updateUser(User user) throws UsersServiceException {
    String password = user.getPassword();
    if (password != null) {
      user.setPassword(passwordEncoder.encode(password));
    }

    User old = findUserById(user.getId());
    Utilities.merge(user, old);

    try {
      return usersRepo.save(user);
    } catch (DataAccessException ex) {
      throw new UsersServiceException(ex.getMessage());
    }
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteUser(int id) throws UsersServiceException {
    try {
      usersRepo.deleteById(id);
    } catch (DataAccessException ex) {
      throw new UsersServiceException(ex.getMessage());
    }
  }

  @Transactional(readOnly = true)
  public byte[] getUsersImage(int userId) throws UsersServiceException {
    Optional<UsersImage> usersImageOptional = usersImagesRepo.findById(userId);

    if (usersImageOptional.isEmpty()) {
      throw new UsersServiceException(UsersServiceException.IMAGE_NOT_FOUND);
    }

    return usersImageOptional.get().getImage();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void putImage(UsersImage usersImage) throws UsersServiceException {
    User user = findUserById(usersImage.getUserId());

    if (!user.getIsImagePresented()) {
      user.setIsImagePresented(true);
      usersRepo.save(user);
    }

    try {
      usersImagesRepo.save(usersImage);
    } catch (DataAccessException ex) {
      throw new UsersServiceException(ex.getMessage());
    }
  }
/*
  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void insertImage(UsersImage usersImage) throws UsersServiceException {
    if (usersImagesRepo.existsById(usersImage.getUserId())) {
      throw new UsersServiceException(UsersServiceException.IMAGE_ALREADY_EXISTS);
    }

    try {
      usersImagesRepo.save(usersImage);
    } catch (DataAccessException ex) {
      throw new UsersServiceException(ex.getMessage());
    }
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void updateImage(UsersImage usersImage) throws UsersServiceException {
    if (!usersImagesRepo.existsById(usersImage.getUserId())) {
      throw new UsersServiceException(UsersServiceException.IMAGE_NOT_FOUND);
    }

    try {
      usersImagesRepo.save(usersImage);
    } catch (DataAccessException ex) {
      throw new UsersServiceException(ex.getMessage());
    }
  }*/

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteImage(int userId) throws UsersServiceException {
    try {
      usersImagesRepo.deleteById(userId);
    } catch (DataAccessException ex) {
      throw new UsersServiceException(ex.getMessage());
    }
  }
}

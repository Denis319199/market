package com.db.service;

import com.db.exception.ServiceException;
import com.db.model.User;
import com.db.model.UsersImage;
import com.db.repo.UsersImagesRepo;
import com.db.repo.UsersRepo;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
  public User findUserByUsername(String username) {
    return usersRepo.findByUsername(username).orElse(null);
  }

  @Transactional(readOnly = true)
  public User findUserById(int id) {
    return usersRepo.findById(id).orElse(null);
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public User insertUser(User user) throws ServiceException {
    if (usersRepo.existsByUsername(user.getUsername())) {
      throw new ServiceException(ServiceException.USER_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return usersRepo.save(user);
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public User saveUser(User user) throws ServiceException {
    User old = findUserById(user.getId());
    if (Objects.isNull(old)) {
      throw new ServiceException(ServiceException.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }

    String newPassword = user.getPassword();
    if (Objects.nonNull(newPassword)) {
      user.setPassword(passwordEncoder.encode(newPassword));
    }

    user.mergeWith(old);
    return usersRepo.save(user);
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteUser(int id) {
    usersRepo.deleteById(id);
  }

  @Transactional(readOnly = true)
  public byte[] getUsersImage(int userId) throws ServiceException {
    Optional<UsersImage> usersImageOptional = usersImagesRepo.findById(userId);

    if (usersImageOptional.isEmpty()) {
      throw new ServiceException(ServiceException.IMAGE_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }

    return usersImageOptional.get().getImage();
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void insertImage(UsersImage usersImage) throws ServiceException {
    if (usersImagesRepo.existsById(usersImage.getUserId())) {
      throw new ServiceException(ServiceException.IMAGE_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
    }

    usersImagesRepo.save(usersImage);
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void updateImage(UsersImage usersImage) throws ServiceException {
    if (!usersImagesRepo.existsById(usersImage.getUserId())) {
      throw new ServiceException(ServiceException.IMAGE_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }

    usersImagesRepo.save(usersImage);
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void deleteImage(int userId) {
    usersImagesRepo.deleteById(userId);
  }
}

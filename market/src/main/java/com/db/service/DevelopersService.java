package com.db.service;

import com.db.exception.DevelopersServiceException;
import com.db.model.Developer;
import java.util.List;

public interface DevelopersService {
  List<Developer> getAllDevelopers(int page, int size);

  Developer findDeveloperById(int id) throws DevelopersServiceException;

  Developer insertDeveloper(Developer developer) throws DevelopersServiceException;

  Developer updateDeveloper(Developer developer) throws DevelopersServiceException;

  void deleteDeveloper(int id) throws DevelopersServiceException;
}

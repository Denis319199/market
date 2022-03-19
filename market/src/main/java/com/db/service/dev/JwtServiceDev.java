package com.db.service.dev;

import com.db.app.configuration.properties.JwtClaimsProperties;
import com.db.app.configuration.properties.JwtProperties;
import com.db.app.configuration.properties.JwtServiceRequestProperties;
import com.db.exception.JwtServiceException;
import com.db.service.impl.JwtServiceImpl;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("unsecured")
public class JwtServiceDev extends JwtServiceImpl {
  public JwtServiceDev(
      JwtProperties jwtProperties,
      JwtClaimsProperties jwtClaimsProperties,
      JwtServiceRequestProperties jwtServiceRequestProperties) {
    super(jwtProperties, jwtClaimsProperties, jwtServiceRequestProperties);
  }

  @Override
  public String getAccessTokenFromRequest(HttpServletRequest request) throws JwtServiceException {
    return createServiceRequestToken();
  }
}

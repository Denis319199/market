package com.db.app.configuration;

import java.util.List;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfiguration {
  @Bean
  public ModelMapper modelMapper(List<? extends Converter<?, ?>> converters) {
    final ModelMapper modelMapper = new ModelMapper();
    for (final Converter<?, ?> converter : converters) {
      modelMapper.addConverter(converter);
    }
    return modelMapper;
  }
}

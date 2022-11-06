package com.example.g31_ffs_be.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;

@Configuration
public class MapperConfig {
        @Bean
        @ElementCollection(fetch = FetchType.LAZY)
        public ModelMapper modelMapper() {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper;
        }
    }


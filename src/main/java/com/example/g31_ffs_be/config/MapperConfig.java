package com.example.g31_ffs_be.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.ElementCollection;
import javax.persistence.FetchType;

@Configuration
public class MapperConfig {
        @Bean
        @ElementCollection(fetch = FetchType.LAZY)
        public ModelMapper MapperConfig() {
            return new ModelMapper();
        }
    }


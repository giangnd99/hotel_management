package com.poly.promotion.application.controller;

import com.poly.promotion.application.service.HateoasLinkBuilderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Simple unit tests for {@link RootController} using JUnit and Mockito.
 * Tests focus on core controller logic without complex HATEOAS structures.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Root Controller Tests")
class RootControllerTest {

    @Mock
    private HateoasLinkBuilderService hateoasLinkBuilderService;

    @InjectMocks
    private RootController rootController;

    private Link[] mockLinks;

    @BeforeEach
    void setUp() {
        mockLinks = new Link[]{Link.of("/test", "self")};
        when(hateoasLinkBuilderService.buildRootLinks()).thenReturn(mockLinks);
    }

    @Test
    @DisplayName("Should return root with HATEOAS links")
    void shouldReturnRootWithHateoasLinks() {
        // When
        ResponseEntity<?> response = rootController.getRoot();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        verify(hateoasLinkBuilderService).buildRootLinks();
    }

    @Test
    @DisplayName("Should return API discovery with HATEOAS links")
    void shouldReturnApiDiscoveryWithHateoasLinks() {
        // When
        ResponseEntity<?> response = rootController.getApiDiscovery();

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        
        verify(hateoasLinkBuilderService).buildRootLinks();
    }

    @Test
    @DisplayName("Should handle HATEOAS service exceptions gracefully")
    void shouldHandleHateoasServiceExceptionsGracefully() {
        // Given
        when(hateoasLinkBuilderService.buildRootLinks())
                .thenThrow(new RuntimeException("HATEOAS service error"));

        // When & Then
        assertThatThrownBy(() -> rootController.getRoot())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("HATEOAS service error");
        
        verify(hateoasLinkBuilderService).buildRootLinks();
    }
}

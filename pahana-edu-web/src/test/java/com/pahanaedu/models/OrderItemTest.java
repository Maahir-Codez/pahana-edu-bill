//package com.pahanaedu.models;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import static org.junit.jupiter.api.Assertions.*;
//
//@DisplayName("OrderItem Model Tests")
//class OrderItemTest {
//
//    @Test
//    @DisplayName("Constructor should calculate lineTotal correctly for integer price and quantity")
//    void constructor_ShouldCalculateLineTotal_ForIntegers() {
//        // Arrange
//        BigDecimal price = new BigDecimal("10.00");
//        int quantity = 5;
//        BigDecimal expectedTotal = new BigDecimal("50.00");
//
//        // Act
//        OrderItem orderItem = new OrderItem(1L, 1L, quantity, price);
//
//        // Assert
//        assertEquals(0, expectedTotal.compareTo(orderItem.getLineTotal()), "Line total should be 10.00 * 5 = 50.00");
//    }
//
//    @Test
//    @DisplayName("Constructor should calculate lineTotal correctly for decimal price")
//    void constructor_ShouldCalculateLineTotal_ForDecimals() {
//        // Arrange
//        BigDecimal price = new BigDecimal("19.99");
//        int quantity = 2;
//        BigDecimal expectedTotal = new BigDecimal("39.98");
//
//        // Act
//        OrderItem orderItem = new OrderItem(1L, 1L, quantity, price);
//
//        // Assert
//        assertEquals(0, expectedTotal.compareTo(orderItem.getLineTotal()), "Line total should be 19.99 * 2 = 39.98");
//    }
//
//    @Test
//    @DisplayName("Constructor should calculate lineTotal correctly for quantity of 1")
//    void constructor_ShouldCalculateLineTotal_ForQuantityOfOne() {
//        // Arrange
//        BigDecimal price = new BigDecimal("123.45");
//        int quantity = 1;
//        BigDecimal expectedTotal = new BigDecimal("123.45");
//
//        // Act
//        OrderItem orderItem = new OrderItem(1L, 1L, quantity, price);
//
//        // Assert
//        assertEquals(0, expectedTotal.compareTo(orderItem.getLineTotal()), "Line total should be equal to the price for quantity 1");
//    }
//
//    @Test
//    @DisplayName("Constructor should calculate lineTotal as zero for zero price")
//    void constructor_ShouldCalculateLineTotal_ForZeroPrice() {
//        // Arrange
//        BigDecimal price = BigDecimal.ZERO;
//        int quantity = 10;
//        BigDecimal expectedTotal = BigDecimal.ZERO;
//
//        // Act
//        OrderItem orderItem = new OrderItem(1L, 1L, quantity, price);
//
//        // Assert
//        assertEquals(0, expectedTotal.compareTo(orderItem.getLineTotal()), "Line total should be zero if the price is zero");
//    }
//
//    @Test
//    @DisplayName("Constructor should handle large numbers correctly")
//    void constructor_ShouldCalculateLineTotal_ForLargeNumbers() {
//        // Arrange
//        BigDecimal price = new BigDecimal("123456.78");
//        int quantity = 100;
//        BigDecimal expectedTotal = new BigDecimal("12345678.00");
//
//        // Act
//        OrderItem orderItem = new OrderItem(1L, 1L, quantity, price);
//
//        // Assert
//        assertEquals(0, expectedTotal.compareTo(orderItem.getLineTotal()), "Line total calculation for large numbers should be accurate");
//    }
//}
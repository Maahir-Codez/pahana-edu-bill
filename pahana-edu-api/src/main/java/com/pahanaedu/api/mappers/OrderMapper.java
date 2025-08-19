package com.pahanaedu.api.mappers;

import com.pahanaedu.api.dto.OrderDTO;
import com.pahanaedu.api.dto.OrderItemDTO;
import com.pahanaedu.models.Customer;
import com.pahanaedu.models.Order;
import com.pahanaedu.models.OrderItem;
import com.pahanaedu.utils.DateTimeUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class OrderMapper {
    private OrderMapper() {}

    public static OrderDTO toDTO(Order order, Customer customer) {
        if (order == null) return null;

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setCustomerName(customer != null ? customer.getFullName() : "N/A"); // Handle case where customer might not be found
        dto.setOrderDate(DateTimeUtil.formatLocalDateTime(order.getOrderDate()));
        dto.setStatus(order.getStatus().name());
        dto.setSubtotal(order.getSubtotal());
        dto.setTotalAmount(order.getTotalAmount());

        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            dto.setItems(
                    order.getOrderItems().stream()
                            .map(OrderMapper::toOrderItemDTO)
                            .collect(Collectors.toList())
            );
        } else {
            dto.setItems(Collections.emptyList());
        }

        return dto;
    }

    public static List<OrderDTO> toDTOList(List<Order> orders, Map<Long, Customer> customersById) {
        return orders.stream()
                .map(order -> toDTO(order, customersById.get(order.getCustomerId())))
                .collect(Collectors.toList());
    }

    public static OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        if (orderItem == null) return null;

        OrderItemDTO dto = new OrderItemDTO();
        // This check is important. The Item object must be loaded by the service/dao first.
        if (orderItem.getItem() != null) {
            dto.setItemName(orderItem.getItem().getName());
        } else {
            dto.setItemName("Item name not available");
        }
        dto.setQuantity(orderItem.getQuantity());
        dto.setPriceAtPurchase(orderItem.getPriceAtPurchase());
        dto.setLineTotal(orderItem.getLineTotal());
        return dto;
    }
}
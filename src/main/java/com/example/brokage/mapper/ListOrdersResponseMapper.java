package com.example.brokage.mapper;

import com.example.brokage.data.dto.OrderDto;
import com.example.brokage.data.response.ListOrdersResponse;
import com.example.brokage.entity.Order;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ListOrdersResponseMapper {
    OrderDto toOrderDto(Order order);

    List<OrderDto> toOrderDtos(List<Order> orders);

    default ListOrdersResponse<OrderDto> toListOrdersResponse(Page<Order> page){
        ListOrdersResponse<OrderDto> listOrdersResponse = new ListOrdersResponse<>();
        listOrdersResponse.setContent(toOrderDtos(page.getContent()));
        listOrdersResponse.setPage(page.getNumber());
        listOrdersResponse.setSize(page.getSize());
        listOrdersResponse.setTotalPages(page.getTotalPages());
        listOrdersResponse.setTotalSize(page.getTotalElements());
        return listOrdersResponse;
    }
}

package com.example.brokage.data.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ListOrdersRequest {
    private Date startDate;
    private Date endDate;
    private int pageSize;
    private int page;
}

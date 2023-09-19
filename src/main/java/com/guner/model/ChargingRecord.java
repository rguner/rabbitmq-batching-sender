package com.guner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargingRecord {
    private String sourceGsm;
    private String targetGsm;
    private Date transactionDate;
}


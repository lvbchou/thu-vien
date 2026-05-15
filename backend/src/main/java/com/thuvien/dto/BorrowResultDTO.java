package com.thuvien.dto;
import java.util.List;
public class BorrowResultDTO {
    private String customerId;
    private List<Long> recordIds;
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String v) { this.customerId = v; }
    public List<Long> getRecordIds() { return recordIds; }
    public void setRecordIds(List<Long> v) { this.recordIds = v; }
}

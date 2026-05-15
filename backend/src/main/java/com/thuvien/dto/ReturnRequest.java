package com.thuvien.dto;
import java.util.List;
public class ReturnRequest {
    private List<Long> recordIds;
    public List<Long> getRecordIds() { return recordIds; }
    public void setRecordIds(List<Long> v) { this.recordIds = v; }
}

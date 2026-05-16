package com.thuvien.dto;
public class ExtendRequest {
    private Long recordId;
    private int additionalDays;
    public Long getRecordId() { return recordId; }
    
    public void setRecordId(Long v) { this.recordId = v; }
    public int getAdditionalDays() { return additionalDays; }
    public void setAdditionalDays(int v) { this.additionalDays = v; }
}

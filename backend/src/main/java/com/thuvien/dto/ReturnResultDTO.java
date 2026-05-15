package com.thuvien.dto;
import java.math.BigDecimal;
import java.util.List;
public class ReturnResultDTO {
    private List<ReturnItemDTO> items;
    private BigDecimal totalFine;
    private BigDecimal totalDeposit;
    private BigDecimal netAmount;
    public List<ReturnItemDTO> getItems() { return items; }
    public void setItems(List<ReturnItemDTO> v) { this.items = v; }
    public BigDecimal getTotalFine() { return totalFine; }
    public void setTotalFine(BigDecimal v) { this.totalFine = v; }
    public BigDecimal getTotalDeposit() { return totalDeposit; }
    public void setTotalDeposit(BigDecimal v) { this.totalDeposit = v; }
    public BigDecimal getNetAmount() { return netAmount; }
    public void setNetAmount(BigDecimal v) { this.netAmount = v; }
}

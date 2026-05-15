package com.thuvien.dto;
import java.util.List;
public class BorrowRequest {
    private List<BorrowItemRequest> items;
    public List<BorrowItemRequest> getItems() { return items; }
    public void setItems(List<BorrowItemRequest> v) { this.items = v; }
}

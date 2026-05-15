package com.thuvien.dto;
import java.util.List;
public class DeleteBooksRequest {
    private List<String> ids;
    public List<String> getIds() { return ids; }
    public void setIds(List<String> v) { this.ids = v; }
}

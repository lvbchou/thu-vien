package com.thuvien.dto;
public class DashboardDTO {
    private long totalBooks;
    private long activeBorrows;
    private long totalCustomers;
    private long overdueCount;
    public long getTotalBooks() { return totalBooks; }
    public void setTotalBooks(long v) { this.totalBooks = v; }
    public long getActiveBorrows() { return activeBorrows; }
    public void setActiveBorrows(long v) { this.activeBorrows = v; }
    public long getTotalCustomers() { return totalCustomers; }
    public void setTotalCustomers(long v) { this.totalCustomers = v; }
    public long getOverdueCount() { return overdueCount; }
    public void setOverdueCount(long v) { this.overdueCount = v; }
}

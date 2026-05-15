export const formatCurrency = (amount) => {
  if (amount == null) return '—';
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
};

export const formatDate = (dateStr) => {
  if (!dateStr) return '—';
  const d = new Date(dateStr);
  return `${d.getDate()}/${d.getMonth() + 1}/${d.getFullYear()}`;
};

export const getBookStatusLabel = (status) => {
  const map = { AVAILABLE: 'Còn', BORROWED: 'Đã mượn', MAINTENANCE: 'Đang bảo hành', LOST: 'Mất' };
  return map[status] || status;
};

export const getBookStatusClass = (status) => {
  const map = { AVAILABLE: 'badge-available', BORROWED: 'badge-borrowed', MAINTENANCE: 'badge-maintenance', LOST: 'badge-lost' };
  return map[status] || '';
};

export const getCardTypeLabel = (type) => {
  const map = { MONTHLY: 'Tháng', YEARLY: 'Năm' };
  return map[type] || type;
};

export const getCardTypeClass = (type) => {
  const map = { MONTHLY: 'badge-monthly', YEARLY: 'badge-yearly' };
  return map[type] || '';
};

export const getCardStatusLabel = (status) => {
  const map = { ACTIVE: 'Đang hoạt động', EXPIRED: 'Hết hạn', BANNED: 'Bị cấm' };
  return map[status] || status;
};

export const getBorrowStatusLabel = (status) => {
  const map = { BORROWING: 'Đang mượn', RETURNED: 'Đã trả', OVERDUE: 'Quá hạn' };
  return map[status] || status;
};

export const getBorrowStatusClass = (status) => {
  const map = { BORROWING: 'badge-borrowed', RETURNED: 'badge-available', OVERDUE: 'badge-overdue' };
  return map[status] || '';
};

export const calcOverdue = (dueDate) => {
  if (!dueDate) return 0;
  const due = new Date(dueDate);
  const today = new Date();
  today.setHours(0,0,0,0);
  due.setHours(0,0,0,0);
  const diff = Math.floor((today - due) / 86400000);
  return Math.max(0, diff);
};

export const toInputDate = (dateStr) => {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  return d.toISOString().split('T')[0];
};

import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, Plus, RotateCcw, RefreshCw, Pencil, Ban, MapPin } from 'lucide-react';
import api from '../utils/api';
import { formatDate, formatCurrency, getCardTypeLabel, getCardTypeClass, getBorrowStatusLabel, getBorrowStatusClass, calcOverdue } from '../utils/helpers';
import { useToast } from '../context/ToastContext';
import CustomerFormModal from '../components/customers/CustomerFormModal';
import RenewCardModal from '../components/customers/RenewCardModal';
import BorrowModal from '../components/customers/BorrowModal';
import ReturnModal from '../components/customers/ReturnModal';
import ConfirmDialog from '../components/common/ConfirmDialog';

export default function CustomerDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const toast = useToast();

  const [customer, setCustomer] = useState(null);
  const [history, setHistory] = useState([]);
  const [filters, setFilters] = useState({ borrowFrom: '', borrowTo: '', returnFrom: '', returnTo: '' });

  const [showEdit, setShowEdit] = useState(false);
  const [showRenew, setShowRenew] = useState(false);
  const [showBorrow, setShowBorrow] = useState(false);
  const [showReturn, setShowReturn] = useState(false);
  const [confirmBan, setConfirmBan] = useState(false);

  const load = () => api.get(`/customers/${id}`).then(r => setCustomer(r.data));
  const loadHistory = () => {
    const p = new URLSearchParams();
    if (filters.borrowFrom) p.set('borrowFrom', filters.borrowFrom);
    if (filters.borrowTo) p.set('borrowTo', filters.borrowTo);
    if (filters.returnFrom) p.set('returnFrom', filters.returnFrom);
    if (filters.returnTo) p.set('returnTo', filters.returnTo);
    api.get(`/customers/${id}/borrow-history?${p}`).then(r => setHistory(r.data));
  };

  useEffect(() => { load(); }, [id]);
  useEffect(() => { loadHistory(); }, [id, filters]);

  const activeBorrows = history.filter(h => h.status === 'BORROWING');

  const handleEditSave = async (data) => {
    try { await api.put(`/customers/${id}`, data); toast('Cập nhật thành công', 'success'); setShowEdit(false); load(); }
    catch (e) { toast(e.response?.data || 'Thất bại', 'error'); }
  };

  const handleRenew = async (cardType) => {
    try { await api.post(`/customers/${id}/renew-card`, { cardType }); toast('Gia hạn thẻ thành công', 'success'); setShowRenew(false); load(); }
    catch { toast('Gia hạn thất bại', 'error'); }
  };

  const handleBan = async () => {
    try { await api.post(`/customers/${id}/ban`); toast('Đã cấm khách hàng', 'info'); setConfirmBan(false); load(); }
    catch { toast('Thất bại', 'error'); }
  };

  const handleUnban = async () => {
    try { await api.post(`/customers/${id}/unban`); toast('Đã huỷ cấm khách hàng', 'success'); load(); }
    catch { toast('Thất bại', 'error'); }
  };

  const handleExtend = async (recordId) => {
    const days = prompt('Gia hạn thêm bao nhiêu ngày?');
    if (!days || isNaN(days)) return;
    try { await api.post('/customers/extend', { recordId, additionalDays: parseInt(days) }); toast('Gia hạn mượn thành công', 'success'); loadHistory(); }
    catch { toast('Gia hạn thất bại', 'error'); }
  };

  if (!customer) return <div className="loading"><div className="spinner" /></div>;

  const cardBadgeClass = customer.cardStatus === 'BANNED' ? 'badge-banned'
    : customer.cardStatus === 'EXPIRED' ? 'badge-expired'
    : getCardTypeClass(customer.cardType);
  const cardBadgeLabel = customer.cardStatus === 'BANNED' ? 'Bị cấm'
    : customer.cardStatus === 'EXPIRED' ? 'Hết hạn'
    : `Thẻ ${getCardTypeLabel(customer.cardType)}`;

  return (
    <>
      <button className="back-link" onClick={() => navigate('/customers')}>
        <ArrowLeft size={15} /> Danh sách khách
      </button>

      <div className="customer-detail-header" style={{display:'flex', justifyContent:'space-between', alignItems:'flex-start', marginBottom:24, flexWrap:'wrap', gap:16}}>
        <div>
          <h1 className="customer-detail-header" style={{fontSize:26, fontWeight:700}}>
            {customer.fullName}
            <span className={`badge ${cardBadgeClass}`} style={{marginLeft:10}}>
              {cardBadgeLabel} · HSD {formatDate(customer.cardExpireDate)}
            </span>
          </h1>
          <p className="customer-meta">{customer.id} · {customer.phone} · CCCD {customer.cccd}</p>
          <p className="customer-meta">Sinh {formatDate(customer.birthDate)} · Tham gia {formatDate(customer.joinDate)}</p>
          {customer.address && (
            <p className="customer-address"><MapPin size={13} /> {customer.address}</p>
          )}
        </div>

        <div className="customer-actions">
          {customer.cardStatus === 'ACTIVE' && (
            <button className="btn btn-primary" onClick={() => setShowBorrow(true)}>
              <Plus size={14} /> Thêm mượn
            </button>
          )}
          <button className="btn btn-secondary" onClick={() => setShowReturn(true)}>
            <RotateCcw size={14} /> Trả sách
          </button>
          <button className="btn btn-secondary" onClick={() => setShowRenew(true)}>
            <RefreshCw size={14} /> Gia hạn thẻ
          </button>
          <button className="btn btn-secondary" onClick={() => setShowEdit(true)}>
            <Pencil size={14} /> Sửa
          </button>
          {customer.cardStatus !== 'BANNED' && (
            <button className="btn btn-danger" onClick={() => setConfirmBan(true)}>
              <Ban size={14} /> Cấm
            </button>
          )}
          {customer.cardStatus === 'BANNED' && (
            <button className="btn btn-secondary" onClick={handleUnban}>
              <Ban size={14} /> Huỷ cấm
            </button>
          )}
        </div>
      </div>

      <div className="stat-cards" style={{gridTemplateColumns:'repeat(4,1fr)', marginBottom:24}}>
        <div className="stat-card"><div className="stat-info"><h2>{customer.totalBorrowed}</h2><p>Sách đã mượn</p></div></div>
        <div className="stat-card"><div className="stat-info"><h2>{customer.currentBorrowing}</h2><p>Đang mượn</p></div></div>
        <div className="stat-card"><div className="stat-info"><h2>{customer.totalReturned}</h2><p>Đã trả</p></div></div>
        <div className="stat-card"><div className="stat-info"><h2>{customer.overdueBooks}</h2><p>Quá hạn</p></div></div>
      </div>

      <div className="section">
        <div className="section-title">Chi tiết các sách đã mượn</div>

        <div className="date-filter-row">
          <div className="date-filter-group"><label>Mượn từ</label><input className="date-input" type="date" value={filters.borrowFrom} onChange={e => setFilters(f => ({...f, borrowFrom: e.target.value}))} /></div>
          <div className="date-filter-group"><label>Mượn đến</label><input className="date-input" type="date" value={filters.borrowTo} onChange={e => setFilters(f => ({...f, borrowTo: e.target.value}))} /></div>
          <div className="date-filter-group"><label>Trả từ</label><input className="date-input" type="date" value={filters.returnFrom} onChange={e => setFilters(f => ({...f, returnFrom: e.target.value}))} /></div>
          <div className="date-filter-group"><label>Trả đến</label><input className="date-input" type="date" value={filters.returnTo} onChange={e => setFilters(f => ({...f, returnTo: e.target.value}))} /></div>
        </div>

        <table>
          <thead>
            <tr>
              <th>Mã sách</th><th>Tên sách</th><th>Tác giả</th>
              <th>Ngày mượn</th><th>Hạn trả</th><th>Ngày trả</th>
              <th>Quá hạn</th><th>Tình trạng trả</th><th>Cọc</th><th>Phạt</th>
              <th>Trạng thái</th><th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {history.length === 0 && (
              <tr><td colSpan={12} className="text-center" style={{padding:32, color:'var(--text-muted)'}}>Chưa có lịch sử mượn</td></tr>
            )}
            {history.map(h => {
              const overdue = h.returnDate ? h.overdueDays : calcOverdue(h.dueDate);
              return (
                <tr key={h.recordId}>
                  <td className="td-link" onClick={() => navigate(`/books/${h.bookId}`)}>{h.bookId}</td>
                  <td>{h.bookTitle}</td>
                  <td>{h.author}</td>
                  <td>{formatDate(h.borrowDate)}</td>
                  <td>{formatDate(h.dueDate)}</td>
                  <td>{formatDate(h.returnDate)}</td>
                  <td>{overdue > 0 ? <span className="text-danger">{overdue}</span> : '0'}</td>
                  <td>—</td>
                  <td>{formatCurrency(h.deposit)}</td>
                  <td>{h.fine > 0 ? <span className="text-danger">{formatCurrency(h.fine)}</span> : '—'}</td>
                  <td><span className={`badge ${getBorrowStatusClass(h.status)}`}>{getBorrowStatusLabel(h.status)}</span></td>
                  <td>
                    {h.status === 'BORROWING' && (
                      <button className="btn btn-ghost btn-sm" onClick={() => handleExtend(h.recordId)}>
                        <RefreshCw size={12} /> Gia hạn
                      </button>
                    )}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>

      {showEdit && <CustomerFormModal customer={customer} onSave={handleEditSave} onClose={() => setShowEdit(false)} />}
      {showRenew && <RenewCardModal customer={customer} onRenew={handleRenew} onClose={() => setShowRenew(false)} />}
      {showBorrow && (
        <BorrowModal customer={customer} onDone={() => { setShowBorrow(false); load(); loadHistory(); toast('Tạo phiếu mượn thành công', 'success'); }} onClose={() => setShowBorrow(false)} />
      )}
      {showReturn && (
        <ReturnModal customer={customer} activeBorrows={activeBorrows} onDone={() => { load(); loadHistory(); toast('Trả sách thành công', 'success'); }} onClose={() => setShowReturn(false)} />
      )}
      {confirmBan && (
        <ConfirmDialog title="Cấm khách hàng" message={`Cấm "${customer.fullName}" không được mượn sách?`}
          onConfirm={handleBan} onCancel={() => setConfirmBan(false)} confirmLabel="Cấm" />
      )}
    </>
  );
}

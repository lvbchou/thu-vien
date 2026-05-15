import React, { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Pencil, Trash2, ArrowUpDown } from 'lucide-react';
import api from '../utils/api';
import { formatDate, getCardTypeLabel, getCardTypeClass } from '../utils/helpers';
import { useToast } from '../context/ToastContext';
import CustomerFormModal from '../components/customers/CustomerFormModal';
import ConfirmDialog from '../components/common/ConfirmDialog';

export default function CustomersPage() {
  const [customers, setCustomers] = useState([]);
  const [search, setSearch] = useState('');
  const [cardFilter, setCardFilter] = useState('all');
  const [sort, setSort] = useState('id_desc');
  const [showForm, setShowForm] = useState(false);
  const [editCustomer, setEditCustomer] = useState(null);
  const [confirmDelete, setConfirmDelete] = useState(null);
  const toast = useToast();
  const navigate = useNavigate();

  const load = useCallback(() => {
    const p = new URLSearchParams();
    if (search) p.set('search', search);
    if (cardFilter !== 'all') p.set('cardType', cardFilter);
    if (sort) p.set('sort', sort);
    api.get(`/customers?${p}`).then(r => setCustomers(r.data));
  }, [search, cardFilter, sort]);

  useEffect(() => { load(); }, [load]);

  const handleSave = async (data) => {
    try {
      if (editCustomer) { await api.put(`/customers/${editCustomer.id}`, data); toast('Cập nhật thành công', 'success'); }
      else { await api.post('/customers', data); toast('Thêm khách hàng thành công', 'success'); }
      setShowForm(false); setEditCustomer(null); load();
    } catch (e) { toast(e.response?.data || 'Thao tác thất bại', 'error'); }
  };

  const handleDelete = async (id) => {
    try {
      await api.delete(`/customers/${id}`);
      toast('Xoá khách hàng thành công', 'success');
      setConfirmDelete(null); load();
    } catch { toast('Xoá thất bại', 'error'); }
  };

  const getCardStatusBadge = (c) => {
    if (c.cardStatus === 'BANNED') return <span className="badge badge-banned">Bị cấm</span>;
    if (c.cardStatus === 'EXPIRED') return <span className="badge badge-expired">Hết hạn</span>;
    return <span className={`badge ${getCardTypeClass(c.cardType)}`}>{getCardTypeLabel(c.cardType)}</span>;
  };

  return (
    <>
      <div className="page-header">
        <div className="page-title">
          <h1>Khách hàng</h1>
        </div>
        <div className="page-controls">
          <select className="filter-select" value={cardFilter} onChange={e => setCardFilter(e.target.value)}>
            <option value="all">Tất cả thẻ</option>
            <option value="MONTHLY">Tháng</option>
            <option value="YEARLY">Năm</option>
            <option value="BANNED">Bị cấm</option>
            <option value="EXPIRED">Hết hạn</option>
          </select>
          <input className="search-input" placeholder="Tìm tên, mã, SĐT, CCCD..." value={search}
            onChange={e => setSearch(e.target.value)} style={{minWidth:220}} />
          <button className="btn btn-primary" onClick={() => { setEditCustomer(null); setShowForm(true); }}>
            <Plus size={15} /> Thêm khách
          </button>
        </div>
      </div>

      <div className="table-wrapper">
        <table>
          <thead>
            <tr>
              <th>Mã</th>
              <th>Họ tên</th>
              <th>SĐT</th>
              <th>CCCD</th>
              <th className="sortable" onClick={() => setSort(s => s === 'birthDate_asc' ? 'birthDate_desc' : 'birthDate_asc')}>
                Ngày sinh <ArrowUpDown size={11} style={{verticalAlign:'middle'}} />
              </th>
              <th className="sortable" onClick={() => setSort(s => s === 'joinDate_asc' ? 'joinDate_desc' : 'joinDate_asc')}>
                Tham gia <ArrowUpDown size={11} style={{verticalAlign:'middle'}} />
              </th>
              <th>Ngày làm thẻ</th>
              <th>Hạn thẻ</th>
              <th className="sortable" onClick={() => setSort(s => s === 'borrowed_desc' ? 'borrowed_asc' : 'borrowed_desc')}>
                Đã mượn <ArrowUpDown size={11} style={{verticalAlign:'middle'}} />
              </th>
              <th>Đã trả</th>
              <th className="sortable" onClick={() => setSort(s => s === 'borrowing_desc' ? 'borrowing_asc' : 'borrowing_desc')}>
                Đang mượn <ArrowUpDown size={11} style={{verticalAlign:'middle'}} />
              </th>
              <th className="sortable" onClick={() => setSort(s => s === 'overdue_desc' ? 'overdue_asc' : 'overdue_desc')}>
                Quá hạn <ArrowUpDown size={11} style={{verticalAlign:'middle'}} />
              </th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {customers.length === 0 && (
              <tr><td colSpan={13} className="text-center" style={{padding:40, color:'var(--text-muted)'}}>Không có dữ liệu</td></tr>
            )}
            {customers.map(c => (
              <tr key={c.id}>
                <td style={{color:'var(--primary)', fontWeight:600}}>{c.id}</td>
                <td>
                  <div style={{display:'flex', alignItems:'center', gap:8}}>
                    <span className="td-link" onClick={() => navigate(`/customers/${c.id}`)}>{c.fullName}</span>
                    {getCardStatusBadge(c)}
                  </div>
                </td>
                <td>{c.phone}</td>
                <td>{c.cccd}</td>
                <td>{formatDate(c.birthDate)}</td>
                <td>{formatDate(c.joinDate)}</td>
                <td>{formatDate(c.cardStartDate)}</td>
                <td>{formatDate(c.cardExpireDate)}</td>
                <td>{c.totalBorrowed}</td>
                <td>{c.totalReturned}</td>
                <td>{c.currentBorrowing}</td>
                <td>{c.overdueBooks > 0 ? <span className="text-danger font-bold">{c.overdueBooks}</span> : 0}</td>
                <td>
                  <div className="action-btns">
                    <button className="btn-icon primary" onClick={() => { setEditCustomer(c); setShowForm(true); }}><Pencil size={14} /></button>
                    <button className="btn-icon danger" onClick={() => setConfirmDelete(c)}><Trash2 size={14} /></button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {showForm && (
        <CustomerFormModal customer={editCustomer} onSave={handleSave} onClose={() => { setShowForm(false); setEditCustomer(null); }} />
      )}

      {confirmDelete && (
        <ConfirmDialog
          title="Xoá khách hàng"
          message={`Bạn có chắc muốn xoá "${confirmDelete.fullName}"?`}
          onConfirm={() => handleDelete(confirmDelete.id)}
          onCancel={() => setConfirmDelete(null)}
        />
      )}
    </>
  );
}

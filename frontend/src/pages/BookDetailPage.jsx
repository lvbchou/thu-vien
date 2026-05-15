import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, Pencil, BookOpen } from 'lucide-react';
import api from '../utils/api';
import { formatCurrency, formatDate, getBookStatusLabel, getBookStatusClass } from '../utils/helpers';
import { useToast } from '../context/ToastContext';
import BookFormModal from '../components/books/BookFormModal';

export default function BookDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const toast = useToast();
  const [book, setBook] = useState(null);
  const [history, setHistory] = useState([]);
  const [showEdit, setShowEdit] = useState(false);
  const [filters, setFilters] = useState({ borrowFrom: '', borrowTo: '', returnFrom: '', returnTo: '' });

  const load = () => {
    api.get(`/books/${id}`).then(r => setBook(r.data));
  };

  const loadHistory = () => {
    const p = new URLSearchParams();
    if (filters.borrowFrom) p.set('borrowFrom', filters.borrowFrom);
    if (filters.borrowTo) p.set('borrowTo', filters.borrowTo);
    if (filters.returnFrom) p.set('returnFrom', filters.returnFrom);
    if (filters.returnTo) p.set('returnTo', filters.returnTo);
    api.get(`/books/${id}/borrow-history?${p}`).then(r => setHistory(r.data));
  };

  useEffect(() => { load(); }, [id]);
  useEffect(() => { loadHistory(); }, [id, filters]);

  const handleSave = async (data) => {
    try {
      await api.put(`/books/${id}`, data);
      toast('Cập nhật sách thành công', 'success');
      setShowEdit(false); load();
    } catch { toast('Cập nhật thất bại', 'error'); }
  };

  if (!book) return <div className="loading"><div className="spinner" /></div>;

  // Calc deposit
  const twoYearsAgo = new Date(); twoYearsAgo.setFullYear(twoYearsAgo.getFullYear() - 2);
  const purchaseDate = book.purchaseDate ? new Date(book.purchaseDate) : null;
  const depositRate = purchaseDate && purchaseDate > twoYearsAgo ? 0.5 : 0.3;
  const deposit = book.price ? book.price * depositRate : 0;

  return (
    <>
      <button className="back-link" onClick={() => navigate('/books')}>
        <ArrowLeft size={15} /> Danh sách sách
      </button>

      <div style={{display:'flex', alignItems:'flex-start', justifyContent:'space-between', marginBottom:24, gap:16}}>
        <div>
          <h1 style={{fontSize:26, fontWeight:700}}>{book.title}</h1>
          <p style={{color:'var(--primary)', fontWeight:500, marginTop:2}}>{book.author}</p>
        </div>
        <button className="btn btn-secondary" onClick={() => setShowEdit(true)}>
          <Pencil size={14} /> Sửa
        </button>
      </div>

      <div className="book-detail-top">
        <div className="book-cover">
          {book.imageUrl ? (
            <img src={book.imageUrl} alt={book.title} />
          ) : (
            <BookOpen size={60} color="var(--text-muted)" />
          )}
        </div>

        <div className="book-info-grid">
          <div className="info-box"><label>Mã sách</label><span>{book.id}</span></div>
          <div className="info-box"><label>Thể loại</label><span>{book.genre}</span></div>
          <div className="info-box"><label>Kệ</label><span>{book.shelf}</span></div>
          <div className="info-box"><label>Giá sách</label><span>{formatCurrency(book.price)}</span></div>
          <div className="info-box"><label>Ngày nhập</label><span>{formatDate(book.importDate)}</span></div>
          <div className="info-box"><label>Ngày mua</label><span>{formatDate(book.purchaseDate)}</span></div>
          <div className="info-box"><label>Tiền cọc dự kiến</label><span>{formatCurrency(deposit)}</span></div>
          <div className="info-box"><label>Tình trạng</label><span>{book.externalCondition}</span></div>
          <div className="info-box">
            <label>Trạng thái</label>
            <span className={`badge ${getBookStatusClass(book.status)}`}>{getBookStatusLabel(book.status)}</span>
          </div>
          <div className="info-box"><label>Ngày trả gần nhất</label><span>{formatDate(book.lastReturnDate)}</span></div>
        </div>
      </div>

      <div className="section">
        <div className="section-title">Lịch sử mượn</div>

        <div className="date-filter-row">
          <div className="date-filter-group"><label>Mượn từ</label><input className="date-input" type="date" value={filters.borrowFrom} onChange={e => setFilters(f => ({...f, borrowFrom: e.target.value}))} /></div>
          <div className="date-filter-group"><label>Mượn đến (hạn)</label><input className="date-input" type="date" value={filters.borrowTo} onChange={e => setFilters(f => ({...f, borrowTo: e.target.value}))} /></div>
          <div className="date-filter-group"><label>Trả từ</label><input className="date-input" type="date" value={filters.returnFrom} onChange={e => setFilters(f => ({...f, returnFrom: e.target.value}))} /></div>
          <div className="date-filter-group"><label>Trả đến</label><input className="date-input" type="date" value={filters.returnTo} onChange={e => setFilters(f => ({...f, returnTo: e.target.value}))} /></div>
        </div>

        <table>
          <thead>
            <tr>
              <th>Mã KH</th><th>Họ tên</th><th>Ngày mượn</th><th>Hạn trả</th>
              <th>Ngày trả</th><th>Quá hạn</th><th>Tổng ngày</th><th>Cọc</th><th>Phạt</th>
            </tr>
          </thead>
          <tbody>
            {history.length === 0 && (
              <tr><td colSpan={9} className="text-center" style={{padding:32,color:'var(--text-muted)'}}>Chưa có lịch sử mượn</td></tr>
            )}
            {history.map(h => (
              <tr key={h.recordId}>
                <td className="td-link" onClick={() => navigate(`/customers/${h.customerId}`)}>{h.customerId}</td>
                <td>{h.customerName}</td>
                <td>{formatDate(h.borrowDate)}</td>
                <td>{formatDate(h.dueDate)}</td>
                <td>{formatDate(h.returnDate)}</td>
                <td>{h.overdueDays > 0 ? <span className="text-danger">{h.overdueDays}</span> : '0'}</td>
                <td>{h.totalDays}</td>
                <td>{formatCurrency(h.deposit)}</td>
                <td>{h.fine > 0 ? <span className="text-danger">{formatCurrency(h.fine)}</span> : '—'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {showEdit && (
        <BookFormModal book={book} onSave={handleSave} onClose={() => setShowEdit(false)} />
      )}
    </>
  );
}

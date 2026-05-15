import React, { useState, useCallback, useRef } from 'react';
import { X, Plus, Trash2 } from 'lucide-react';
import api from '../../utils/api';
import { formatDate, formatCurrency } from '../../utils/helpers';

function addDays(n) {
  const d = new Date();
  d.setDate(d.getDate() + n);
  return d.toISOString().split('T')[0];
}

export default function BorrowModal({ customer, onDone, onClose }) {
  const [items, setItems] = useState([{ bookQuery: '', bookData: null, days: 14, suggestions: [], showDropdown: false }]);
  const timers = useRef({});

  const updateItem = (i, patch) => setItems(prev => prev.map((x, idx) => idx === i ? { ...x, ...patch } : x));

  const searchBooks = async (i, query) => {
    updateItem(i, { bookQuery: query, bookData: null, showDropdown: true });
    if (timers.current[i]) clearTimeout(timers.current[i]);
    if (!query.trim()) { updateItem(i, { suggestions: [] }); return; }
    timers.current[i] = setTimeout(async () => {
      try {
        const res = await api.get(`/books/search-available?keyword=${encodeURIComponent(query)}`);
        updateItem(i, { suggestions: res.data });
      } catch {}
    }, 300);
  };

  const selectBook = (i, book) => {
    const alreadySelected = items.some((x, idx) => idx !== i && x.bookData?.id === book.id);
    if (alreadySelected) {
      alert(`Sách "${book.title}" đã được thêm vào danh sách!`);
      updateItem(i, { bookQuery: '', bookData: null, suggestions: [], showDropdown: false });
      return;
    }
    updateItem(i, { bookData: book, bookQuery: `${book.title} / ${book.id}`, suggestions: [], showDropdown: false });
  };

  const addItem = () => {
    if (items.length >= 5 - (customer.currentBorrowing || 0)) return;
    setItems(prev => [...prev, { bookQuery: '', bookData: null, days: 14, suggestions: [], showDropdown: false }]);
  };

  const removeItem = (i) => setItems(prev => prev.filter((_, idx) => idx !== i));

  const handleSubmit = async () => {
    const validItems = items.filter(x => x.bookData);
    if (validItems.length === 0) return alert('Vui lòng chọn ít nhất 1 sách');
    // Check duplicates
    const ids = validItems.map(x => x.bookData.id);
    if (new Set(ids).size !== ids.length) return alert('Danh sách có sách bị trùng, vui lòng kiểm tra lại!');
    try {
      await api.post(`/customers/${customer.id}/borrow`, {
        items: validItems.map(x => ({ bookId: x.bookData.id, days: Number(x.days) }))
      });
      onDone();
    } catch (e) {
      alert(e.response?.data || 'Thêm phiếu mượn thất bại');
    }
  };

  const maxCanBorrow = 5 - (customer.currentBorrowing || 0);

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Thêm phiếu mượn</h2>
          <button className="modal-close" onClick={onClose}><X size={18} /></button>
        </div>
        <div className="modal-body">
          <div style={{background:'var(--bg)', borderRadius:'var(--radius)', padding:'10px 14px', marginBottom:16}}>
            <strong>{customer.fullName} ({customer.id})</strong>
            <p style={{fontSize:12.5, color:'var(--text-secondary)', marginTop:2}}>
              Đang mượn: {customer.currentBorrowing}/5 sách
            </p>
          </div>

          <div style={{marginBottom:10, fontSize:13, fontWeight:500}}>Danh sách sách mượn</div>

          {items.map((item, i) => (
            <div key={i} className="borrow-book-item">
              <div className="borrow-book-item-row">
                <div className="autocomplete-wrapper" style={{flex:1}}>
                  <input
                    className="form-input"
                    style={{width:'100%'}}
                    placeholder="Tên sách hoặc mã (vd: Đắc Nhân Tâm / S001)"
                    value={item.bookQuery}
                    onChange={e => searchBooks(i, e.target.value)}
                    onFocus={() => updateItem(i, { showDropdown: true })}
                    onBlur={() => setTimeout(() => updateItem(i, { showDropdown: false }), 150)}
                  />
                  {item.showDropdown && item.suggestions.length > 0 && (
                    <div className="autocomplete-dropdown">
                      {item.suggestions.map(b => (
                        <div key={b.id} className="autocomplete-item" onMouseDown={() => selectBook(i, b)}>
                          <h4>{b.title} <span style={{color:'var(--text-muted)', fontWeight:400}}>({b.id})</span></h4>
                          <p>{b.author} · {formatCurrency(b.price)}</p>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
                {items.length > 1 && (
                  <button className="btn-icon danger" onClick={() => removeItem(i)}><Trash2 size={14} /></button>
                )}
              </div>

              {item.bookData && (
                <div style={{fontSize:12.5, color:'var(--text-secondary)', marginBottom:8}}>
                  Giá: {formatCurrency(item.bookData.price)} · 
                  Cọc: {formatCurrency(item.bookData.price * (new Date(item.bookData.purchaseDate) > new Date(Date.now() - 2*365*86400000) ? 0.5 : 0.3))}
                </div>
              )}

              <div className="days-input-row">
                <span style={{fontSize:13, color:'var(--text-secondary)'}}>Số ngày</span>
                <input className="days-input" type="number" min={1} max={60} value={item.days}
                  onChange={e => updateItem(i, { days: e.target.value })} />
                <span style={{fontSize:13}}>→ Trả ngày <strong>{formatDate(addDays(Number(item.days)))}</strong></span>
              </div>
            </div>
          ))}

          {items.length < maxCanBorrow && (
            <button className="btn btn-ghost btn-sm" onClick={addItem} style={{marginTop:8}}>
              <Plus size={14} /> Thêm sách mượn
            </button>
          )}
        </div>
        <div className="modal-footer">
          <button className="btn btn-secondary" onClick={onClose}>Huỷ</button>
          <button className="btn btn-primary" onClick={handleSubmit}>Hoàn thành</button>
        </div>
      </div>
    </div>
  );
}

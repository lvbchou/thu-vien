import React, { useState, useEffect } from 'react';
import { X } from 'lucide-react';

const STATUSES = [
  { value: 'AVAILABLE', label: 'Còn' },
  { value: 'BORROWED', label: 'Đã mượn' },
  { value: 'MAINTENANCE', label: 'Đang bảo hành' },
  { value: 'LOST', label: 'Mất' },
];

const today = new Date().toISOString().split('T')[0];

export default function BookFormModal({ book, onSave, onClose }) {
  const isEdit = !!book?.id;
  const [form, setForm] = useState({
    id: '', title: '', author: '', publisher: '', genre: '',
    price: 0, shelf: '', importDate: today, purchaseDate: today,
    externalCondition: 'Tốt', status: 'AVAILABLE', imageUrl: '',
    ...book,
    importDate: book?.importDate ? book.importDate.substring(0,10) : today,
    purchaseDate: book?.purchaseDate ? book.purchaseDate.substring(0,10) : today,
  });

  const set = (k, v) => setForm(f => ({ ...f, [k]: v }));

  const handleSubmit = () => {
    if (!form.title) return alert('Vui lòng nhập tên sách');
    onSave(form);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>{isEdit ? 'Chỉnh sửa sách' : 'Thêm sách'}</h2>
          <button className="modal-close" onClick={onClose}><X size={18} /></button>
        </div>
        <div className="modal-body">
          <div className="form-grid">
            {isEdit && (
              <div className="form-group">
                <label className="form-label">Mã sách</label>
                <input className="form-input" value={form.id} disabled style={{background:'#f5f5f5', color:'#888'}} />
              </div>
            )}
            <div className="form-group">
              <label className="form-label">Tên sách</label>
              <input className="form-input" value={form.title} onChange={e => set('title', e.target.value)} />
            </div>
            <div className="form-group">
              <label className="form-label">Tác giả</label>
              <input className="form-input" value={form.author} onChange={e => set('author', e.target.value)} />
            </div>
            <div className="form-group">
              <label className="form-label">Nhà xuất bản</label>
              <input className="form-input" value={form.publisher} onChange={e => set('publisher', e.target.value)} />
            </div>
            <div className="form-group">
              <label className="form-label">Thể loại</label>
              <input className="form-input" value={form.genre} onChange={e => set('genre', e.target.value)} />
            </div>
            <div className="form-group">
              <label className="form-label">Kệ</label>
              <input className="form-input" value={form.shelf} onChange={e => set('shelf', e.target.value)} placeholder="VD: A1" />
            </div>
            <div className="form-group">
              <label className="form-label">Giá nhập (VND)</label>
              <input className="form-input" type="number" value={form.price} onChange={e => set('price', e.target.value)} />
            </div>
            <div className="form-group">
              <label className="form-label">Trạng thái</label>
              <select className="form-select" value={form.status} onChange={e => set('status', e.target.value)}>
                {STATUSES.map(s => <option key={s.value} value={s.value}>{s.label}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Ngày nhập</label>
              <input className="form-input" type="date" value={form.importDate} onChange={e => set('importDate', e.target.value)} />
            </div>
            <div className="form-group">
              <label className="form-label">Ngày mua</label>
              <input className="form-input" type="date" value={form.purchaseDate} onChange={e => set('purchaseDate', e.target.value)} />
            </div>
            <div className="form-group full">
              <label className="form-label">Ảnh (URL)</label>
              <input className="form-input" value={form.imageUrl} onChange={e => set('imageUrl', e.target.value)} placeholder="https://..." />
            </div>
            <div className="form-group full">
              <label className="form-label">Tình trạng bề ngoài (nứt/rách...)</label>
              <textarea className="form-textarea" value={form.externalCondition} onChange={e => set('externalCondition', e.target.value)} />
            </div>
          </div>
        </div>
        <div className="modal-footer">
          <button className="btn btn-secondary" onClick={onClose}>Huỷ</button>
          <button className="btn btn-primary" onClick={handleSubmit}>Lưu</button>
        </div>
      </div>
    </div>
  );
}

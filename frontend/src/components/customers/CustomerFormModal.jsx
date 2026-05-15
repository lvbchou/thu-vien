import React, { useState } from 'react';
import { X } from 'lucide-react';
import { formatCurrency } from '../../utils/helpers';

const today = new Date().toISOString().split('T')[0];

function calcExpiry(type, startDate) {
  if (!startDate) return '';
  const d = new Date(startDate);
  if (type === 'MONTHLY') d.setMonth(d.getMonth() + 1);
  else d.setFullYear(d.getFullYear() + 1);
  return d.toISOString().split('T')[0];
}

export default function CustomerFormModal({ customer, onSave, onClose }) {
  const isEdit = !!customer?.id;
  const [form, setForm] = useState({
    id: '', fullName: '', phone: '', cccd: '',
    birthDate: '', address: '', cardType: 'MONTHLY',
    cardStartDate: today, cardExpireDate: '',
    ...customer,
    birthDate: customer?.birthDate ? customer.birthDate.substring(0,10) : '',
    cardStartDate: customer?.cardStartDate ? customer.cardStartDate.substring(0,10) : today,
    cardExpireDate: customer?.cardExpireDate ? customer.cardExpireDate.substring(0,10) : calcExpiry('MONTHLY', today),
  });

  const set = (k, v) => setForm(f => {
    const next = { ...f, [k]: v };
    if (k === 'cardType' || k === 'cardStartDate') {
      next.cardExpireDate = calcExpiry(next.cardType, next.cardStartDate);
    }
    return next;
  });

  const fee = form.cardType === 'MONTHLY' ? 100000 : 600000;

  const handleSubmit = () => {
    if (!form.fullName) return alert('Vui lòng nhập họ tên');
    onSave(form);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>{isEdit ? 'Chỉnh sửa khách hàng' : 'Thêm khách'}</h2>
          <button className="modal-close" onClick={onClose}><X size={18} /></button>
        </div>
        <div className="modal-body">
          <div className="form-grid">
            {isEdit && (
              <div className="form-group">
                <label className="form-label">Mã khách</label>
                <input className="form-input" value={form.id} disabled style={{background:'#f5f5f5', color:'#888'}} />
              </div>
            )}
            <div className="form-group">
              <label className="form-label">Họ tên</label>
              <input className="form-input" value={form.fullName} onChange={e => set('fullName', e.target.value)} />
            </div>
            <div className="form-group">
              <label className="form-label">Số điện thoại</label>
              <input className="form-input" value={form.phone} onChange={e => set('phone', e.target.value)} placeholder="0901234567" />
            </div>
            <div className="form-group">
              <label className="form-label">CCCD</label>
              <input className="form-input" value={form.cccd} onChange={e => set('cccd', e.target.value)} placeholder="012345678901" />
            </div>
            <div className="form-group">
              <label className="form-label">Ngày sinh</label>
              <input className="form-input" type="date" value={form.birthDate} onChange={e => set('birthDate', e.target.value)} />
            </div>
            <div className="form-group">
              <label className="form-label">Ngày làm thẻ</label>
              <input className="form-input" type="date" value={form.cardStartDate} onChange={e => set('cardStartDate', e.target.value)} />
            </div>
            <div className="form-group full">
              <label className="form-label">Địa chỉ</label>
              <input className="form-input" value={form.address} onChange={e => set('address', e.target.value)} />
            </div>
            <div className="form-group">
              <label className="form-label">Loại thẻ thành viên</label>
              <select className="form-select" value={form.cardType} onChange={e => set('cardType', e.target.value)}>
                <option value="MONTHLY">Tháng – 100.000 đ</option>
                <option value="YEARLY">Năm – 600.000 đ</option>
              </select>
            </div>
            <div className="form-group">
              <label className="form-label">Hạn thẻ</label>
              <input className="form-input" type="date" value={form.cardExpireDate} onChange={e => set('cardExpireDate', e.target.value)} />
            </div>
          </div>
          <p className="form-hint" style={{marginTop:10}}>
            Phí thẻ sẽ là {formatCurrency(fee)} (phiếu chi).
          </p>
        </div>
        <div className="modal-footer">
          <button className="btn btn-secondary" onClick={onClose}>Huỷ</button>
          <button className="btn btn-primary" onClick={handleSubmit}>Lưu</button>
        </div>
      </div>
    </div>
  );
}

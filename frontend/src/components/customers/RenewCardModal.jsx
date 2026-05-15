import React, { useState } from 'react';
import { X } from 'lucide-react';
import { formatDate, formatCurrency } from '../../utils/helpers';

function calcNewExpiry(type, currentExpiry) {
  const start = currentExpiry && new Date(currentExpiry) > new Date()
    ? new Date(new Date(currentExpiry).getTime() + 86400000)
    : new Date();
  const end = new Date(start);
  if (type === 'MONTHLY') end.setMonth(end.getMonth() + 1);
  else end.setFullYear(end.getFullYear() + 1);
  return { start: start.toISOString().split('T')[0], end: end.toISOString().split('T')[0] };
}

export default function RenewCardModal({ customer, onRenew, onClose }) {
  const [cardType, setCardType] = useState(customer.cardType || 'MONTHLY');
  const { start, end } = calcNewExpiry(cardType, customer.cardExpireDate);
  const fee = cardType === 'MONTHLY' ? 100000 : 600000;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal modal-sm" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Gia hạn thẻ</h2>
          <button className="modal-close" onClick={onClose}><X size={18} /></button>
        </div>
        <div className="modal-body">
          <p style={{marginBottom:4}}>Khách: <strong>{customer.fullName}</strong></p>
          <p style={{marginBottom:16, fontSize:13, color:'var(--text-secondary)'}}>
            Hạn thẻ hiện tại: <strong>{formatDate(customer.cardExpireDate)}</strong>
          </p>

          <div className="form-group">
            <label className="form-label">Loại thẻ</label>
            <select className="form-select" value={cardType} onChange={e => setCardType(e.target.value)}>
              <option value="MONTHLY">Tháng – 600.000 đ</option>
              <option value="YEARLY">Năm – 600.000 đ</option>
            </select>
          </div>

          <div className="renew-info-box" style={{marginTop:16}}>
            <div className="renew-info-item">
              <label>Bắt đầu</label>
              <span>{formatDate(start)}</span>
            </div>
            <div className="renew-info-item">
              <label>Hết hạn mới</label>
              <span>{formatDate(end)}</span>
            </div>
          </div>
        </div>
        <div className="modal-footer">
          <button className="btn btn-secondary" onClick={onClose}>Huỷ</button>
          <button className="btn btn-primary" onClick={() => onRenew(cardType)}>
            Xác nhận · {formatCurrency(fee)}
          </button>
        </div>
      </div>
    </div>
  );
}

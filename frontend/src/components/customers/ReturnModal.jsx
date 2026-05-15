import React, { useState } from 'react';
import { X } from 'lucide-react';
import api from '../../utils/api';
import { formatDate, formatCurrency, calcOverdue } from '../../utils/helpers';

export default function ReturnModal({ customer, activeBorrows, onDone, onClose }) {
  const [selected, setSelected] = useState([]);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const toggle = (id) => setSelected(prev => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]);

  const handleConfirm = async () => {
    if (selected.length === 0) return alert('Vui lòng chọn ít nhất 1 sách');
    setLoading(true);
    try {
      const res = await api.post(`/customers/${customer.id}/return`, { recordIds: selected });
      setResult(res.data);
    } catch (e) {
      alert(e.response?.data || 'Trả sách thất bại');
    }
    setLoading(false);
  };

  if (result) {
    const net = result.netAmount;
    const isRefund = net < 0;
    return (
      <div className="modal-overlay" onClick={onClose}>
        <div className="modal" onClick={e => e.stopPropagation()}>
          <div className="modal-header">
            <h2>Kết quả trả sách</h2>
            <button className="modal-close" onClick={onClose}><X size={18} /></button>
          </div>
          <div className="modal-body">
            {result.items.map(item => (
              <div key={item.recordId} style={{padding:'10px 0', borderBottom:'1px solid var(--border)'}}>
                <div style={{display:'flex', justifyContent:'space-between', alignItems:'center'}}>
                  <div>
                    <strong>{item.bookTitle}</strong>
                    <p style={{fontSize:12.5, color:'var(--text-secondary)', marginTop:2}}>
                      {item.overdueDays > 0 ? `Quá hạn ${item.overdueDays} ngày` : 'Đúng hạn'}
                    </p>
                  </div>
                  <div style={{textAlign:'right'}}>
                    <div style={{fontSize:12.5, color:'var(--text-secondary)'}}>Cọc: {formatCurrency(item.deposit)}</div>
                    {item.fine > 0 && <div style={{fontSize:12.5, color:'var(--danger)'}}>Phạt: +{formatCurrency(item.fine)}</div>}
                  </div>
                </div>
              </div>
            ))}

            <div className="return-summary">
              <div className="return-summary-row"><span>Tổng cọc đã nhận</span><span>{formatCurrency(result.totalDeposit)}</span></div>
              <div className="return-summary-row"><span>Tổng tiền phạt</span><span className="text-danger">{formatCurrency(result.totalFine)}</span></div>
              <div className="return-summary-total">
                <span>{isRefund ? '→ Hoàn lại cho khách' : '→ Khách phải trả thêm'}</span>
                <span style={{color: isRefund ? 'var(--success)' : 'var(--danger)'}}>
                  {formatCurrency(Math.abs(net))}
                </span>
              </div>
            </div>
          </div>
          <div className="modal-footer">
            <button className={`btn ${isRefund ? 'btn-secondary' : 'btn-primary'}`} onClick={() => { onDone(); onClose(); }}>
              {isRefund ? `Hoàn ${formatCurrency(Math.abs(net))} cho khách` : `Nhận ${formatCurrency(Math.abs(net))} từ khách`}
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>Trả sách</h2>
          <button className="modal-close" onClick={onClose}><X size={18} /></button>
        </div>
        <div className="modal-body">
          <p style={{marginBottom:14, color:'var(--text-secondary)', fontSize:13.5}}>Chọn các sách cần trả:</p>
          {activeBorrows.length === 0 && (
            <p style={{textAlign:'center', color:'var(--text-muted)', padding:24}}>Không có sách đang mượn</p>
          )}
          {activeBorrows.map(r => {
            const overdue = calcOverdue(r.dueDate);
            return (
              <div key={r.recordId} className="return-book-item">
                <input type="checkbox" checked={selected.includes(r.recordId)} onChange={() => toggle(r.recordId)} />
                <div className="return-book-info">
                  <h4>{r.bookTitle} <span style={{fontWeight:400, color:'var(--text-muted)', fontSize:13}}>({r.bookId})</span></h4>
                  <p>Hạn {formatDate(r.dueDate)} · Cọc {formatCurrency(r.deposit)}</p>
                </div>
                <div className="return-fine">
                  <span className={`badge ${overdue > 0 ? 'badge-overdue' : 'badge-on-time'}`}>
                    {overdue > 0 ? `Quá ${overdue} ngày` : 'Đúng hạn'}
                  </span>
                  <div style={{marginTop:4, fontSize:13}}>
                    {overdue > 0
                      ? <span className="fine-amount">{formatCurrency(r.fine || 0)}</span>
                      : <span className="fine-zero">0 đ</span>}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
        <div className="modal-footer">
          <button className="btn btn-secondary" onClick={onClose}>Huỷ</button>
          <button className="btn btn-primary" onClick={handleConfirm} disabled={loading || selected.length === 0}>
            {loading ? 'Đang xử lý...' : 'Xác nhận'}
          </button>
        </div>
      </div>
    </div>
  );
}

import React from 'react';
import { AlertTriangle, X } from 'lucide-react';

export default function ConfirmDialog({ title, message, onConfirm, onCancel, confirmLabel = 'Xoá', confirmClass = 'btn-danger' }) {
  return (
    <div className="modal-overlay" onClick={onCancel}>
      <div className="modal modal-sm confirm-modal" onClick={e => e.stopPropagation()}>
        <div className="modal-body" style={{textAlign:'center', padding:'28px 24px 0'}}>
          <div className="confirm-icon">
            <AlertTriangle size={26} color="var(--danger)" />
          </div>
          <h3 style={{fontSize:17, fontWeight:600, marginBottom:8}}>{title}</h3>
          <p style={{color:'var(--text-secondary)', fontSize:13.5, marginBottom:8}}>{message}</p>
        </div>
        <div className="modal-footer" style={{justifyContent:'center', paddingTop:16}}>
          <button className="btn btn-secondary" onClick={onCancel}>Huỷ</button>
          <button className={`btn ${confirmClass}`} onClick={onConfirm}>{confirmLabel}</button>
        </div>
      </div>
    </div>
  );
}

import React, { useEffect, useState } from 'react';
import { BookOpen, BookMarked, Users, AlertTriangle } from 'lucide-react';
import api from '../utils/api';
import { formatDate } from '../utils/helpers';

export default function Dashboard() {
  const [data, setData] = useState(null);

  useEffect(() => {
    api.get('/dashboard').then(r => setData(r.data)).catch(console.error);
  }, []);

  if (!data) return <div className="loading"><div className="spinner" /> Đang tải...</div>;

  return (
    <>
      <div className="page-header">
        <div className="page-title">
          <h1>Tổng quan</h1>
        </div>
      </div>

      <div className="stat-cards">
        <div className="stat-card">
          <div className="stat-icon orange"><BookOpen size={22} /></div>
          <div className="stat-info">
            <h2>{data.totalBooks}</h2>
            <p>Tổng sách</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon blue"><BookMarked size={22} /></div>
          <div className="stat-info">
            <h2>{data.activeBorrows}</h2>
            <p>Đang mượn</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon green"><Users size={22} /></div>
          <div className="stat-info">
            <h2>{data.totalCustomers}</h2>
            <p>Khách hàng</p>
          </div>
        </div>
        <div className="stat-card">
          <div className="stat-icon red"><AlertTriangle size={22} /></div>
          <div className="stat-info">
            <h2>{data.overdueCount}</h2>
            <p>Quá hạn</p>
          </div>
        </div>
      </div>
    </>
  );
}

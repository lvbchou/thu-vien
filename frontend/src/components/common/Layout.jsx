import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { BookOpen, LayoutDashboard, Users, LogOut, Menu, ChevronLeft, Book } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

export default function Layout({ children }) {
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const { username, logout } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  const navItems = [
    { path: '/dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { path: '/books', label: 'Sách', icon: Book },
    { path: '/customers', label: 'Khách hàng', icon: Users },
  ];

  const handleLogout = () => { logout(); navigate('/login'); };

  return (
    <div className="app-layout">
      <aside className={`sidebar ${!sidebarOpen ? 'collapsed' : ''}`}>
        <div className="sidebar-logo">
          <div className="sidebar-logo-icon">
            <BookOpen size={18} color="white" />
          </div>
          <span className="sidebar-logo-text">Thư Viện</span>
        </div>

        <nav className="sidebar-nav">
          {navItems.map(({ path, label, icon: Icon }) => (
            <button
              key={path}
              className={`nav-item ${location.pathname === path || (path !== '/' && location.pathname.startsWith(path)) ? 'active' : ''}`}
              onClick={() => navigate(path)}
            >
              <Icon size={17} />
              <span>{label}</span>
            </button>
          ))}
        </nav>

        <div className="sidebar-footer">
          <button className="sidebar-collapse-btn" onClick={() => setSidebarOpen(false)}>
            <ChevronLeft size={16} />
            <span>Thu gọn</span>
          </button>
        </div>
      </aside>

      <div className={`main-content ${!sidebarOpen ? 'sidebar-collapsed' : ''}`}>
        <header className="topbar">
          <div className="topbar-left">
            {!sidebarOpen && (
              <>
                <button className="toggle-sidebar-btn" onClick={() => setSidebarOpen(true)}>
                  <Menu size={18} />
                </button>
                <div className="topbar-logo-collapsed">
                  <div className="logo-icon">
                    <BookOpen size={16} color="white" />
                  </div>
                  <span>Thư Viện</span>
                </div>
              </>
            )}
            {sidebarOpen && (
              <button className="toggle-sidebar-btn" onClick={() => setSidebarOpen(false)}>
                <Menu size={18} />
              </button>
            )}
          </div>
          <div className="topbar-right">
            <button className="logout-btn" onClick={handleLogout}>
              <LogOut size={14} />
              Đăng xuất
            </button>
          </div>
        </header>

        <main className="page-content">
          {children}
        </main>
      </div>
    </div>
  );
}

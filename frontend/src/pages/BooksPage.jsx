import React, { useEffect, useState, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, Pencil, Trash2, ArrowUpDown } from 'lucide-react';
import api from '../utils/api';
import { formatCurrency, formatDate, getBookStatusLabel, getBookStatusClass } from '../utils/helpers';
import { useToast } from '../context/ToastContext';
import BookFormModal from '../components/books/BookFormModal';
import ConfirmDialog from '../components/common/ConfirmDialog';

export default function BooksPage() {
  const [books, setBooks] = useState([]);
  const [genres, setGenres] = useState([]);
  const [search, setSearch] = useState('');
  const [genre, setGenre] = useState('all');
  const [sort, setSort] = useState('id_desc');
  const [priceFilter, setPriceFilter] = useState('all');
  const [selected, setSelected] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editBook, setEditBook] = useState(null);
  const [confirmDelete, setConfirmDelete] = useState(null);
  const [confirmBulk, setConfirmBulk] = useState(false);
  const toast = useToast();
  const navigate = useNavigate();

  const load = useCallback(() => {
    const params = new URLSearchParams();
    if (search) params.set('search', search);
    if (genre !== 'all') params.set('genre', genre);
    if (sort) params.set('sort', sort);
    api.get(`/books?${params}`).then(r => {
      let data = r.data;
      // Filter giá ở frontend
      if (priceFilter === 'under50') data = data.filter(b => b.price < 50000);
      else if (priceFilter === '50to100') data = data.filter(b => b.price >= 50000 && b.price <= 100000);
      else if (priceFilter === '100to200') data = data.filter(b => b.price > 100000 && b.price <= 200000);
      else if (priceFilter === 'over200') data = data.filter(b => b.price > 200000);
      setBooks(data);
    });
    api.get('/books/genres').then(r => setGenres(r.data));
  }, [search, genre, sort, priceFilter]);

  useEffect(() => { load(); }, [load]);

  const handleSelectAll = (e) => setSelected(e.target.checked ? books.map(b => b.id) : []);
  const toggleSelect = (id) => setSelected(prev => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]);

  const handleSave = async (data) => {
    try {
      if (data.id && books.find(b => b.id === data.id && editBook)) {
        await api.put(`/books/${data.id}`, data);
        toast('Cập nhật sách thành công', 'success');
      } else {
        await api.post('/books', data);
        toast('Thêm sách thành công', 'success');
      }
      setShowForm(false); setEditBook(null); load();
    } catch (e) {
      toast(e.response?.data || 'Thao tác thất bại', 'error');
    }
  };

  const handleDelete = async (id) => {
    try {
      await api.delete(`/books/${id}`);
      toast('Xoá sách thành công', 'success');
      setConfirmDelete(null); load();
    } catch { toast('Xoá thất bại', 'error'); }
  };

  const handleBulkDelete = async () => {
    try {
      await api.delete('/books/batch', { data: { ids: selected } });
      toast(`Đã xoá ${selected.length} sách`, 'success');
      setSelected([]); setConfirmBulk(false); load();
    } catch { toast('Xoá thất bại', 'error'); }
  };

  const toggleSort = () => {
    setSort(s => s === 'borrows_desc' ? 'borrows_asc' : 'borrows_desc');
  };

  return (
    <>
      <div className="page-header">
        <div className="page-title">
          <h1>Sách</h1>
        </div>
        <div className="page-controls">
          {selected.length > 0 && (
            <button className="btn btn-danger btn-sm" onClick={() => setConfirmBulk(true)}>
              <Trash2 size={14} /> Xoá {selected.length} sách
            </button>
          )}
          <select className="filter-select" value={genre} onChange={e => setGenre(e.target.value)}>
            <option value="all">Tất cả thể loại</option>
            {genres.map(g => <option key={g} value={g}>{g}</option>)}
          </select>
          <select className="filter-select" value={priceFilter} onChange={e => setPriceFilter(e.target.value)}>
            <option value="all">Tất cả giá</option>
            <option value="under50">Dưới 50.000đ</option>
            <option value="50to100">50.000 – 100.000đ</option>
            <option value="100to200">100.000 – 200.000đ</option>
            <option value="over200">Trên 200.000đ</option>
          </select>
          <input className="search-input" placeholder="Tìm sách..." value={search}
            onChange={e => setSearch(e.target.value)} style={{minWidth:200}} />
          <button className="btn btn-primary" onClick={() => { setEditBook(null); setShowForm(true); }}>
            <Plus size={15} /> Thêm sách
          </button>
        </div>
      </div>

      <div className="table-wrapper">
        <table>
          <thead>
            <tr>
              <th style={{width:40}}><input type="checkbox" checked={selected.length === books.length && books.length > 0} onChange={handleSelectAll} /></th>
              <th>Mã</th>
              <th>Tên sách</th>
              <th>Tác giả</th>
              <th>NXB</th>
              <th>Thể loại</th>
              <th>Giá nhập</th>
              <th>Kệ</th>
              <th className="sortable" onClick={() => setSort(s => s === 'importDate_desc' ? 'importDate_asc' : 'importDate_desc')}>
                Ngày nhập <ArrowUpDown size={12} style={{verticalAlign:'middle'}} />
              </th>
              <th>Tình trạng bề ngoài</th>
              <th>Trạng thái</th>
              <th className="sortable" onClick={toggleSort}>
                Số lần mượn <ArrowUpDown size={12} style={{verticalAlign:'middle'}} />
              </th>
              <th>Ngày trả KH cuối</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {books.length === 0 && (
              <tr><td colSpan={14} className="text-center" style={{padding:40,color:'var(--text-muted)'}}>Không có dữ liệu</td></tr>
            )}
            {books.map(b => (
              <tr key={b.id}>
                <td><input type="checkbox" checked={selected.includes(b.id)} onChange={() => toggleSelect(b.id)} /></td>
                <td className="td-bold" style={{color:'var(--text-secondary)'}}>{b.id}</td>
                <td>
                  <span className="td-link" onClick={() => navigate(`/books/${b.id}`)}>{b.title}</span>
                </td>
                <td>{b.author}</td>
                <td>{b.publisher}</td>
                <td>{b.genre}</td>
                <td>{formatCurrency(b.price)}</td>
                <td>{b.shelf}</td>
                <td>{formatDate(b.importDate)}</td>
                <td style={{maxWidth:140, overflow:'hidden', textOverflow:'ellipsis', whiteSpace:'nowrap'}}>{b.externalCondition}</td>
                <td><span className={`badge ${getBookStatusClass(b.status)}`}>{getBookStatusLabel(b.status)}</span></td>
                <td>{b.totalBorrows}</td>
                <td className="td-muted">{formatDate(b.lastReturnDate)}</td>
                <td>
                  <div className="action-btns">
                    <button className="btn-icon primary" onClick={() => { setEditBook(b); setShowForm(true); }}><Pencil size={15} /></button>
                    <button className="btn-icon danger" onClick={() => setConfirmDelete(b)}><Trash2 size={15} /></button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {showForm && (
        <BookFormModal
          book={editBook}
          onSave={handleSave}
          onClose={() => { setShowForm(false); setEditBook(null); }}
        />
      )}

      {confirmDelete && (
        <ConfirmDialog
          title="Xoá sách"
          message={`Bạn có chắc muốn xoá sách "${confirmDelete.title}"?`}
          onConfirm={() => handleDelete(confirmDelete.id)}
          onCancel={() => setConfirmDelete(null)}
        />
      )}

      {confirmBulk && (
        <ConfirmDialog
          title="Xoá nhiều sách"
          message={`Bạn có chắc muốn xoá ${selected.length} sách đã chọn?`}
          onConfirm={handleBulkDelete}
          onCancel={() => setConfirmBulk(false)}
        />
      )}
    </>
  );
}

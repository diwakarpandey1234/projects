import React, { useEffect, useRef, useState } from 'react';
import { ChevronDown, LogIn, LogOut, Search, Shield, User, UserCircle, UserPlus } from 'lucide-react';
import { useAuth } from './AuthContext';
import { useLocation, useNavigate } from 'react-router-dom';

export default function Navbar({ activeTab, setActiveTab }) {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [open, setOpen] = useState(false);
  const menuRef = useRef(null);

  useEffect(() => {
    const close = (e) => {
      if (menuRef.current && !menuRef.current.contains(e.target)) setOpen(false);
    };
    document.addEventListener('mousedown', close);
    return () => document.removeEventListener('mousedown', close);
  }, []);

  const fullName = user?.fullName || [user?.firstName, user?.lastName].filter(Boolean).join(' ') || user?.username || user?.email || 'User';
  const initials = fullName.split(' ').map(x => x[0]).join('').slice(0, 2).toUpperCase();

  const profile = () => {
    setOpen(false);
    if (isAuthenticated) setActiveTab('profile');
    else navigate('/login');
  };

  return (
    <nav style={navStyle}>
      <div onClick={() => navigate(isAuthenticated ? '/detector' : '/login')} style={brandStyle}>
        <Shield color="#38bdf8" size={24} />
        <span>MarkDetect <span style={{ color: '#38bdf8' }}>AI</span></span>
      </div>

      <div style={rightStyle}>
        {isAuthenticated && (
          <>
            <button type="button" onClick={() => setActiveTab('detector')} style={navButton(activeTab === 'detector')}>
              <Search size={16} /> Detector
            </button>

            <div ref={menuRef} style={{ position: 'relative' }}>
              <button type="button" onClick={() => setOpen(v => !v)} style={profileButton}>
                <span style={avatar}>{initials || <User size={17} />}</span>
                <span style={{ maxWidth: 130, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>{fullName}</span>
                <ChevronDown size={15} />
              </button>

              {open && (
                <div style={menuStyle}>
                  <div style={menuHeader}>
                    <div style={avatarLarge}>{initials || <User size={20} />}</div>
                    <div style={{ minWidth: 0 }}>
                      <div style={{ fontWeight: 700, color: '#f8fafc', overflow: 'hidden', textOverflow: 'ellipsis' }}>{fullName}</div>
                      <div style={{ fontSize: '.78rem', color: '#64748b', overflow: 'hidden', textOverflow: 'ellipsis' }}>{user?.email}</div>
                    </div>
                  </div>
                  <button style={menuItem} onClick={profile}><UserCircle size={17} /> Profile</button>
                  <button style={{ ...menuItem, color: '#f87171' }} onClick={() => { setOpen(false); logout(); navigate('/login'); }}>
                    <LogOut size={17} /> Sign out
                  </button>
                </div>
              )}
            </div>
          </>
        )}

        {!isAuthenticated && (
          <>
            <button type="button" onClick={() => navigate('/login')} style={authNavButton(location.pathname === '/login')}>
              <LogIn size={16} /> Login
            </button>
            <button type="button" onClick={() => navigate('/signup')} style={signupButton}>
              <UserPlus size={16} /> Sign up
            </button>
          </>
        )}
      </div>
    </nav>
  );
}

const navStyle = { position: 'sticky', top: 0, zIndex: 20, display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '1rem 2rem', background: 'rgba(15,23,42,.92)', backdropFilter: 'blur(14px)', borderBottom: '1px solid #1e293b', color: '#f8fafc' };
const brandStyle = { display: 'flex', alignItems: 'center', gap: '.6rem', fontSize: '1.25rem', fontWeight: 800, cursor: 'pointer' };
const rightStyle = { display: 'flex', alignItems: 'center', gap: '.65rem' };
const navButton = (active) => ({ display: 'flex', alignItems: 'center', gap: '.4rem', padding: '.5rem .9rem', background: active ? '#1e293b' : 'transparent', color: active ? '#38bdf8' : '#94a3b8', border: active ? '1px solid #334155' : '1px solid transparent', borderRadius: 8, cursor: 'pointer', fontWeight: 600 });
const authNavButton = (active) => ({ ...navButton(active), color: '#cbd5e1' });
const signupButton = { display: 'flex', alignItems: 'center', gap: '.4rem', padding: '.55rem .95rem', background: 'linear-gradient(135deg,#0284c7,#2563eb)', color: '#fff', border: 0, borderRadius: 8, cursor: 'pointer', fontWeight: 700 };
const profileButton = { display: 'flex', alignItems: 'center', gap: '.45rem', padding: '.35rem .55rem', background: '#1e293b', color: '#e2e8f0', border: '1px solid #334155', borderRadius: 999, cursor: 'pointer', fontWeight: 600 };
const avatar = { width: 28, height: 28, display: 'grid', placeItems: 'center', borderRadius: '50%', background: 'linear-gradient(135deg,#0ea5e9,#2563eb)', color: '#fff', fontSize: '.7rem', fontWeight: 800 };
const avatarLarge = { flex: '0 0 auto', width: 40, height: 40, display: 'grid', placeItems: 'center', borderRadius: '50%', background: 'linear-gradient(135deg,#0ea5e9,#2563eb)', color: '#fff', fontWeight: 800 };
const menuStyle = { position: 'absolute', right: 0, top: 'calc(100% + .6rem)', width: 260, background: '#0f172a', border: '1px solid #334155', borderRadius: 12, boxShadow: '0 20px 45px rgba(0,0,0,.45)', overflow: 'hidden' };
const menuHeader = { display: 'flex', gap: '.75rem', alignItems: 'center', padding: '1rem', borderBottom: '1px solid #1e293b' };
const menuItem = { width: '100%', display: 'flex', alignItems: 'center', gap: '.65rem', padding: '.75rem 1rem', background: 'transparent', border: 0, color: '#cbd5e1', cursor: 'pointer', textAlign: 'left', fontWeight: 600 };

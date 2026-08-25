import React, { useState } from 'react';
import { BrowserRouter, Navigate, Route, Routes, useLocation, useNavigate } from 'react-router-dom';
import Navbar from './Navbar';
import DetectorHome from './DetectorHome';
import Profile from './Profile';
import Login from './Login';
import Signup from './Signup';
import { AuthProvider, useAuth } from './AuthContext';

function ProtectedRoute({ children }) {
  const { isAuthenticated, loading } = useAuth();
  if (loading) return <div style={pageLoader}>Restoring secure session...</div>;
  return isAuthenticated ? children : <Navigate to="/login" replace />;
}

function PublicRoute({ children }) {
  const { isAuthenticated, loading } = useAuth();
  if (loading) return <div style={pageLoader}>Loading...</div>;
  return isAuthenticated ? <Navigate to="/detector" replace /> : children;
}

function AppShell() {
  const { isAuthenticated } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState(location.pathname.startsWith('/profile') ? 'profile' : 'detector');

  const goTo = (tab) => {
    setActiveTab(tab);
    navigate(tab === 'profile' ? '/profile' : '/detector');
  };

  return (
    <div style={{ minHeight: '100vh', backgroundColor: '#020617' }}>
      <Navbar activeTab={activeTab} setActiveTab={goTo} />
      <main>
        <Routes>
          <Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />
          <Route path="/signup" element={<PublicRoute><Signup /></PublicRoute>} />
          <Route path="/detector" element={<ProtectedRoute><DetectorHome /></ProtectedRoute>} />
          <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
          <Route path="*" element={<Navigate to={isAuthenticated ? '/detector' : '/login'} replace />} />
        </Routes>
      </main>
    </div>
  );
}

const pageLoader = {
  minHeight: '100vh',
  display: 'grid',
  placeItems: 'center',
  color: '#94a3b8',
  background: '#020617'
};

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppShell />
      </AuthProvider>
    </BrowserRouter>
  );
}

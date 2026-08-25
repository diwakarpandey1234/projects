import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ArrowRight, Lock, Mail, Shield, Loader2 } from 'lucide-react';
import { apiFetch } from './api';
import { useAuth } from './AuthContext';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const auth = await apiFetch('/authenticate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: email.trim().toLowerCase(), password }),
      });

      localStorage.setItem('refresh_token', auth.refreshToken);
      localStorage.setItem('jwt_token', auth.accessToken);

      const currentUser = await apiFetch('/api/v1/user/me');
      login(currentUser, auth.accessToken, auth.refreshToken);
      navigate('/detector', { replace: true });
    } catch (err) {
      setError(err.message || 'Unable to sign in. Check your email and password.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthCard
      title="Welcome back"
      subtitle="Sign in to access your AI watermark detection workspace."
    >
      <form onSubmit={handleLogin}>
        <Field icon={<Mail size={17} />} label="EMAIL">
          <input type="email" placeholder="you@example.com" value={email}
            onChange={(e) => setEmail(e.target.value)} required autoComplete="email" />
        </Field>

        <Field icon={<Lock size={17} />} label="PASSWORD">
          <input type="password" placeholder="••••••••" value={password}
            onChange={(e) => setPassword(e.target.value)} required autoComplete="current-password" />
        </Field>

        {error && <ErrorBox message={error} />}

        <button disabled={loading} style={primaryButton}>
          {loading ? <><Loader2 size={17} className="spin" /> Signing in...</> : <>Sign In <ArrowRight size={17} /></>}
        </button>

        <p style={switchText}>
          Don't have an account? <Link to="/signup" style={linkStyle}>Create one</Link>
        </p>
      </form>
    </AuthCard>
  );
}

export function AuthCard({ title, subtitle, children }) {
  return (
    <div style={page}>
      <div style={card}>
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <div style={logoBox}><Shield size={31} /></div>
          <h1 style={{ fontSize: '1.65rem', margin: '0 0 .45rem', fontWeight: 800 }}>{title}</h1>
          <p style={{ color: '#94a3b8', fontSize: '.9rem', margin: 0 }}>{subtitle}</p>
        </div>
        {children}
      </div>
    </div>
  );
}

export function Field({ icon, label, children }) {
  return (
    <div style={{ marginBottom: '1.25rem' }}>
      <label style={labelStyle}>{label}</label>
      <div style={inputWrap}>{icon}{children}</div>
    </div>
  );
}

export function ErrorBox({ message }) {
  return <div style={errorBox}>{message}</div>;
}

const page = { minHeight: 'calc(100vh - 73px)', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '2rem 1rem' };
const card = { width: '100%', maxWidth: 440, background: 'rgba(15,23,42,.82)', border: '1px solid rgba(255,255,255,.08)', borderRadius: 18, padding: '2.5rem', boxShadow: '0 24px 60px rgba(0,0,0,.35)' };
const logoBox = { display: 'inline-flex', padding: '.8rem', borderRadius: 13, background: 'rgba(56,189,248,.1)', color: '#38bdf8', marginBottom: '1rem' };
const labelStyle = { display: 'block', color: '#94a3b8', fontSize: '.78rem', fontWeight: 700, letterSpacing: '.04em', marginBottom: '.5rem' };
const inputWrap = { display: 'flex', alignItems: 'center', gap: '.65rem', background: '#020617', border: '1px solid #334155', borderRadius: 9, padding: '.7rem .85rem', color: '#64748b' };
const primaryButton = { width: '100%', padding: '.8rem', border: 0, borderRadius: 9, background: 'linear-gradient(135deg,#0284c7,#2563eb)', color: '#fff', fontWeight: 700, cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '.5rem' };
const switchText = { textAlign: 'center', color: '#64748b', fontSize: '.88rem', marginTop: '1.25rem' };
const linkStyle = { color: '#38bdf8', fontWeight: 700, textDecoration: 'none' };
const errorBox = { marginBottom: '1rem', padding: '.75rem .9rem', borderRadius: 8, background: 'rgba(239,68,68,.1)', border: '1px solid rgba(239,68,68,.25)', color: '#fca5a5', fontSize: '.85rem' };

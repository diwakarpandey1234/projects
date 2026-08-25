import React from 'react';
import { Mail, ShieldCheck, UserRound, Coins } from 'lucide-react';
import { useAuth } from './AuthContext';

export default function Profile() {
  const { user } = useAuth();
  const fullName = user?.fullName || [user?.firstName, user?.lastName].filter(Boolean).join(' ') || 'User';

  return (
    <div style={{ maxWidth: 960, margin: '2.5rem auto', padding: '0 1.5rem', color: '#f8fafc' }}>
      <div style={{ marginBottom: '2rem' }}>
        <div style={{ display: 'inline-flex', alignItems: 'center', gap: '.45rem', color: '#38bdf8', fontSize: '.8rem', fontWeight: 700, marginBottom: '.5rem' }}>
          <ShieldCheck size={16} /> SECURE WORKSPACE
        </div>
        <h1 style={{ fontSize: '2rem', fontWeight: 800, marginBottom: '.45rem' }}>Account Profile</h1>
        <p style={{ color: '#94a3b8' }}>Your identity and detection workspace information.</p>
      </div>

      <div style={grid}>
        <Info icon={<UserRound size={22} />} label="FULL NAME" value={fullName} />
        <Info icon={<Mail size={22} />} label="EMAIL" value={user?.email || '—'} />
        <Info icon={<Coins size={22} />} label="AVAILABLE CREDITS" value={`${user?.tokenBalance ?? user?.tokens ?? 0} credits`} />
        <Info icon={<ShieldCheck size={22} />} label="ACCOUNT ROLE" value={user?.role || 'USER'} />
      </div>

      <div style={panel}>
        <h2 style={{ fontSize: '1.05rem', marginBottom: '.5rem' }}>Detection history</h2>
        <p style={{ color: '#64748b', margin: 0 }}>
          Previous detection records are temporarily hidden from this profile while the activity-history experience is being finalized.
        </p>
      </div>
    </div>
  );
}

function Info({ icon, label, value }) {
  return (
    <div style={infoCard}>
      <div style={iconBox}>{icon}</div>
      <div>
        <div style={{ color: '#64748b', fontSize: '.75rem', fontWeight: 700, marginBottom: '.2rem' }}>{label}</div>
        <div style={{ fontSize: '1.05rem', fontWeight: 700, overflowWrap: 'anywhere' }}>{value}</div>
      </div>
    </div>
  );
}

const grid = { display: 'grid', gridTemplateColumns: 'repeat(auto-fit,minmax(230px,1fr))', gap: '1rem' };
const infoCard = { background: 'rgba(15,23,42,.7)', border: '1px solid rgba(255,255,255,.08)', borderRadius: 14, padding: '1.2rem', display: 'flex', alignItems: 'center', gap: '1rem' };
const iconBox = { width: 45, height: 45, display: 'grid', placeItems: 'center', borderRadius: 11, background: 'rgba(56,189,248,.1)', color: '#38bdf8' };
const panel = { marginTop: '1.5rem', padding: '1.35rem', borderRadius: 14, background: 'rgba(15,23,42,.55)', border: '1px solid rgba(255,255,255,.08)' };

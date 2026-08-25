import React, { useState } from 'react';
import {
  FileText, Image as ImageIcon, UploadCloud, AlertCircle, Loader2, Sparkles,
  ShieldCheck, ShieldAlert, Cpu, Timer, Percent
} from 'lucide-react';
import { apiFetch } from './api';
import { useAuth } from './AuthContext';

export default function DetectorHome() {
  const { updateUser, logout } = useAuth();
  const [activeMode, setActiveMode] = useState('text');
  const [inputText, setInputText] = useState('');
  const [selectedFile, setSelectedFile] = useState(null);
  const [filePreview, setFilePreview] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [result, setResult] = useState(null);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    setSelectedFile(file);
    setFilePreview(file.type.startsWith('image/') ? URL.createObjectURL(file) : null);
    setError('');
    setResult(null);
  };

  const handleModeSwitch = (mode) => {
    setActiveMode(mode);
    setError('');
    setResult(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setResult(null);
    setLoading(true);

    try {
      let data;
      const started = performance.now();

      if (activeMode === 'text') {
        if (!inputText.trim()) throw new Error('Please enter text to analyze.');
        data = await apiFetch('/api/v1/detect/text', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ text: inputText }),
        });
      } else {
        if (!selectedFile) throw new Error('Please choose a media file to analyze.');
        const formData = new FormData();
        formData.append('file', selectedFile);
        data = await apiFetch('/api/v1/detect/image', { method: 'POST', body: formData });
      }

      const executionTimeMs = Math.round(performance.now() - started);
      setResult({
        watermarkDetected: data.watermarked ?? data.isWatermarked,
        confidence: data.confidenceScore,
        algorithm: data.detectionMethod,
        metrics: data.statisticalMetrics,
        remainingTokens: data.remainingTokens,
        message: data.message,
        executionTimeMs,
      });

      if (typeof data.remainingTokens === 'number') {
        updateUser({ ...JSON.parse(localStorage.getItem('user') || '{}'), tokenBalance: data.remainingTokens });
      }
    } catch (err) {
      if (err.status === 401) logout();
      setError(err.message || 'Detection failed. Please check the backend.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 960, margin: '2.5rem auto', padding: '0 1.5rem', color: '#f8fafc' }}>
      <div style={{ textAlign: 'center', marginBottom: '2.5rem' }}>
        <div style={badge}><Sparkles size={14} /> AI Verification Engine</div>
        <h1 style={heroTitle}>Detect Synthetic & AI Watermarks</h1>
        <p style={{ color: '#94a3b8', fontSize: '1.05rem', maxWidth: 580, margin: '0 auto' }}>
          Inspect your text and media for supported forensic watermark signals.
        </p>
      </div>

      <div style={panel}>
        <div style={tabs}>
          <button type="button" onClick={() => handleModeSwitch('text')} style={tab(activeMode === 'text')}><FileText size={18} /> Analyze Text</button>
          <button type="button" onClick={() => handleModeSwitch('image')} style={tab(activeMode === 'image')}><ImageIcon size={18} /> Analyze Image / Media</button>
        </div>

        <form onSubmit={handleSubmit}>
          {activeMode === 'text' ? (
            <textarea rows={7} value={inputText} onChange={e => setInputText(e.target.value)}
              placeholder="Paste at least 20 characters of text for watermark analysis..." style={textarea} />
          ) : (
            <>
              <input type="file" id="mediaUpload" accept="image/*,video/*,audio/*" hidden onChange={handleFileChange} />
              <label htmlFor="mediaUpload" style={uploadBox}>
                {filePreview ? (
                  <div style={{ textAlign: 'center' }}>
                    <img src={filePreview} alt="Preview" style={{ maxHeight: 160, maxWidth: '100%', borderRadius: 8, marginBottom: '.75rem' }} />
                    <div style={{ color: '#38bdf8', fontWeight: 700 }}>{selectedFile?.name}</div>
                  </div>
                ) : (
                  <>
                    <UploadCloud size={32} color="#38bdf8" />
                    <div style={{ marginTop: '.7rem', fontWeight: 700 }}>Click to upload media</div>
                    <div style={{ color: '#64748b', fontSize: '.85rem', marginTop: '.25rem' }}>Supported image/audio/video files</div>
                  </>
                )}
              </label>
            </>
          )}

          {error && <div style={errorBox}><AlertCircle size={18} /> {error}</div>}

          <button type="submit" disabled={loading || (activeMode === 'text' ? !inputText.trim() : !selectedFile)}
            style={{ ...submitButton, opacity: loading || (activeMode === 'text' ? !inputText.trim() : !selectedFile) ? .5 : 1 }}>
            {loading ? <><Loader2 size={18} className="spin" /> Running analysis...</> : <>Run {activeMode === 'text' ? 'Text' : 'Media'} Watermark Scan</>}
          </button>
        </form>
      </div>

      {result && (
        <div style={{ ...panel, animation: 'fadeIn .3s ease-out' }}>
          <div style={resultHeader}>
            <div style={{ color: result.watermarkDetected ? '#ef4444' : '#22c55e', fontWeight: 800, fontSize: '1.1rem', display: 'flex', alignItems: 'center', gap: '.4rem' }}>
              {result.watermarkDetected ? <ShieldAlert size={23} /> : <ShieldCheck size={23} />}
              {result.watermarkDetected ? 'Watermark Detected' : 'No Supported Watermark Detected'}
            </div>
            <span style={status}>COMPLETED</span>
          </div>

          <div style={metricsGrid}>
            <Metric icon={<Percent size={14} />} label="Confidence" value={typeof result.confidence === 'number' ? `${(result.confidence * 100).toFixed(1)}%` : '—'} />
            <Metric icon={<Timer size={14} />} label="Latency" value={`${result.executionTimeMs} ms`} />
            <Metric icon={<Cpu size={14} />} label="Detection Method" value={result.algorithm || '—'} />
          </div>

          {/* <div style={{ color: '#94a3b8', fontSize: '.85rem', fontWeight: 700, marginBottom: '.5rem' }}>Inspection Metadata</div>
          <pre style={pre}>{JSON.stringify(result.metrics || result, null, 2)}</pre> */}
          {result.message && <div style={{ color: '#64748b', fontSize: '.82rem', marginTop: '.75rem' }}>{result.message}</div>}
        </div>
      )}
    </div>
  );
}

function Metric({ icon, label, value }) {
  return <div style={metric}><div style={{ display: 'flex', gap: '.4rem', color: '#64748b', fontSize: '.78rem' }}>{icon}{label}</div><div style={{ marginTop: '.2rem', fontSize: '1.2rem', fontWeight: 800 }}>{value}</div></div>;
}

const badge = { display: 'inline-flex', alignItems: 'center', gap: '.4rem', padding: '.4rem 1rem', borderRadius: 999, background: 'rgba(56,189,248,.1)', border: '1px solid rgba(56,189,248,.2)', color: '#38bdf8', fontSize: '.85rem', fontWeight: 700, marginBottom: '1rem' };
const heroTitle = { fontSize: '2.5rem', fontWeight: 800, letterSpacing: '-.025em', background: 'linear-gradient(to right,#fff,#94a3b8)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent', marginBottom: '.75rem' };
const panel = { background: 'rgba(15,23,42,.68)', backdropFilter: 'blur(16px)', border: '1px solid rgba(255,255,255,.08)', borderRadius: 16, boxShadow: '0 20px 40px -15px rgba(0,0,0,.5)', padding: '2rem', marginBottom: '2rem' };
const tabs = { display: 'flex', background: '#090d16', padding: '.35rem', borderRadius: 10, border: '1px solid rgba(255,255,255,.05)', marginBottom: '1.75rem' };
const tab = active => ({ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '.5rem', padding: '.75rem 1rem', borderRadius: 8, border: 0, background: active ? '#1e293b' : 'transparent', color: active ? '#38bdf8' : '#64748b', fontWeight: 700, cursor: 'pointer' });
const textarea = { width: '100%', background: '#020617', border: '1px solid #334155', borderRadius: 10, padding: '1rem', color: '#f8fafc', fontSize: '.95rem', lineHeight: 1.5, resize: 'vertical', outline: 'none' };
const uploadBox = { display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', minHeight: 220, padding: '2rem', border: '2px dashed rgba(56,189,248,.3)', borderRadius: 12, background: '#020617', cursor: 'pointer', textAlign: 'center' };
const submitButton = { width: '100%', marginTop: '1.5rem', padding: '.85rem 1.5rem', borderRadius: 10, border: 0, background: 'linear-gradient(135deg,#0284c7,#2563eb)', color: '#fff', fontWeight: 700, cursor: 'pointer', display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '.5rem' };
const errorBox = { display: 'flex', alignItems: 'center', gap: '.6rem', marginTop: '1.25rem', padding: '.85rem 1rem', borderRadius: 8, background: 'rgba(239,68,68,.1)', border: '1px solid rgba(239,68,68,.25)', color: '#fca5a5', fontSize: '.9rem' };
const resultHeader = { display: 'flex', alignItems: 'center', justifyContent: 'space-between', paddingBottom: '1.25rem', borderBottom: '1px solid rgba(255,255,255,.06)', marginBottom: '1.5rem' };
const status = { fontSize: '.72rem', padding: '.3rem .7rem', borderRadius: 999, background: '#1e293b', color: '#38bdf8', fontWeight: 700 };
const metricsGrid = { display: 'grid', gridTemplateColumns: 'repeat(auto-fit,minmax(180px,1fr))', gap: '1rem', marginBottom: '1.5rem' };
const metric = { background: '#020617', padding: '1rem', borderRadius: 10, border: '1px solid #1e293b' };
const pre = { background: '#020617', padding: '1rem', borderRadius: 8, border: '1px solid #1e293b', color: '#93c5fd', fontSize: '.8rem', overflowX: 'auto', margin: 0 };

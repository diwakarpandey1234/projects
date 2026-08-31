import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { ArrowRight, Lock, Mail, UserRound, Loader2 } from "lucide-react";
import { apiFetch } from "./api";
import { useAuth } from "./AuthContext";
import { AuthCard, ErrorBox, Field } from "./Login";
import Input from "./components/Input";

export default function Signup() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const update = (key) => (e) =>
    setForm((prev) => ({ ...prev, [key]: e.target.value }));

  const handleSignup = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      await apiFetch("/api/v1/user/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      const auth = await apiFetch("/authenticate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username: form.email.trim().toLowerCase(),
          password: form.password,
        }),
      });

      const currentUser = await apiFetch("/api/v1/user/me", {
        headers: { Authorization: `Bearer ${auth.accessToken}` },
      });

      login(currentUser, auth.accessToken, auth.refreshToken);
      navigate("/detector", { replace: true });
    } catch (err) {
      setError(err.message || "Unable to create your account.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthCard
      title="Create your account"
      subtitle="Set up your secure workspace in less than a minute."
    >
      <form onSubmit={handleSignup}>
        <div>
          <Field icon={<UserRound size={17} />} label="FIRST NAME">
            <Input
              type={"text"}
              placeholder={"test"}
              value={form.firstName}
              onChange={update("firstName")}
              required={false}
              autoComplete={"given-name"}
            />
          </Field>
          <Field icon={<UserRound size={17} />} label="LAST NAME">
            <Input
              type={"text"}
              placeholder={"test"}
              value={form.lastName}
              onChange={update("lastName")}
              required={false}
              autoComplete={"given-name"}
            />
          </Field>
        </div>

        <Field icon={<Mail size={17} />} label="EMAIL">
          <Input
            type={"email"}
            placeholder={"you@example.com"}
            value={form.email}
            onChange={update("email")}
            required={false}
            autoComplete={'email'}
          />
        </Field>

        <Field icon={<Lock size={17} />} label="PASSWORD">
          <Input
            type={'password'}
            placeholder={"At least 8 characters"}
            value={form.password}
            onChange={update("password")}
            minLength={8}
            required={false}
            autoComplete={'new-password'}

          />
        </Field>

        {error && <ErrorBox message={error} />}

        <button disabled={loading} style={{ ...primaryButton }}>
          {loading ? (
            <>
              <Loader2 size={17} /> Creating account...
            </>
          ) : (
            <>
              Create Account <ArrowRight size={17} />
            </>
          )}
        </button>

        <p style={switchText}>
          Already have an account?{" "}
          <Link to="/login" style={linkStyle}>
            Sign in
          </Link>
        </p>
      </form>
    </AuthCard>
  );
}

const primaryButton = {
  width: "100%",
  padding: ".8rem",
  border: 0,
  borderRadius: 9,
  background: "linear-gradient(135deg,#0284c7,#2563eb)",
  color: "#fff",
  fontWeight: 700,
  cursor: "pointer",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  gap: ".5rem",
};
const switchText = {
  textAlign: "center",
  color: "#64748b",
  fontSize: ".88rem",
  marginTop: "1.25rem",
};
const linkStyle = { color: "#38bdf8", fontWeight: 700, textDecoration: "none" };

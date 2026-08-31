import React from "react";

function Input({ type, placeholder, value, onChange, required=true, autoComplete,minlength }) {
  return (
    <input

      className="bg-gray-800"
      type={type}
      placeholder={placeholder}
      value={value}
      onChange={onChange}
      required={required}
      autoComplete={autoComplete}
      minLength={minlength}

    />
  );
}

export default Input;

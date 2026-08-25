# AI Watermark Detection Platform

A modern React application built with **Vite**, **Tailwind CSS v4**, **React Router v7**, and **Lucide Icons** to detect synthetic AI watermarks in text and images.

---

## 🚀 Quick Start Guide

### 1. Extract the Project
Unzip the downloaded `ai-watermark-detection-platform.zip` and open the folder in VS Code or your terminal.

### 2. Install Dependencies
Run the following command in the project root:
```bash
npm install
```

### 3. Run Development Server
Start the local server:
```bash
npm run dev
```
Open [http://localhost:5173](http://localhost:5173) in your browser.

---

## 📁 Features Implemented
- **Top Centered Brand Header:** `AI WATERMARK DETECTION PLATFORM`
- **Authentication State:**
  - Login / Sign Up actions on top right
  - Clickable circular user profile avatar with status indicator
- **Profile Details Page (`/profile`):**
  - Displays user avatar, name, email address, scan quota credits, and engine details
  - "Back to Scanner" button and session sign-out
- **Verification Selector:**
  - Toggle between **Text Verification** and **Image Verification**
- **Square Bracketed Text Input:**
  - Stylized brackets `[ ... ]` wrapping the payload input
- **Image Uploader:**
  - Drag-and-drop or click-to-upload area with live thumbnail preview and removal button
- **Validation Guard:**
  - Checks if text is empty or less than minimum length
  - Checks if image is missing before scanning
  - Shaking warning banner: *"Please enter valid content before proceeding."*
- **Analysis Engine Simulation:**
  - Simulated latency, entropy calculation, and confidence scoring

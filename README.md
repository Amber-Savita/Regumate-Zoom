# REGUMATE – Intelligent Zoom Meeting Automation for Linux

![REGUMATE Banner](Backend_Files/img/banner.png)

> *"Automating attendance. Maximizing productivity."*

REGUMATE is a Linux-based Zoom Meeting Automation Platform designed to automatically join scheduled Zoom meetings without manual intervention. Built with Python, GUI automation, and database-driven scheduling, REGUMATE streamlines the repetitive process of joining daily online meetings, allowing students, professionals, organizations, and remote teams to focus on what matters most.

The project demonstrates how automation can be leveraged to increase efficiency, reduce missed meetings, and simplify virtual collaboration.

---

## 🚀 Features

### Automated Meeting Joining

* Launches Zoom automatically
* Detects Zoom interface elements using image recognition
* Enters Meeting ID and Passcode automatically
* Joins meetings according to predefined schedules

### Database-Driven Scheduling

* Stores meeting credentials securely in MySQL/MariaDB
* Reads meeting information dynamically
* Supports multiple scheduled meetings

### Linux Service Integration

* Runs as a background systemd service
* Starts automatically after system boot
* Minimal user interaction required

### GUI-Based Setup Utility

* One-click dependency installation
* Database configuration assistance
* Service installation and management
* User-friendly setup experience

### Smart Image Detection

* Uses template matching for Zoom UI buttons
* Handles Join Meeting workflow automatically
* Easily customizable for future Zoom UI updates

---

# 🏗️ Project Structure

```text
REGUMATE/
│
├── Backend_Files/
│   ├── regumate.py                 # Main automation engine
│   ├── setup.py                    # Installation & configuration GUI
│   ├── requirements.txt            # Python dependencies
│   ├── regumate.service.template   # Linux systemd template
│   ├── run_regumate.sh             # Manual execution script
│   │
│   └── img/
│       ├── joinIMG.png
│       ├── meetidimage.png
│       └── other template assets
│
└── README.md
```

---

# ⚙️ Technology Stack

* Python 3
* MySQL / MariaDB
* OpenCV
* PyAutoGUI
* Linux Systemd
* Image Recognition
* GUI Automation

---

# 📋 Prerequisites

Before installing REGUMATE, ensure the following are available:

* Linux Operating System
* X11 Desktop Environment
* Zoom Client Installed
* Python 3.8+
* MySQL or MariaDB
* Internet Connection

> **Note:** REGUMATE relies on GUI automation and image recognition. It does not use Zoom APIs.

---

# 🔧 Installation

Clone the repository:

```bash
git clone https://github.com/yourusername/regumate.git
cd regumate
```

Install Python dependencies:

```bash
cd Backend_Files
pip3 install -r requirements.txt
```

Make helper script executable:

```bash
chmod +x run_regumate.sh
```

Launch the setup utility:

```bash
python3 setup.py
```

The setup wizard will assist with:

* Dependency installation
* Database configuration
* Service setup
* Automation initialization

---

# ▶️ Running REGUMATE

## Manual Mode

```bash
cd Backend_Files
./run_regumate.sh
```

## Direct Execution

```bash
python3 regumate.py
```

---

# 🔄 Running as a Linux Service

REGUMATE can operate continuously in the background using systemd.

Using the Setup Utility:

1. Open `setup.py`
2. Configure the required settings
3. Click **Start Service**

The installer automatically generates and installs the systemd service from:

```text
Backend_Files/regumate.service.template
```

---

# 🗄️ Database Integration

REGUMATE stores meeting information inside a local MySQL database.

Example fields:

| Field      | Description             |
| ---------- | ----------------------- |
| Meeting ID | Zoom Meeting Identifier |
| Passcode   | Meeting Password        |
| Date       | Scheduled Meeting Date  |
| Time       | Scheduled Meeting Time  |
| Status     | Active / Inactive       |

The automation engine continuously checks upcoming meetings and launches Zoom when the scheduled time arrives.

---

# 🧠 How It Works

1. REGUMATE reads upcoming meetings from MySQL.
2. At the scheduled time, Zoom is launched.
3. OpenCV searches for required Zoom interface elements.
4. PyAutoGUI interacts with the interface.
5. Meeting credentials are entered automatically.
6. REGUMATE joins the meeting without user intervention.

---

# 🛡️ Reliability Improvements

Recent enhancements include:

* Improved image template validation
* Better dependency management
* Correct MySQL connector package support
* Robust systemd service generation
* Cleaner project structure
* Git ignore rules for logs and temporary files

---

# ⚠️ Important Notes

* Designed primarily for X11 environments.
* Wayland support may be limited.
* Zoom UI updates may require refreshing image templates.
* Ensure correct display permissions when running as a service.
* Default display configuration assumes:

```bash
DISPLAY=:0
```

---

# 🎯 Vision

REGUMATE was created with a simple goal:

**Eliminate repetitive meeting-joining tasks through intelligent automation.**

The project showcases how automation, computer vision, and system integration can work together to improve productivity in modern remote-learning and remote-working environments.

---

# 👨‍💻 Author

**Amber**

Blockchain Developer • Automation Enthusiast • Technology Researcher

Passionate about building solutions where software automation, intelligent systems, and productivity intersect.

---

## ⭐ Support

If you find REGUMATE useful, consider giving the project a star and contributing to future improvements.

**Automate the routine. Focus on the important.**

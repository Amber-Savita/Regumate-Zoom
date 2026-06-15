# Regumate Zoom Automation

A Linux-based Zoom meeting automation project that opens Zoom, joins scheduled meetings, and handles credentials from a local MySQL database.

## What this project contains

- `Backend_Files/regumate.py` - main automation script
- `Backend_Files/setup.py` - installer GUI for dependencies, database, and service
- `Backend_Files/requirements.txt` - Python dependency list
- `Backend_Files/regumate.service.template` - systemd service template for Linux
- `Backend_Files/img/` - image templates used for Zoom button detection
- `Backend_Files/run_regumate.sh` - manual run helper script

## Prerequisites

- Linux with X11 display (this script uses GUI automation, not headless Zoom API)
- Zoom installed and configured
- Python 3 installed
- MySQL or MariaDB installed

## Recommended setup steps

1. Clone or copy this repository into your Linux machine.
2. Open a terminal inside the repository root.
3. Install dependencies:
   ```bash
   cd Backend_Files
   python3 -m pip install -r requirements.txt
   ```
4. Make the helper script executable:
   ```bash
   chmod +x Backend_Files/run_regumate.sh
   ```
5. Run the setup GUI (optional):
   ```bash
   python3 Backend_Files/setup.py
   ```

## Manual run

Use the helper script to start the automation manually:

```bash
cd Backend_Files
./run_regumate.sh
```

## Service installation

If you want the automation to run as a background Linux service, use the setup GUI and click **Start Service**. That command will generate a systemd service file from `Backend_Files/regumate.service.template` and install it.

## Important notes

- The automation works by matching Zoom UI buttons using the images in `Backend_Files/img/`.
- If Zoom UI changes, you may need to update `joinIMG.png` and `meetidimage.png`.
- Use the correct Linux display: `DISPLAY=:0` is assumed.
- If your system uses Wayland, this script may not work.

## Improvements added

- `requirements.txt` now uses the correct `mysql-connector-python` package name.
- `regumate.py` now validates image templates before using them.
- `setup.py` now installs Python dependencies with the current Python interpreter and writes a proper systemd service file.
- `.gitignore` added to keep logs and temporary files out of Git.

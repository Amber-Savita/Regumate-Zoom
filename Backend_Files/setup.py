                                                                                            #  R E G U M A T E  #
import os
import subprocess
import tkinter as tk
from tkinter import messagebox
import logging
import sys
import shutil
import getpass  
import platform

# Create the 'Logs' directory if it doesn't exist
LOGS_DIR = 'Logs'
os.makedirs(LOGS_DIR, exist_ok=True)

# Configure logging to capture both stdout and stderr
logging.basicConfig(
    filename=os.path.join(LOGS_DIR, 'regumate_setup.logs'),
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    filemode='a'  # Append mode to avoid overwriting
)

# Create a handler to log stdout and stderr to the file
log_handler = logging.StreamHandler(open(os.path.join(LOGS_DIR, 'output.log'), 'a'))
log_handler.setLevel(logging.INFO)  # Adjust log level as needed
formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
log_handler.setFormatter(formatter)
logging.getLogger().addHandler(log_handler)


# To check if user 'reguuser' exists in MySQL
def check_user(mysql_password):
    try:
        # Check if the user 'reguuser' already exists in MySQL
        mysql_command = [
            'sudo', 'mysql', '-u', 'root', '-e', "SELECT EXISTS(SELECT 1 FROM mysql.user WHERE user = 'reguuser');"
        ]
        result = subprocess.run(mysql_command, capture_output=True, text=True, check=True)
        exists = result.stdout.strip()

        if exists == "0":
            # User 'reguuser' does not exist, so create it
            subprocess.run(["sudo", "mysql", "-u", "root", "-e",
                            "CREATE USER 'reguuser'@'localhost' IDENTIFIED BY 'regupass';"],
                           check=True)

            # Grant all privileges to the new user
            subprocess.run(["sudo", "mysql", "-u", "root", "-e",
                            "GRANT ALL PRIVILEGES ON *.* TO 'reguuser'@'localhost';"],
                           check=True)

            mysql_password = 'regupass'
            print('User created')
        else:
            return mysql_password 
            print('User already exists')

        return mysql_password

    except Exception as e:
        print(f"An error occurred: {e}")
        return None


# To install fresh mysql in any system
def install_mysql():
    try:
        try:
            subprocess.run(["sudo", "pkill", "unattended-upgr"], check=True)
            print("unattended-upgrades process killed successfully.")
        except subprocess.CalledProcessError as e:
            print(f"An error occurred: {e}")

        subprocess.run(["sudo", "apt", "update"])

        os_name = platform.system().lower()
        if os_name == "linux" and "kali" in platform.release().lower():
            # If the OS is Kali Linux, install MariaDB instead of MySQL
            command = "sudo -S apt-get install -y mariadb-server"
        else:
            # Otherwise, install MySQL
            command = "sudo -S apt-get install -y mysql-server"

        # Execute the command to install MySQL or MariaDB
        subprocess.run(command, shell=True, check=True)
        mysql_password = mysql_password_entry.get()  
        mysql_password = check_user(mysql_password)  
        print("MySQL or MariaDB installed successfully")
        logging.info("MySQL or MariaDB installed successfully")

    except Exception as e:
        print(f"An error occurred: {e}")
        logging.error(f"An error occurred: {e}")

# To check path of sql in environment
def find_mysql_bin_path():
    
    possible_paths = ['/usr/bin', '/usr/local/bin', '/usr/sbin', '/usr/local/sbin']

    for path in possible_paths:
        if os.path.exists(os.path.join(path, 'mysql')):
            return path

    
    return input("Enter the path to the MySQL binary directory: ") or '/usr/bin'


# To create a database named as 'regumate' and create table 'meetings' in reguuser
def setup_database():
    try:
        #mysql_password = check_user()
        mysql_password = mysql_password_entry.get()  
        mysql_password = check_user(mysql_password)
        
        command = f"mysql -u root -p'{mysql_password}' -e 'CREATE DATABASE IF NOT EXISTS regumate;'"
        subprocess.run(command, shell=True, check=True)

        table_structure = """
            CREATE TABLE IF NOT EXISTS meetings (
                id INT NOT NULL AUTO_INCREMENT,
                meeting_id VARCHAR(255) NOT NULL,
                passcode VARCHAR(255) NOT NULL,
                meeting_time VARCHAR(255),
                total_meeting INT,
                PRIMARY KEY (id)
            );
        """

        # Execute the command to create the table within the 'regumate' database
        command = f"mysql -u root -p'{mysql_password}' -e '{table_structure}' regumate"
        subprocess.run(command, shell=True, check=True)

        print("Database 'regumate' and table 'meetings' created successfully.")
        messagebox.showinfo("Success", "Database created successfully")
        logging.info("Database created successfuly")
        
    except Exception as e:
        print(f"An error occurred: {e}")


# To install all Libraries required for our software (Requirements.txt)
def install_requirements():
    try:
        subprocess.run(["sudo", "apt", "update"], check=True)
        subprocess.run(["sudo", "apt-get", "install", "-y", "python3-pip"], check=True)
        subprocess.run([sys.executable, "-m", "pip", "install", "--upgrade", "pip"], check=True)
        subprocess.run(["sudo", "apt-get", "install", "-y", "python3-tk"], check=True)
        subprocess.run([sys.executable, "-m", "pip", "install", "-r", os.path.join(os.getcwd(), "Backend_Files", "requirements.txt")], check=True)
        messagebox.showinfo("Success", "Requirements installed successfully.")
        logging.info("Requirements installed successfully.")
    except Exception as e:
        messagebox.showerror("Error", f"An error occurred: {e}")
        logging.error(f"An error occurred: {e}")


# To start/enable/reload DAEMON-SERVICE named as regumate.service (it would keep running regumate.py on background)
def install_service():
    try:
        current_user = getpass.getuser()
        backend_dir = os.path.join(os.getcwd(), "Backend_Files")
        template_path = os.path.join(backend_dir, "regumate.service.template")

        if not os.path.exists(template_path):
            raise FileNotFoundError(f"Service template not found: {template_path}")

        with open(template_path, 'r') as f:
            service_content = f.read()

        service_content = service_content.format(
            USER=current_user,
            WORKDIR=backend_dir,
            PYTHON=sys.executable
        )

        temp_service_file = os.path.join(backend_dir, "regumate.service")
        with open(temp_service_file, 'w') as f:
            f.write(service_content)

        subprocess.run(["sudo", "cp", temp_service_file, "/etc/systemd/system/regumate.service"], check=True)
        subprocess.run(["sudo", "systemctl", "daemon-reload"], check=True)
        subprocess.run(["sudo", "systemctl", "enable", "regumate.service"], check=True)
        subprocess.run(["sudo", "systemctl", "start", "regumate.service"], check=True)

        messagebox.showinfo("Success", "Service installed and started successfully.")
        logging.info("Service installed and started successfully.")

        os.remove(temp_service_file)

    except Exception as e:
        messagebox.showerror("Error", f"An error occurred: {e}")
        logging.error(f"An error occurred: {e}")



def main():
    # Main application window
    root = tk.Tk()
    root.title("Regumate Setup")

    # Label for MySQL password input
    mysql_password_label = tk.Label(root, text="Enter MySQL root password:")
    mysql_password_label.pack()

    # Entry field for MySQL password input
    global mysql_password_entry
    mysql_password_entry = tk.Entry(root, show="*")
    mysql_password_entry.pack()

    # Button to install requirements
    reqs_button = tk.Button(root, text="Install Requirements", command=install_requirements)
    reqs_button.pack(pady=10)

    # Button to install MySQL
    mysql_button = tk.Button(root, text="Install MySQL", command=install_mysql)
    mysql_button.pack(pady=10)

    # Button to setup database
    db_button = tk.Button(root, text="Setup Database", command=setup_database)
    db_button.pack(pady=10)

    # Button to start service
    service_button = tk.Button(root, text="Start Service", command=install_service)
    service_button.pack(pady=10)

    root.mainloop()

if __name__ == "__main__":
    main()


                                                                                               #  R E G U M A T E  #

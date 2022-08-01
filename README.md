#########################################################
# notifyExec
#########################################################

This is a very basic and extremely unsecure system for executing a predefined command in NodeJS by using a virtual notification device in the Hubitat Elevation.

Warning! This version is NOT SECURE, use at your own risk!!!!

#########################################################

##### Requirements:
- Current version of Linux running on a compatible machine
  - Will likely run on most Linux capable systems
  - Latest stable release of NodeJS w/NPM installed.
  - Additional software as necessary like lsof, espeak etc. (see below for espeak instructions)
  - notifyexec has been tested on the following hardware:
    - RPI Zero2W (on Debian Bullseye with WiFI)
    - RPI 4B (On Ubuntu with ethernet)
    - Odroid XU4 (on Armbian with ethernet)
    - Odroid N2 (on Ubuntu with ethernet)
    - LXC Container running Ubuntu 22.04 LTS "Jammy"
  - Note: Windows has not been tested at this time but may work with additional modifications.
- Hubitat Elevation hub with the custom "Virtual Notification and Execute" device added to the "Drivers Code" section and a new virtual device created.
- For espeak configuration see below.

##### Linux Server side:
- As root clone this repo to a working directory (I used the PI's home directory).
- from a terminal, cd to the directory and run "npm install". This should install all the necessary components.
- see if it runs via "node app.js", leave it running.

##### HE side:
1) From github view raw format for the https://github.com/adsavia/Hubitat-DoNS/blob/master/HE_Driver/DoNS-Email.groovy
2) Cut and paste it into a new device in the drivers code section on your HE. Save.
3) Add a virtual device from the Devices page and select the "Virtual Notification and Execute (user)" driver under "Virtual Drivers". Save.
4) Modify configuration, add in NodeJS server IP + any additional parameters required. 
5) Test by entering some text in the "Device Notification" box and clicking on "Device Notification"

##### Verify and finalize:
1) On the Linux Server side - See if a directory listing has appeared in the console, if so then success! If not, check to make sure your firewall is not blocking port 3000.
2) On the Linux Server side - Modify "nex_config.json" and change "ls" to whatever command you want, recommend you add the path.
3) Test again in HE by sending another manual "Device Notification".

##### Autostart via systemd:
1) Copy notifyexec.service over to systemd system dir.
```
sudo cp notifyexec.service /etc/systemd/system
```
2) Edit file `notifyExec/misc/systemd/notifyexec.service` and change working directory to the current notifyExec directory.

3) Enable Service.
sudo systemctl enable notifyexec.service

###### usage:

Use the notification device in HE in your rules per usual.

###### misc:

- Systemd setup
Check if the service is running:
```
sudo systemctl status notifyexec
```

Check for notifyexec service issues:
```
sudo journalctl -xe
```

To change port# in systemd edit the service file usually located at /etc/systemd/system/notifyexec.service
and change the following line from 3000 to desired port. 
```
Environment=PORT=3000
```
Also make sure things like "User", "WorkingDirectory" and "ExecStart" are all relevent to your particular installation.

- Checking Ports
Check if the port specified in your configuration is being listened to:
```
sudo lsof -i -P -n | grep LISTEN
```

- To run manually, make sure systemd service is stopped..then go into the appropriate directory and run..
```
sudo systemctl stop notifyexec
cd ./notifyexec
PORT=3000 npm start
```
Note: leaving off the `PORT=3000 ` bit will default the port to 3000. Also you can run 2 or more instances manually using different port #'s and different terminal sessions - see the "screen" utility for a great way to handle disconnected terminals.


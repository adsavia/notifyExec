#########################################################
# notifyExec
#########################################################

This is a very basic and extremely unsecure system for executing a predefined command in NodeJS by using a virtual notification device in the Hubitat Elevation.

Warning! This version is NOT SECURE, use at your own risk!!!!

##### Requirements:

Linux computer/server (like RaspberryPI) with Git, NodeJS, npm installed.
Hubitat Elevation hub with the custom "Virtual Notification and Execute" device added to the "Drivers Code" section and a new virtual device created.

#########################################################

##### Simple installation instructions - your mileage may vary depending 
##### upon your system - I am using a Raspberry PI:

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
cd /home/pi/notifyexec
PORT=3000 node app.js
```
Note: leaving off the `PORT=3000 ` bit will default the port to 3000. Also you can run 2 or more instances manually using different port #'s and different terminal sessions - see the "screen" utility for a great way to handle disconnected terminals.


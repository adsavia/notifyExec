/*
 * Hub Info
 *
 *  Licensed Virtual the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Change History:
 *
 *    Date                Who            What
 *    ----                ---            ----
 *    22-07-28 12:28      erktrek        copied over virtual driver written by Robert Morris, modified to send to NodeJS server.
 *    22-07-28 15:26      erktrek        added quote delim property to bound notification text in quotes, on by default.
*/

@SuppressWarnings('unused')
static String version() {return "1.01"}

metadata {
    definition (name: "Virtual Notification and Execute", namespace: "erktrek", author: "Eric H") {
        capability "Notification"
        capability "Actuator"
    }
       
   preferences {
       input( name: "nodeAddr",type:"string",title: "NodeJS server address", description:"The location of the NodeJS server including port #.", defaultValue:"http://[NodeJS ip address]:[port]")
       input( name: "addParms",type:"string",title: "Additional Parameters [optional]", description:"Prepend additional parameters before text.", defaultValue:"")
       input(name: "useDelim", type: "bool", title: "Use quotes around notification text?", defaultValue: true)
       input(name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: true)
       input(name: "txtEnable", type: "bool", title: "Enable descriptionText logging", defaultValue: true)
   }
}

void installed() {
   log.debug "installed()"
   initialize()
}

void updated() {
   log.debug "updated()"
   initialize()
}

void initialize() {
   log.debug "initialize()"
   Integer disableTime = 1800
   if (logEnable) {
      log.debug "Debug logging will be automatically disabled in ${disableTime} seconds"
      runIn(disableTime, "debugOff")
   }
}

void debugOff() {
   log.warn "Disabling debug logging"
   device.updateSetting("logEnable", [value:"false", type:"bool"])
}

void deviceNotify() {
}

void myAsynchttpHandler(resp, data) {
   if (logEnable) log.debug "HTTP ${resp.status}"
  // whatever you might need to do here (check for errors, etc.),
   if (logEnable) log.debug "HTTP ${resp.body}"
}


void deviceNotification(notificationText) {
	if (logEnable) log.debug "deviceNotification(notificationText = ${notificationText})"
    sendEvent(name: "deviceNotification", value: notificationText, isStateChange: true)
    
    String notifyText = (useDelim ? "\"" : "" ) + notificationText + (useDelim ? "\"" : "" );
    
    Map params = [
      uri:  nodeAddr,
      contentType: "application/json",  // or whatever
      path: "/exec",
      body: [[data: notifyText], [addParms: addParms]],  // this will get converted to an array of JSON objs.
      timeout: 15
   ]

	if (logEnable) log.debug params;
    
    asynchttpPost("myAsynchttpHandler", params)

}

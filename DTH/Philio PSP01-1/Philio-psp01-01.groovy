/**
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "Philio PSP01-1 Sensor", namespace: "philipstreet", author: "Philip Street") {
		capability "Motion Sensor"
		capability "Temperature Measurement"
		capability "Configuration"
		capability "Illuminance Measurement"
		capability "Sensor"
		capability "Battery"
		capability "Health Check"

        fingerprint deviceId: "0x2001", inClusters: "0x30,0x31,0x70,0x72,0x80,0x84,0x85,0x86", outClusters: "0x20"
        fingerprint mfr:"013C", prod:"0002", model:"0002"
	}

	tiles(scale: 2) {
		multiAttributeTile(name:"motion", type: "generic", width: 6, height: 4){
			tileAttribute ("device.motion", key: "PRIMARY_CONTROL") {
				attributeState "active", label:'motion', icon:"st.motion.motion.active", backgroundColor:"#00a0dc"
				attributeState "inactive", label:'no motion', icon:"st.motion.motion.inactive", backgroundColor:"#cccccc"
			}
		}
		valueTile("temperature", "device.temperature", inactiveLabel: false, width: 2, height: 2) {
			state "temperature", label:'${currentValue}°',
			backgroundColors:[
				[value: 31, color: "#153591"],
				[value: 44, color: "#1e9cbb"],
				[value: 59, color: "#90d2a7"],
				[value: 74, color: "#44b621"],
				[value: 84, color: "#f1d801"],
				[value: 95, color: "#d04e00"],
				[value: 96, color: "#bc2323"]
			]
		}
		valueTile("illuminance", "device.illuminance", inactiveLabel: false, width: 2, height: 2) {
			state "luminosity", label:'${currentValue} lux', unit:""
		}
		valueTile("battery", "device.battery", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "battery", label:'${currentValue}% battery', unit:""
		}
		standardTile("configure", "device.configure", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "configure", label:'', action:"configuration.configure", icon:"st.secondary.configure"
		}

		main(["motion", "temperature", "illuminance"])
		details(["motion", "temperature", "illuminance", "battery", "configure"])
	}

	preferences {
	// 	// input name: "param2", type: "number", range: "1..100", required: true, description: "Parameter 2 Description", 
	// 	// 	title: "Setting the BASIC command value to turn on the light.\n"+
	// 	// 		"The 0xFF(-1) means turn on the light.\n"+
	// 	// 		"For dimmer equipment 1 to 100 means the light strength.\n"+
	// 	// 		"Default value:?"

	// 	input (
	// 		name: "param3", 
	// 		type: "number", 
	// 		range: "0..99", 
	// 		required: true,
	// 		title: "PIR sensitivity settings\n",
	// 		description: "0 means disable the PIR motion.\n"+
	// 			"1 means the lowest sensitivity, 99 means the highest sensitivity.\n"+
	// 			"High sensitivity means can detected long distance, but if there is more noise signal in the environment, it will re-trigger too frequency.\n"+
	// 			"Default value: 70"
	// 		)

	// 	input (
	// 		name: "param4", 
	// 		type: "number", 
	// 		range: "0..100", 
	// 		required: true,
	// 		title: "Setting the illumination threshold to turn on the light.\n",
	// 		description: "When the event triggered and the environment illumination lower then the threshold, the device will turn on the light.\n"+
	// 			"0 means turn off illumination detected function. And never turn on the light.\n"+
	// 			"1 means darkest.\n"+
	// 			"99 means brightest.\n"+
	// 			"100 means turn off illumination detected function. And always turn on the light.\n"+
	// 			"\n"+
	// 			"Notice: In none test mode, only the value in 1 to 99 will enable the illumination detected function and update the illumination value.\n"+
	// 			"Default value: 99"
	// 	)

	// 	input (
	// 		name: "param5", 
	// 		type: "number", 
	// 		range: "0..127", 
	// 		required: true,
	// 		title: "Operation mode.\n",
	// 		description: "Using bit to control.\n"+
	// 			"Bit0: 1 means security mode, 0 means home automation mode.\n"+
	// 			"Bit1: 1 means enable test mode, 0 means disable test mode.\n"+
	// 			"Notice: Bit0 and bit1 will effect when the DIP Switch setting to program mode. If bit1 is enabled, the bit0 is useless.\n"+
	// 			"Default value: 0"
	// 	)

	// 	input (
	// 		name: "param6", 
	// 		type: "number", 
	// 		range: "0.127", 
	// 		required: true,
	// 		title: "Multi-Sensor function switch.\n",
	// 		description: "Using bit to control.\n"+
	// 			"Bit0: Reserved, always 1.\n"+
	// 			"Bit1: Disable PIR integrate Illumination.\n"+
	// 			"Bit2: Reserved, always 1.\n"+
	// 			"Bit3: Reserved.\n"+
	// 			"Bit4: Reserved.\n"+
	// 			"Bit5: Reserved.\n"+
	// 			"Bit6:Enable temperature monitoring.\n"+
	// 			"When this bit enable, the temperature changed 3 degree Fahrenheit, it will report. And also the temperature over 140 degree Fahrenheit, it will report every 64 seconds.\n"+
	// 			"Default value: 4"
	// 	)

	// 	input (
	// 		name: "param8", 
	// 		type: "number", 
	// 		range: "3..127", 
	// 		required: true,
	// 		title: "PIR Re-Detect Interval Time\n",
	// 		description: "In the security mode, after the PIR motion detected, setting the re-detect time. 8 seconds per tick, and minimum time is 24 seconds, default tick is 3 (24 seconds).\n"+
	// 			"Setting the suitable value to prevent received the trigger signal too frequency. Also can save the battery energy.\n"+
	// 			"Notice: If this value bigger than the configuration setting NO. 9. There is a period after the light turned off and the PIR not detecting.\n"+
	// 			"Default value: 3"
	// 	)

	// 	input (
	// 		name: "param9", 
	// 		type: "number", 
	// 		range: "4..127", 
	// 		required: true,
	// 		title: "Turn Off Light Time\n",
	// 		description: "After turn on the light, setting the delay time to turn off the light when the PIR motion is not detected. 8 seconds per tick, and minimum time is 32 seconds, default tick is 4 (32 seconds).\n"+
	// 			"Default value: 4"
	// 	)

	// 	input (
	// 		name: "param10", 
	// 		type: "number", 
	// 		range: "1..127", 
	// 		required: true,
	// 		title: "Auto Report Battery Time\n",
	// 		description: "The interval time for auto report the battery level. 30 minutes per tick and minimum time is 30 minutes, default tick is 12 (6 hours).\n"+
	// 			"Default value: 12"
	// 	)

	// 	input (
	// 		name: "param12", 
	// 		type: "number", 
	// 		range: "1..127", 
	// 		required: true,
	// 		title: "Auto Report Illumination Time\n",
	// 		description: "The interval time for auto report the illumination. 30 minutes per tick and minimum time is 30 minutes, default tick is 12 (6 hours).\n"+
	// 			"Default value: 12"
	// 	)

	// 	input (
	// 		name: "param13", 
	// 		type: "number", 
	// 		range: "1..127", 
	// 		required: true,
	// 		title: "Auto Report Temperature Time\n",
	// 		description: "The interval time for auto report the temperature. 30 minutes per tick and minimum time is 30 minutes, default tick is 12 (6 hours).\n"+
	// 			"Default value: 12"
	// 	)
	}
}

def installed(){
// Device-Watch simply pings if no device events received for 32min(checkInterval)
	log.debug "installed() called"
	sendEvent(name: "checkInterval", value: 2 * 60 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
	configure()
}

def updated(){
// Device-Watch simply pings if no device events received for 32min(checkInterval)
	log.debug "updated() called"
	sendEvent(name: "checkInterval", value: 2 * 60 * 60 + 2 * 60, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
	configure()
}

// Parse incoming device messages to generate events
def parse(String description)
{
	//log.debug "parse() called with ${description}"
	def result = []
	def cmd = zwave.parse(description, [0x30: 2, 0x31: 5, 0x70: 1, 0x72: 2, 0x80: 1, 0x84: 2, 0x85: 2, 0x86: 1])
	//log.debug "Parsed CMD: ${cmd.toString()}"
	if (cmd) {
		if( cmd.CMD == "8407" ) { result << new physicalgraph.device.HubAction(zwave.wakeUpV2.wakeUpNoMoreInformation().format()) }
		def evt = zwaveEvent(cmd)
		//log.debug "EVT: ${evt.toString()}"
		result << createEvent(evt)
	}
    def statusTextmsg = "Status:"

    if (device.currentState('motion')) {
        statusTextmsg += " motion is ${device.currentState('motion').value},"
    }
    if (device.currentState('temperature')) {
        statusTextmsg += " temperature is ${device.currentState('temperature').value}°,"
    }
    if (device.currentState('illuminance')) {
        statusTextmsg += " illuminance is ${device.currentState('illuminance').value} lux,"
    }
    // trim trailing comma:
    statusTextmsg = statusTextmsg.substring(0, statusTextmsg.length() - 1)
    //log.debug statusTextmsg

	log.debug "Parse returned ${result}"
	return result
}

// Event Generation
def zwaveEvent(physicalgraph.zwave.commands.wakeupv2.WakeUpNotification cmd)
{
	log.debug "zwaveEvent: ${cmd.toString()}"
	[descriptionText: "${device.displayName} woke up", isStateChange: false]
}

def zwaveEvent(physicalgraph.zwave.commands.sensormultilevelv5.SensorMultilevelReport cmd)
{
	log.debug "zwaveEvent: ${cmd.toString()}"
	def map = [:]
	switch (cmd.sensorType) {
		case 1:
			// temperature
			def cmdScale = cmd.scale == 1 ? "F" : "C"
			map.value = convertTemperatureIfNeeded(cmd.scaledSensorValue, cmdScale, cmd.precision)
			map.unit = getTemperatureScale()
			map.name = "temperature"
			break;
		case 3:
			// luminance
			map.value = cmd.scaledSensorValue.toInteger().toString()
			map.unit = "lux"
			map.name = "illuminance"
			break;
	}
	//map
	createEvent(map)
}

def zwaveEvent(physicalgraph.zwave.commands.batteryv1.BatteryReport cmd) {
	log.debug "zwaveEvent: ${cmd.toString()}"
	def map = [:]
	map.name = "battery"
	map.value = cmd.batteryLevel > 0 ? cmd.batteryLevel.toString() : 1
	map.unit = "%"
	map.displayed = false
	
	//map
	createEvent(map)
}

def zwaveEvent(physicalgraph.zwave.commands.sensorbinaryv2.SensorBinaryReport cmd) {
	log.debug "zwaveEvent: ${cmd.toString()}"
	def map = [:]
	switch (cmd.sensorType) {
		case 12: // motion sensor
			map.name = "motion"
			//log.debug "PSP01-1 cmd.sensorValue: ${cmd.sensorValue}"
			if (cmd.sensorValue.toInteger() > 0 ) {
				//log.debug "PSP01-1 Motion Detected"
				map.value = "active"
				map.descriptionText = "$device.displayName is active"
			} else {
				log.debug "PSP01-1 No Motion"
				map.value = "inactive"
				map.descriptionText = "$device.displayName no motion"
			}
			map.isStateChange = true
			break;
	}
	//map
	createEvent(map)
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
	log.debug "Catchall reached for cmd: ${cmd.toString()}}"
	[:]
}

/**
 * PING is used by Device-Watch in attempt to reach the Device
 * */
def ping() {
	log.debug "ping() called"
	secure(zwave.batteryV1.batteryGet())
}

def configure() {
	log.debug "configure() called"
	delayBetween([

		// PIR Sensitivity 1-100
		zwave.configurationV1.configurationSet(parameterNumber: 3, size: 1, scaledConfigurationValue: 70).format(), 
		// Light Threshold
		zwave.configurationV1.configurationSet(parameterNumber: 4, size: 1, scaledConfigurationValue: 99).format(), 
		// Operation Mode
		zwave.configurationV1.configurationSet(parameterNumber: 5, size: 1, scaledConfigurationValue: 0).format(), 
		// Multi-Sensor Function Switch
		zwave.configurationV1.configurationSet(parameterNumber: 6, size: 1, scaledConfigurationValue: 4).format(), 
		// Customer Function
		zwave.configurationV1.configurationSet(parameterNumber: 7, size: 1, scaledConfigurationValue: 4).format(), 
		// PIR Re-Detect Interval Time
		zwave.configurationV1.configurationSet(parameterNumber: 8, size: 1, scaledConfigurationValue: 3).format(), 
		// Turn Off Light Time
		zwave.configurationV1.configurationSet(parameterNumber: 9, size: 1, scaledConfigurationValue: 4).format(), 
		// Auto Report Battery Time
		zwave.configurationV1.configurationSet(parameterNumber: 10, size: 1, scaledConfigurationValue: 12).format(), 
		// Auto Report Illumination Time
		zwave.configurationV1.configurationSet(parameterNumber: 12, size: 1, scaledConfigurationValue: 12).format(), 
		// Auto Report Temperature Time
		zwave.configurationV1.configurationSet(parameterNumber: 13, size: 1, scaledConfigurationValue: 12).format(), 
		// Wake up every hour
		zwave.wakeUpV2.wakeUpIntervalSet(seconds: 1 * 3600, nodeid:zwaveHubNodeId).format(),                        

	])
}
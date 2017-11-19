/**
 *  Copyright: Philip Street (philipstreet)
 *
 *  Name: Philio PSP01-1 3-in-1 Multisensor
 *
 *  Date: 2017-11-18
 *
 *  Version: 1.0.1
 *
 *  Source: https://github.com/philipstreet/SmartThings/tree/master/devices/philio-psp01-1
 *
 *  Author: Philip Street (philipstreet)
 *
 *  Description: An advanced SmartThings device handler for the Philio PSP01-1 3-in-1 Multisensor.
 *
 *  Acknowledgments: Parts of this code were inspired by David Lomas (codersaur) device handler for GreenWave PowerNode 
 *  (Single socket) Z-Wave power outlet (https://github.com/codersaur/SmartThings/tree/master/devices/greenwave-powernode-single).
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

		// COMMAND_CLASS_BASIC = 0x20 V1
		// COMMAND_CLASS_SENSOR_BINARY_V2 = 0x30 V2
		// COMMAND_CLASS_SENSOR_MULTILEVEL_V5 = 0x31 V5
		// COMMAND_CLASS_CONFIGURATION = 0x70 V1
		// COMMAND_CLASS_MANUFACTURER_SPECIFIC_V2 = 0x72 V2
		// COMMAND_CLASS_BATTERY = 0x80 V1
		// COMMAND_CLASS_WAKE_UP_V2 = 0x84 V2
		// COMMAND_CLASS_ASSOCIATION_V2 = 0x85 V2
		// COMMAND_CLASS_VERSION = 0x86 V1

        fingerprint deviceId: "0x2001", inClusters: "0x20,0x30,0x31,0x70,0x72,0x80,0x84,0x85,0x86", outClusters: "0x20"
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
	 	input (name: "param2", 
		 	defaultValue: "-1",
	 		type: "number", 
	 		range: "-1..100",
	 		title: "Setting the BASIC command value to turn on the light.\n"+
            	"The 0xFF(-1) means turn on the light. For dimmer equipment 1 to 100 means the light strength.\n"+
                "Default value: 255 (according to documentation, but valid range is -1 to 100)\n"+
				"Suggested Value: -1")
	 	
	 	input (name: "param3", 
		 	defaultValue: "70",
	 		type: "number", 
	 		range: "0..99", 
	 		title: "0 means disable the PIR motion.\n"+
	 			"1 means the lowest sensitivity, 99 means the highest sensitivity.\n"+
	 			"High sensitivity means can detected long distance, but if there is more noise signal in the environment, it will re-trigger too frequency.\n"+
	 			"Default value: 70")

	 	input (name: "param4", 
		 	defaultValue: "99",
	 		type: "number", 
	 		range: "0..100", 
	 		title: "Setting the illumination threshold to turn on the light.\n"+
                "When the event triggered and the environment illumination lower then the threshold, the device will turn on the light.\n"+
	 			"0 means turn off illumination detected function. And never turn on the light.\n"+
	 			"1 means darkest.\n"+
	 			"99 means brightest.\n"+
	 			"100 means turn off illumination detected function. And always turn on the light.\n"+
	 			"\n"+
	 			"Notice: In none test mode, only the value in 1 to 99 will enable the illumination detected function and update the illumination value.\n"+
	 			"Default value: 99")

	 	input (name: "param5", 
		 	defaultValue: "13",
	 		type: "number", 
	 		range: "0..127", 
	 		title: "Operation mode.\n"+
                "Using bit to control.\n"+
	 			"Bit0: 1 means security mode, 0 means home automation mode.\n"+
	 			"Bit1: 1 means enable test mode, 0 means disable test mode.\n"+
				"Bit2: 1 (Reserved, always 1)\n"+
				"Bit3: 0 = Fahrenheit, 1 = Celsius\n"+
				"Bit4: Enable illumination report\n"+
				"Bit5: Enable temperature report\n"+
				"Bit6: Unknown\n"+
				"Bit7: Unknown\n"+
	 			"Notice: Bit0 and bit1 will take effect when the DIP Switch setting to program mode. If bit1 is enabled, the bit0 is useless.\n"+
				"Default value: 0\n"+
	 			"Suggested value: 13")

	 	input (name: "param6",
		 	defaultValue: "69", 
	 		type: "number", 
	 		range: "0..127", 
	 		title: "Multi-Sensor function switch.\n"+
                "Using bit to control.\n"+
	 			"Bit0: Reserved, always 1\n"+
	 			"Bit1: Disable PIR integrated illumination\n"+
	 			"Bit2: Reserved, always 1\n"+
	 			"Bit3: Reserved\n"+
	 			"Bit4: Reserved\n"+
	 			"Bit5: Reserved\n"+
	 			"Bit6: Enable temperature monitoring\n"+
				"Bit7: Unknown\n"+
	 			"When bit6 is enabled, a temperature change of 3 degree Fahrenheit will be reported. Also, when the temperature is over 140 degree Fahrenheit, it will report every 64 seconds.\n"+
	 			"Default value: 4\n"+
				"Suggested value: 69")

            input (name: "param7",
				defaultValue: "22",
                type: "number",
                range: "0..127",
                title: "Customer function switch, using bit control.\n"+
					"Bit0: Unknown\n"+
					"Bit1: Unknown\n"+
				    "Bit2: Enable PIR super sensitivity mode\n"+
					"Bit3: Unknown\n"+
					"Bit4: Unknown\n"+
					"Bit5: Unknown\n"+
					"Bit6: Unknown\n"+
					"Bit7: Unknown\n"+
                    "Default value: 4\n"+
					"Suggested value: 22")

	 	input (name: "param8", 
		 	defaultValue: "3",
	 		type: "number", 
	 		range: "3..127", 
	 		title: "PIR Re-Detect Interval Time\n"+
                "In the security mode, after the PIR motion detected, setting the re-detect time. 8 seconds per tick, and minimum time is 24 seconds, default tick is 3 (24 seconds).\n"+
	 			"Setting the suitable value to prevent received the trigger signal too frequency. Also can save the battery energy.\n"+
	 			"Notice: If this value bigger than the configuration setting NO. 9. There is a period after the light turned off and the PIR not detecting.\n"+
	 			"Default value: 3")

	 	input (name: "param9", 
		 	defaultValue: "4",
	 		type: "number", 
	 		range: "4..127", 
	 		title: "Turn Off Light Time\n"+
                "After turn on the light, setting the delay time to turn off the light when the PIR motion is not detected. 8 seconds per tick, and minimum time is 32 seconds, default tick is 4 (32 seconds).\n"+
	 			"Default value: 4")

	 	input (name: "param10", 
		 	defaultValue: "12",
	 		type: "number", 
	 		range: "1..127", 
	 		title: "Auto Report Battery Time\n"+
                "The interval time for auto report the battery level. 30 minutes per tick and minimum time is 30 minutes, default tick is 12 (6 hours).\n"+
	 			"Default value: 12")

	 	input (name: "param12", 
		 	defaultValue: "12",
	 		type: "number", 
	 		range: "1..127", 
	 		title: "Auto Report Illumination Time\n"+
                "The interval time for auto report the illumination. 30 minutes per tick and minimum time is 30 minutes, default tick is 12 (6 hours).\n"+
	 			"Default value: 12")

		input (name: "param13",
			defaultValue: "12", 
	 		type: "number", 
	 		range: "1..127", 
	 		title: "Auto Report Temperature Time\n"+
                "The interval time for auto report the temperature. 30 minutes per tick and minimum time is 30 minutes, default tick is 12 (6 hours).\n"+
	 			"Default value: 12")

		input (name: "configLoggingLevelIDE",
			title: "IDE Live Logging Level:\nMessages with this level and higher will be logged to the IDE.",
			type: "enum",
			options: [
				"0" : "None",
				"1" : "Error",
				"2" : "Warning",
				"3" : "Info",
				"4" : "Debug",
				"5" : "Trace"
			],
			defaultValue: "3",
			required: false
		)

		input (name: "configLoggingLevelDevice",
			title: "Device Logging Level:\nMessages with this level and higher will be logged to the logMessage attribute.",
			type: "enum",
			options: [
				"0" : "None",
				"1" : "Error",
				"2" : "Warning"
			],
			defaultValue: "2",
			required: false
		)
	}
}

def wakeUpInterval()
{
	return 1 * 60 * 60
}

def installed(){
	logger("installed()","trace")

	// Initialise internal state:
	state.loggingLevelIDE     = 3
    state.loggingLevelDevice  = 2

	// 2 * 60 * 60 + 2 * 60
	sendEvent(name: "checkInterval", value: wakeUpInterval(), displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
	configure()
}

def updated(){
	logger("updated()","trace")

	// Update internal state:
	if (!state.updatedLastRanAt || now() >= state.updatedLastRanAt + 2000) {
		state.updatedLastRanAt = now()

		state.loggingLevelIDE     = (settings.configLoggingLevelIDE) ? settings.configLoggingLevelIDE.toInteger() : 3
		state.loggingLevelDevice  = (settings.configLoggingLevelDevice) ? settings.configLoggingLevelDevice.toInteger(): 2

		sendEvent(name: "checkInterval", value: wakeUpInterval(), displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])
		configure()
    }
    else {
        logger("updated(): Ran within last 2 seconds so aborting.","debug")
    }

}

// Parse incoming device messages to generate events
def parse(String description)
{
    logger("parse(): Parsing raw message: ${description}","trace")

	def result = []
	def cmd = zwave.parse(description, [0x20: 1, 0x30: 2, 0x31: 5, 0x70: 1, 0x72: 2, 0x80: 1, 0x84: 2, 0x85: 2, 0x86: 1])

	if (cmd) {
		if( cmd.CMD == "8407" ) { 
			logger("CMD=8407, wakeUpNoMoreInformation()","trace")
			result << new physicalgraph.device.HubAction(zwave.wakeUpV2.wakeUpNoMoreInformation().format()) 
			}
		def evt = zwaveEvent(cmd)
		result << createEvent(evt)
	} else {
        logger("parse(): Could not parse raw message: ${description}","error")
    }

	return result
}

// Ping device
def ping() {
	logger("ping()","trace")
	zwave.batteryV1.batteryGet()
}

// Configure devices settings
def configure() {
	logger("configure()","trace")
	delayBetween([

		// PIR Sensitivity 1-100
		zwave.configurationV1.configurationSet(parameterNumber: 3, size: 1, scaledConfigurationValue: 70).format(), 
		// Light Threshold
		zwave.configurationV1.configurationSet(parameterNumber: 4, size: 1, scaledConfigurationValue: 99).format(), 
		// Operation Mode
		// 13 = 00001101
		// Bit7 = 0 (Unknown)
		// Bit6 = 0 (Unknown)
		// Bit5 = 0 (Enable temp report) 
		// Bit4 = 0 (Enable illumination report) 
		// Bit3 = 1 (Celsius)
		// Bit2 = 1 (Reserverd, always 1)
		// Bit1 = 0 (disable test mode)
		// Bit0 = 1 (security mode)
		zwave.configurationV1.configurationSet(parameterNumber: 5, size: 1, scaledConfigurationValue: 13).format(), 
		// Multi-Sensor Function Switch
		// 69 = 01000101
		// Bit7 = 0 (Unknown)
		// Bit6 = 1 (Enable temp monitoring)
		// Bit5 = 0 (Reserved)
		// Bit4 = 0 (Reserved)
		// Bit3 = 0 (Reserved)
		// Bit2 = 1 (Reserved, always 1)
		// Bit1 = 0 (Enable PIR integrate illumination)
		// Bit0 = 1 (Reserved, always 1)
		zwave.configurationV1.configurationSet(parameterNumber: 6, size: 1, scaledConfigurationValue: 69).format(), 
		// Customer Function
		// 22 = 00010110
		// Bit7 = 0 (Unknown)
		// Bit6 = 0 (Unknown)
		// Bit5 = 0 (Unknown)
		// Bit4 = 1 (Unknown)
		// Bit3 = 0 (Unknown)
		// Bit2 = 1 (Enable PIR super sensitivity mode)
		// Bit1 = 0 (Unknown)
		// Bit0 = 1 (Unknown)
		zwave.configurationV1.configurationSet(parameterNumber: 7, size: 1, scaledConfigurationValue: 22).format(), 
		// PIR Re-Detect Interval Time
		zwave.configurationV1.configurationSet(parameterNumber: 8, size: 1, scaledConfigurationValue: 3).format(), 
		// Turn Off Light Time
		zwave.configurationV1.configurationSet(parameterNumber: 9, size: 1, scaledConfigurationValue: 4).format(), 
		// Auto Report Battery Time
		zwave.configurationV1.configurationSet(parameterNumber: 10, size: 1, scaledConfigurationValue: 12).format(), 
        // Unknown
        zwave.configurationV1.configurationSet(parameterNumber: 11, size: 1, scaledConfigurationValue: 12).format(),
		// Auto Report Illumination Time
		zwave.configurationV1.configurationSet(parameterNumber: 12, size: 1, scaledConfigurationValue: 12).format(), 
		// Auto Report Temperature Time
		zwave.configurationV1.configurationSet(parameterNumber: 13, size: 1, scaledConfigurationValue: 12).format(), 
		// Wake up every hour
		zwave.wakeUpV2.wakeUpIntervalSet(seconds: wakeUpInterval(), nodeid:zwaveHubNodeId).format(),                        

	])
}

/*****************************************************************************************************************
 *  Z-wave Event Handlers.
 *****************************************************************************************************************/

// COMMAND_CLASS_BASIC = 0x20 V1
def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
    logger("zwaveEvent(): Basic Report received: ${cmd}","trace")
    [:]
}

// COMMAND_CLASS_SENSOR_BINARY_V2 = 0x30 V2
def zwaveEvent(physicalgraph.zwave.commands.sensorbinaryv2.SensorBinaryReport cmd) {
    logger("zwaveEvent(): Sensor Binary Report received: ${cmd}","trace")

	def result = []
	def map = [:]
	switch (cmd.sensorType) {
		case 12: // motion sensor
			map.name = "motion"
			if (cmd.sensorValue.toInteger() > 0 ) {
				logger("Motion is active","info")
				map.value = "active"
				map.descriptionText = "$device.displayName is active"
			} else {
				logger("Motion is inactive","info")
				map.value = "inactive"
				map.descriptionText = "$device.displayName no motion"
			}
			map.isStateChange = true
			break;
	}
	//map
	result = createEvent(map)
	return result
}

// COMMAND_CLASS_SENSOR_MULTILEVEL_V5 = 0x31 V5
def zwaveEvent(physicalgraph.zwave.commands.sensormultilevelv5.SensorMultilevelReport cmd)
{
	logger("zwaveEvent(): Sensor Multilevel Report received: ${cmd}","trace")
	def result = []
	def map = [:]
	switch (cmd.sensorType) {
		case 1:
			// temperature
			def cmdScale = cmd.scale == 1 ? "F" : "C"
			map.value = convertTemperatureIfNeeded(cmd.scaledSensorValue, cmdScale, cmd.precision)
			map.unit = getTemperatureScale()
			map.name = "temperature"
			log.info "Temperature reading is ${map.value} ${map.unit}"
			break;
		case 3:
			// luminance
			map.value = cmd.scaledSensorValue.toInteger().toString()
			map.unit = "lux"
			map.name = "illuminance"
			log.info "Illuminance reading is ${map.value} ${map.unit}"
			break;
	}
	//map
	result = createEvent(map)
	return result
}

// COMMAND_CLASS_CONFIGURATION = 0x70 V1
def zwaveEvent(physicalgraph.zwave.commands.configurationv1.ConfigurationReport cmd) {
	logger("zwaveEvent(): Configuration Report received: ${cmd}","trace")

	def result = []

	return result
}

// COMMAND_CLASS_MANUFACTURER_SPECIFIC_V2 = 0x72 V2
def zwaveEvent(physicalgraph.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
    logger("zwaveEvent(): Manufacturer-Specific Report received: ${cmd}","trace")

    // Display as hex strings:
    def manufacturerIdDisp = String.format("%04X",cmd.manufacturerId)
    def productIdDisp = String.format("%04X",cmd.productId)
    def productTypeIdDisp = String.format("%04X",cmd.productTypeId)

    logger("Manufacturer-Specific Report: Manufacturer ID: ${manufacturerIdDisp}, Manufacturer Name: ${cmd.manufacturerName}" +
    ", Product Type ID: ${productTypeIdDisp}, Product ID: ${productIdDisp}","info")

}

// COMMAND_CLASS_BATTERY = 0x80 V1
def zwaveEvent(physicalgraph.zwave.commands.batteryv1.BatteryReport cmd) {
    logger("zwaveEvent(): Battery Report received: ${cmd}","trace")

	def result = []
	def map = [:]
	map.name = "battery"
	map.value = cmd.batteryLevel > 0 ? cmd.batteryLevel.toString() : 1
	map.unit = "%"
	map.displayed = false
	
	//map
	result = createEvent(map)
	return result
}

// COMMAND_CLASS_WAKE_UP_V2 = 0x84 V2
def zwaveEvent(physicalgraph.zwave.commands.wakeupv2.WakeUpNotification cmd)
{
    logger("zwaveEvent(): Wake Up Notification received: ${cmd}","trace")
	[descriptionText: "${device.displayName} woke up", isStateChange: false]
}

// COMMAND_CLASS_ASSOCIATION_V2 = 0x85 V2
def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationReport cmd) {
    logger("zwaveEvent(): Association Report received: ${cmd}","trace")

    // Display to user in hex format (same as IDE):
    def hexArray  = []
    cmd.nodeId.each { hexArray.add(String.format("%02X", it)) };
    def assocGroupMd = getAssocGroupsMd().find( { it.id == cmd.groupingIdentifier })
    logger("Association Group ${cmd.groupingIdentifier} [${assocGroupMd?.name}] contains nodes: ${hexArray} (hexadecimal format)","info")
}

// COMMAND_CLASS_VERSION = 0x86 V1
def zwaveEvent(physicalgraph.zwave.commands.versionv1.VersionReport cmd) {
    logger("zwaveEvent(): Version Report received: ${cmd}","trace")

    def zWaveLibraryTypeDisp  = String.format("%02X",cmd.zWaveLibraryType)
    def zWaveLibraryTypeDesc  = ""
    switch(cmd.zWaveLibraryType) {
        case 1:
            zWaveLibraryTypeDesc = "Static Controller"
            break

        case 2:
            zWaveLibraryTypeDesc = "Controller"
            break

        case 3:
            zWaveLibraryTypeDesc = "Enhanced Slave"
            break

        case 4:
            zWaveLibraryTypeDesc = "Slave"
            break

        case 5:
            zWaveLibraryTypeDesc = "Installer"
            break

        case 6:
            zWaveLibraryTypeDesc = "Routing Slave"
            break

        case 7:
            zWaveLibraryTypeDesc = "Bridge Controller"
            break

        case 8:
            zWaveLibraryTypeDesc = "Device Under Test (DUT)"
            break

        case 0x0A:
            zWaveLibraryTypeDesc = "AV Remote"
            break

        case 0x0B:
            zWaveLibraryTypeDesc = "AV Device"
            break

        default:
            zWaveLibraryTypeDesc = "N/A"
    }

    def applicationVersionDisp = String.format("%d.%02d",cmd.applicationVersion,cmd.applicationSubVersion)
    def zWaveProtocolVersionDisp = String.format("%d.%02d",cmd.zWaveProtocolVersion,cmd.zWaveProtocolSubVersion)

    state.fwVersion = new BigDecimal(applicationVersionDisp)

    logger("Version Report: Application Version: ${applicationVersionDisp}, " +
           "Z-Wave Protocol Version: ${zWaveProtocolVersionDisp}, " +
           "Z-Wave Library Type: ${zWaveLibraryTypeDisp} (${zWaveLibraryTypeDesc})","info")

}

// Catch-all Command
def zwaveEvent(physicalgraph.zwave.Command cmd) {
	logger("zwaveEvent(): Command received: ${cmd}","trace")
	[:]
}

/*****************************************************************************************************************
 *  Private Helper Functions:
 *****************************************************************************************************************/

/**
 *  logger()
 *
 *  Wrapper function for all logging:
 *    Logs messages to the IDE (Live Logging), and also keeps a historical log of critical error and warning
 *    messages by sending events for the device's logMessage attribute.
 *    Configured using configLoggingLevelIDE and configLoggingLevelDevice preferences.
 **/
private logger(msg, level = "debug") {

    switch(level) {
        case "error":
            if (state.loggingLevelIDE >= 1) log.error msg
            if (state.loggingLevelDevice >= 1) sendEvent(name: "logMessage", value: "ERROR: ${msg}", displayed: false, isStateChange: true)
            break

        case "warn":
            if (state.loggingLevelIDE >= 2) log.warn msg
            if (state.loggingLevelDevice >= 2) sendEvent(name: "logMessage", value: "WARNING: ${msg}", displayed: false, isStateChange: true)
            break

        case "info":
            if (state.loggingLevelIDE >= 3) log.info msg
            break

        case "debug":
            if (state.loggingLevelIDE >= 4) log.debug msg
            break

        case "trace":
            if (state.loggingLevelIDE >= 5) log.trace msg
            break

        default:
            log.debug msg
            break
    }
}

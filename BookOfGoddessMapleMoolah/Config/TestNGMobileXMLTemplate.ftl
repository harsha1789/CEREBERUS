<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="Parallel test suite" parallel="tests" thread-count="10"
	verbose="10">
	
	<#list DeviceList as deviceInfo>
			
		<test name="${deviceInfo.testName}">
			<parameter name="IpAddress" value="127.0.0.1" />
			<parameter name="DeviceId" value="${deviceInfo.serial}" />
			<parameter name="Port" value="4723" />
			<parameter name="OSVersion" value="${deviceInfo.version}" />
			<parameter name="Proxy" value="0" />
			<parameter name="Username" value="${deviceInfo.username}" />
			<parameter name="ZenReplica" value= "True" />
			<parameter name="DeviceName" value="${deviceInfo.model}" />
			<parameter name="Browser" value="${deviceInfo.browserName}" />
			<parameter name="OSPlatform" value="${deviceInfo.osPlatform}" />
			<parameter name="GameName" value="${gameName}" />
			<parameter name="CheckedOutDeviceNum" value="${deviceInfo.checkedOutDeviceNum}" />
			<classes>
				<class name="com.zensar.automation.framework.driver.MobileDriver" />
			</classes>
		</test>
	</#list>
 </suite>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="Parallel test suite" parallel="tests" thread-count="10"
	verbose="10">
	
	<#list DeviceList as deviceInfo>
			
		<test name="${deviceInfo.testName}">
			<parameter name="ipAddress" value="127.0.0.1" />
			<parameter name="deviceid" value="${deviceInfo.serial}" />
			<parameter name="port" value="4723" />
			<parameter name="osVersion" value="6.0.1" />
			<parameter name="proxy" value="0" />
			<parameter name="username" value="${deviceInfo.username}" />
			<parameter name="ZenReplica" value= "True" />
			<parameter name="DeviceName" value="${deviceInfo.marketName}" />
			<parameter name="LockType" value="Pin" />
			<parameter name="LockKey" value="12345" />
			<parameter name="GameName" value="${gameName}" />
			<parameter name="checkedOutDeviceNum" value="${deviceInfo.checkedOutDeviceNum}" />
			<classes>
				<class name="Driver.MobileDriver" />
			</classes>
		</test>
	</#list>

 </suite>
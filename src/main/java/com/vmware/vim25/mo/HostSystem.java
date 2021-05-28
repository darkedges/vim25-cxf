/*================================================================================
Copyright (c) 2012 Steve Jin. All Rights Reserved.
Copyright (c) 2008 VMware, Inc. All Rights Reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, 
this list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice, 
this list of conditions and the following disclaimer in the documentation 
and/or other materials provided with the distribution.

* Neither the name of VMware, Inc. nor the names of its contributors may be used
to endorse or promote products derived from this software without specific prior 
written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL VMWARE, INC. OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.
================================================================================*/

package com.vmware.vim25.mo;

import java.util.List;

import com.vmware.vim25.DasConfigFaultFaultMsg;
import com.vmware.vim25.HostCapability;
import com.vmware.vim25.HostConfigFaultFaultMsg;
import com.vmware.vim25.HostConfigInfo;
import com.vmware.vim25.HostConfigManager;
import com.vmware.vim25.HostConnectFaultFaultMsg;
import com.vmware.vim25.HostConnectInfo;
import com.vmware.vim25.HostConnectSpec;
import com.vmware.vim25.HostFlagInfo;
import com.vmware.vim25.HostHardwareInfo;
import com.vmware.vim25.HostIpmiInfo;
import com.vmware.vim25.HostLicensableResourceInfo;
import com.vmware.vim25.HostListSummary;
import com.vmware.vim25.HostMaintenanceSpec;
import com.vmware.vim25.HostPowerOpFailedFaultMsg;
import com.vmware.vim25.HostRuntimeInfo;
import com.vmware.vim25.HostServiceTicket;
import com.vmware.vim25.HostSystemReconnectSpec;
import com.vmware.vim25.HostSystemResourceInfo;
import com.vmware.vim25.HostSystemSwapConfiguration;
import com.vmware.vim25.HostTpmAttestationReport;
import com.vmware.vim25.InvalidIpmiLoginInfoFaultMsg;
import com.vmware.vim25.InvalidIpmiMacAddressFaultMsg;
import com.vmware.vim25.InvalidLoginFaultMsg;
import com.vmware.vim25.InvalidNameFaultMsg;
import com.vmware.vim25.InvalidStateFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NotSupportedFaultMsg;
import com.vmware.vim25.RequestCanceledFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TimedoutFaultMsg;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.mo.util.MorUtil;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class HostSystem extends ManagedEntity {

	private HostConfigManager configManager = null;

	public HostSystem(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	public HostCapability getCapability() {
		return (HostCapability) getCurrentProperty("capability");
	}

	public HostConfigInfo getConfig() {
		return (HostConfigInfo) getCurrentProperty("config");
	}

	public List<Datastore> getDatastores() {
		return getDatastores("datastore");
	}

	public HostDatastoreBrowser getDatastoreBrowser() {
		return (HostDatastoreBrowser) getManagedObject("datastoreBrowser");
	}

	public HostHardwareInfo getHardware() {
		return (HostHardwareInfo) getCurrentProperty("hardware");
	}

	/**
	 * @since SDK5.0
	 */
	public HostLicensableResourceInfo getLicensableResource() {
		return (HostLicensableResourceInfo) getCurrentProperty("licensableResource");
	}

	public List<Network> getNetworks() {
		return getNetworks("network");
	}

	public HostRuntimeInfo getRuntime() {
		return (HostRuntimeInfo) getCurrentProperty("runtime");
	}

	public HostListSummary getSummary() {
		return (HostListSummary) getCurrentProperty("summary");
	}

	public HostSystemResourceInfo getSystemResources() {
		return (HostSystemResourceInfo) getCurrentProperty("systemResources");
	}

	public List<VirtualMachine> getVms() {
		return getVms("vm");
	}

	public HostServiceTicket acquireCimServicesTicket() throws RuntimeFaultFaultMsg {
		return getVimService().acquireCimServicesTicket(getMOR());
	}

	public Task disconnectHost() throws RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().disconnectHostTask(getMOR());
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since SDK4.1
	 */
	public void enterLockdownMode() throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg {
		getVimService().enterLockdownMode(getMOR());
	}

	public Task enterMaintenanceMode(int timeout, boolean evacuatePoweredOffVms,
			HostMaintenanceSpec hostMaintenanceSpec)
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TimedoutFaultMsg {
		ManagedObjectReference mor = getVimService().enterMaintenanceModeTask(getMOR(), timeout,
				new Boolean(evacuatePoweredOffVms), hostMaintenanceSpec);
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since SDK4.1
	 */
	public void exitLockdownMode() throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg {
		getVimService().exitLockdownMode(getMOR());
	}

	public Task exitMaintenanceMode(int timeout) throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TimedoutFaultMsg {
		ManagedObjectReference mor = getVimService().exitMaintenanceModeTask(getMOR(), timeout);
		return new Task(getServerConnection(), mor);
	}

	public Task powerDownHostToStandBy(int timeSec, boolean evacuatePoweredOffVms)
			throws HostPowerOpFailedFaultMsg, InvalidStateFaultMsg, NotSupportedFaultMsg, RequestCanceledFaultMsg,
			RuntimeFaultFaultMsg, TimedoutFaultMsg {
		ManagedObjectReference mor = getVimService().powerDownHostToStandByTask(getMOR(), timeSec,
				new Boolean(evacuatePoweredOffVms));
		return new Task(getServerConnection(), mor);
	}

	public Task powerUpHostFromStandBy(int timeSec) throws HostPowerOpFailedFaultMsg, InvalidStateFaultMsg,
			NotSupportedFaultMsg, RuntimeFaultFaultMsg, TimedoutFaultMsg {
		ManagedObjectReference mor = getVimService().powerUpHostFromStandByTask(getMOR(), timeSec);
		return new Task(getServerConnection(), mor);
	}

	public HostConnectInfo queryHostConnectionInfo() throws RuntimeFaultFaultMsg {
		return getVimService().queryHostConnectionInfo(getMOR());
	}

	/** @since SDK5.1 */
	public HostTpmAttestationReport queryTpmAttestationReport() throws RuntimeFaultFaultMsg {
		return getVimService().queryTpmAttestationReport(getMOR());
	}

	/** @since SDK5.1 */
	public void updateSystemSwapConfiguration(HostSystemSwapConfiguration sysSwapConfig) throws RuntimeFaultFaultMsg {
		getVimService().updateSystemSwapConfiguration(getMOR(), sysSwapConfig);
	}

	public long queryMemoryOverhead(long memorySize, int videoRamSize, int numVcpus) throws RuntimeFaultFaultMsg {
		return getVimService().queryMemoryOverhead(getMOR(), memorySize, new Integer(videoRamSize), numVcpus);
	}

	public long queryMemoryOverheadEx(VirtualMachineConfigInfo vmConfigInfo) throws RuntimeFaultFaultMsg {
		return getVimService().queryMemoryOverheadEx(getMOR(), vmConfigInfo);
	}

	public Task rebootHost(boolean force) throws InvalidStateFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().rebootHostTask(getMOR(), force);
		return new Task(getServerConnection(), mor);
	}

	public Task reconfigureHostForDAS() throws DasConfigFaultFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().reconfigureHostForDASTask(getMOR());
		return new Task(getServerConnection(), mor);
	}

	// SDK4.1 signature for back compatibility
	public Task reconnectHostTask(HostConnectSpec hcs) throws HostConnectFaultFaultMsg, InvalidLoginFaultMsg,
			InvalidNameFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg {
		return reconnectHostTask(hcs, null);
	}

	// SDK5.0 signature
	public Task reconnectHostTask(HostConnectSpec cnxSpec, HostSystemReconnectSpec reconnectSpec)
			throws HostConnectFaultFaultMsg, InvalidLoginFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg,
			RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().reconnectHostTask(getMOR(), cnxSpec, reconnectSpec);
		return new Task(getServerConnection(), mor);
	}

	/** @since SDK4.1 */
	public long retrieveHardwareUptime() throws RuntimeFaultFaultMsg {
		return getVimService().retrieveHardwareUptime(getMOR());
	}

	public Task shutdownHostTask(boolean force) throws InvalidStateFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().shutdownHostTask(getMOR(), force);
		return new Task(getServerConnection(), mor);
	}

	public void updateFlags(HostFlagInfo hfi) throws RuntimeFaultFaultMsg {
		getVimService().updateFlags(getMOR(), hfi);
	}

	public void updateSystemResources(HostSystemResourceInfo resourceInfo) throws RuntimeFaultFaultMsg {
		getVimService().updateSystemResources(getMOR(), resourceInfo);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidIpmiMacAddressFaultMsg
	 * @throws InvalidIpmiLoginInfoFaultMsg
	 * @since 4.0
	 */
	public void updateIpmi(HostIpmiInfo ipmiInfo)
			throws InvalidIpmiLoginInfoFaultMsg, InvalidIpmiMacAddressFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateIpmi(getMOR(), ipmiInfo);
	}

	private HostConfigManager getConfigManager() {
		if (configManager == null) {
			configManager = (HostConfigManager) getCurrentProperty("configManager");
		}
		return configManager;
	}

	public OptionManager getOptionManager() {
		return new OptionManager(getServerConnection(), getConfigManager().getAdvancedOption());
	}

	public HostAutoStartManager getHostAutoStartManager() {
		return new HostAutoStartManager(getServerConnection(), getConfigManager().getAutoStartManager());
	}

	public HostBootDeviceSystem getHostBootDeviceSystem() {
		return (HostBootDeviceSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getBootDeviceSystem());
	}

	public HostDateTimeSystem getHostDateTimeSystem() {
		return (HostDateTimeSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getDateTimeSystem());
	}

	public HostDiagnosticSystem getHostDiagnosticSystem() {
		return (HostDiagnosticSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getDiagnosticSystem());
	}

	public HostEsxAgentHostManager getHostEsxAgentHostManager() {
		return (HostEsxAgentHostManager) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getEsxAgentHostManager());
	}

	public HostCacheConfigurationManager getHostCacheConfigurationManager() {
		return (HostCacheConfigurationManager) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getCacheConfigurationManager());
	}

	public HostCpuSchedulerSystem getHostCpuSchedulerSystem() {
		return (HostCpuSchedulerSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getCpuScheduler());
	}

	public HostDatastoreSystem getHostDatastoreSystem() {
		return (HostDatastoreSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getDatastoreSystem());
	}

	public HostFirmwareSystem getHostFirmwareSystem() {
		return (HostFirmwareSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getFirmwareSystem());
	}

	/** @since SDK4.0 */
	public HostKernelModuleSystem getHostKernelModuleSystem() {
		return (HostKernelModuleSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getKernelModuleSystem());
	}

	/** @since SDK4.0 */
	public LicenseManager getLicenseManager() {
		return (LicenseManager) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getLicenseManager());
	}

	/** @since SDK4.0 */
	public HostPciPassthruSystem getHostPciPassthruSystem() {
		return (HostPciPassthruSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getPciPassthruSystem());
	}

	/** @since SDK4.0 */
	public HostVirtualNicManager getHostVirtualNicManager() {
		return (HostVirtualNicManager) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getVirtualNicManager());
	}

	public HostHealthStatusSystem getHealthStatusSystem() {
		return (HostHealthStatusSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getHealthStatusSystem());
	}

	public HostFirewallSystem getHostFirewallSystem() {
		return (HostFirewallSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getFirewallSystem());
	}

	public HostImageConfigManager getHostImageConfigManager() {
		return (HostImageConfigManager) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getImageConfigManager());
	}

	public HostMemorySystem getHostMemorySystem() {
		return (HostMemorySystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getMemoryManager());
	}

	public HostNetworkSystem getHostNetworkSystem() {
		return (HostNetworkSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getNetworkSystem());
	}

	public HostPatchManager getHostPatchManager() {
		return (HostPatchManager) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getPatchManager());
	}

	public HostServiceSystem getHostServiceSystem() {
		return (HostServiceSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getServiceSystem());
	}

	public HostSnmpSystem getHostSnmpSystem() {
		return (HostSnmpSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getSnmpSystem());
	}

	public HostStorageSystem getHostStorageSystem() {
		return (HostStorageSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getStorageSystem());
	}

	public IscsiManager getIscsiManager() {
		return (IscsiManager) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getIscsiManager());
	}

	/** @deprecated as of SDK 4.0, use getHostVirtualNicManager instead */
	public HostVMotionSystem getHostVMotionSystem() {
		return (HostVMotionSystem) MorUtil.createExactManagedObject(getServerConnection(),
				getConfigManager().getVmotionSystem());
	}
}
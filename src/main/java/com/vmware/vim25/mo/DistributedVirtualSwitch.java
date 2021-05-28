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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.vmware.vim25.AlreadyExistsFaultMsg;
import com.vmware.vim25.ConcurrentAccessFaultMsg;
import com.vmware.vim25.DVPortConfigSpec;
import com.vmware.vim25.DVPortgroupConfigSpec;
import com.vmware.vim25.DVSCapability;
import com.vmware.vim25.DVSConfigInfo;
import com.vmware.vim25.DVSConfigSpec;
import com.vmware.vim25.DVSHealthCheckConfig;
import com.vmware.vim25.DVSNetworkResourcePool;
import com.vmware.vim25.DVSNetworkResourcePoolConfigSpec;
import com.vmware.vim25.DVSRuntimeInfo;
import com.vmware.vim25.DVSSummary;
import com.vmware.vim25.DistributedVirtualPort;
import com.vmware.vim25.DistributedVirtualSwitchPortCriteria;
import com.vmware.vim25.DistributedVirtualSwitchProductSpec;
import com.vmware.vim25.DuplicateNameFaultMsg;
import com.vmware.vim25.DvsFaultFaultMsg;
import com.vmware.vim25.DvsNotAuthorizedFaultMsg;
import com.vmware.vim25.EntityBackupConfig;
import com.vmware.vim25.InvalidHostStateFaultMsg;
import com.vmware.vim25.InvalidNameFaultMsg;
import com.vmware.vim25.InvalidStateFaultMsg;
import com.vmware.vim25.LimitExceededFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NotFoundFaultMsg;
import com.vmware.vim25.ResourceInUseFaultMsg;
import com.vmware.vim25.ResourceNotAvailableFaultMsg;
import com.vmware.vim25.RollbackFailureFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TaskInProgressFaultMsg;
import com.vmware.vim25.mo.util.MorUtil;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 * @since 4.0
 */

public class DistributedVirtualSwitch extends ManagedEntity {

	public DistributedVirtualSwitch(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	public DVSCapability getCapability() {
		return (DVSCapability) getCurrentProperty("capability");
	}

	public DVSConfigInfo getConfig() {
		return (DVSConfigInfo) getCurrentProperty("config");
	}

	/** @since SDK4.1 */
	public DVSNetworkResourcePool[] getNetworkResourcePool() {
		return (DVSNetworkResourcePool[]) getCurrentProperty("networkResourcePool");
	}

	public List<DistributedVirtualPortgroup> getPortgroup() {
		List<ManagedObjectReference> pgMors = (List<ManagedObjectReference>) getCurrentProperty("portgroup");
		if (pgMors == null) {
			return new ArrayList<DistributedVirtualPortgroup>();
		}

		ArrayList<DistributedVirtualPortgroup> dvpgs = new ArrayList<DistributedVirtualPortgroup>();
		for (int i = 0; i < pgMors.size(); i++) {
			dvpgs.add(new DistributedVirtualPortgroup(getServerConnection(), pgMors.get(i)));
		}
		return dvpgs;
	}

	public DVSSummary getSummary() {
		return (DVSSummary) getCurrentProperty("summary");
	}

	public String getUuid() {
		return (String) getCurrentProperty("uuid");
	}

	/** @since SDK5.1 */
	public DVSRuntimeInfo getRuntime() {
		return (DVSRuntimeInfo) getCurrentProperty("runtime");
	}

	public Task addDVPortgroupTask(List<DVPortgroupConfigSpec> spec)
			throws DuplicateNameFaultMsg, DvsFaultFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().addDVPortgroupTask(getMOR(), spec);
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidNameFaultMsg
	 * @throws DvsFaultFaultMsg
	 * @throws DuplicateNameFaultMsg
	 * @since SDK5.1
	 */
	public Task createDVPortgroupTask(DVPortgroupConfigSpec spec)
			throws DuplicateNameFaultMsg, DvsFaultFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().createDVPortgroupTask(getMOR(), spec);
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws RollbackFailureFaultMsg
	 * @throws DvsFaultFaultMsg
	 * @since SDK5.1
	 */
	public Task dVSRollbackTask(EntityBackupConfig spec)
			throws DvsFaultFaultMsg, RollbackFailureFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().dvsRollbackTask(getMOR(), spec);
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @since SDK5.1
	 */
	public DistributedVirtualPortgroup lookupDvPortGroup(String portgroupKey)
			throws NotFoundFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().lookupDvPortGroup(getMOR(), portgroupKey);
		return new DistributedVirtualPortgroup(getServerConnection(), mor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws DvsFaultFaultMsg
	 * @since SDK5.1
	 */
	public Task updateDVSHealthCheckConfigTask(List<DVSHealthCheckConfig> healthCheckConfig)
			throws DvsFaultFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().updateDVSHealthCheckConfigTask(getMOR(), healthCheckConfig);
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidNameFaultMsg
	 * @throws DvsFaultFaultMsg
	 * @since SDK5.0
	 */
	public void addNetworkResourcePool(List<DVSNetworkResourcePoolConfigSpec> configSpec)
			throws DvsFaultFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		getVimService().addNetworkResourcePool(getMOR(), configSpec);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @since SDK4.1
	 */
	public void enableNetworkResourceManagement(boolean enable) throws DvsFaultFaultMsg, RuntimeFaultFaultMsg {
		getVimService().enableNetworkResourceManagement(getMOR(), enable);
	}

	public List<String> fetchDVPortKeys(DistributedVirtualSwitchPortCriteria criteria) throws RuntimeFaultFaultMsg {
		return getVimService().fetchDVPortKeys(getMOR(), criteria);
	}

	public List<DistributedVirtualPort> fetchDVPorts(DistributedVirtualSwitchPortCriteria criteria)
			throws RuntimeFaultFaultMsg {
		return getVimService().fetchDVPorts(getMOR(), criteria);
	}

	public Task mergeDvsTask(DistributedVirtualSwitch dvs) throws DvsFaultFaultMsg, InvalidHostStateFaultMsg,
			NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().mergeDvsTask(getMOR(), dvs.getMOR());
		return new Task(getServerConnection(), taskMor);
	}

	public Task moveDVPortTask(List<String> portKey, String destinationPortgroupKey)
			throws ConcurrentAccessFaultMsg, DvsFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().moveDVPortTask(getMOR(), portKey, destinationPortgroupKey);
		return new Task(getServerConnection(), taskMor);
	}

	public Task performDvsProductSpecOperationTask(String operation, DistributedVirtualSwitchProductSpec productSpec)
			throws DvsFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference taskMor = getVimService().performDvsProductSpecOperationTask(getMOR(), operation,
				productSpec);
		return new Task(getServerConnection(), taskMor);
	}

	public List<Integer> queryUsedVlanIdInDvs() throws RuntimeFaultFaultMsg {
		return getVimService().queryUsedVlanIdInDvs(getMOR());
	}

	public Task reconfigureDvsTask(DVSConfigSpec spec)
			throws AlreadyExistsFaultMsg, ConcurrentAccessFaultMsg, DuplicateNameFaultMsg, DvsFaultFaultMsg,
			DvsNotAuthorizedFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg, LimitExceededFaultMsg,
			NotFoundFaultMsg, ResourceInUseFaultMsg, ResourceNotAvailableFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().reconfigureDvsTask(getMOR(), spec);
		return new Task(getServerConnection(), taskMor);
	}

	public Task rectifyDvsHostTask(List<ManagedObject> hosts)
			throws DvsFaultFaultMsg, RemoteException, DvsFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		List<ManagedObjectReference> mors = MorUtil.createMORs(hosts);
		ManagedObjectReference mor = getVimService().rectifyDvsHostTask(getMOR(), mors);
		return new Task(getServerConnection(), mor);
	}

	public void refreshDVPortState(List<String> portKeys)
			throws DvsFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().refreshDVPortState(getMOR(), portKeys);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws ResourceInUseFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws InvalidNameFaultMsg
	 * @throws DvsFaultFaultMsg
	 * @since SDK5.0
	 */
	public void removeNetworkResourcePool(List<String> key) throws DvsFaultFaultMsg, InvalidNameFaultMsg,
			NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg {
		getVimService().removeNetworkResourcePool(getMOR(), key);
	}

	public void updateDvsCapability(DVSCapability capability) throws DvsFaultFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateDvsCapability(getMOR(), capability);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws InvalidNameFaultMsg
	 * @throws DvsFaultFaultMsg
	 * @throws ConcurrentAccessFaultMsg
	 * @since SDK4.1
	 */
	public void updateNetworkResourcePool(List<DVSNetworkResourcePoolConfigSpec> configSpec)
			throws ConcurrentAccessFaultMsg, DvsFaultFaultMsg, InvalidNameFaultMsg, NotFoundFaultMsg,
			RuntimeFaultFaultMsg {
		getVimService().updateNetworkResourcePool(getMOR(), configSpec);
	}

	public Task reconfigureDVPortTask(List<DVPortConfigSpec> port) throws ConcurrentAccessFaultMsg, DvsFaultFaultMsg,
			NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().reconfigureDVPortTask(getMOR(), port);
		return new Task(getServerConnection(), mor);
	}

}

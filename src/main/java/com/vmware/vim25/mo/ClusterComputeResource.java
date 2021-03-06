/*================================================================================
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

import com.vmware.vim25.ClusterActionHistory;
import com.vmware.vim25.ClusterConfigInfo;
import com.vmware.vim25.ClusterConfigSpec;
import com.vmware.vim25.ClusterDasAdvancedRuntimeInfo;
import com.vmware.vim25.ClusterDrsFaults;
import com.vmware.vim25.ClusterDrsMigration;
import com.vmware.vim25.ClusterDrsRecommendation;
import com.vmware.vim25.ClusterEnterMaintenanceResult;
import com.vmware.vim25.ClusterHostRecommendation;
import com.vmware.vim25.ClusterRecommendation;
import com.vmware.vim25.DuplicateNameFaultMsg;
import com.vmware.vim25.HostConnectFaultFaultMsg;
import com.vmware.vim25.HostConnectSpec;
import com.vmware.vim25.InvalidLoginFaultMsg;
import com.vmware.vim25.InvalidStateFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.OptionValue;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TooManyHostsFaultMsg;
import com.vmware.vim25.mo.util.MorUtil;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class ClusterComputeResource extends ComputeResource {

	public ClusterComputeResource(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	public ClusterActionHistory[] getActionHistory() {
		return (ClusterActionHistory[]) this.getCurrentProperty("actionHistory");
	}

	/*
	 * @deprecated
	 */
	public ClusterConfigInfo getConfiguration() {
		return (ClusterConfigInfo) getCurrentProperty("configuration");
	}

	/**
	 * @since 4.0
	 */
	public ClusterDrsFaults[] getDrsFault() {
		return (ClusterDrsFaults[]) getCurrentProperty("drsFault");
	}

	public ClusterDrsRecommendation[] getDrsRecommendation() {
		return (ClusterDrsRecommendation[]) getCurrentProperty("drsRecommendation");
	}

	public ClusterDrsMigration[] getMigrationHistory() {
		return (ClusterDrsMigration[]) getCurrentProperty("migrationHistory");
	}

	public ClusterRecommendation[] getRecommendation() {
		return (ClusterRecommendation[]) getCurrentProperty("recommendation");
	}

	// SDK 2.5 signature for back compatibility
	public Task addHostTask(HostConnectSpec spec, boolean asConnected, ResourcePool resourcePool)
			throws DuplicateNameFaultMsg, HostConnectFaultFaultMsg, InvalidLoginFaultMsg, RuntimeFaultFaultMsg {
		return addHostTask(spec, asConnected, resourcePool, null);
	}

	// new SDK 4.0 signature
	public Task addHostTask(HostConnectSpec spec, boolean asConnected, ResourcePool resourcePool, String license)
			throws DuplicateNameFaultMsg, HostConnectFaultFaultMsg, InvalidLoginFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMOR = getVimService().addHostTask(getMOR(), spec, asConnected,
				resourcePool == null ? null : resourcePool.getMOR(), license);
		return new Task(getServerConnection(), taskMOR);
	}

	public void applyRecommendation(String key) throws RuntimeFaultFaultMsg {
		getVimService().applyRecommendation(getMOR(), key);
	}

	/** @since SDK4.1 */
	public void cancelRecommendation(String key) throws RuntimeFaultFaultMsg {
		getVimService().cancelRecommendation(getMOR(), key);
	}

	/**
	 * @since SDK5.0
	 */
	public ClusterEnterMaintenanceResult clusterEnterMaintenanceMode(List<ManagedObject> hosts,
			List<OptionValue> option) throws RuntimeFaultFaultMsg {
		List<ManagedObjectReference> hostMors = MorUtil.createMORs(hosts);
		return getVimService().clusterEnterMaintenanceMode(getMOR(), hostMors, option);
	}

	public Task moveHostIntoTask(HostSystem host, ResourcePool resourcePool)
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TooManyHostsFaultMsg {
		if (host == null) {
			throw new IllegalArgumentException("host must not be null.");
		}
		ManagedObjectReference taskMOR = getVimService().moveHostIntoTask(getMOR(), host.getMOR(),
				resourcePool == null ? null : resourcePool.getMOR());
		return new Task(getServerConnection(), taskMOR);
	}

	public Task moveIntoTask(List<ManagedObject> hosts)
			throws DuplicateNameFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TooManyHostsFaultMsg {
		if (hosts == null) {
			throw new IllegalArgumentException("hosts must not be null.");
		}
		ManagedObjectReference taskMOR = getVimService().moveIntoTask(getMOR(), MorUtil.createMORs(hosts));
		return new Task(getServerConnection(), taskMOR);
	}

	public List<ClusterHostRecommendation> recommendHostsForVm(VirtualMachine vm, ResourcePool pool)
			throws RuntimeFaultFaultMsg {
		if (vm == null) {
			throw new IllegalArgumentException("vm must not be null.");
		}
		return getVimService().recommendHostsForVm(getMOR(), vm.getMOR(), pool == null ? null : pool.getMOR());
	}

	public Task reconfigureClusterTask(ClusterConfigSpec spec, boolean modify) throws RuntimeFaultFaultMsg {
		ManagedObjectReference taskMOR = getVimService().reconfigureClusterTask(getMOR(), spec, modify);
		return new Task(getServerConnection(), taskMOR);
	}

	public void refreshRecommendation() throws RuntimeFaultFaultMsg {
		getVimService().refreshRecommendation(getMOR());
	}

	/**
	 * @since 4.0
	 */
	public ClusterDasAdvancedRuntimeInfo retrieveDasAdvancedRuntimeInfo() throws RuntimeFaultFaultMsg {
		return getVimService().retrieveDasAdvancedRuntimeInfo(getMOR());
	}
}

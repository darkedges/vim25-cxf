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

import java.util.ArrayList;
import java.util.List;

import com.vmware.vim25.BackupBlobWriteFailureFaultMsg;
import com.vmware.vim25.DVSFeatureCapability;
import com.vmware.vim25.DVSManagerDvsConfigTarget;
import com.vmware.vim25.DistributedVirtualSwitchHostProductSpec;
import com.vmware.vim25.DistributedVirtualSwitchManagerCompatibilityResult;
import com.vmware.vim25.DistributedVirtualSwitchManagerDvsProductSpec;
import com.vmware.vim25.DistributedVirtualSwitchManagerHostContainer;
import com.vmware.vim25.DistributedVirtualSwitchManagerHostDvsFilterSpec;
import com.vmware.vim25.DistributedVirtualSwitchProductSpec;
import com.vmware.vim25.DvsFaultFaultMsg;
import com.vmware.vim25.EntityBackupConfig;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NotFoundFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.SelectionSet;
import com.vmware.vim25.mo.util.MorUtil;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @since 4.0
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class DistributedVirtualSwitchManager extends ManagedObject {
	public DistributedVirtualSwitchManager(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	public List<DistributedVirtualSwitchProductSpec> queryAvailableDvsSpec(Boolean recommended)
			throws RuntimeFaultFaultMsg {
		return getVimService().queryAvailableDvsSpec(getMOR(), recommended);
	}

	public List<HostSystem> queryCompatibleHostForExistingDvs(ManagedEntity container, boolean recursive,
			DistributedVirtualSwitch dvs) throws RuntimeFaultFaultMsg {
		List<ManagedObjectReference> mors = getVimService().queryCompatibleHostForExistingDvs(getMOR(),
				container.getMOR(), recursive, dvs.getMOR());

		List<HostSystem> hosts = new ArrayList<HostSystem>();
		for (int i = 0; i < hosts.size(); i++) {
			hosts.add(new HostSystem(getServerConnection(), mors.get(i)));
		}
		return hosts;
	}

	public List<HostSystem> queryCompatibleHostForNewDvs(ManagedEntity container, boolean recursive,
			DistributedVirtualSwitchProductSpec switchProductSpec) throws RuntimeFaultFaultMsg {
		List<ManagedObjectReference> mors = getVimService().queryCompatibleHostForNewDvs(getMOR(), container.getMOR(),
				recursive, switchProductSpec);

		List<HostSystem> hosts = new ArrayList<HostSystem>();
		for (int i = 0; i < hosts.size(); i++) {
			hosts.add(new HostSystem(getServerConnection(), mors.get(i)));
		}
		return hosts;
	}

	public List<DistributedVirtualSwitchHostProductSpec> queryDvsCompatibleHostSpec(
			DistributedVirtualSwitchProductSpec switchProductSpec) throws RuntimeFaultFaultMsg {
		return getVimService().queryDvsCompatibleHostSpec(getMOR(), switchProductSpec);
	}

	/** @since SDK4.1 */
	public List<DistributedVirtualSwitchManagerCompatibilityResult> queryDvsCheckCompatibility(
			DistributedVirtualSwitchManagerHostContainer hostContainer,
			DistributedVirtualSwitchManagerDvsProductSpec dvsProductSpec,
			List<DistributedVirtualSwitchManagerHostDvsFilterSpec> hostFilterSpec) throws RuntimeFaultFaultMsg {
		return getVimService().queryDvsCheckCompatibility(getMOR(), hostContainer, dvsProductSpec, hostFilterSpec);
	}

	public DVSManagerDvsConfigTarget queryDvsConfigTarget(HostSystem host, DistributedVirtualSwitch dvs)
			throws RuntimeFaultFaultMsg {
		return getVimService().queryDvsConfigTarget(getMOR(), host == null ? null : host.getMOR(),
				dvs == null ? null : dvs.getMOR());
	}

	public DistributedVirtualSwitch queryDvsByUuid(String uuid) throws NotFoundFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().queryDvsByUuid(getMOR(), uuid);
		return new DistributedVirtualSwitch(getServerConnection(), mor);
	}

	/** @since SDK4.1 */
	public DVSFeatureCapability queryDvsFeatureCapability(DistributedVirtualSwitchProductSpec switchProductSpec)
			throws RuntimeFaultFaultMsg {
		return getVimService().queryDvsFeatureCapability(getMOR(), switchProductSpec);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws DvsFaultFaultMsg
	 * @since SDK5.0
	 */
	public Task rectifyDvsOnHostTask(List<ManagedObject> hosts) throws DvsFaultFaultMsg, RuntimeFaultFaultMsg {
		List<ManagedObjectReference> hostMors = MorUtil.createMORs(hosts);
		ManagedObjectReference taskMor = getVimService().rectifyDvsOnHostTask(getMOR(), hostMors);
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws BackupBlobWriteFailureFaultMsg
	 * @since SDK5.1
	 */
	public Task dVSManagerExportEntityTask(List<SelectionSet> selectionSet)
			throws BackupBlobWriteFailureFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().dvsManagerExportEntityTask(getMOR(), selectionSet);
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws DvsFaultFaultMsg
	 * @since SDK5.1
	 */
	public Task dVSManagerImportEntityTask(List<EntityBackupConfig> entityBackup, String importType)
			throws DvsFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().dvsManagerImportEntityTask(getMOR(), entityBackup, importType);
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @since SDK5.1
	 */
	public DistributedVirtualPortgroup dVSManagerLookupDvPortGroup(String switchUuid, String portgroupKey)
			throws NotFoundFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().dvsManagerLookupDvPortGroup(getMOR(), switchUuid, portgroupKey);
		return new DistributedVirtualPortgroup(getServerConnection(), mor);
	}
}
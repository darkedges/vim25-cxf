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

import java.util.ArrayList;
import java.util.List;

import com.vmware.vim25.AlreadyExistsFaultMsg;
import com.vmware.vim25.ClusterConfigSpec;
import com.vmware.vim25.ClusterConfigSpecEx;
import com.vmware.vim25.ComputeResourceConfigSpec;
import com.vmware.vim25.ConcurrentAccessFaultMsg;
import com.vmware.vim25.DVSCreateSpec;
import com.vmware.vim25.DuplicateNameFaultMsg;
import com.vmware.vim25.DvsFaultFaultMsg;
import com.vmware.vim25.DvsNotAuthorizedFaultMsg;
import com.vmware.vim25.FileFaultFaultMsg;
import com.vmware.vim25.HostConnectFaultFaultMsg;
import com.vmware.vim25.HostConnectSpec;
import com.vmware.vim25.InsufficientResourcesFaultFaultMsg;
import com.vmware.vim25.InvalidDatastoreFaultMsg;
import com.vmware.vim25.InvalidFolderFaultMsg;
import com.vmware.vim25.InvalidLoginFaultMsg;
import com.vmware.vim25.InvalidNameFaultMsg;
import com.vmware.vim25.InvalidStateFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NotFoundFaultMsg;
import com.vmware.vim25.OutOfBoundsFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VmConfigFaultFaultMsg;
import com.vmware.vim25.mo.util.MorUtil;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class Folder extends ManagedEntity {
	public Folder(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	// the array could have different real types, therefore cannot use
	// getManagedObjects()
	public List<ManagedEntity> getChildEntity() {
		List<ManagedObjectReference> mors = (List<ManagedObjectReference>) getCurrentProperty("childEntity");
		;

		if (mors == null) {
			return new ArrayList<ManagedEntity>();
		}

		List<ManagedEntity> mes = new ArrayList<ManagedEntity>();
		for (int i = 0; i < mors.size(); i++) {
			mes.add(MorUtil.createExactManagedEntity(getServerConnection(), mors.get(i)));
		}
		return mes;
	}

	public String[] getChildType() {
		return (String[]) getCurrentProperty("childType");
	}

	// SDK2.5 signature for back compatibility
	public Task addStandaloneHostTask(HostConnectSpec spec, ComputeResourceConfigSpec compResSpec, boolean addConnected)
			throws DuplicateNameFaultMsg, HostConnectFaultFaultMsg, InvalidLoginFaultMsg, RuntimeFaultFaultMsg {
		return addStandaloneHostTask(spec, compResSpec, addConnected, null);
	}

	// new 4.0 signature
	public Task addStandaloneHostTask(HostConnectSpec spec, ComputeResourceConfigSpec compResSpec, boolean addConnected,
			String license)
			throws DuplicateNameFaultMsg, HostConnectFaultFaultMsg, InvalidLoginFaultMsg, RuntimeFaultFaultMsg {
		return new Task(getServerConnection(),
				getVimService().addStandaloneHostTask(getMOR(), spec, compResSpec, addConnected, license));
	}

	public ClusterComputeResource createCluster(String name, ClusterConfigSpec spec) throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		return new ClusterComputeResource(getServerConnection(), getVimService().createCluster(getMOR(), name, spec));
	}

	public ClusterComputeResource createClusterEx(String name, ClusterConfigSpecEx spec) throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		return new ClusterComputeResource(getServerConnection(), getVimService().createClusterEx(getMOR(), name, spec));
	}

	public Datacenter createDatacenter(String name) throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		return new Datacenter(getServerConnection(), getVimService().createDatacenter(getMOR(), name));
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws InvalidNameFaultMsg
	 * @throws DvsNotAuthorizedFaultMsg
	 * @throws DvsFaultFaultMsg
	 * @throws DuplicateNameFaultMsg
	 * @since 4.0
	 */
	public Task createDVSTask(DVSCreateSpec spec) throws DuplicateNameFaultMsg, DvsFaultFaultMsg, DvsNotAuthorizedFaultMsg, InvalidNameFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg  {
		ManagedObjectReference taskMor = getVimService().createDVSTask(getMOR(), spec);
		return new Task(getServerConnection(), taskMor);
	}

	public Folder createFolder(String name) throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		return new Folder(getServerConnection(), getVimService().createFolder(getMOR(), name));
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidNameFaultMsg
	 * @throws DuplicateNameFaultMsg
	 * @since SDK5.0
	 */
	public StoragePod createStoragePod(String name) throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().createStoragePod(getMOR(), name);
		return new StoragePod(getServerConnection(), mor);
	}

	public Task createVMTask(VirtualMachineConfigSpec config, ResourcePool pool, HostSystem host)
			throws AlreadyExistsFaultMsg, DuplicateNameFaultMsg, FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg,
			InvalidDatastoreFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg, OutOfBoundsFaultMsg,
			RuntimeFaultFaultMsg, VmConfigFaultFaultMsg {
		return new Task(getServerConnection(),
				getVimService().createVMTask(getMOR(), config, pool.getMOR(), host == null ? null : host.getMOR()));
	}

	public Task moveIntoFolderTask(List<ManagedObject> entities)
			throws DuplicateNameFaultMsg, InvalidFolderFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg {
		if (entities == null) {
			throw new IllegalArgumentException("entities must not be null");
		}

		return new Task(getServerConnection(),
				getVimService().moveIntoFolderTask(getMOR(), MorUtil.createMORs(entities)));
	}

	public Task registerVMTask(String path, String name, boolean asTemplate, ResourcePool pool, HostSystem host)
			throws AlreadyExistsFaultMsg, DuplicateNameFaultMsg, FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg,
			InvalidDatastoreFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, OutOfBoundsFaultMsg,
			RuntimeFaultFaultMsg, VmConfigFaultFaultMsg {
		return new Task(getServerConnection(), getVimService().registerVMTask(getMOR(), path, name, asTemplate,
				pool == null ? null : pool.getMOR(), host == null ? null : host.getMOR()));
	}

	public Task unregisterAndDestroyTask() throws ConcurrentAccessFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg {
		return new Task(getServerConnection(), getVimService().unregisterAndDestroyTask(getMOR()));
	}
}

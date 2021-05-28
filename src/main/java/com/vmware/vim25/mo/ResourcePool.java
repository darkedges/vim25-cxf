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

import com.vmware.vim25.AlreadyExistsFaultMsg;
import com.vmware.vim25.ConcurrentAccessFaultMsg;
import com.vmware.vim25.DuplicateNameFaultMsg;
import com.vmware.vim25.FileFaultFaultMsg;
import com.vmware.vim25.ImportSpec;
import com.vmware.vim25.InsufficientResourcesFaultFaultMsg;
import com.vmware.vim25.InvalidDatastoreFaultMsg;
import com.vmware.vim25.InvalidNameFaultMsg;
import com.vmware.vim25.InvalidStateFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NotFoundFaultMsg;
import com.vmware.vim25.OutOfBoundsFaultMsg;
import com.vmware.vim25.ResourceConfigOption;
import com.vmware.vim25.ResourceConfigSpec;
import com.vmware.vim25.ResourcePoolRuntimeInfo;
import com.vmware.vim25.ResourcePoolSummary;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.VAppConfigSpec;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VmConfigFaultFaultMsg;
import com.vmware.vim25.mo.util.MorUtil;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class ResourcePool extends ManagedEntity {
	public ResourcePool(ServerConnection serverConnection, ManagedObjectReference mor) {
		super(serverConnection, mor);
	}

	public ResourceConfigSpec[] getChildConfiguration() {
		return (ResourceConfigSpec[]) this.getCurrentProperty("childConfiguration");
	}

	public ResourceConfigSpec getConfig() {
		return (ResourceConfigSpec) this.getCurrentProperty("config");
	}

	public ComputeResource getOwner() {
		return (ComputeResource) this.getManagedObject("owner");
	}

	public List<ResourcePool> getResourcePools() {
		return getResourcePools("resourcePool");
	}

	public ResourcePoolRuntimeInfo getRuntime() {
		return (ResourcePoolRuntimeInfo) this.getCurrentProperty("runtime");
	}

	public ResourcePoolSummary getSummary() {
		return (ResourcePoolSummary) this.getCurrentProperty("summary");
	}

	public List<VirtualMachine> getVMs() {
		return getVms("vm");
	}

	/**
	 * @throws VmConfigFaultFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws OutOfBoundsFaultMsg
	 * @throws InvalidNameFaultMsg
	 * @throws InvalidDatastoreFaultMsg
	 * @throws InsufficientResourcesFaultFaultMsg
	 * @throws FileFaultFaultMsg
	 * @since SDK4.0
	 */
	public Task createChildVMTask(VirtualMachineConfigSpec config, HostSystem host)
			throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidNameFaultMsg,
			OutOfBoundsFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().createChildVMTask(getMOR(), config,
				host == null ? null : host.getMOR());
		return new Task(getServerConnection(), taskMor);
	}

	/** @throws VmConfigFaultFaultMsg 
	 * @throws RuntimeFaultFaultMsg 
	 * @throws InvalidStateFaultMsg 
	 * @throws InvalidNameFaultMsg 
	 * @throws InsufficientResourcesFaultFaultMsg 
	 * @throws DuplicateNameFaultMsg 
	 * @since SDK4.0 */
	public VirtualApp createVApp(String name, ResourceConfigSpec resSpec, VAppConfigSpec configSpec, Folder vmFolder) throws DuplicateNameFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference vaMor = getVimService().createVApp(getMOR(), name, resSpec, configSpec,
				vmFolder == null ? null : vmFolder.getMOR());
		return new VirtualApp(getServerConnection(), vaMor);
	}

	/**
	 * @throws VmConfigFaultFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws OutOfBoundsFaultMsg
	 * @throws InvalidNameFaultMsg
	 * @throws InvalidDatastoreFaultMsg
	 * @throws InsufficientResourcesFaultFaultMsg
	 * @throws FileFaultFaultMsg
	 * @throws DuplicateNameFaultMsg
	 * @since SDK4.0
	 */
	public HttpNfcLease importVApp(ImportSpec spec, Folder folder, HostSystem host) throws DuplicateNameFaultMsg,
			FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidNameFaultMsg,
			OutOfBoundsFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference mor = getVimService().importVApp(getMOR(), spec, folder == null ? null : folder.getMOR(),
				host == null ? null : host.getMOR());
		return new HttpNfcLease(getServerConnection(), mor);
	}

	/** @since SDK4.1 */
	public void refreshRuntime() throws RuntimeFaultFaultMsg {
		getVimService().refreshRuntime(getMOR());
	}

	/**
	 * @throws VmConfigFaultFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws OutOfBoundsFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws InvalidNameFaultMsg
	 * @throws InvalidDatastoreFaultMsg
	 * @throws InsufficientResourcesFaultFaultMsg
	 * @throws FileFaultFaultMsg
	 * @throws AlreadyExistsFaultMsg
	 * @since SDK4.0
	 */
	public Task registerChildVMTask(String path, String name, HostSystem host) throws AlreadyExistsFaultMsg,
			FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidNameFaultMsg,
			NotFoundFaultMsg, OutOfBoundsFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference mor = getVimService().registerChildVMTask(getMOR(), path, name,
				host == null ? null : host.getMOR());
		return new Task(getServerConnection(), mor);
	}

	public ResourcePool createResourcePool(String name, ResourceConfigSpec spec) throws DuplicateNameFaultMsg,
			InsufficientResourcesFaultFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference rpMor = getVimService().createResourcePool(getMOR(), name, spec);
		return new ResourcePool(getServerConnection(), rpMor);
	}

	public void destroyChildren() throws RuntimeFaultFaultMsg {
		getVimService().destroyChildren(getMOR());
	}

	public void moveIntoResourcePool(List<ManagedObject> entities)
			throws DuplicateNameFaultMsg, InsufficientResourcesFaultFaultMsg, RuntimeFaultFaultMsg {
		if (entities == null) {
			throw new IllegalArgumentException("entities must not be null.");
		}
		getVimService().moveIntoResourcePool(getMOR(), MorUtil.createMORs(entities));
	}

	/** @since SDK4.1 */
	public ResourceConfigOption queryResourceConfigOption() throws RuntimeFaultFaultMsg {
		return getVimService().queryResourceConfigOption(getMOR());
	}

	public void updateChildResourceConfiguration(List<ResourceConfigSpec> spec)
			throws InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateChildResourceConfiguration(getMOR(), spec);
	}

	public void updateConfig(String name, ResourceConfigSpec spec) throws ConcurrentAccessFaultMsg,
			DuplicateNameFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateConfig(getMOR(), name, spec);
	}
}

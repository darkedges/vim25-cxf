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
import com.vmware.vim25.DatastoreNotWritableOnHostFaultMsg;
import com.vmware.vim25.DuplicateNameFaultMsg;
import com.vmware.vim25.FileNotFoundFaultMsg;
import com.vmware.vim25.HostConfigFaultFaultMsg;
import com.vmware.vim25.HostDatastoreSystemCapabilities;
import com.vmware.vim25.HostNasVolumeSpec;
import com.vmware.vim25.HostScsiDisk;
import com.vmware.vim25.HostUnresolvedVmfsResignatureSpec;
import com.vmware.vim25.HostUnresolvedVmfsVolume;
import com.vmware.vim25.InaccessibleDatastoreFaultMsg;
import com.vmware.vim25.InvalidNameFaultMsg;
import com.vmware.vim25.InvalidStateFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NotFoundFaultMsg;
import com.vmware.vim25.ResourceInUseFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.VmfsAmbiguousMountFaultMsg;
import com.vmware.vim25.VmfsDatastoreCreateSpec;
import com.vmware.vim25.VmfsDatastoreExpandSpec;
import com.vmware.vim25.VmfsDatastoreExtendSpec;
import com.vmware.vim25.VmfsDatastoreOption;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class HostDatastoreSystem extends ManagedObject {

	public HostDatastoreSystem(ServerConnection serverConnection, ManagedObjectReference mor) {
		super(serverConnection, mor);
	}

	public HostDatastoreSystemCapabilities getCapabilities() {
		return (HostDatastoreSystemCapabilities) getCurrentProperty("capabilities");
	}

	public List<Datastore> getDatastores() {
		return getDatastores("datastore");
	}

	public void configureDatastorePrincipal(String userName, String password)
			throws HostConfigFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg {
		getVimService().configureDatastorePrincipal(getMOR(), userName, password);
	}

	public Datastore createLocalDatastore(String name, String path) throws DuplicateNameFaultMsg, FileNotFoundFaultMsg,
			HostConfigFaultFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().createLocalDatastore(getMOR(), name, path);
		return new Datastore(getServerConnection(), mor);
	}

	public Datastore createNasDatastore(HostNasVolumeSpec spec)
			throws AlreadyExistsFaultMsg, DuplicateNameFaultMsg, HostConfigFaultFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().createNasDatastore(getMOR(), spec);
		return new Datastore(getServerConnection(), mor);
	}

	public Datastore createVmfsDatastore(VmfsDatastoreCreateSpec spec)
			throws DuplicateNameFaultMsg, HostConfigFaultFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().createVmfsDatastore(getMOR(), spec);
		return new Datastore(getServerConnection(), mor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since 4.0
	 */
	public Datastore expandVmfsDatastore(Datastore datastore, VmfsDatastoreExpandSpec spec)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().expandVmfsDatastore(getMOR(), datastore.getMOR(), spec);
		return new Datastore(getServerConnection(), mor);
	}

	public Datastore extendVmfsDatastore(Datastore datastore, VmfsDatastoreExtendSpec spec)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		if (datastore == null) {
			throw new IllegalArgumentException("datastore must not be null.");
		}
		ManagedObjectReference mor = getVimService().extendVmfsDatastore(getMOR(), datastore.getMOR(), spec);
		return new Datastore(getServerConnection(), mor);
	}

	public List<HostScsiDisk> queryAvailableDisksForVmfs(Datastore datastore)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().queryAvailableDisksForVmfs(getMOR(), datastore == null ? null : datastore.getMOR());
	}

	// SDK4.1 signature for back compatibility
	public List<VmfsDatastoreOption> queryVmfsDatastoreCreateOptions(String devicePath, Integer vmfsMajorVersion)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().queryVmfsDatastoreCreateOptions(getMOR(), devicePath, vmfsMajorVersion);
	}

	// SDK5.0 signature
	public List<VmfsDatastoreOption> queryVmfsDatastoreCreateOptions(String devicePath, int vmfsMajorVersion)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().queryVmfsDatastoreCreateOptions(getMOR(), devicePath, vmfsMajorVersion);
	}

	// SDK2.5 signature for back compatibility
	public List<VmfsDatastoreOption> queryVmfsDatastoreExtendOptions(Datastore datastore, String devicePath)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		return queryVmfsDatastoreExtendOptions(datastore, devicePath, null);
	}

	// SDK4.0 signature
	public List<VmfsDatastoreOption> queryVmfsDatastoreExtendOptions(Datastore datastore, String devicePath,
			Boolean suppressExpandCandidates) throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		if (datastore == null) {
			throw new IllegalArgumentException("datastore must not be null.");
		}
		return getVimService().queryVmfsDatastoreExtendOptions(getMOR(), datastore.getMOR(), devicePath,
				suppressExpandCandidates);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since 4.0
	 */
	public List<VmfsDatastoreOption> queryVmfsDatastoreExpandOptions(Datastore datastore)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().queryVmfsDatastoreExpandOptions(getMOR(), datastore.getMOR());
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @since 4.0
	 */
	public List<HostUnresolvedVmfsVolume> queryUnresolvedVmfsVolumes() throws RuntimeFaultFaultMsg {
		return getVimService().queryUnresolvedVmfsVolumes(getMOR());
	}

	public void removeDatastore(Datastore datastore)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg {
		if (datastore == null) {
			throw new IllegalArgumentException("datastore must not be null.");
		}
		getVimService().removeDatastore(getMOR(), datastore.getMOR());
	}

	/**
	 * @throws VmfsAmbiguousMountFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since 4.0
	 */
	public Task resignatureUnresolvedVmfsVolumeTask(HostUnresolvedVmfsResignatureSpec resolutionSpec)
			throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg, VmfsAmbiguousMountFaultMsg {
		ManagedObjectReference taskMor = getVimService().resignatureUnresolvedVmfsVolumeTask(getMOR(), resolutionSpec);
		return new Task(getServerConnection(), taskMor);
	}

	public void updateLocalSwapDatastore(Datastore datastore)
			throws DatastoreNotWritableOnHostFaultMsg, InaccessibleDatastoreFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateLocalSwapDatastore(getMOR(), datastore == null ? null : datastore.getMOR());
	}

}

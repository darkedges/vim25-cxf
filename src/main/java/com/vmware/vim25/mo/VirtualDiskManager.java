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

import com.vmware.vim25.FileFaultFaultMsg;
import com.vmware.vim25.HostDiskDimensionsChs;
import com.vmware.vim25.InvalidDatastoreFaultMsg;
import com.vmware.vim25.InvalidDiskFormatFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.VirtualDiskSpec;
import com.vmware.vim25.VirtualMachineProfileSpec;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class VirtualDiskManager extends ManagedObject {

	public VirtualDiskManager(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	public Task copyVirtualDiskTask(String sourceName, Datacenter sourceDatacenter, String destName,
			Datacenter destDatacenter, VirtualDiskSpec destSpec, Boolean force)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidDiskFormatFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().copyVirtualDiskTask(getMOR(), sourceName,
				sourceDatacenter == null ? null : sourceDatacenter.getMOR(), destName,
				destDatacenter == null ? null : destDatacenter.getMOR(), destSpec, force);
		return new Task(getServerConnection(), taskMor);
	}

	public Task createVirtualDiskTask(String name, Datacenter datacenter, VirtualDiskSpec spec)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg {
		return new Task(getServerConnection(), getVimService().createVirtualDiskTask(getMOR(), name,
				datacenter == null ? null : datacenter.getMOR(), spec));
	}

	public Task defragmentVirtualDiskTask(String name, Datacenter datacenter)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg {
		return new Task(getServerConnection(), getVimService().defragmentVirtualDiskTask(getMOR(), name,
				datacenter == null ? null : datacenter.getMOR()));
	}

	public Task deleteVirtualDiskTask(String name, Datacenter datacenter)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg {
		return new Task(getServerConnection(),
				getVimService().deleteVirtualDiskTask(getMOR(), name, datacenter == null ? null : datacenter.getMOR()));
	}

	// SDK2.5 signature for back compatibility
	public Task extendVirtualDiskTask(String name, Datacenter datacenter, long newCapacityKb)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg {
		return extendVirtualDiskTask(name, datacenter, newCapacityKb, null);
	}

	// SDK4.0 signature
	public Task extendVirtualDiskTask(String name, Datacenter datacenter, long newCapacityKb, Boolean eagerZero)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg {
		return new Task(getServerConnection(), getVimService().extendVirtualDiskTask(getMOR(), name,
				datacenter == null ? null : datacenter.getMOR(), newCapacityKb, eagerZero));
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidDatastoreFaultMsg
	 * @throws FileFaultFaultMsg
	 * @since SDK4.0
	 */
	public Task eagerZeroVirtualDiskTask(String name, Datacenter datacenter)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg {
		return new Task(getServerConnection(), getVimService().eagerZeroVirtualDiskTask(getMOR(), name,
				datacenter == null ? null : datacenter.getMOR()));
	}

	public Task inflateVirtualDiskTask(String name, Datacenter datacenter)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg {
		return new Task(getServerConnection(), getVimService().inflateVirtualDiskTask(getMOR(), name,
				datacenter == null ? null : datacenter.getMOR()));
	}

	public Task moveVirtualDiskTask(String sourceName, Datacenter sourceDatacenter, String destName,
			Datacenter destDatacenter, Boolean force, List<VirtualMachineProfileSpec> virtualMachineProfileSpec)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().moveVirtualDiskTask(getMOR(), sourceName,
				sourceDatacenter == null ? null : sourceDatacenter.getMOR(), destName,
				destDatacenter == null ? null : destDatacenter.getMOR(), force, virtualMachineProfileSpec);

		return new Task(getServerConnection(), taskMor);
	}

	public int queryVirtualDiskFragmentation(String name, Datacenter datacenter)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().queryVirtualDiskFragmentation(getMOR(), name,
				datacenter == null ? null : datacenter.getMOR());
	}

	public HostDiskDimensionsChs queryVirtualDiskGeometry(String name, Datacenter datacenter)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().queryVirtualDiskGeometry(getMOR(), name,
				datacenter == null ? null : datacenter.getMOR());
	}

	public String queryVirtualDiskUuid(String name, Datacenter datacenter)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().queryVirtualDiskUuid(getMOR(), name, datacenter == null ? null : datacenter.getMOR());
	}

	public void setVirtualDiskUuid(String name, Datacenter datacenter, String uuid)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg {
		getVimService().setVirtualDiskUuid(getMOR(), name, datacenter == null ? null : datacenter.getMOR(), uuid);
	}

	public Task shrinkVirtualDiskTask(String name, Datacenter datacenter, boolean copy)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg {
		return new Task(getServerConnection(), getVimService().shrinkVirtualDiskTask(getMOR(), name,
				datacenter == null ? null : datacenter.getMOR(), copy));
	}

	public Task zeroFillVirtualDiskTask(String name, Datacenter datacenter)
			throws FileFaultFaultMsg, InvalidDatastoreFaultMsg, RuntimeFaultFaultMsg {
		return new Task(getServerConnection(), getVimService().zeroFillVirtualDiskTask(getMOR(), name,
				datacenter == null ? null : datacenter.getMOR()));
	}

}

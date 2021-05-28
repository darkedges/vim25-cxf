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

import com.vmware.vim25.FileFaultFaultMsg;
import com.vmware.vim25.InsufficientResourcesFaultFaultMsg;
import com.vmware.vim25.InvalidNameFaultMsg;
import com.vmware.vim25.InvalidStateFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TaskInProgressFaultMsg;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VmConfigFaultFaultMsg;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class VirtualMachineSnapshot extends ManagedObject {

	public VirtualMachineSnapshot(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	/** @since SDK4.1 */
	public List<VirtualMachineSnapshot> getChildSnapshot() {
		List<ManagedObjectReference> mors = (List<ManagedObjectReference>) getCurrentProperty("childSnapshot");
		List<VirtualMachineSnapshot> vmns = new ArrayList<VirtualMachineSnapshot>();
		for (int i = 0; i < mors.size(); i++) {
			vmns.add(new VirtualMachineSnapshot(getServerConnection(), mors.get(i)	));
		}
		return vmns;
	}

	public VirtualMachineConfigInfo getConfig() {
		return (VirtualMachineConfigInfo) getCurrentProperty("config");
	}

	// SDK4.1 signature for back compatibility
	public Task removeSnapshotTask(boolean removeChildren) throws RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		return removeSnapshotTask(removeChildren, null);
	}

	// SDK5.0 signature
	public Task removeSnapshotTask(boolean removeChildren, Boolean consolidate)
			throws RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		return new Task(getServerConnection(),
				getVimService().removeSnapshotTask(getMOR(), removeChildren, consolidate));
	}

	public void renameSnapshot(String name, String description)
			throws InvalidNameFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		getVimService().renameSnapshot(getMOR(), name, description);
	}

	// SDK2.5 signature for back compatibility
	public Task revertToSnapshotTask(HostSystem host) throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg,
			InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		return revertToSnapshotTask(host, null);
	}

	// SDK4.0 signature
	public Task revertToSnapshotTask(HostSystem host, Boolean suppressPowerOn)
			throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg,
			TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		return new Task(getServerConnection(),
				getVimService().revertToSnapshotTask(getMOR(), host == null ? null : host.getMOR(), suppressPowerOn));
	}

}

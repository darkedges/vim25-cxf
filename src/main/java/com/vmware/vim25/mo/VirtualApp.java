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

import java.rmi.RemoteException;
import java.util.List;

import com.vmware.vim25.ConcurrentAccessFaultMsg;
import com.vmware.vim25.DuplicateNameFaultMsg;
import com.vmware.vim25.FileFaultFaultMsg;
import com.vmware.vim25.InsufficientResourcesFaultFaultMsg;
import com.vmware.vim25.InvalidDatastoreFaultMsg;
import com.vmware.vim25.InvalidNameFaultMsg;
import com.vmware.vim25.InvalidPowerStateFaultMsg;
import com.vmware.vim25.InvalidStateFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.MigrationFaultFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TaskInProgressFaultMsg;
import com.vmware.vim25.VAppCloneSpec;
import com.vmware.vim25.VAppConfigFaultFaultMsg;
import com.vmware.vim25.VAppConfigInfo;
import com.vmware.vim25.VAppConfigSpec;
import com.vmware.vim25.VirtualAppLinkInfo;
import com.vmware.vim25.VirtualAppSummary;
import com.vmware.vim25.VmConfigFaultFaultMsg;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */
public class VirtualApp extends ResourcePool {
	public VirtualApp(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	/** @since SDK4.1 */
	public VirtualAppLinkInfo[] getChildLink() {
		return (VirtualAppLinkInfo[]) getCurrentProperty("childLink");
	}

	public List<Datastore> getDatastore() {
		return getDatastores("datastore");
	}

	public List<Network> getNetwork() {
		return getNetworks("network");
	}

	@Override
	public VirtualAppSummary getSummary() {
		return (VirtualAppSummary) this.getCurrentProperty("summary");
	}

	public Folder getParentFolder() {
		ManagedObjectReference mor = (ManagedObjectReference) getCurrentProperty("parentFolder");
		return new Folder(getServerConnection(), mor);
	}

	/** @since SDK4.1 */
	public ManagedEntity getParentVApp() {
		ManagedObjectReference mor = (ManagedObjectReference) getCurrentProperty("parentVApp");
		return new ManagedEntity(getServerConnection(), mor);
	}

	public VAppConfigInfo getVAppConfig() {
		return (VAppConfigInfo) getCurrentProperty("vAppConfig");
	}

	public Task cloneVAppTask(String name, ManagedObjectReference target, VAppCloneSpec spec) throws FileFaultFaultMsg,
			InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidStateFaultMsg, MigrationFaultFaultMsg,
			RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().cloneVAppTask(getMOR(), name, target, spec);
		return new Task(getServerConnection(), taskMor);
	}

	public HttpNfcLease exportVApp() throws FileFaultFaultMsg, InvalidPowerStateFaultMsg, InvalidStateFaultMsg,
			RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference mor = getVimService().exportVApp(getMOR());
		return new HttpNfcLease(getServerConnection(), mor);
	}

	public Task powerOffVAppTask(boolean force)
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VAppConfigFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().powerOffVAppTask(getMOR(), force);
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @throws VAppConfigFaultFaultMsg
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since SDK4.1
	 */
	public Task suspendVAppTask()
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VAppConfigFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().suspendVAppTask(getMOR());
		return new Task(getServerConnection(), taskMor);
	}

	public Task powerOnVAppTask() throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg,
			RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VAppConfigFaultFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().powerOnVAppTask(getMOR());
		return new Task(getServerConnection(), taskMor);
	}

	public Task unregisterVAppTask()
			throws RemoteException, ConcurrentAccessFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().unregisterVAppTask(getMOR());
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws ConcurrentAccessFaultMsg
	 * @since SDK4.1
	 */
	public void updateLinkedChildren(List<VirtualAppLinkInfo> addChangeSet, List<ManagedObjectReference> removeSet)
			throws ConcurrentAccessFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateLinkedChildren(getMOR(), addChangeSet, removeSet);
	}

	public void updateVAppConfig(VAppConfigSpec spec) throws ConcurrentAccessFaultMsg, DuplicateNameFaultMsg,
			FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidNameFaultMsg,
			InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		getVimService().updateVAppConfig(getMOR(), spec);
	}
}

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

import com.vmware.vim25.DatacenterConfigInfo;
import com.vmware.vim25.DatacenterConfigSpec;
import com.vmware.vim25.HostConnectFaultFaultMsg;
import com.vmware.vim25.HostConnectInfo;
import com.vmware.vim25.InvalidLoginFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.OptionValue;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.VirtualMachineConfigOptionDescriptor;
import com.vmware.vim25.mo.util.MorUtil;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class Datacenter extends ManagedEntity {

	public Datacenter(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	public Folder getVmFolder() {
		return (Folder) this.getManagedObject("vmFolder");
	}

	public Folder getHostFolder() {
		return (Folder) this.getManagedObject("hostFolder");
	}

	public List<Datastore> getDatastores() {
		return getDatastores("datastore");
	}

	/** @since SDK5.1 */
	public DatacenterConfigInfo getConfiguration() {
		return (DatacenterConfigInfo) getCurrentProperty("configuration");
	}

	/**
	 * @since 4.0
	 */
	public Folder getDatastoreFolder() {
		return (Folder) getManagedObject("datastoreFolder");
	}

	/**
	 * @since 4.0
	 */
	public Folder getNetworkFolder() {
		return (Folder) getManagedObject("networkFolder");
	}

	public List<Network> getNetworks() {
		return getNetworks("network");
	}

	/** old signature for back compatibility with 2.5 and 4.0 */
	public Task powerOnMultiVMTask(List<ManagedObject> vms) throws RuntimeFaultFaultMsg {
		return powerOnMultiVMTask(vms, null);
	}

	/** @since SDK4.1 */
	public Task powerOnMultiVMTask(List<ManagedObject> vms, List<OptionValue> option) throws RuntimeFaultFaultMsg {
		if (vms == null) {
			throw new IllegalArgumentException("vms must not be null.");
		}
		List<ManagedObjectReference> mors = MorUtil.createMORs(vms);
		ManagedObjectReference tmor = getVimService().powerOnMultiVMTask(getMOR(), mors, option);
		return new Task(getServerConnection(), tmor);
	}

	/** @since SDK5.1 */
	public Task reconfigureDatacenterTask(DatacenterConfigSpec spec, boolean modify) throws RuntimeFaultFaultMsg {
		ManagedObjectReference tmor = getVimService().reconfigureDatacenterTask(getMOR(), spec, modify);
		return new Task(getServerConnection(), tmor);
	}

	public HostConnectInfo queryConnectionInfo(String hostname, int port, String username, String password,
			String sslThumbprint) throws HostConnectFaultFaultMsg, InvalidLoginFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().queryConnectionInfo(getMOR(), hostname, port, username, password, sslThumbprint);
	}

	/** @since SDK5.1 */
	public List<VirtualMachineConfigOptionDescriptor> queryDatacenterConfigOptionDescriptor()
			throws RuntimeFaultFaultMsg {
		return getVimService().queryDatacenterConfigOptionDescriptor(getMOR());
	}
}

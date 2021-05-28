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

import com.vmware.vim25.HostPatchManagerLocator;
import com.vmware.vim25.HostPatchManagerPatchManagerOperationSpec;
import com.vmware.vim25.InvalidStateFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NoDiskSpaceFaultMsg;
import com.vmware.vim25.PatchBinariesNotFoundFaultMsg;
import com.vmware.vim25.PatchInstallFailedFaultMsg;
import com.vmware.vim25.PatchMetadataInvalidFaultMsg;
import com.vmware.vim25.PatchNotApplicableFaultMsg;
import com.vmware.vim25.PlatformConfigFaultFaultMsg;
import com.vmware.vim25.RebootRequiredFaultMsg;
import com.vmware.vim25.RequestCanceledFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.TaskInProgressFaultMsg;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class HostPatchManager extends ManagedObject {

	public HostPatchManager(ServerConnection serverConnection, ManagedObjectReference mor) {
		super(serverConnection, mor);
	}

	/**
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws RequestCanceledFaultMsg
	 * @throws PlatformConfigFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since 4.0
	 */
	public Task checkHostPatchTask(List<String> metaUrls, List<String> bundleUrls,
			HostPatchManagerPatchManagerOperationSpec spec) throws InvalidStateFaultMsg, PlatformConfigFaultFaultMsg,
			RequestCanceledFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference taskMor = getVimService().checkHostPatchTask(getMOR(), metaUrls, bundleUrls, spec);
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws RequestCanceledFaultMsg
	 * @throws PlatformConfigFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since 4.0
	 */
	public Task installHostPatchV2Task(List<String> metaUrls, List<String> bundleUrls, List<String> vibUrls,
			HostPatchManagerPatchManagerOperationSpec spec) throws InvalidStateFaultMsg, PlatformConfigFaultFaultMsg,
			RequestCanceledFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference taskMor = getVimService().installHostPatchV2Task(getMOR(), metaUrls, bundleUrls, vibUrls,
				spec);
		return new Task(getServerConnection(), taskMor);
	}

	public Task installHostPatchTask(HostPatchManagerLocator repository, String updateID, Boolean force)
			throws InvalidStateFaultMsg, NoDiskSpaceFaultMsg, PatchBinariesNotFoundFaultMsg, PatchInstallFailedFaultMsg,
			PatchMetadataInvalidFaultMsg, PatchNotApplicableFaultMsg, RebootRequiredFaultMsg, RuntimeFaultFaultMsg,
			TaskInProgressFaultMsg {
		return new Task(getServerConnection(),
				getVimService().installHostPatchTask(getMOR(), repository, updateID, force));
	}

	/**
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws RequestCanceledFaultMsg
	 * @throws PlatformConfigFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since 4.0
	 */
	public Task queryHostPatchTask(HostPatchManagerPatchManagerOperationSpec spec) throws InvalidStateFaultMsg,
			PlatformConfigFaultFaultMsg, RequestCanceledFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference taskMor = getVimService().queryHostPatchTask(getMOR(), spec);
		return new Task(getServerConnection(), taskMor);
	}

	public Task scanHostPatchTask(HostPatchManagerLocator repository, List<String> updateID)
			throws PatchMetadataInvalidFaultMsg, PlatformConfigFaultFaultMsg, RequestCanceledFaultMsg,
			RuntimeFaultFaultMsg {
		return new Task(getServerConnection(), getVimService().scanHostPatchTask(getMOR(), repository, updateID));
	}

	/**
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws RequestCanceledFaultMsg
	 * @throws PlatformConfigFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since 4.0
	 */
	public Task scanHostPatchV2Task(List<String> metaUrls, List<String> bundleUrls,
			HostPatchManagerPatchManagerOperationSpec spec) throws InvalidStateFaultMsg, PlatformConfigFaultFaultMsg,
			RequestCanceledFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference taskMor = getVimService().scanHostPatchV2Task(getMOR(), metaUrls, bundleUrls, spec);
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws RequestCanceledFaultMsg
	 * @throws PlatformConfigFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since 4.0
	 */
	public Task stageHostPatchTask(List<String> metaUrls, List<String> bundleUrls, List<String> vibUrls,
			HostPatchManagerPatchManagerOperationSpec spec) throws InvalidStateFaultMsg, PlatformConfigFaultFaultMsg,
			RequestCanceledFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference taskMor = getVimService().stageHostPatchTask(getMOR(), metaUrls, bundleUrls, vibUrls,
				spec);
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws PlatformConfigFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since 4.0
	 */
	public Task uninstallHostPatchTask(List<String> bulletinIds, HostPatchManagerPatchManagerOperationSpec spec)
			throws RemoteException, InvalidStateFaultMsg, PlatformConfigFaultFaultMsg, RuntimeFaultFaultMsg,
			TaskInProgressFaultMsg {
		ManagedObjectReference taskMor = getVimService().uninstallHostPatchTask(getMOR(), bulletinIds, spec);
		return new Task(getServerConnection(), taskMor);
	}
}

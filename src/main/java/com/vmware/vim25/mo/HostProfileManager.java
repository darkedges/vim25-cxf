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

import com.vmware.vim25.AnswerFile;
import com.vmware.vim25.AnswerFileCreateSpec;
import com.vmware.vim25.AnswerFileStatusResult;
import com.vmware.vim25.AnswerFileUpdateFailedFaultMsg;
import com.vmware.vim25.ApplyProfile;
import com.vmware.vim25.HostApplyProfile;
import com.vmware.vim25.HostConfigFailedFaultMsg;
import com.vmware.vim25.HostConfigSpec;
import com.vmware.vim25.HostProfileManagerConfigTaskList;
import com.vmware.vim25.InvalidStateFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ProfileDeferredPolicyOptionParameter;
import com.vmware.vim25.ProfileMetadata;
import com.vmware.vim25.ProfileProfileStructure;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.mo.util.MorUtil;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 * @since 4.0
 */
public class HostProfileManager extends ProfileManager {
	public HostProfileManager(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	// SDK4.1 signature for back compatibility
	public Task applyHostConfigTask(HostSystem host, HostConfigSpec configSpec)
			throws HostConfigFailedFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg {
		return applyHostConfigTask(host, configSpec, null);
	}

	// SDK5.0 signature
	public Task applyHostConfigTask(HostSystem host, HostConfigSpec configSpec,
			List<ProfileDeferredPolicyOptionParameter> userInputs)
			throws HostConfigFailedFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().applyHostConfigTask(getMOR(), host.getMOR(), configSpec,
				userInputs);
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @since SDK5.0
	 */
	public Task checkAnswerFileStatusTask(List<ManagedObject> hosts) throws RuntimeFaultFaultMsg {
		List<ManagedObjectReference> hostMors = MorUtil.createMORs(hosts);
		ManagedObjectReference taskMor = getVimService().checkAnswerFileStatusTask(getMOR(), hostMors);
		return new Task(getServerConnection(), taskMor);
	}

	// SDK4.1 signature for back compatibility
	public ApplyProfile createDefaultProfile(String profileType) throws RuntimeFaultFaultMsg {
		return createDefaultProfile(profileType, null, null);
	}

	// SDK5.0 signature
	public ApplyProfile createDefaultProfile(String profileType, String profileTypeName, Profile profile)
			throws RuntimeFaultFaultMsg {
		return getVimService().createDefaultProfile(getMOR(), profileType, profileTypeName,
				profile == null ? null : profile.getMOR());
	}

	/**
	 * @since SDK5.0
	 */
	public Task exportAnswerFileTask(HostSystem host) throws RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().exportAnswerFileTask(getMOR(), host.getMOR());
		return new Task(getServerConnection(), taskMor);
	}

	public HostProfileManagerConfigTaskList generateConfigTaskList(HostConfigSpec configSpec, HostSystem host)
			throws RuntimeFaultFaultMsg {
		return getVimService().generateConfigTaskList(getMOR(), configSpec, host.getMOR());
	}

	/**
	 * @since SDK5.0
	 */
	public List<AnswerFileStatusResult> queryAnswerFileStatus(List<ManagedObject> hosts) throws RuntimeFaultFaultMsg {
		List<ManagedObjectReference> hostMors = MorUtil.createMORs(hosts);
		return getVimService().queryAnswerFileStatus(getMOR(), hostMors);
	}

	// SDK4.1 signature for back compatibility
	public List<ProfileMetadata> queryHostProfileMetadata(List<String> profileName) throws RuntimeFaultFaultMsg {
		return getVimService().queryHostProfileMetadata(getMOR(), profileName, null);
	}

	// SDK5.0 signature
	public List<ProfileMetadata> queryHostProfileMetadata(List<String> profileNames, Profile profile)
			throws RuntimeFaultFaultMsg {
		return getVimService().queryHostProfileMetadata(getMOR(), profileNames,
				profile == null ? null : profile.getMOR());
	}

	/**
	 * @since SDK5.0
	 */
	public ProfileProfileStructure queryProfileStructure(Profile profile) throws RuntimeFaultFaultMsg {
		return getVimService().queryProfileStructure(getMOR(), profile.getMOR());
	}

	/**
	 * @since SDK5.0
	 */
	public AnswerFile retrieveAnswerFile(HostSystem host) throws RuntimeFaultFaultMsg {
		return getVimService().retrieveAnswerFile(getMOR(), host.getMOR());
	}

	/** @since SDK5.1 */
	public AnswerFile retrieveAnswerFileForProfile(HostSystem host, HostApplyProfile applyProfile)
			throws RuntimeFaultFaultMsg {
		return getVimService().retrieveAnswerFileForProfile(getMOR(), host == null ? null : host.getMOR(),
				applyProfile);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws AnswerFileUpdateFailedFaultMsg
	 * @since SDK5.0
	 */
	public Task updateAnswerFileTask(HostSystem host, AnswerFileCreateSpec configSpec)
			throws AnswerFileUpdateFailedFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().updateAnswerFileTask(getMOR(), host.getMOR(), configSpec);
		return new Task(getServerConnection(), taskMor);
	}
}
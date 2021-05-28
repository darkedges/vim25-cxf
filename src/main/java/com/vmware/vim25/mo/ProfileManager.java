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

import com.vmware.vim25.DuplicateNameFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ProfileCreateSpec;
import com.vmware.vim25.ProfilePolicyMetadata;
import com.vmware.vim25.RuntimeFaultFaultMsg;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */
public class ProfileManager extends ManagedObject {
	public ProfileManager(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	public List<Profile> getProfile() {
		List<ManagedObjectReference> mors = (List<ManagedObjectReference>) getCurrentProperty("profile");
		return convert2Profiles(mors);
	}

	public Profile createProfile(ProfileCreateSpec createSpec) throws DuplicateNameFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference profileMor = getVimService().createProfile(getMOR(), createSpec);
		return new Profile(getServerConnection(), profileMor);
	}

	public List<Profile> findAssociatedProfile(ManagedEntity entity) throws RuntimeFaultFaultMsg {
		List<ManagedObjectReference> mors = getVimService().findAssociatedProfile(getMOR(), entity.getMOR());
		return convert2Profiles(mors);
	}

	// SDK4.1 signature for back compatibility
	public List<ProfilePolicyMetadata> queryPolicyMetadata(List<String> policyName) throws RuntimeFaultFaultMsg {
		return queryPolicyMetadata(policyName, null);
	}

	// SDK5.0 signature
	public List<ProfilePolicyMetadata> queryPolicyMetadata(List<String> policyName, Profile profile)
			throws RuntimeFaultFaultMsg {
		return getVimService().queryPolicyMetadata(getMOR(), policyName, profile == null ? null : profile.getMOR());
	}

	private List<Profile> convert2Profiles(List<ManagedObjectReference> mors) {
		List<Profile> pfs = new ArrayList<Profile>();

		for (int i = 0; i < mors.size(); i++) {
			pfs.add(new Profile(getServerConnection(), mors.get(i)));
		}
		return pfs;
	}

}

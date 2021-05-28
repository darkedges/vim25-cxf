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
import java.util.Calendar;
import java.util.List;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ProfileConfigInfo;
import com.vmware.vim25.ProfileDescription;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.mo.util.MorUtil;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @since 4.0
 * @author Steve JIN (http://www.doublecloud.org)
 */
public class Profile extends ManagedObject {
	public Profile(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	public String getComplianceStatus() {
		return (String) getCurrentProperty("info");
	}

	public ProfileConfigInfo getConfig() {
		return (ProfileConfigInfo) getCurrentProperty("config");
	}

	public Calendar getCreatedTime() {
		return (Calendar) getCurrentProperty("createdTime");
	}

	public ProfileDescription getDescriptioin() {
		return (ProfileDescription) getCurrentProperty("description");
	}

	public List<ManagedEntity> getEntity() {
		List<ManagedObject> objs = getManagedObjects("entity");
		if (objs.isEmpty()) {
			return new ArrayList<ManagedEntity>();
		}
		List<ManagedEntity> mes = new ArrayList<ManagedEntity>();

		for (int i = 0; i < objs.size(); i++) {
			mes.add((ManagedEntity) objs.get(i));
		}
		return mes;
	}

	public Calendar getModifiedTime() {
		return (Calendar) getCurrentProperty("modifiedTime");
	}

	public String getName() {
		return (String) getCurrentProperty("name");
	}

	public void associateProfile(List<ManagedObject> mes) throws RuntimeFaultFaultMsg {
		List<ManagedObjectReference> mors = MorUtil.createMORs(mes);
		getVimService().associateProfile(getMOR(), mors);
	}

	public Task checkProfileComplianceTask(List<ManagedObject> mes) throws RuntimeFaultFaultMsg {
		List<ManagedObjectReference> mors = MorUtil.createMORs(mes);
		ManagedObjectReference taskMor = getVimService().checkProfileComplianceTask(getMOR(), mors);
		return new Task(getServerConnection(), taskMor);
	}

	public void destroyProfile() throws RuntimeFaultFaultMsg {
		getVimService().destroyProfile(getMOR());
	}

	public String exportProfile() throws RuntimeFaultFaultMsg {
		return (String) getVimService().exportProfile(getMOR());
	}

	public void dissociateProfile(List<ManagedObject> mes) throws RuntimeFaultFaultMsg {
		List<ManagedObjectReference> mors = MorUtil.createMORs(mes);
		getVimService().dissociateProfile(getMOR(), mors);
	}

	/**
	 * @since SDK5.0
	 */
	public ProfileDescription retrieveDescription() throws RuntimeFaultFaultMsg {
		return getVimService().retrieveDescription(getMOR());
	}
}
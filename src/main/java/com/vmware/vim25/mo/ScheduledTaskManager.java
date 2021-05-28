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
import com.vmware.vim25.InvalidNameFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.ScheduledTaskDescription;
import com.vmware.vim25.ScheduledTaskSpec;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class ScheduledTaskManager extends ManagedObject {
	public ScheduledTaskManager(ServerConnection serverConnection, ManagedObjectReference mor) {
		super(serverConnection, mor);
	}

	public ScheduledTaskDescription getDescriptioin() {
		return (ScheduledTaskDescription) getCurrentProperty("description");
	}

	public List<ScheduledTask> getScheduledTasks() {
		return getScheduledTasks("scheduledTask");
	}

	public ScheduledTask createScheduledTask(ManagedEntity entity, ScheduledTaskSpec spec)
			throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		if (entity == null) {
			throw new IllegalArgumentException("entity must not be null.");
		}
		return new ScheduledTask(getServerConnection(),
				getVimService().createScheduledTask(getMOR(), entity.getMOR(), spec));
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidNameFaultMsg
	 * @throws DuplicateNameFaultMsg
	 * @since SDK4.0
	 */
	public ScheduledTask createObjectScheduledTask(ManagedObject obj, ScheduledTaskSpec spec)
			throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		if (obj == null) {
			throw new IllegalArgumentException("managed object must not be null.");
		}
		return new ScheduledTask(getServerConnection(),
				getVimService().createObjectScheduledTask(getMOR(), obj.getMOR(), spec));
	}

	public List<ScheduledTask> retrieveEntityScheduledTask(ManagedEntity entity) throws RuntimeFaultFaultMsg {
		List<ManagedObjectReference> mors = getVimService().retrieveEntityScheduledTask(getMOR(),
				entity == null ? null : entity.getMOR());

		List<ScheduledTask> tasks = new ArrayList<ScheduledTask>();
		for (int i = 0; i < mors.size(); i++) {
			tasks.add(new ScheduledTask(getServerConnection(), mors.get(i)));
		}
		return tasks;
	}

	/** @since SDK4.0 */
	public List<ScheduledTask> retrieveObjectScheduledTask(ManagedObject obj) throws RuntimeFaultFaultMsg {
		List<ManagedObjectReference> mors = getVimService().retrieveObjectScheduledTask(getMOR(),
				obj == null ? null : obj.getMOR());

		List<ScheduledTask> tasks = new ArrayList<ScheduledTask>();
		for (int i = 0; i < mors.size(); i++) {
			tasks.add(new ScheduledTask(getServerConnection(), mors.get(i)));
		}
		return tasks;
	}
}

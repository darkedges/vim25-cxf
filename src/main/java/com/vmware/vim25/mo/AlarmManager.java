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

import com.vmware.vim25.AlarmDescription;
import com.vmware.vim25.AlarmExpression;
import com.vmware.vim25.AlarmSpec;
import com.vmware.vim25.AlarmState;
import com.vmware.vim25.DuplicateNameFaultMsg;
import com.vmware.vim25.InvalidNameFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFaultFaultMsg;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class AlarmManager extends ManagedObject {
	public AlarmManager(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	public AlarmExpression[] getDefaultExpression() {
		return (AlarmExpression[]) getCurrentProperty("defaultExpression");
	}

	public AlarmDescription getDescription() {
		return (AlarmDescription) this.getCurrentProperty("description");
	}

	/**
	 * @since 4.0
	 */
	public void acknowledgeAlarm(Alarm alarm, ManagedEntity entity) throws RuntimeFaultFaultMsg {
		getVimService().acknowledgeAlarm(getMOR(), alarm.getMOR(), entity.getMOR());
	}

	/**
	 * @since 4.0
	 */
	public boolean areAlarmActionsEnabled(ManagedEntity entity) throws RuntimeFaultFaultMsg {
		return getVimService().areAlarmActionsEnabled(getMOR(), entity.getMOR());
	}

	/**
	 * @since 4.0
	 */
	public void enableAlarmActions(ManagedEntity entity, boolean enabled) throws RuntimeFaultFaultMsg {
		getVimService().enableAlarmActions(getMOR(), entity.getMOR(), enabled);
	}

	public Alarm createAlarm(ManagedEntity me, AlarmSpec as)
			throws DuplicateNameFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		if (me == null) {
			throw new IllegalArgumentException("entity must not be null.");
		}
		ManagedObjectReference mor = getVimService().createAlarm(getMOR(), me.getMOR(), as);
		return new Alarm(getServerConnection(), mor);
	}

	public List<Alarm> getAlarm(ManagedEntity me) throws RuntimeFaultFaultMsg {
		List<ManagedObjectReference> mors = getVimService().getAlarm(getMOR(), me == null ? null : me.getMOR());

		if (mors == null) {
			return new ArrayList<Alarm>();
		}

		List<Alarm> alarms = new ArrayList<Alarm>();
		for (int i = 0; i < mors.size(); i++) {
			alarms.add(new Alarm(getServerConnection(), mors.get(i)));
		}
		return alarms;
	}

	public List<AlarmState> getAlarmState(ManagedEntity me) throws RuntimeFaultFaultMsg {
		if (me == null) {
			throw new IllegalArgumentException("entity must not be null.");
		}
		return getVimService().getAlarmState(getMOR(), me.getMOR());
	}
}

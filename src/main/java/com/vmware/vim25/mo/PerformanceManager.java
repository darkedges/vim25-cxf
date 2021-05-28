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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.PerfCompositeMetric;
import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.PerfEntityMetricBase;
import com.vmware.vim25.PerfInterval;
import com.vmware.vim25.PerfMetricId;
import com.vmware.vim25.PerfProviderSummary;
import com.vmware.vim25.PerfQuerySpec;
import com.vmware.vim25.PerformanceDescription;
import com.vmware.vim25.RuntimeFaultFaultMsg;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class PerformanceManager extends ManagedObject {

	public PerformanceManager(ServerConnection serverConnection, ManagedObjectReference mor) {
		super(serverConnection, mor);
	}

	public PerformanceDescription getDescription() {
		return (PerformanceDescription) getCurrentProperty("description");
	}

	public PerfInterval[] getHistoricalInterval() {
		return (PerfInterval[]) getCurrentProperty("historicalInterval");
	}

	public PerfCounterInfo[] getPerfCounter() {
		return (PerfCounterInfo[]) getCurrentProperty("perfCounter");
	}

	/**
	 * @throws
	 * @throws
	 * @deprecated use UpdatePerfInterval instead
	 */
	public void createPerfInterval(PerfInterval intervalId) throws RuntimeFaultFaultMsg {
		getVimService().createPerfInterval(getMOR(), intervalId);
	}

	public List<PerfMetricId> queryAvailablePerfMetric(ManagedEntity entity, Calendar beginTime, Calendar endTime,
			Integer intervalId) throws RuntimeFaultFaultMsg, DatatypeConfigurationException {
		if (entity == null) {
			throw new IllegalArgumentException("entity must not be null.");
		}
		XMLGregorianCalendar bTime = DatatypeFactory.newInstance()
				.newXMLGregorianCalendar((GregorianCalendar) beginTime);
		XMLGregorianCalendar eTime = DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar) endTime);
		return getVimService().queryAvailablePerfMetric(getMOR(), entity.getMOR(), bTime, eTime, intervalId);
	}

	public List<PerfEntityMetricBase> queryPerf(List<PerfQuerySpec> querySpec) throws RuntimeFaultFaultMsg {
		return getVimService().queryPerf(getMOR(), querySpec);
	}

	public PerfCompositeMetric queryPerfComposite(PerfQuerySpec querySpec) throws RuntimeFaultFaultMsg {
		return getVimService().queryPerfComposite(getMOR(), querySpec);
	}

	public List<PerfCounterInfo> queryPerfCounter(List<Integer> counterIds) throws RuntimeFaultFaultMsg {
		return getVimService().queryPerfCounter(getMOR(), counterIds);
	}

	public List<PerfCounterInfo> queryPerfCounterByLevel(int level) throws RuntimeFaultFaultMsg {
		return getVimService().queryPerfCounterByLevel(getMOR(), level);
	}

	public PerfProviderSummary queryPerfProviderSummary(ManagedEntity entity) throws RuntimeFaultFaultMsg {
		if (entity == null) {
			throw new IllegalArgumentException("entity must not be null.");
		}
		return getVimService().queryPerfProviderSummary(getMOR(), entity.getMOR());
	}

	public void removePerfInterval(int samplePeriod) throws RuntimeFaultFaultMsg {
		getVimService().removePerfInterval(getMOR(), samplePeriod);
	}

	public void updatePerfInterval(PerfInterval interval) throws RuntimeFaultFaultMsg {
		getVimService().updatePerfInterval(getMOR(), interval);
	}

}

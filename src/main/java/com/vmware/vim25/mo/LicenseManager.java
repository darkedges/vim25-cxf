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

import java.util.List;

import com.vmware.vim25.*;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class LicenseManager extends ManagedObject {

	public LicenseManager(ServerConnection serverConnection, ManagedObjectReference mor) {
		super(serverConnection, mor);
	}

	/**
	 * @deprecated in SDK4.0
	 */
	public LicenseDiagnostics getDiagnostics() {
		return (LicenseDiagnostics) getCurrentProperty("diagnostics");
	}

	public LicenseManagerEvaluationInfo getEvaluation() {
		return (LicenseManagerEvaluationInfo) getCurrentProperty("evaluation");
	}

	public LicenseAssignmentManager getLicenseAssignmentManager() {
		return (LicenseAssignmentManager) getManagedObject("licenseAssignmentManager");
	}

	public LicenseManagerLicenseInfo[] getLicenses() {
		return (LicenseManagerLicenseInfo[]) getCurrentProperty("licenses");
	}

	/**
	 * @deprecated in SDK4.0
	 */
	public LicenseFeatureInfo[] getFeatureInfo() {
		return (LicenseFeatureInfo[]) getCurrentProperty("featureInfo");
	}

	/**
	 * @deprecated in SDK4.0
	 */
	public String getLicensedEdition() {
		return (String) getCurrentProperty("licensedEdition");
	}

	/**
	 * @deprecated in SDK4.0
	 */
	public LicenseSource getSource() {
		return (LicenseSource) getCurrentProperty("source");
	}

	/** @deprecated in SDK4.0 */
	public boolean getSourceAvailable() {
		return ((Boolean) getCurrentProperty("sourceAvailable")).booleanValue();
	}

	/** @since SDK4.0 */
	public LicenseManagerLicenseInfo addLicense(String licenseKey, List<KeyValue> labels) throws RuntimeFaultFaultMsg {
		return (LicenseManagerLicenseInfo) getVimService().addLicense(getMOR(), licenseKey, labels);
	}

	/** @since SDK4.0 */
	public LicenseManagerLicenseInfo decodeLicense(String licenseKey) throws RuntimeFaultFaultMsg {
		return (LicenseManagerLicenseInfo) getVimService().decodeLicense(getMOR(), licenseKey);
	}

	public boolean checkLicenseFeature(HostSystem host, String featureKey)
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().checkLicenseFeature(getMOR(), host == null ? null : host.getMOR(), featureKey);
	}

	public void configureLicenseSource(HostSystem host, LicenseSource licenseSource)
			throws CannotAccessLocalSourceFaultMsg, InvalidLicenseFaultMsg, LicenseServerUnavailableFaultMsg,
			RuntimeFaultFaultMsg {
		getVimService().configureLicenseSource(getMOR(), host == null ? null : host.getMOR(), licenseSource);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws LicenseServerUnavailableFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @deprecated in SDK4.0
	 */
	public void disableFeature(HostSystem host, String featureKey)
			throws InvalidStateFaultMsg, LicenseServerUnavailableFaultMsg, RuntimeFaultFaultMsg {
		getVimService().disableFeature(getMOR(), host == null ? null : host.getMOR(), featureKey);
	}

	public void enableFeature(HostSystem host, String featureKey)
			throws InvalidStateFaultMsg, LicenseServerUnavailableFaultMsg, RuntimeFaultFaultMsg {
		getVimService().enableFeature(getMOR(), host == null ? null : host.getMOR(), featureKey);
	}

	public List<LicenseAvailabilityInfo> queryLicenseSourceAvailability(HostSystem host) throws RuntimeFaultFaultMsg {
		return getVimService().queryLicenseSourceAvailability(getMOR(), host == null ? null : host.getMOR());
	}

	public LicenseUsageInfo queryLicenseUsage(HostSystem host) throws RuntimeFaultFaultMsg {
		return getVimService().queryLicenseUsage(getMOR(), host == null ? null : host.getMOR());
	}

	public List<LicenseFeatureInfo> querySupportedFeatures(HostSystem host) throws RuntimeFaultFaultMsg {
		return getVimService().querySupportedFeatures(getMOR(), host == null ? null : host.getMOR());
	}

	/** @since SDK4.0 */
	public void removeLicense(String licenseKey) throws RuntimeFaultFaultMsg {
		getVimService().removeLicense(getMOR(), licenseKey);
	}

	/** @since SDK4.0 */
	public void removeLicenseLabel(String licenseKey, String labelKey) throws RuntimeFaultFaultMsg {
		getVimService().removeLicenseLabel(getMOR(), licenseKey, labelKey);
	}

	/** @since SDK4.0 */
	public void updateLicense(String licenseKey, List<KeyValue> labels) throws RuntimeFaultFaultMsg {
		getVimService().updateLicense(getMOR(), licenseKey, labels);
	}

	/** @since SDK4.0 */
	public void updateLicenseLabel(String licenseKey, String labelKey, String labelValue) throws RuntimeFaultFaultMsg {
		getVimService().updateLicenseLabel(getMOR(), licenseKey, labelKey, labelValue);
	}

	public void setLicenseEdition(HostSystem host, String featureKey)
			throws InvalidStateFaultMsg, LicenseServerUnavailableFaultMsg, RuntimeFaultFaultMsg {
		getVimService().setLicenseEdition(getMOR(), host == null ? null : host.getMOR(), featureKey);
	}

}

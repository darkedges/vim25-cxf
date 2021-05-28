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

import com.vmware.vim25.AlreadyExistsFaultMsg;
import com.vmware.vim25.DuplicateNameFaultMsg;
import com.vmware.vim25.FcoeConfigFcoeSpecification;
import com.vmware.vim25.FcoeFaultPnicHasNoPortSetFaultMsg;
import com.vmware.vim25.HostConfigFaultFaultMsg;
import com.vmware.vim25.HostDiskPartitionBlockRange;
import com.vmware.vim25.HostDiskPartitionInfo;
import com.vmware.vim25.HostDiskPartitionLayout;
import com.vmware.vim25.HostDiskPartitionSpec;
import com.vmware.vim25.HostFileSystemVolumeInfo;
import com.vmware.vim25.HostInternetScsiHbaAuthenticationProperties;
import com.vmware.vim25.HostInternetScsiHbaDigestProperties;
import com.vmware.vim25.HostInternetScsiHbaDiscoveryProperties;
import com.vmware.vim25.HostInternetScsiHbaIPProperties;
import com.vmware.vim25.HostInternetScsiHbaParamValue;
import com.vmware.vim25.HostInternetScsiHbaSendTarget;
import com.vmware.vim25.HostInternetScsiHbaStaticTarget;
import com.vmware.vim25.HostInternetScsiHbaTargetSet;
import com.vmware.vim25.HostMultipathInfoLogicalUnitPolicy;
import com.vmware.vim25.HostMultipathStateInfo;
import com.vmware.vim25.HostPathSelectionPolicyOption;
import com.vmware.vim25.HostScsiDiskPartition;
import com.vmware.vim25.HostStorageArrayTypePolicyOption;
import com.vmware.vim25.HostStorageDeviceInfo;
import com.vmware.vim25.HostUnresolvedVmfsResolutionResult;
import com.vmware.vim25.HostUnresolvedVmfsResolutionSpec;
import com.vmware.vim25.HostUnresolvedVmfsVolume;
import com.vmware.vim25.HostVmfsSpec;
import com.vmware.vim25.HostVmfsVolume;
import com.vmware.vim25.InvalidNameFaultMsg;
import com.vmware.vim25.InvalidStateFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NotFoundFaultMsg;
import com.vmware.vim25.ResourceInUseFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class HostStorageSystem extends ExtensibleManagedObject {

	public HostStorageSystem(ServerConnection serverConnection, ManagedObjectReference mor) {
		super(serverConnection, mor);
	}

	public HostFileSystemVolumeInfo getFileSystemVolumeInfo() {
		return (HostFileSystemVolumeInfo) getCurrentProperty("fileSystemVolumeInfo");
	}

	/**
	 * @since 4.0
	 */
	public HostMultipathStateInfo getMultipathStateInfo() {
		return (HostMultipathStateInfo) getCurrentProperty("multipathStateInfo");
	}

	public HostStorageDeviceInfo getStorageDeviceInfo() {
		return (HostStorageDeviceInfo) getCurrentProperty("storageDeviceInfo");
	}

	/** @since SDK4.1 */
	public String[] getSystemFile() {
		return (String[]) getCurrentProperty("systemFile");
	}

	public void addInternetScsiSendTargets(String iScsiHbaDevice, List<HostInternetScsiHbaSendTarget> targets)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().addInternetScsiSendTargets(getMOR(), iScsiHbaDevice, targets);
	}

	public void addInternetScsiStaticTargets(String iScsiHbaDevice, List<HostInternetScsiHbaStaticTarget> targets)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().addInternetScsiStaticTargets(getMOR(), iScsiHbaDevice, targets);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since SDK5.0
	 */
	public void attachScsiLun(String lunUuid) throws RemoteException, HostConfigFaultFaultMsg, InvalidStateFaultMsg,
			NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().attachScsiLun(getMOR(), lunUuid);
	}

	public void attachVmfsExtent(String vmfsPath, HostScsiDiskPartition extent)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().attachVmfsExtent(getMOR(), vmfsPath, extent);
	}

	// SDK4.1 signature for back compatibility
	public HostDiskPartitionInfo computeDiskPartitionInfo(String devicePath, HostDiskPartitionLayout layout)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		return computeDiskPartitionInfo(devicePath, layout, null);
	}

	// SDK5.0 signature
	public HostDiskPartitionInfo computeDiskPartitionInfo(String devicePath, HostDiskPartitionLayout layout,
			String partitionFormat) throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().computeDiskPartitionInfo(getMOR(), devicePath, layout, partitionFormat);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since 4.0
	 */
	// SDK4.1 signature for back compatibility
	public HostDiskPartitionInfo computeDiskPartitionInfoForResize(HostScsiDiskPartition partition,
			HostDiskPartitionBlockRange blockRange)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		return computeDiskPartitionInfoForResize(partition, blockRange, null);
	}

	// SDK5.0 signature
	public HostDiskPartitionInfo computeDiskPartitionInfoForResize(HostScsiDiskPartition partition,
			HostDiskPartitionBlockRange blockRange, String partitionFormat)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().computeDiskPartitionInfoForResize(getMOR(), partition, blockRange, partitionFormat);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws ResourceInUseFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since SDK5.0
	 */
	public void detachScsiLun(String lunUuid) throws RemoteException, HostConfigFaultFaultMsg, InvalidStateFaultMsg,
			NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg {
		getVimService().detachScsiLun(getMOR(), lunUuid);
	}

	public void disableMultipathPath(String pathName)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().disableMultipathPath(getMOR(), pathName);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @throws FcoeFaultPnicHasNoPortSetFaultMsg
	 * @since SDK5.0
	 */
	public void discoverFcoeHbas(FcoeConfigFcoeSpecification fcoeSpec) throws RemoteException,
			FcoeFaultPnicHasNoPortSetFaultMsg, HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().discoverFcoeHbas(getMOR(), fcoeSpec);
	}

	public void enableMultipathPath(String pathName)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().enableMultipathPath(getMOR(), pathName);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since 4.0
	 */
	public void expandVmfsExtent(String vmfsPath, HostScsiDiskPartition extent)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().expandVmfsExtent(getMOR(), vmfsPath, extent);
	}

	public HostVmfsVolume formatVmfs(HostVmfsSpec createSpec)
			throws RemoteException, AlreadyExistsFaultMsg, HostConfigFaultFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().formatVmfs(getMOR(), createSpec);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since SDK5.0
	 */
	public void markForRemoval(String hbaName, boolean remove)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().markForRemoval(getMOR(), hbaName, remove);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws ResourceInUseFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since SDK5.0
	 */
	public void mountVmfsVolume(String vmfsUuid) throws RemoteException, HostConfigFaultFaultMsg, InvalidStateFaultMsg,
			NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg {
		getVimService().mountVmfsVolume(getMOR(), vmfsUuid);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since 4.0
	 */
	public List<HostPathSelectionPolicyOption> queryPathSelectionPolicyOptions()
			throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().queryPathSelectionPolicyOptions(getMOR());
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since 4.0
	 */
	public List<HostStorageArrayTypePolicyOption> queryStorageArrayTypePolicyOptions()
			throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().queryStorageArrayTypePolicyOptions(getMOR());
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @since 4.0
	 */
	public List<HostUnresolvedVmfsVolume> queryUnresolvedVmfsVolume() throws RuntimeFaultFaultMsg {
		return getVimService().queryUnresolvedVmfsVolume(getMOR());
	}

	public void refreshStorageSystem() throws RuntimeFaultFaultMsg {
		getVimService().refreshStorageSystem(getMOR());
	}

	public void removeInternetScsiSendTargets(String iScsiHbaDevice, List<HostInternetScsiHbaStaticTarget> targets)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().removeInternetScsiStaticTargets(getMOR(), iScsiHbaDevice, targets);
	}

	public void removeInternetScsiStaticTargets(String iScsiHbaDevice, List<HostInternetScsiHbaStaticTarget> targets)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().removeInternetScsiStaticTargets(getMOR(), iScsiHbaDevice, targets);
	}

	public void rescanAllHba() throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg {
		getVimService().rescanAllHba(getMOR());
	}

	public void rescanHba(String hbaDevice) throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().rescanHba(getMOR(), hbaDevice);
	}

	public void rescanVmfs() throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg {
		getVimService().rescanVmfs(getMOR());
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since 4.0
	 */
	public List<HostUnresolvedVmfsResolutionResult> resolveMultipleUnresolvedVmfsVolumes(
			List<HostUnresolvedVmfsResolutionSpec> resolutionSpec)
			throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().resolveMultipleUnresolvedVmfsVolumes(getMOR(), resolutionSpec);
	}

	public List<HostDiskPartitionInfo> retrieveDiskPartitionInfo(List<String> devicePath) throws RuntimeFaultFaultMsg {
		return getVimService().retrieveDiskPartitionInfo(getMOR(), devicePath);
	}

	public void setMultipathLunPolicy(String lunId, HostMultipathInfoLogicalUnitPolicy policy)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().setMultipathLunPolicy(getMOR(), lunId, policy);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since SDK4.0
	 */
	public void unmountForceMountedVmfsVolume(String vmfsUuid)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().unmountForceMountedVmfsVolume(getMOR(), vmfsUuid);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws ResourceInUseFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since SDK5.0
	 */
	public void unmountVmfsVolume(String vmfsUuid) throws RemoteException, HostConfigFaultFaultMsg,
			InvalidStateFaultMsg, NotFoundFaultMsg, ResourceInUseFaultMsg, RuntimeFaultFaultMsg {
		getVimService().unmountVmfsVolume(getMOR(), vmfsUuid);
	}

	public void updateDiskPartitions(String devicePath, HostDiskPartitionSpec spec)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateDiskPartitions(getMOR(), devicePath, spec);
	}

	public void updateInternetScsiAlias(String iScsiHbaDevice, String iScsiAlias)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateInternetScsiAlias(getMOR(), iScsiHbaDevice, iScsiAlias);
	}

	// SDK2.5 signature for back compatibility
	public void updateInternetScsiAuthenticationProperties(String iScsiHbaDevice,
			HostInternetScsiHbaAuthenticationProperties authenticationProperties)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg

	{
		updateInternetScsiAuthenticationProperties(iScsiHbaDevice, authenticationProperties, null);
	}

	// SDK4.0 signature
	public void updateInternetScsiAuthenticationProperties(String iScsiHbaDevice,
			HostInternetScsiHbaAuthenticationProperties authenticationProperties,
			HostInternetScsiHbaTargetSet targetSet)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg

	{
		getVimService().updateInternetScsiAuthenticationProperties(getMOR(), iScsiHbaDevice, authenticationProperties,
				targetSet);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since 4.0
	 */
	public void updateInternetScsiAdvancedOptions(String iScsiHbaDevice, HostInternetScsiHbaTargetSet targetSet,
			List<HostInternetScsiHbaParamValue> options)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateInternetScsiAdvancedOptions(getMOR(), iScsiHbaDevice, targetSet, options);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @since 4.0
	 */
	public void updateInternetScsiDigestProperties(String iScsiHbaDevice, HostInternetScsiHbaTargetSet targetSet,
			HostInternetScsiHbaDigestProperties digestProperties)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateInternetScsiDigestProperties(getMOR(), iScsiHbaDevice, targetSet, digestProperties);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws InvalidNameFaultMsg
	 * @throws HostConfigFaultFaultMsg
	 * @throws DuplicateNameFaultMsg
	 * @since 4.0
	 */
	public void updateScsiLunDisplayName(String lunUuid, String displayName) throws DuplicateNameFaultMsg,
			HostConfigFaultFaultMsg, InvalidNameFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateScsiLunDisplayName(getMOR(), lunUuid, displayName);
	}

	public void updateInternetScsiDiscoveryProperties(String iScsiHbaDevice,
			HostInternetScsiHbaDiscoveryProperties discoveryProperties)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateInternetScsiDiscoveryProperties(getMOR(), iScsiHbaDevice, discoveryProperties);
	}

	public void updateInternetScsiIPProperties(String iScsiHbaDevice, HostInternetScsiHbaIPProperties ipProperties)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg

	{
		getVimService().updateInternetScsiIPProperties(getMOR(), iScsiHbaDevice, ipProperties);
	}

	public void updateInternetScsiName(String iScsiHbaDevice, String iScsiName)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateInternetScsiName(getMOR(), iScsiHbaDevice, iScsiName);
	}

	public void updateSoftwareInternetScsiEnabled(boolean enabled)
			throws HostConfigFaultFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateSoftwareInternetScsiEnabled(getMOR(), enabled);
	}

	public void upgradeVmfs(String vmfsPath) throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().upgradeVmfs(getMOR(), vmfsPath);
	}

	public void upgradeVmLayout(String vmfsPath)
			throws HostConfigFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().upgradeVmfs(getMOR(), vmfsPath);
	}
}

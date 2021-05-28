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

import com.vmware.vim25.AlreadyExistsFaultMsg;
import com.vmware.vim25.AlreadyUpgradedFaultMsg;
import com.vmware.vim25.ConcurrentAccessFaultMsg;
import com.vmware.vim25.CustomizationFaultFaultMsg;
import com.vmware.vim25.CustomizationSpec;
import com.vmware.vim25.DiskChangeInfo;
import com.vmware.vim25.DuplicateNameFaultMsg;
import com.vmware.vim25.FileFaultFaultMsg;
import com.vmware.vim25.GuestInfo;
import com.vmware.vim25.HostIncompatibleForRecordReplayFaultMsg;
import com.vmware.vim25.InsufficientResourcesFaultFaultMsg;
import com.vmware.vim25.InvalidDatastoreFaultMsg;
import com.vmware.vim25.InvalidNameFaultMsg;
import com.vmware.vim25.InvalidPowerStateFaultMsg;
import com.vmware.vim25.InvalidStateFaultMsg;
import com.vmware.vim25.LocalizedMethodFault;
import com.vmware.vim25.ManagedEntityStatus;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.MigrationFaultFaultMsg;
import com.vmware.vim25.NoDiskFoundFaultMsg;
import com.vmware.vim25.NotFoundFaultMsg;
import com.vmware.vim25.RecordReplayDisabledFaultMsg;
import com.vmware.vim25.ResourceConfigSpec;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.SnapshotFaultFaultMsg;
import com.vmware.vim25.TaskInProgressFaultMsg;
import com.vmware.vim25.TimedoutFaultMsg;
import com.vmware.vim25.ToolsUnavailableFaultMsg;
import com.vmware.vim25.VirtualDisk;
import com.vmware.vim25.VirtualMachineCapability;
import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachineDisplayTopology;
import com.vmware.vim25.VirtualMachineFileLayout;
import com.vmware.vim25.VirtualMachineFileLayoutEx;
import com.vmware.vim25.VirtualMachineMksTicket;
import com.vmware.vim25.VirtualMachineMovePriority;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSnapshotInfo;
import com.vmware.vim25.VirtualMachineStorageInfo;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.VirtualMachineTicket;
import com.vmware.vim25.VmConfigFaultFaultMsg;
import com.vmware.vim25.VmFaultToleranceIssueFaultMsg;
import com.vmware.vim25.VmToolsUpgradeFaultFaultMsg;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class VirtualMachine extends ManagedEntity {
	public VirtualMachine(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	public VirtualMachineCapability getCapability() {
		return (VirtualMachineCapability) getCurrentProperty("capability");
	}

	public VirtualMachineConfigInfo getConfig() {
		return (VirtualMachineConfigInfo) getCurrentProperty("config");
	}

	public List<Datastore> getDatastores() {
		return getDatastores("datastore");
	}

	public EnvironmentBrowser getEnvironmentBrowser() {
		return (EnvironmentBrowser) getManagedObject("environmentBrowser");
	}

	public GuestInfo getGuest() {
		return (GuestInfo) getCurrentProperty("guest");
	}

	public ManagedEntityStatus getGuestHeartbeatStatus() {
		return (ManagedEntityStatus) getCurrentProperty("guestHeartbeatStatus");
	}

	public VirtualMachineFileLayout getLayout() {
		return (VirtualMachineFileLayout) getCurrentProperty("layout");
	}

	/** @since SDK4.0 */
	public VirtualMachineFileLayoutEx getLayoutEx() {
		return (VirtualMachineFileLayoutEx) getCurrentProperty("layoutEx");
	}

	/** @since SDK4.0 */
	public VirtualMachineStorageInfo getStorage() {
		return (VirtualMachineStorageInfo) getCurrentProperty("storage");
	}

	public List<Network> getNetworks() {
		return getNetworks("network");
	}

	/** @since SDK4.1 */
	public ManagedEntity getParentVApp() {
		ManagedObjectReference mor = (ManagedObjectReference) getCurrentProperty("parentVApp");
		return new ManagedEntity(getServerConnection(), mor);
	}

	public ResourceConfigSpec getResourceConfig() {
		return (ResourceConfigSpec) getCurrentProperty("resourceConfig");
	}

	public ResourcePool getResourcePool() {
		return (ResourcePool) getManagedObject("resourcePool");
	}

	/** @since SDK4.1 */
	public List<VirtualMachineSnapshot> getRootSnapshot() {
		List<ManagedObjectReference> mors = (List<ManagedObjectReference>) getCurrentProperty("rootSnapshot");
		if (mors == null) {
			return new ArrayList<VirtualMachineSnapshot>();
		}
		List<VirtualMachineSnapshot> vmns = new ArrayList<VirtualMachineSnapshot>();
		for (int i = 0; i < mors.size(); i++) {
			vmns.add(new VirtualMachineSnapshot(getServerConnection(), mors.get(i)));
		}
		return vmns;
	}

	public VirtualMachineRuntimeInfo getRuntime() {
		return (VirtualMachineRuntimeInfo) getCurrentProperty("runtime");
	}

	public VirtualMachineSnapshotInfo getSnapshot() {
		return (VirtualMachineSnapshotInfo) getCurrentProperty("snapshot");
	}

	public VirtualMachineSnapshot getCurrentSnapShot() {
		return (VirtualMachineSnapshot) getManagedObject("snapshot.currentSnapshot");
	}

	public VirtualMachineSummary getSummary() {
		return (VirtualMachineSummary) getCurrentProperty("summary");
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @deprecated as of SDK4.1. Use acquireTicket instead.
	 */
	public VirtualMachineMksTicket acquireMksTicket() throws RuntimeFaultFaultMsg {
		return getVimService().acquireMksTicket(getMOR());
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since SDK4.1
	 */
	public VirtualMachineTicket acquireTicket(String ticketType) throws InvalidStateFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().acquireTicket(getMOR(), ticketType);
	}

	public void answerVM(String questionId, String answerChoice) throws ConcurrentAccessFaultMsg, RuntimeFaultFaultMsg {
		getVimService().answerVM(getMOR(), questionId, answerChoice);
	}

	public void checkCustomizationSpec(CustomizationSpec spec) throws CustomizationFaultFaultMsg, RuntimeFaultFaultMsg {
		getVimService().checkCustomizationSpec(getMOR(), spec);
	}

	public Task cloneVMTask(Folder folder, String name, VirtualMachineCloneSpec spec) throws CustomizationFaultFaultMsg,
			FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidStateFaultMsg,
			MigrationFaultFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		if (folder == null) {
			throw new IllegalArgumentException("folder must not be null.");
		}
		ManagedObjectReference mor = getVimService().cloneVMTask(getMOR(), folder.getMOR(), name, spec);
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws VmConfigFaultFaultMsg
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws FileFaultFaultMsg
	 * @since SDK5.0
	 */
	public Task consolidateVMDisksTask() throws FileFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg,
			TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().consolidateVMDisksTask(getMOR());
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws FileFaultFaultMsg
	 * @since SDK4.0
	 */
	public Task CreateScreenshotTask()
			throws FileFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference mor = getVimService().createScreenshotTask(getMOR());
		return new Task(getServerConnection(), mor);
	}

	public Task createSnapshotTask(String name, String description, boolean memory, boolean quiesce)
			throws FileFaultFaultMsg, InvalidNameFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg,
			SnapshotFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference mor = getVimService().createSnapshotTask(getMOR(), name, description, memory, quiesce);
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws VmFaultToleranceIssueFaultMsg
	 * @throws VmConfigFaultFaultMsg
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws InsufficientResourcesFaultFaultMsg
	 * @throws FileFaultFaultMsg
	 * @since SDK4.0
	 */
	public Task createSecondaryVMTask(HostSystem host)
			throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg,
			TaskInProgressFaultMsg, VmConfigFaultFaultMsg, VmFaultToleranceIssueFaultMsg {
		ManagedObjectReference mor = getVimService().createSecondaryVMTask(getMOR(),
				host == null ? null : host.getMOR());
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws VmFaultToleranceIssueFaultMsg
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since SDK4.0
	 */
	public Task disableSecondaryVMTask(VirtualMachine vm)
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmFaultToleranceIssueFaultMsg {
		ManagedObjectReference mor = getVimService().disableSecondaryVMTask(getMOR(), vm.getMOR());
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws VmFaultToleranceIssueFaultMsg
	 * @throws VmConfigFaultFaultMsg
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since SDK4.0
	 */
	public Task enableSecondaryVMTask(VirtualMachine vm, HostSystem host) throws InvalidStateFaultMsg,
			RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg, VmFaultToleranceIssueFaultMsg {
		ManagedObjectReference mor = getVimService().enableSecondaryVMTask(getMOR(), vm.getMOR(),
				host == null ? null : host.getMOR());
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws VmConfigFaultFaultMsg
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws FileFaultFaultMsg
	 * @since SDK5.0
	 */
	public Task estimateStorageForConsolidateSnapshotsTask() throws FileFaultFaultMsg, InvalidStateFaultMsg,
			RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference taskMor = getVimService().estimateStorageForConsolidateSnapshotsTask(getMOR());
		return new Task(getServerConnection(), taskMor);
	}

	/**
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws InvalidPowerStateFaultMsg
	 * @throws FileFaultFaultMsg
	 * @since SDK4.0
	 */
	public HttpNfcLease exportVm() throws FileFaultFaultMsg, InvalidPowerStateFaultMsg, InvalidStateFaultMsg,
			RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference mor = getVimService().exportVm(getMOR());
		return new HttpNfcLease(getServerConnection(), mor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since SDK4.0
	 */
	public String extractOvfEnvironment() throws InvalidStateFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().extractOvfEnvironment(getMOR());
	}

	/**
	 * @throws VmFaultToleranceIssueFaultMsg
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since SDK4.0
	 */
	public Task makePrimaryVMTask(VirtualMachine vm)
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmFaultToleranceIssueFaultMsg {
		ManagedObjectReference mor = getVimService().makePrimaryVMTask(getMOR(), vm.getMOR());
		return new Task(getServerConnection(), mor);
	}

	public Task customizeVMTask(CustomizationSpec spec) throws CustomizationFaultFaultMsg, RuntimeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().customizeVMTask(getMOR(), spec);
		return new Task(getServerConnection(), mor);
	}

	public void defragmentAllDisks() throws FileFaultFaultMsg, InvalidPowerStateFaultMsg, InvalidStateFaultMsg,
			RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		getVimService().defragmentAllDisks(getMOR());
	}

	public void markAsTemplate()
			throws FileFaultFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg {
		getVimService().markAsTemplate(getMOR());
	}

	public void markAsVirtualMachine(ResourcePool pool, HostSystem host) throws FileFaultFaultMsg,
			InvalidDatastoreFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg {
		if (pool == null) {
			throw new IllegalArgumentException("pool must not be null.");
		}
		getVimService().markAsVirtualMachine(getMOR(), pool.getMOR(), host == null ? null : host.getMOR());
	}

	public Task migrateVMTask(ResourcePool pool, HostSystem host, VirtualMachineMovePriority priority,
			VirtualMachinePowerState state)
			throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, MigrationFaultFaultMsg,
			RuntimeFaultFaultMsg, TimedoutFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference mor = getVimService().migrateVMTask(getMOR(), pool == null ? null : pool.getMOR(),
				host == null ? null : host.getMOR(), priority, state);
		return new Task(getServerConnection(), mor);
	}

	public void mountToolsInstaller()
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg, VmToolsUpgradeFaultFaultMsg {
		getVimService().mountToolsInstaller(getMOR());
	}

	public Task powerOffVMTask() throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference mor = getVimService().powerOffVMTask(getMOR());
		return new Task(getServerConnection(), mor);
	}

	public Task powerOnVMTask(HostSystem host) throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg,
			InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference mor = getVimService().powerOnVMTask(getMOR(), host == null ? null : host.getMOR());
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws InvalidPowerStateFaultMsg
	 * @since SDK4.0
	 */
	public Task promoteDisksTask(boolean unlink, List<VirtualDisk> disks)
			throws InvalidPowerStateFaultMsg, InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference mor = getVimService().promoteDisksTask(getMOR(), unlink, disks);
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws FileFaultFaultMsg
	 * @since SDK4.0
	 */
	public DiskChangeInfo queryChangedDiskAreas(VirtualMachineSnapshot snapshot, int deviceKey, long startOffset,
			String changeId) throws FileFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().queryChangedDiskAreas(getMOR(), snapshot == null ? null : snapshot.getMOR(), deviceKey,
				startOffset, changeId);
	}

	/**
	 * @throws VmConfigFaultFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since SDK4.1
	 */
	public List<LocalizedMethodFault> queryFaultToleranceCompatibility()
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg {
		return getVimService().queryFaultToleranceCompatibility(getMOR());
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @since SDK4.0
	 */
	public List<String> queryUnownedFiles() throws RuntimeFaultFaultMsg {
		return getVimService().queryUnownedFiles(getMOR());
	}

	public void rebootGuest()
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, ToolsUnavailableFaultMsg {
		getVimService().rebootGuest(getMOR());
	}

	public Task reconfigVMTask(VirtualMachineConfigSpec spec) throws ConcurrentAccessFaultMsg, DuplicateNameFaultMsg,
			FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidNameFaultMsg,
			InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference mor = getVimService().reconfigVMTask(getMOR(), spec);
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws VmConfigFaultFaultMsg
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws InvalidPowerStateFaultMsg
	 * @throws FileFaultFaultMsg
	 * @throws AlreadyExistsFaultMsg
	 * @since SDK4.1
	 */
	public Task reloadVirtualMachineFromPathTask(String configurationPath)
			throws AlreadyExistsFaultMsg, FileFaultFaultMsg, InvalidPowerStateFaultMsg, InvalidStateFaultMsg,
			RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference mor = getVimService().reloadVirtualMachineFromPathTask(getMOR(), configurationPath);
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws RuntimeFaultFaultMsg
	 * @since SDK4.0
	 */
	public void refreshStorageInfo() throws RuntimeFaultFaultMsg {
		getVimService().refreshStorageInfo(getMOR());
	}

	// SDK2.5 signature for back compatibility
	public Task relocateVMTask(VirtualMachineRelocateSpec spec) throws FileFaultFaultMsg,
			InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg, InvalidStateFaultMsg, MigrationFaultFaultMsg,
			RuntimeFaultFaultMsg, TimedoutFaultMsg, VmConfigFaultFaultMsg {
		return relocateVMTask(spec, null);
	}

	// SDK4.0 signature
	public Task relocateVMTask(VirtualMachineRelocateSpec spec, VirtualMachineMovePriority priority)
			throws FileFaultFaultMsg, InsufficientResourcesFaultFaultMsg, InvalidDatastoreFaultMsg,
			InvalidStateFaultMsg, MigrationFaultFaultMsg, RuntimeFaultFaultMsg, TimedoutFaultMsg,
			VmConfigFaultFaultMsg {
		ManagedObjectReference mor = getVimService().relocateVMTask(getMOR(), spec, priority);
		return new Task(getServerConnection(), mor);
	}

	// SDK4.1 signature for back compatibility
	public Task removeAllSnapshotsTask()
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, SnapshotFaultFaultMsg, TaskInProgressFaultMsg {
		return removeAllSnapshotsTask(null);
	}

	// SDK5.0 signature
	public Task removeAllSnapshotsTask(Boolean consolidate)
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, SnapshotFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference mor = getVimService().removeAllSnapshotsTask(getMOR(), consolidate);
		return new Task(getServerConnection(), mor);
	}

	public void resetGuestInformation() throws InvalidStateFaultMsg, RuntimeFaultFaultMsg {
		getVimService().resetGuestInformation(getMOR());
	}

	public Task resetVMTask() throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference mor = getVimService().resetVMTask(getMOR());
		return new Task(getServerConnection(), mor);
	}

	// SDK2.5 signature for back compatibility
	public Task revertToCurrentSnapshotTask(HostSystem host)
			throws InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg,
			SnapshotFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		return revertToCurrentSnapshotTask(host, null);
	}

	// SDK4.0 signature
	public Task revertToCurrentSnapshotTask(HostSystem host, Boolean suppressPowerOn)
			throws InsufficientResourcesFaultFaultMsg, InvalidStateFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg,
			SnapshotFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference mor = getVimService().revertToCurrentSnapshotTask(getMOR(),
				host == null ? null : host.getMOR(), suppressPowerOn);
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws ToolsUnavailableFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since SDK4.0
	 */
	public void setDisplayTopology(List<VirtualMachineDisplayTopology> displays)
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, ToolsUnavailableFaultMsg {
		getVimService().setDisplayTopology(getMOR(), displays);
	}

	public void setScreenResolution(int width, int height)
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, ToolsUnavailableFaultMsg {
		getVimService().setScreenResolution(getMOR(), width, height);
	}

	public void shutdownGuest()
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, ToolsUnavailableFaultMsg {
		getVimService().shutdownGuest(getMOR());
	}

	/**
	 * @throws VmConfigFaultFaultMsg
	 * @throws TaskInProgressFaultMsg
	 * @throws SnapshotFaultFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws RecordReplayDisabledFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws InvalidPowerStateFaultMsg
	 * @throws InvalidNameFaultMsg
	 * @throws HostIncompatibleForRecordReplayFaultMsg
	 * @throws FileFaultFaultMsg
	 * @since SDK4.0
	 */
	public Task startRecordingTask(String name, String description)
			throws FileFaultFaultMsg, HostIncompatibleForRecordReplayFaultMsg, InvalidNameFaultMsg,
			InvalidPowerStateFaultMsg, InvalidStateFaultMsg, RecordReplayDisabledFaultMsg, RuntimeFaultFaultMsg,
			SnapshotFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference mor = getVimService().startRecordingTask(getMOR(), name, description);
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws TaskInProgressFaultMsg
	 * @throws SnapshotFaultFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws InvalidPowerStateFaultMsg
	 * @throws FileFaultFaultMsg
	 * @since SDK4.0
	 */
	public Task stopRecordingTask() throws FileFaultFaultMsg, InvalidPowerStateFaultMsg, InvalidStateFaultMsg,
			RuntimeFaultFaultMsg, SnapshotFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference mor = getVimService().stopRecordingTask(getMOR());
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws TaskInProgressFaultMsg
	 * @throws SnapshotFaultFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws InvalidPowerStateFaultMsg
	 * @throws FileFaultFaultMsg
	 * @since SDK4.0
	 */
	public Task stopReplayingTask() throws FileFaultFaultMsg, InvalidPowerStateFaultMsg, InvalidStateFaultMsg,
			RuntimeFaultFaultMsg, SnapshotFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference mor = getVimService().stopReplayingTask(getMOR());
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws VmConfigFaultFaultMsg
	 * @throws TaskInProgressFaultMsg
	 * @throws SnapshotFaultFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws RecordReplayDisabledFaultMsg
	 * @throws NotFoundFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @throws InvalidPowerStateFaultMsg
	 * @throws HostIncompatibleForRecordReplayFaultMsg
	 * @throws FileFaultFaultMsg
	 * @since SDK4.0
	 */
	public Task startReplayingTask(VirtualMachineSnapshot replaySnapshot)
			throws FileFaultFaultMsg, HostIncompatibleForRecordReplayFaultMsg, InvalidPowerStateFaultMsg,
			InvalidStateFaultMsg, NotFoundFaultMsg, RecordReplayDisabledFaultMsg, RuntimeFaultFaultMsg,
			SnapshotFaultFaultMsg, TaskInProgressFaultMsg, VmConfigFaultFaultMsg {
		ManagedObjectReference mor = getVimService().startReplayingTask(getMOR(), replaySnapshot.getMOR());
		return new Task(getServerConnection(), mor);
	}

	public void standbyGuest()
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, ToolsUnavailableFaultMsg {
		getVimService().standbyGuest(getMOR());
	}

	public Task suspendVMTask() throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference mor = getVimService().suspendVMTask(getMOR());
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws VmFaultToleranceIssueFaultMsg
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since SDK4.0
	 */
	public Task terminateFaultTolerantVMTask(VirtualMachine vm)
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmFaultToleranceIssueFaultMsg {
		ManagedObjectReference mor = getVimService().terminateFaultTolerantVMTask(getMOR(),
				vm == null ? null : vm.getMOR());
		return new Task(getServerConnection(), mor);
	}

	/**
	 * @throws VmFaultToleranceIssueFaultMsg
	 * @throws TaskInProgressFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidStateFaultMsg
	 * @since SDK4.0
	 */
	public Task turnOffFaultToleranceForVMTask()
			throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg, VmFaultToleranceIssueFaultMsg {
		ManagedObjectReference mor = getVimService().turnOffFaultToleranceForVMTask(getMOR());
		return new Task(getServerConnection(), mor);
	}

	public void unmountToolsInstaller() throws InvalidStateFaultMsg, RuntimeFaultFaultMsg, VmConfigFaultFaultMsg {
		getVimService().unmountToolsInstaller(getMOR());
	}

	public void unregisterVM() throws InvalidPowerStateFaultMsg, RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		getVimService().unregisterVM(getMOR());
	}

	public Task upgradeToolsTask(String installerOptions) throws InvalidStateFaultMsg, RuntimeFaultFaultMsg,
			TaskInProgressFaultMsg, ToolsUnavailableFaultMsg, VmConfigFaultFaultMsg, VmToolsUpgradeFaultFaultMsg {
		ManagedObjectReference mor = getVimService().upgradeToolsTask(getMOR(), installerOptions);
		return new Task(getServerConnection(), mor);
	}

	public Task upgradeVMTask(String version) throws AlreadyUpgradedFaultMsg, InvalidStateFaultMsg, NoDiskFoundFaultMsg,
			RuntimeFaultFaultMsg, TaskInProgressFaultMsg {
		ManagedObjectReference mor = getVimService().upgradeVMTask(getMOR(), version);
		return new Task(getServerConnection(), mor);
	}
}

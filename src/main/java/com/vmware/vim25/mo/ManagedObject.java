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

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.InvalidCollectorVersionFaultMsg;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.ObjectUpdate;
import com.vmware.vim25.PropertyChange;
import com.vmware.vim25.PropertyChangeOp;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertyFilterUpdate;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.RetrieveOptions;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.UpdateSet;
import com.vmware.vim25.VimPortType;
import com.vmware.vim25.WaitOptions;
import com.vmware.vim25.mo.util.MorUtil;
import com.vmware.vim25.mo.util.PropertyCollectorUtil;

/**
 * This class is intended to provide a wrapper around a managed object class.
 * The abstraction will hide the web service details and make the managed
 * objects OO style in the client side programming. Every managed object class
 * can inherit from this and take advantage of this abstraction.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 * @param <R>
 */

abstract public class ManagedObject {
	private static String MO_PACKAGE_NAME = null;
	static {
		MO_PACKAGE_NAME = ManagedObject.class.getPackage().getName();
	}

	/** holds the ServerConnection instance */
	private ServerConnection serverConnection = null;
	/** holds the ExtensionManager managed object reference */
	private ManagedObjectReference mor = null;

	protected ManagedObject() {
	}

	/**
	 * Constructor that reuse exiting web service connection Use this contructor
	 * when you can re-use existing web service connection.
	 * 
	 * @param serverConnection
	 * @param mor
	 */
	public ManagedObject(ServerConnection serverConnection, ManagedObjectReference mor) {
		this.serverConnection = serverConnection;
		this.mor = mor;
	}

	/**
	 * Set the ManagedObjectReference object pointing to the managed object
	 */
	protected void setMOR(ManagedObjectReference mor) {
		this.mor = mor;
	}

	/**
	 * get the ManagedObjectReference object pointing to the managed object
	 * 
	 * @return
	 */
	public ManagedObjectReference getMOR() {
		return this.mor;
	}

	/**
	 * Get the web service
	 * 
	 * @return
	 */
	protected VimPortType getVimService() {
		return serverConnection.getVimService();
	}

	public ServerConnection getServerConnection() {
		return serverConnection;
	}

	/**
	 * Set up the ServerConnection, only when it hasn't been set yet.
	 * 
	 * @param sc
	 */
	protected void setServerConnection(ServerConnection sc) {
		if (this.serverConnection == null) {
			this.serverConnection = sc;
		}
	}

	@SuppressWarnings("deprecation")
	protected ObjectContent retrieveObjectProperties(List<String> properties, RetrieveOptions options) {
		ObjectSpec oSpec = PropertyCollectorUtil.creatObjectSpec(getMOR(), Boolean.FALSE, null);

		PropertySpec pSpec = PropertyCollectorUtil.createPropertySpec(getMOR().getType(),
				properties == null || properties.size() == 0, // if true, all props of this obj are to be read
																// regardless of propName
				properties);

		PropertyFilterSpec pfSpec = new PropertyFilterSpec();
		pfSpec.getObjectSet().add(oSpec);
		pfSpec.getPropSet().add(pSpec);

		PropertyCollector pc = getServerConnection().getServiceInstance().getPropertyCollector();

		List<ObjectContent> objs;
		try {
			if (options != null)
				objs = pc.retrievePropertiesEx(Arrays.asList(pfSpec), options).getObjects();
			else
				objs = pc.retrieveProperties(Arrays.asList(pfSpec));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (objs == null || objs.isEmpty())
			return null;
		else
			return objs.get(0);
	}

	protected Object getCurrentProperty(String propertyName) {
		return getCurrentProperty(propertyName, null);
	}

	/**
	 * @param propertyName The property name of current managed object
	 * @return it will return either an array of related data objects, or an data
	 *         object itself. ManagedObjectReference objects are data objects!!!
	 * @throws RemoteException
	 * @throws RuntimeFault
	 * @throws InvalidProperty @
	 */
	protected Object getCurrentProperty(String propertyName, RetrieveOptions options) {
		ObjectContent objContent = retrieveObjectProperties(Arrays.asList(propertyName), options);

		Object propertyValue = null;

		if (objContent != null) {
			List<DynamicProperty> dynaProps = objContent.getPropSet();

			if ((dynaProps != null) && (!dynaProps.isEmpty())) {
				propertyValue = PropertyCollectorUtil.convertProperty(dynaProps.get(0).getVal());
			}
		}
		return propertyValue;
	}

	public Object getPropertyByPath(String propPath) {
		return getCurrentProperty(propPath);
	}

	/**
	 * Get multiple properties by their paths
	 * 
	 * @param propPaths an array of strings for property path
	 * @return a Hashtable holding with the property path as key, and the value.
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidPropertyFaultMsg
	 * @throws InvalidProperty
	 * @throws RuntimeFault
	 * @throws RemoteException
	 */
	public Hashtable<String, Object> getPropertiesByPaths(List<String> propPaths)
			throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
		return getPropertiesByPaths(propPaths, null);
	}

	/**
	 * Get multiple properties by their paths
	 * 
	 * @param propPaths an array of strings for property path
	 * @return a Hashtable holding with the property path as key, and the value.
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidPropertyFaultMsg
	 * @throws InvalidProperty
	 * @throws RuntimeFault
	 * @throws RemoteException
	 */
	public Hashtable<String, Object> getPropertiesByPaths(List<String> propPaths, RetrieveOptions options)
			throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
		Hashtable<String, Object>[] pht = PropertyCollectorUtil.retrieveProperties(Arrays.asList(this),
				getMOR().getType(), propPaths, options);
		if (pht.length != 0)
			return pht[0];
		else
			return null;
	}
	
	/**
	 * Get All properties
	 * @return a Hashtable holding with the property as key, and the value.
	 * @throws InvalidPropertyFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 */
	public Hashtable<String, Object> getProperties()
			throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
		Hashtable<String, Object>[] pht = PropertyCollectorUtil.retrieveProperties(Arrays.asList(this),
				getMOR().getType(), null, null);
		if (pht.length != 0)
			return pht[0];
		else
			return null;
	}
	/**
	 * Get All properties
	 * @param options RetrieveOptions set for the operation
	 * @return a Hashtable holding with the property as key, and the value.
	 * @throws InvalidPropertyFaultMsg
	 * @throws RuntimeFaultFaultMsg
	 */
	public Hashtable<String, Object> getProperties(RetrieveOptions options)
			throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
		Hashtable<String, Object>[] pht = PropertyCollectorUtil.retrieveProperties(Arrays.asList(this),
				getMOR().getType(), null, options);
		if (pht.length != 0)
			return pht[0];
		else
			return null;
	}

	protected List<ManagedObject> getManagedObjects(String propName, boolean mixedType) {
		Object object = getCurrentProperty(propName);
		ManagedObjectReference[] mors = null;
		if (object instanceof ManagedObjectReference[]) {
			mors = (ManagedObjectReference[]) object;
		}

		if (mors == null || mors.length == 0) {
			return new ArrayList<ManagedObject>();
		}

		Object mos = new ManagedObject[mors.length];

		try {
			Class<?> moClass = null;

			if (mixedType == false) {
				moClass = Class.forName(MO_PACKAGE_NAME + "." + mors[0].getType());
				mos = Array.newInstance(moClass, mors.length);
			}

			for (int i = 0; i < mors.length; i++) {
				if (mixedType == true) {
					moClass = Class.forName(MO_PACKAGE_NAME + "." + mors[i].getType());
				}
				Constructor<?> constructor = moClass
						.getConstructor(new Class[] { ServerConnection.class, ManagedObjectReference.class });

				Array.set(mos, i, constructor.newInstance(new Object[] { getServerConnection(), mors[i] }));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<ManagedObject> imos = new ArrayList<ManagedObject>();
		for (Object id : (Object[]) mos) {
			imos.add((ManagedObject) id);
		}
		return imos;
	}

	protected List<ManagedObject> getManagedObjects(String propName) {
		return getManagedObjects(propName, false);
	}

	protected List<Datastore> getDatastores(String propName) {
		List<ManagedObject> objs = getManagedObjects(propName);
		if (objs.isEmpty()) {
			return new ArrayList<Datastore>();
		}
		return MorUtil.convertArraytoList(objs);
	}

	protected List<Network> getNetworks(String propName) {
		List<ManagedObject> objs = getManagedObjects(propName, true);
		if (objs.isEmpty()) {
			return new ArrayList<Network>();
		}
		List<Network> nets = new ArrayList<Network>();
		for (int i = 0; i < objs.size(); i++) {
			nets.add((Network) objs.get(i));
		}
		return nets;
	}

	protected List<VirtualMachine> getVms(String propName) {
		List<ManagedObject> objs = getManagedObjects(propName);
		if (objs.isEmpty()) {
			return new ArrayList<VirtualMachine>();
		}
		return MorUtil.convertArraytoList(objs);
	}

	protected List<PropertyFilter> getFilter(String propName) {
		List<ManagedObject> objs = getManagedObjects(propName);
		if (objs.isEmpty()) {
			return new ArrayList<PropertyFilter>();
		}
		return MorUtil.convertArraytoList(objs);
	}

	protected List<ResourcePool> getResourcePools(String propName) {
		List<ManagedObject> objs = getManagedObjects(propName, true);
		List<ResourcePool> rps = new ArrayList<ResourcePool>();
		for (int i = 0; i < rps.size(); i++) {
			rps.add((ResourcePool) objs.get(i));
		}
		return rps;
	}

	protected List<Task> getTasks(String propName) {
		List<ManagedObject> objs = getManagedObjects(propName);
		if (objs.isEmpty()) {
			return new ArrayList<Task>();
		}
		return MorUtil.convertArraytoList(objs);
	}

	protected List<ScheduledTask> getScheduledTasks(String propName) {
		List<ManagedObject> objs = getManagedObjects(propName);
		if (objs.isEmpty()) {
			return new ArrayList<ScheduledTask>();
		}
		return MorUtil.convertArraytoList(objs);
	}

	protected List<View> getViews(String propName) {
		List<ManagedObject> objs = getManagedObjects(propName);
		if (objs.isEmpty()) {
			return new ArrayList<View>();
		}
		return MorUtil.convertArraytoList(objs);
	}

	protected List<HostSystem> getHosts(String propName) {
		List<ManagedObject> objs = getManagedObjects(propName);
		if (objs.isEmpty()) {
			return new ArrayList<HostSystem>();
		}
		return MorUtil.convertArraytoList(objs);
	}

	protected ManagedObject getManagedObject(String propName) {
		ManagedObjectReference mor = (ManagedObjectReference) getCurrentProperty(propName);
		return MorUtil.createExactManagedObject(getServerConnection(), mor);
	}

	/**
	 * Handle Updates for a single object. waits till expected values of properties
	 * to check are reached Destroys the ObjectFilter when done.
	 * 
	 * @param filterProps  Properties list to filter
	 * @param endWaitProps Properties list to check for expected values these be
	 *                     properties of a property in the filter properties list
	 * @param expectedVals values for properties to end the wait
	 * @return true indicating expected values were met, and false otherwise
	 * @throws InvalidPropertyFaultMsg
	 * @throws RemoteException
	 * @throws RuntimeFault
	 * @throws InvalidProperty
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidCollectorVersionFaultMsg
	 */
	protected Object[] waitForValues(List<String> filterProps, List<String> endWaitProps, Object[][] expectedVals)
			throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {
		return waitForValues(filterProps, endWaitProps, expectedVals, null);
	}

	/**
	 * Handle Updates for a single object. waits till expected values of properties
	 * to check are reached Destroys the ObjectFilter when done.
	 * 
	 * @param filterProps  Properties list to filter
	 * @param endWaitProps Properties list to check for expected values these be
	 *                     properties of a property in the filter properties list
	 * @param expectedVals values for properties to end the wait
	 * @return true indicating expected values were met, and false otherwise
	 * @throws InvalidPropertyFaultMsg
	 * @throws RemoteException
	 * @throws RuntimeFault
	 * @throws InvalidProperty
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidCollectorVersionFaultMsg
	 */
	protected Object[] waitForValues(List<String> filterProps, List<String> endWaitProps, Object[][] expectedVals,
			WaitOptions waitOptions)
			throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {
		String version = "";
		Object[] endVals = new Object[endWaitProps.size()];
		Object[] filterVals = new Object[filterProps.size()];

		ObjectSpec oSpec = PropertyCollectorUtil.creatObjectSpec(getMOR(), Boolean.FALSE, null);

		PropertySpec pSpec = PropertyCollectorUtil.createPropertySpec(getMOR().getType(),
				filterProps == null || filterProps.size() == 0, // if true, all props of this obj are to be read
																// regardless of propName
				filterProps);

		PropertyFilterSpec spec = new PropertyFilterSpec();
		spec.getObjectSet().add(oSpec);
		spec.getPropSet().add(pSpec);

		PropertyCollector pc = serverConnection.getServiceInstance().getPropertyCollector();
		PropertyFilter pf = pc.createFilter(spec, true);

		boolean reached = false;

		while (!reached) {
			UpdateSet updateset;
			if (waitOptions != null)
				updateset = pc.waitForUpdatesEx(version, waitOptions);
			else
				updateset = pc.waitForUpdates(version);
			if (updateset == null) {
				continue;
			}
			version = updateset.getVersion();
			List<PropertyFilterUpdate> filtupary = updateset.getFilterSet();
			if (filtupary == null) {
				continue;
			}

			// Make this code more general purpose when PropCol changes later.
			for (int i = 0; i < filtupary.size(); i++) {
				PropertyFilterUpdate filtup = filtupary.get(i);
				if (filtup == null) {
					continue;
				}
				List<ObjectUpdate> objupary = filtup.getObjectSet();
				for (int j = 0; objupary != null && j < objupary.size(); j++) {
					ObjectUpdate objup = objupary.get(j);
					if (objup == null) {
						continue;
					}
					List<PropertyChange> propchgary = objup.getChangeSet();
					for (int k = 0; propchgary != null && k < propchgary.size(); k++) {
						PropertyChange propchg = propchgary.get(k);
						updateValues(endWaitProps, endVals, propchg);
						updateValues(filterProps, filterVals, propchg);
					}
				}
			}

			// Check if the expected values have been reached and exit the loop if done.
			// Also exit the WaitForUpdates loop if this is the case.
			for (int chgi = 0; chgi < endVals.length && !reached; chgi++) {
				for (int vali = 0; vali < expectedVals[chgi].length && !reached; vali++) {
					Object expctdval = expectedVals[chgi][vali];
					reached = expctdval.equals(endVals[chgi]) || reached;
				}
			}
		}

		pf.destroyPropertyFilter();

		return filterVals;
	}

	private void updateValues(List<String> props, Object[] vals, PropertyChange propchg) {
		for (int i = 0; i < props.size(); i++) {
			if (propchg.getName().lastIndexOf(props.get(i)) >= 0) {
				if (propchg.getOp() == PropertyChangeOp.REMOVE) {
					vals[i] = "";
				} else {
					vals[i] = propchg.getVal();
				}
			}
		}
	}

	public String toString() {
		return mor.getType() + ":" + mor.getValue() + " @ " + getServerConnection().getUrl();
	}

	protected List<ManagedObjectReference> convertMors(List<ManagedObject> mos) {
		List<ManagedObjectReference> mors = null;
		if (mos != null) {
			mors = MorUtil.createMORs(mos);
		}
		return mors;
	}

}

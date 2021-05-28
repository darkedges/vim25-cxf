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

package com.vmware.vim25.mo.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.RetrieveOptions;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.SelectionSpec;
import com.vmware.vim25.TraversalSpec;
import com.vmware.vim25.mo.ManagedObject;
import com.vmware.vim25.mo.PropertyCollector;

/**
 * Utility class for the PropertyCollector API.
 * 
 * @author Steve JIN (sjin@vmware.com)
 */
public class PropertyCollectorUtil {
	final public static Object NULL = new Object();

	/**
	 * Retrieves properties from multiple managed objects.
	 * 
	 * @param mos       the array of managed objects which could be of single type
	 *                  or mixed types. When they are mix-typed, the moType must be
	 *                  super type of all these managed objects.
	 * @param moType    the type of the managed object. This managed object type
	 *                  must have all the properties defined as in propPaths.
	 * @param propPaths the array of property path which has dot as separator, for
	 *                  example, "name", "guest.toolsStatus".
	 * @return an array of Hashtable whose order is the same as the mos array. Each
	 *         Hashtable has the properties for one managed object. Note: some of
	 *         the properties you want to retrieve might not be set, and therefore
	 *         you don't have an entry in the Hashtable at all. In other words, it's
	 *         possible for you to get null for a property from the resulted
	 *         Hashtable.
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidPropertyFaultMsg
	 * @throws InvalidProperty
	 * @throws
	 * @throws
	 */
	public static Hashtable<String,Object>[] retrieveProperties(List<? extends ManagedObject> mos, String moType, List<String> propPaths)
			throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
		return retrieveProperties(mos, moType, propPaths, null);
	}

	/**
	 * Retrieves properties from multiple managed objects.
	 * 
	 * @param mos       the array of managed objects which could be of single type
	 *                  or mixed types. When they are mix-typed, the moType must be
	 *                  super type of all these managed objects.
	 * @param moType    the type of the managed object. This managed object type
	 *                  must have all the properties defined as in propPaths.
	 * @param propPaths the array of property path which has dot as separator, for
	 *                  example, "name", "guest.toolsStatus".
	 * @param options   RetrieveOptions
	 * @return an array of Hashtable whose order is the same as the mos array. Each
	 *         Hashtable has the properties for one managed object. Note: some of
	 *         the properties you want to retrieve might not be set, and therefore
	 *         you don't have an entry in the Hashtable at all. In other words, it's
	 *         possible for you to get null for a property from the resulted
	 *         Hashtable.
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidPropertyFaultMsg
	 * @throws InvalidProperty
	 * @throws
	 * @throws
	 */
	public static Hashtable<String,Object>[] retrieveProperties(List<? extends ManagedObject> mos, String moType, List<String> propPaths,
			RetrieveOptions options) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
		if (mos == null)
			throw new IllegalArgumentException("Managed object array cannot be null.");
		if (mos.isEmpty())
			return new Hashtable[] {};

		PropertyCollector pc = mos.get(0).getServerConnection().getServiceInstance().getPropertyCollector();
		List<ObjectSpec> oss = new ArrayList<ObjectSpec>();
		for (int i = 0; i < mos.size(); i++) {
			oss.add(new ObjectSpec());
			oss.get(i).setObj(mos.get(i).getMOR());
		}

		PropertySpec pSpec = createPropertySpec(moType, false, propPaths);

		PropertyFilterSpec pfs = new PropertyFilterSpec();
		pfs.getObjectSet().addAll(oss);
		pfs.getPropSet().addAll(Arrays.asList(pSpec));
		List<ObjectContent> objs;
		if (options != null) {
			objs = pc.retrievePropertiesEx(Arrays.asList(pfs), options).getObjects();
		} else {
			objs = pc.retrieveProperties(Arrays.asList(pfs));
		}

		Hashtable<String,Object>[] pTables = new Hashtable[mos.size()];

		for (int i = 0; objs != null && i < objs.size() && !objs.isEmpty(); i++) {
			List<DynamicProperty> props = objs.get(i).getPropSet();
			ManagedObjectReference mor = objs.get(i).getObj();

			int index = -1;
			if (mor.getType().equals(mos.get(i).getMOR().getType())
					&& mor.getValue().equals(mos.get(i).getMOR().getValue())) {
				index = i;
			} else {
				index = findIndex(mos, mor);
				if (index == -1)
					throw new RuntimeException(
							"Unexpected managed object in result: " + mor.getType() + ":" + mor.getValue());
			}
			pTables[index] = new Hashtable<String,Object>();
			for (int j = 0; props != null && j < props.size(); j++) {
				Object obj = convertProperty(props.get(j).getVal());
				if (obj == null) {
					obj = NULL;
				}
				pTables[index].put(props.get(j).getName(), obj);
			}
		}
		return pTables;
	}

	private static int findIndex(List<? extends ManagedObject> mos, ManagedObjectReference mor) {
		for (int i = 0; i < mos.size(); i++) {
			if (mor.getType().equals(mos.get(i).getMOR().getType())
					&& mor.getValue().equals(mos.get(i).getMOR().getValue()))
				return i;
		}
		return -1;
	}

	public static Object convertProperty(Object dynaPropVal) {
		Object propertyValue = null;

		Class propClass = dynaPropVal.getClass();
		String propName = propClass.getName();
		if (propName.indexOf("ArrayOf") != -1) // Check the dynamic propery for ArrayOfXXX object
		{
			String methodName = propName.substring(propName.indexOf("ArrayOf") + "ArrayOf".length());
//			 If object is ArrayOfXXX object, then get the XXX[] by invoking getXXX() on the object. For Ex:
//			 ArrayOfManagedObjectReference.getManagedObjectReference() returns List<ManagedObjectReference> array.
			try {
				Method getMethod = null;
				try {
					getMethod = propClass.getMethod("get" + methodName, (Class[]) null);
				} catch (NoSuchMethodException nsme) {
					getMethod = propClass.getMethod("get_" + methodName.toLowerCase(), (Class[]) null);
				}
				propertyValue = getMethod.invoke(dynaPropVal, (Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (dynaPropVal.getClass().isArray()) // Handle the case of an unwrapped array being deserialized.
		{
			propertyValue = dynaPropVal;
		} else {
			propertyValue = dynaPropVal;
		}

		return propertyValue;
	}

	public static ObjectSpec creatObjectSpec(ManagedObjectReference mor, boolean skip, List<SelectionSpec> selSet) {
		ObjectSpec oSpec = new ObjectSpec();
		oSpec.setObj(mor);
		oSpec.setSkip(skip);
		if (selSet != null && !selSet.isEmpty())
			oSpec.getSelectSet().addAll(selSet);
		return oSpec;
	}

	public static PropertySpec createPropertySpec(String type, boolean allProp, List<String> pathSet) {
		PropertySpec pSpec = new PropertySpec();
		pSpec.setType(type);
		pSpec.setAll(allProp); // whether or not all properties of the object are read. If this property is set
								// to true, the pathSet property is ignored.
		if (pathSet != null && !pathSet.isEmpty())
			pSpec.getPathSet().addAll(pathSet);
		return pSpec;
	}

	public static List<SelectionSpec> createSelectionSpec(String[] names) {
		List<SelectionSpec> sss = new ArrayList<SelectionSpec>();
		for (int i = 0; i < names.length; i++) {
			sss.add(new SelectionSpec());
			sss.get(i).setName(names[i]);
		}
		return sss;
	}

	public static TraversalSpec createTraversalSpec(String name, String type, String path, String[] selectPath,
			boolean skipEnable) {
		return createTraversalSpec(name, type, path, createSelectionSpec(selectPath), skipEnable);
	}

	public static TraversalSpec createTraversalSpec(String name, String type, String path, String[] selectPath) {
		return createTraversalSpec(name, type, path, createSelectionSpec(selectPath), true);
	}

	public static TraversalSpec createTraversalSpec(String name, String type, String path,
			List<SelectionSpec> selectSet) {
		return createTraversalSpec(name, type, path, selectSet, true);
	}

	public static TraversalSpec createTraversalSpec(String name, String type, String path,
			List<SelectionSpec> selectSet, boolean enableSkip) {
		TraversalSpec ts = new TraversalSpec();
		ts.setName(name);
		ts.setType(type);
		ts.setPath(path);
		if (enableSkip)
			ts.setSkip(Boolean.FALSE);
		if (!selectSet.isEmpty())
			ts.getSelectSet().addAll(selectSet);
		return ts;
	}

	/**
	 * This code takes an array of [typename, property, property, ...] and converts
	 * it into a PropertySpec[].
	 * 
	 * @param typeProplists 2D array of type and properties to retrieve
	 * @return Array of container filter specs
	 */
	public static List<PropertySpec> buildPropertySpecArray(String[][] typeProplists) {
		PropertySpec[] pSpecs = new PropertySpec[typeProplists.length];

		for (int i = 0; i < typeProplists.length; i++) {
			String type = typeProplists[i][0];
			List<String> props = new ArrayList<String>();
			for (int j = 0; j < typeProplists[i].length - 1; j++) {
				props.add(typeProplists[i][j + 1]);
			}

			boolean all = (props.isEmpty()) ? true : false;
			pSpecs[i] = createPropertySpec(type, all, props);
		}
		return Arrays.asList(pSpecs);
	}

	/**
	 * This method creates a SelectionSpec[] to traverses the entire inventory tree
	 * starting at a Folder NOTE: This full traversal is based on VC2/ESX3 inventory
	 * structure. It does not search new ManagedEntities like Network, DVS, etc. If
	 * you want a full traversal with VC4/ESX4, use buildFullTraversalV4().
	 * 
	 * @return The SelectionSpec[]
	 */
	public static List<SelectionSpec> buildFullTraversal() {
		List<TraversalSpec> tSpecs = buildFullTraversalV2NoFolder();

		// Recurse through the folders
		TraversalSpec VisitFolders = createTraversalSpec("VisitFolders", "Folder", "childEntity",
				new String[] { "VisitFolders", "dcToHf", "dcToVmf", "crToH", "crToRp", "HToVm", "rpToVm" });

		SelectionSpec[] sSpecs = new SelectionSpec[tSpecs.size() + 1];
		sSpecs[0] = VisitFolders;
		for (int i = 1; i < sSpecs.length; i++)
			sSpecs[i] = tSpecs.get(i - 1);

		return Arrays.asList(sSpecs);
	}

	/**
	 * This method creates basic set of TraveralSpec without VisitFolders spec
	 * 
	 * @return The TraversalSpec[]
	 */
	private static List<TraversalSpec> buildFullTraversalV2NoFolder() {
		// Recurse through all ResourcePools
		TraversalSpec rpToRp = createTraversalSpec("rpToRp", "ResourcePool", "resourcePool",
				new String[] { "rpToRp", "rpToVm" });

		// Recurse through all ResourcePools
		TraversalSpec rpToVm = createTraversalSpec("rpToVm", "ResourcePool", "vm", new ArrayList<SelectionSpec>());

		// Traversal through ResourcePool branch
		TraversalSpec crToRp = createTraversalSpec("crToRp", "ComputeResource", "resourcePool",
				new String[] { "rpToRp" });

		// Traversal through host branch
		TraversalSpec crToH = createTraversalSpec("crToH", "ComputeResource", "host", new ArrayList<SelectionSpec>());

		// Traversal through hostFolder branch
		TraversalSpec dcToHf = createTraversalSpec("dcToHf", "Datacenter", "hostFolder",
				new String[] { "VisitFolders" });

		// Traversal through vmFolder branch
		TraversalSpec dcToVmf = createTraversalSpec("dcToVmf", "Datacenter", "vmFolder",
				new String[] { "VisitFolders" });

		TraversalSpec HToVm = createTraversalSpec("HToVm", "HostSystem", "vm", new String[] { "VisitFolders" });

		return Arrays.asList(crToRp, crToH, dcToVmf, dcToHf, rpToRp, HToVm, rpToVm);
	}

	/**
	 * This method creates a SelectionSpec[] to traverses the entire inventory tree
	 * starting at a Folder
	 * 
	 * @return The SelectionSpec[]
	 */
	public static List<SelectionSpec> buildFullTraversalV4() {
		List<TraversalSpec> tSpecs = buildFullTraversalV2NoFolder();

		TraversalSpec dcToDs = createTraversalSpec("dcToDs", "Datacenter", "datastoreFolder",
				new String[] { "visitFolders" });

		TraversalSpec vAppToRp = createTraversalSpec("vAppToRp", "VirtualApp", "resourcePool",
				new String[] { "rpToRp", "vAppToRp" });

		/**
		 * Copyright 2009 Altor Networks, contribution by Elsa Bignoli
		 * 
		 * @author Elsa Bignoli (elsa@altornetworks.com)
		 */
		// Traversal through netFolder branch
		TraversalSpec dcToNetf = createTraversalSpec("dcToNetf", "Datacenter", "networkFolder",
				new String[] { "visitFolders" });

		// Recurse through the folders
		TraversalSpec visitFolders = createTraversalSpec("visitFolders", "Folder", "childEntity", new String[] {
				"visitFolders", "dcToHf", "dcToVmf", "dcToDs", "dcToNetf", "crToH", "crToRp", "HToVm", "rpToVm" });

		List<SelectionSpec> sSpecs = new ArrayList<SelectionSpec>();
		sSpecs.add(visitFolders);
		sSpecs.add(dcToDs);
		sSpecs.add(dcToNetf);
		sSpecs.add(vAppToRp);
		for (int i = 0; i < 4; i++)
			sSpecs.add(tSpecs.get(i));

		return sSpecs;
	}

	/**
	 * This method creates a SelectionSpec[] to traverses the entire inventory tree
	 * starting at a Folder
	 * 
	 * @return The SelectionSpec[]
	 */
	public static List<SelectionSpec> buildFullTraversalV7() {
		List<TraversalSpec> tSpecs = buildFullTraversalV2NoFolder();

		TraversalSpec dcToDs = createTraversalSpec("dcToDs", "Datacenter", "datastore", new String[] {});

		TraversalSpec vAppToRp = createTraversalSpec("vAppToRp", "VirtualApp", "resourcePool",
				new String[] { "rpToRp" }, false);

		TraversalSpec vAppToVm = createTraversalSpec("vAppToVM", "VirtualApp", "vm", new String[] {}, false);

		/**
		 * Copyright 2009 Altor Networks, contribution by Elsa Bignoli
		 * 
		 * @author Elsa Bignoli (elsa@altornetworks.com)
		 */
		// Traversal through netFolder branch
		TraversalSpec dcToNetf = createTraversalSpec("dcToNetwork", "Datacenter", "network", new String[] {});

		// Recurse through the folders
		TraversalSpec visitFolders = createTraversalSpec("VisitFolders", "Folder", "childEntity", new String[] {
				"crToRp", "crToH", "dcToVmf", "dcToHf", "vAppToRp", "vAppToVM", "dcToDs", "rpToVm", "VisitFolders" });

		TraversalSpec rpToRp = createTraversalSpec("rpToRp", "ResourcePool", "resourcePool", new String[] { "rpToRp" });
		TraversalSpec rpToVm = createTraversalSpec("rpToVm", "ResourcePool", "vm", new String[] {});

		List<SelectionSpec> sSpecs = new ArrayList<SelectionSpec>();
		sSpecs.add(visitFolders);
		for (int i = 0; i < 4; i++)
			sSpecs.add(tSpecs.get(i));
		sSpecs.add(vAppToRp);
		sSpecs.add(vAppToVm);
		sSpecs.add(dcToDs);
		sSpecs.add(rpToVm);
		sSpecs.add(rpToRp);
		sSpecs.add(dcToNetf);

		return sSpecs;
	}

}
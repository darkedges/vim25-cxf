package com.vmware.vim25.mo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vmware.vim25.AboutInfo;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.InvalidPropertyFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.RetrieveOptions;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.SelectionSpec;
import com.vmware.vim25.mo.util.MorUtil;
import com.vmware.vim25.mo.util.PropertyCollectorUtil;

public class InventoryNavigator {
	private ManagedEntity rootEntity = null;
	private List<SelectionSpec> selectionSpecs = null;

	public InventoryNavigator(ManagedEntity rootEntity) {
		this.rootEntity = rootEntity;
	}

	/**
	 * Retrieve container contents from specified parent recursively if requested.
	 * 
	 * @param recurse retrieve contents recursively from the root down
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidPropertyFaultMsg
	 * @throws RemoteException
	 * @throws RuntimeFault
	 * @throws InvalidProperty
	 */
	public List<ManagedEntity> searchManagedEntities(boolean recurse)
			throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
		String[][] typeinfo = new String[][] { new String[] { "ManagedEntity", } };
		return searchManagedEntities(typeinfo, recurse);
	}

	/**
	 * Get the first ManagedObjectReference from current node for the specified type
	 * 
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidPropertyFaultMsg
	 */
	public List<ManagedEntity> searchManagedEntities(String type) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
		String[][] typeinfo = new String[][] { new String[] { type, "name", }, };
		return searchManagedEntities(typeinfo, true);
	}

	/**
	 * Retrieve content recursively with multiple properties. the typeinfo array
	 * contains typename + properties to retrieve.
	 *
	 * @param typeinfo 2D array of properties for each typename
	 * @param recurse  retrieve contents recursively from the root down
	 *
	 * @return retrieved object contents
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidPropertyFaultMsg
	 * @throws RemoteException
	 * @throws RuntimeFault
	 * @throws InvalidProperty
	 */
	public List<ManagedEntity> searchManagedEntities(String[][] typeinfo, boolean recurse)
			throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
		return searchManagedEntities(typeinfo, recurse, null);
	}

	/**
	 * Retrieve content recursively with multiple properties. the typeinfo array
	 * contains typename + properties to retrieve.
	 *
	 * @param typeinfo 2D array of properties for each typename
	 * @param recurse  retrieve contents recursively from the root down
	 *
	 * @return retrieved object contents
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidPropertyFaultMsg
	 * @throws RemoteException
	 * @throws RuntimeFault
	 * @throws InvalidProperty
	 */
	public List<ManagedEntity> searchManagedEntities(String[][] typeinfo, boolean recurse, RetrieveOptions options)
			throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
		List<ObjectContent> ocs = retrieveObjectContents(typeinfo, recurse, options);
		return createManagedEntities(ocs);
	}

	private List<ObjectContent> retrieveObjectContents(String[][] typeinfo, boolean recurse, RetrieveOptions options)
			throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
		if (typeinfo == null || typeinfo.length == 0) {
			return null;
		}

		PropertyCollector pc = rootEntity.getServerConnection().getServiceInstance().getPropertyCollector();

		if (recurse && selectionSpecs == null) {
			AboutInfo ai = rootEntity.getServerConnection().getServiceInstance().getAboutInfo();

			/*
			 * The apiVersion values in all the shipped products "2.0.0" VI 3.0 "2.5.0" VI
			 * 3.5 (and u1) "2.5u2" VI 3.5u2 (and u3, u4) "4.0" vSphere 4.0 (and u1) "4.1"
			 * vSphere 4.1 "5.0" vSphere 5.0
			 ******************************************************/
			if (ai.getApiVersion().startsWith("7")) {
				selectionSpecs = PropertyCollectorUtil.buildFullTraversalV7();
				if (options == null)
					options = new RetrieveOptions();
			} else {
				if (ai.getApiVersion().startsWith("4") || ai.getApiVersion().startsWith("5")
						|| ai.getApiVersion().startsWith("6")) {
					selectionSpecs = PropertyCollectorUtil.buildFullTraversalV4();
				} else {
					selectionSpecs = PropertyCollectorUtil.buildFullTraversal();
				}
			}
		}

		List<PropertySpec> propspecary = PropertyCollectorUtil.buildPropertySpecArray(typeinfo);

		ObjectSpec os = new ObjectSpec();
		os.setObj(rootEntity.getMOR());
		os.setSkip(Boolean.FALSE);
		os.getSelectSet().addAll(selectionSpecs);

		PropertyFilterSpec spec = new PropertyFilterSpec();
		spec.getObjectSet().addAll(Arrays.asList(os));
		spec.getPropSet().addAll(propspecary);

		// need to check api version and choose the appropriate function
		if (options != null)
			return pc.retrievePropertiesEx(Arrays.asList(spec), options).getObjects();
		else
			return pc.retrieveProperties(Arrays.asList(spec));
	}

	private List<ManagedEntity> createManagedEntities(List<ObjectContent> ocs) {
		if (ocs == null) {
			return new ArrayList<ManagedEntity>();
		}
		List<ManagedEntity> mes = new ArrayList<ManagedEntity>();

		for (int i = 0; i < ocs.size(); i++) {
			ManagedObjectReference mor = ocs.get(i).getObj();
			mes.add(MorUtil.createExactManagedEntity(rootEntity.getServerConnection(), mor));
		}
		return mes;
	}

	/**
	 * Get the ManagedObjectReference for an item under the specified parent node
	 * that has the type and name specified.
	 *
	 * @param type type of the managed object
	 * @param name name to match
	 * @return First ManagedEntity object of the type / name pair found
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidPropertyFaultMsg
	 * @throws RemoteException
	 * @throws RuntimeFault
	 * @throws InvalidProperty
	 */
	public ManagedEntity searchManagedEntity(String type, String name)
			throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
		return searchManagedEntity(type, name, null);
	}

	/**
	 * Get the ManagedObjectReference for an item under the specified parent node
	 * that has the type and name specified.
	 *
	 * @param type type of the managed object
	 * @param name name to match
	 * @return First ManagedEntity object of the type / name pair found
	 * @throws RuntimeFaultFaultMsg
	 * @throws InvalidPropertyFaultMsg
	 * @throws RemoteException
	 * @throws RuntimeFault
	 * @throws InvalidProperty
	 */
	public ManagedEntity searchManagedEntity(String type, String name, RetrieveOptions options)
			throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
		if (name == null || name.length() == 0) {
			return null;
		}

		if (type == null) {
			type = "ManagedEntity";
		}

		String[][] typeinfo = new String[][] { new String[] { type, "name", }, };

		List<ObjectContent> ocs = retrieveObjectContents(typeinfo, true, options);

		if (ocs == null || ocs.isEmpty()) {
			return null;
		}

		for (int i = 0; i < ocs.size(); i++) {
			List<DynamicProperty> propSet = ocs.get(i).getPropSet();

			if (propSet.size() > 0) {
				String nameInPropSet = (String) propSet.get(i).getVal();
				if (name.equalsIgnoreCase(nameInPropSet)) {
					ManagedObjectReference mor = ocs.get(i).getObj();
					return MorUtil.createExactManagedEntity(rootEntity.getServerConnection(), mor);
				}
			}
		}
		return null;
	}

}

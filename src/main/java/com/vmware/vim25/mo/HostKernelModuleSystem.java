package com.vmware.vim25.mo;

import java.util.List;

import com.vmware.vim25.KernelModuleInfo;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NotFoundFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;

public class HostKernelModuleSystem extends ManagedObject 
{
	public HostKernelModuleSystem(ServerConnection sc, ManagedObjectReference mor) 
	{
		super(sc, mor);
	}
	
	public String queryConfiguredModuleOptionString(String name) throws NotFoundFaultMsg, RuntimeFaultFaultMsg 
	{
		return getVimService().queryConfiguredModuleOptionString(getMOR(), name);
	}
	
	public List<KernelModuleInfo> queryModules() throws RuntimeFaultFaultMsg 
	{
		return getVimService().queryModules(getMOR());
	}
	
	public void updateModuleOptionString(String name, String options) throws NotFoundFaultMsg, RuntimeFaultFaultMsg 
	{
		getVimService().updateModuleOptionString(getMOR(), name, options);
	}

}

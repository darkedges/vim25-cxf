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

import com.vmware.vim25.*;

/**
 * The managed object class corresponding to the one defined in VI SDK API reference.
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class CustomizationSpecManager extends ManagedObject 
{

	public CustomizationSpecManager(ServerConnection sc, ManagedObjectReference mor) 
	{
		super(sc, mor);
	}
	
	public byte[] getEncryptionKey()
	{
		return (byte[]) getCurrentProperty("encryptionKey");
	}
	
	public CustomizationSpecInfo[] getInfo()
	{
		return (CustomizationSpecInfo[]) getCurrentProperty("info");
	}
	
	public void checkCustomizationResources(String guestOs) throws CustomizationFaultFaultMsg, RuntimeFaultFaultMsg   
	{
		getVimService().checkCustomizationResources(getMOR(), guestOs);
	}
	
	public void createCustomizationSpec(CustomizationSpecItem item) throws AlreadyExistsFaultMsg, CustomizationFaultFaultMsg, RuntimeFaultFaultMsg   
	{
		getVimService().createCustomizationSpec(getMOR(), item);
	}
	
	public String customizationSpecItemToXml(CustomizationSpecItem item) throws RuntimeFaultFaultMsg  
	{
		return getVimService().customizationSpecItemToXml(getMOR(), item);
	}
	
	public void deleteCustomizationSpec(String name) throws NotFoundFaultMsg, RuntimeFaultFaultMsg   
	{
		getVimService().deleteCustomizationSpec(getMOR(), name);
	}
	
	public boolean doesCustomizationSpecExist(String name) throws RuntimeFaultFaultMsg  
	{
		return getVimService().doesCustomizationSpecExist(getMOR(), name);
	}
	
	public void duplicateCustomizationSpec(String name, String newName) throws AlreadyExistsFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg   
	{
		getVimService().duplicateCustomizationSpec(getMOR(), name, newName);
	}
	
	public CustomizationSpecItem getCustomizationSpec(String name) throws NotFoundFaultMsg, RuntimeFaultFaultMsg   
	{
		return getVimService().getCustomizationSpec(getMOR(), name);
	}
	
	public void overwriteCustomizationSpec(CustomizationSpecItem item) throws ConcurrentAccessFaultMsg, CustomizationFaultFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg   
	{
		getVimService().overwriteCustomizationSpec(getMOR(), item);
	}
	
	public void renameCustomizationSpec(String name, String newName) throws AlreadyExistsFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg   
	{
		getVimService().renameCustomizationSpec(getMOR(), name, newName);
	}
	
	public CustomizationSpecItem xmlToCustomizationSpecItem(String specItemXml) throws CustomizationFaultFaultMsg, RuntimeFaultFaultMsg   
	{
		return getVimService().xmlToCustomizationSpecItem(getMOR(), specItemXml);
	}
	
}

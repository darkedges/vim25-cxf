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

import com.vmware.vim25.AlreadyExistsFaultMsg;
import com.vmware.vim25.AuthMinimumAdminPermissionFaultMsg;
import com.vmware.vim25.AuthorizationDescription;
import com.vmware.vim25.AuthorizationPrivilege;
import com.vmware.vim25.AuthorizationRole;
import com.vmware.vim25.InvalidNameFaultMsg;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NotFoundFaultMsg;
import com.vmware.vim25.Permission;
import com.vmware.vim25.RemoveFailedFaultMsg;
import com.vmware.vim25.RuntimeFaultFaultMsg;
import com.vmware.vim25.UserNotFoundFaultMsg;

/**
 * The managed object class corresponding to the one defined in VI SDK API
 * reference.
 * 
 * @author Steve JIN (http://www.doublecloud.org)
 */

public class AuthorizationManager extends ManagedObject {

	public AuthorizationManager(ServerConnection sc, ManagedObjectReference mor) {
		super(sc, mor);
	}

	public AuthorizationDescription getDescription() {
		return (AuthorizationDescription) getCurrentProperty("description");

	}

	public AuthorizationPrivilege[] getPrivilegeList() {
		return (AuthorizationPrivilege[]) getCurrentProperty("privilegeList");

	}

	public AuthorizationRole[] getRoleList() {
		return (AuthorizationRole[]) getCurrentProperty("roleList");

	}

	public int addAuthorizationRole(String name, List<String> privIds)
			throws AlreadyExistsFaultMsg, InvalidNameFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().addAuthorizationRole(getMOR(), name, privIds);
	}

	/**
	 * @since SDK5.0
	 */
	public List<Boolean> HasPrivilegeOnEntity(ManagedEntity entity, String sessionId, List<String> privId)
			throws RuntimeFaultFaultMsg {
		return getVimService().hasPrivilegeOnEntity(getMOR(), entity.getMOR(), sessionId, privId);
	}

	public void mergePermissions(int srcRoleId, int dstRoleId)
			throws AuthMinimumAdminPermissionFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().mergePermissions(getMOR(), srcRoleId, dstRoleId);
	}

	public void removeAuthorizationRole(int roleId, boolean failIfUsed)
			throws NotFoundFaultMsg, RemoveFailedFaultMsg, RuntimeFaultFaultMsg {
		getVimService().removeAuthorizationRole(getMOR(), roleId, failIfUsed);
	}

	public void removeEntityPermission(ManagedEntity entity, String user, boolean isGroup)
			throws AuthMinimumAdminPermissionFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		if (entity == null) {
			throw new IllegalArgumentException("entity must not be null.");
		}
		getVimService().removeEntityPermission(getMOR(), entity.getMOR(), user, isGroup);
	}

	public void resetEntityPermissions(ManagedEntity entity, List<Permission> permission)
			throws AuthMinimumAdminPermissionFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg, UserNotFoundFaultMsg {
		if (entity == null) {
			throw new IllegalArgumentException("entity must not be null.");
		}
		getVimService().resetEntityPermissions(getMOR(), entity.getMOR(), permission);
	}

	public List<Permission> retrieveEntityPermissions(ManagedEntity entity, boolean inherited)
			throws RuntimeFaultFaultMsg {
		if (entity == null) {
			throw new IllegalArgumentException("entity must not be null.");
		}
		return getVimService().retrieveEntityPermissions(getMOR(), entity.getMOR(), inherited);
	}

	public List<Permission> retrieveAllPermissions() throws RuntimeFaultFaultMsg {
		return getVimService().retrieveAllPermissions(getMOR());
	}

	public List<Permission> retrieveRolePermissions(int roleId) throws NotFoundFaultMsg, RuntimeFaultFaultMsg {
		return getVimService().retrieveRolePermissions(getMOR(), roleId);
	}

	public void setEntityPermissions(ManagedEntity entity, List<Permission> permission)
			throws AuthMinimumAdminPermissionFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg, UserNotFoundFaultMsg {
		if (entity == null) {
			throw new IllegalArgumentException("entity must not be null.");
		}
		getVimService().setEntityPermissions(getMOR(), entity.getMOR(), permission);
	}

	public void updateAuthorizationRole(int roleId, String newName, List<String> privIds)
			throws AlreadyExistsFaultMsg, InvalidNameFaultMsg, NotFoundFaultMsg, RuntimeFaultFaultMsg {
		getVimService().updateAuthorizationRole(getMOR(), roleId, newName, privIds);
	}
}

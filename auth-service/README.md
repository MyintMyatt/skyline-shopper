# Auth Service

**authentication, authorization, issue token, session management and RBAC management.**

*server is running on port* $\color{blue}{\text{8081}}$

### RBAC control design
#### **sample permissions**
- **all** = *all permission for everything, every feature (create, read, update, delete)*
- **create** = _create permission for everything, every feature (can create for all)_
- **read** = _read permission for everything, every feature (can read for all)_
- **update** = _update permission for everything, every feature (can update for all)_
- **delete** = _delete permission for everything, every feature (can delete for all)_
- **create.user** = _only user create permission (can create user)_
- **read.user** = _only user read permission (can read user)_
- **update.user** = _only user update permission (can update user)_
- **delete.user** = _only user delete permission (can delete user)_

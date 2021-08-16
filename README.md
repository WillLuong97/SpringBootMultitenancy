# SpringBootMultitenancy

Project demo of multi tenancy using Spring boot

1. Understanding the Request Flow

The process to establish a multi-tenant communication usually consists of the following three steps:

	+ Accept the incoming connection, and authenticate the user if necessary.

	+ Intercept the request and identify the tenant for which the user is issuing the request.

	+Establish a connection with the database or schema of the tenant.

Tenant identification is performed against a default schema, which contains the user's information. A user can authenticate himself on an external service and then pass the tenant information using an HTTP header.
To keep things simple, we are not performing any kind of authentication. We will use a custom HTTP header “X-TenantID” for Tenant identification. Let us start with identifying tenants.


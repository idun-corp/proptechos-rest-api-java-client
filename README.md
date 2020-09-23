# ProptechOS Rest API client

ProptechOS Rest API client to be used for calling ProptechOS API main CRUD operations.

## How to start:
To start using SDK library instance of _**ProptechOsClient**_ should be initialized:

``` java
ProptechOsClient.applicationClientBuilder(
         "<BASE_API_URL>",
         AuthenticationConfig.builder(
             "<APP_CLIENT_ID>",
             "<APP_CLIENT_SECRET>",
             "<AZURE_LOGIN_URL>"
         ).build()
     ).build();
```

Properties definitions:

``` properties
BASE_API_URL - base ProptechOS api url "https://test.proptechos.com";
APP_CLIENT_ID - your application id registere in Azure Active Directory;
APP_CLIENT_SECRET - your application secret generated in Azure Active Directory;
AZURE_LOGIN_URL - Azure loging url "https://login.microsoftonline.com/tentant_id/"
```

## Working with ProptechOS Services

_**ProptechOsClient**_ provides access to ServiceFactory class. 
ServiceFactory instantiates all type of services to simplify process of integration with ProptechOS API.

``` java
DeviceService deviceService = client.serviceFactory().deviceService();
``` 

### ProptechOS services provides main operations to work with ProptechOS axioms:

* CRUD operations:

``` java
getById(UUID id)

createDevice(IDevice device)

updateDevice(IDevice device)

deleteDevice(UUID id)

```

* Pagination

``` java

Paged<T> getFirstPage(long pageSize)

Paged<T> getPage(long pageNumber, long pageSize)

Paged<T> getPageFiltered(long pageNumber, long pageSize, IQueryFilter...filters)

Paged<T> getLastPage(long pageSize)

Paged<T> getNextPage(PageMetadata currentPageMetadata)

```

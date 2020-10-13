# ProptechOS Rest API client

ProptechOS Rest API client to be used for calling ProptechOS API main CRUD operations.

## How to start:
To start using SDK library instance of _**ProptechOsClient**_ should be initialized:

``` java
ProptechOsClient.applicationClientBuilder(""<BASE_API_URL>")
    .authConfig(AuthenticationConfig.builder()
            .clientId("<APP_CLIENT_ID>")
            .clientSecret("<APP_CLIENT_SECRET>").build()).build();
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

#### Example of obtaining paged filtered Data:
``` java
Paged<IDevice> pagedDevices 
    = deviceService.getPageFiltered(0, 100, new ClassFilter("Device"));
```

#### Example of query filter implementation:
``` java
public class ClassFilter implements IQueryFilter {

  private String recClass;

  public ClassFilter(String recClass) {
    this.recClass = recClass;
  }

  @Override
  public QueryParam queryParam() {
    return new QueryParam("class", recClass);
  }
}
```

#### Obtaining sensor observations
``` java
sensorService.getLatestObservationBySensorId(UUID.fromString("<SENSOR_ID>"));

sensorService.getObservationsBySensorIdForPeriod(
        UUID.fromString("<SENSOR_ID>"),
        Instant.now().minus(10, ChronoUnit.DAYS), Instant.now())
```
#### Sending actuation request
``` java
actuatorService.sendActuationRequest(
        UUID.fromString("<ACTUATOR_ID>"), "<PAYLOAD_VALUE>");
```

### Access to StreamingApiService:
In order to obtain access to StreamingApiService, KafkaConfig data should be provided

``` java
StreamingApiService streamingApiService = 
    client.serviceFactory().streamingApiService(KafkaConfig.builder()
            .eventHub("<EVENTHUB_NAME>")
            .eventHubNamespace("<EVENTHUB_NAMESPACE>")
            .sharedAccessKey("<SHARED_ACCESS_KEY>")
            .build());
```

Properties definitions:

``` properties
EVENTHUB_NAME - Azure kafka enebled eventhub name;
EVENTHUB_NAMESPACE - Azure eventhub namespace;
EVENTHUB_CONNECTION_STRING - Azure eventhub namespace shared access key to connect to;
```

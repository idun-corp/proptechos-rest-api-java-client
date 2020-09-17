package com.proptechos.http.constants;

public interface HttpStatus {

  int OK = 200;

  int BAD_REQUEST = 400;

  int FORBIDDEN = 403;

  int NOT_FOUND = 404;

  int NOT_ACCEPTABLE = 406;

  int INTERNAL_SERVER_ERROR = 500;

  int BAD_GATEWAY = 502;

  int SERVICE_UNAVAILABLE = 503;

  int GATEWAY_TIMEOUT = 504;
}

package com.http.proxy;

public enum HttpStatus {

	OK(HttpStatusType.SUCCESS, 200),

	CREATED(HttpStatusType.SUCCESS, 201),

	MOVED_TEMPORARILY(HttpStatusType.REDIRECTION, 302),

	BAD_REQUEST(HttpStatusType.CLIENT_ERROR, 400),

	UNAUTHORIZED(HttpStatusType.CLIENT_ERROR, 401),

	FORBIDDEN(HttpStatusType.CLIENT_ERROR, 403),

	NOT_FOUND(HttpStatusType.CLIENT_ERROR, 404),

	NOT_ALLOWED(HttpStatusType.CLIENT_ERROR, 405),

	UNSUPPORTED_MEDIA_TYPE(HttpStatusType.CLIENT_ERROR, 415),

	INTERNAL_SERVER_ERROR(HttpStatusType.SERVER_ERROR, 500),

	BAD_GATEWAY(HttpStatusType.SERVER_ERROR, 502),

	SERVICE_UNAVAILABLE(HttpStatusType.SERVER_ERROR, 503);

	public enum HttpStatusType {

		INFORMATIONAL,
		SUCCESS,
		REDIRECTION,
		CLIENT_ERROR,
		SERVER_ERROR;
	}

	private static final int LOWEST_HTTP_CODE = 100;

	private static final int HIGHEST_HTTP_CODE = 600;

	private int code;

	private HttpStatusType status;

	private HttpStatus(HttpStatusType status, int code) {

		this.code = code;

		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public HttpStatusType getStatusType() {
		return status;
	}

	public String toString() {

		return super.toString() + "(" + status.toString() + ":" + code + ")";
	}

	public static HttpStatus valueOf(int code) {

		HttpStatus key = null;

		if (code > LOWEST_HTTP_CODE && code < HIGHEST_HTTP_CODE) {

			HttpStatus[] array = HttpStatus.values();

			int i = 0;

			while (i < array.length && key == null) {

				if (array[i].getCode() == code) {

					key = array[i];
				} else {
					i++;
				}
			}
		}
		if (key != null) {

			return key;
		} else {
			throw new IllegalArgumentException(String.format("The http status code %s is not supported", code));
		}
	}
}
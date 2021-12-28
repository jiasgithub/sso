package com.allsaints.music.http;

public interface ResultCodeTemplate {
	
	/**
	 * 0000	成功
	 * 1000	参数异常
	 * 3000	权限异常
	 * 4000	请求异常
	 * 5000	其它异常
	 */

	String getCode();

	String getMessage();

	public static final ResultCodeTemplate SUCCESS = new ResultCodeTemplate() {

		@Override
		public String getMessage() {
			return "ok";
		}

		@Override
		public String getCode() {
			return "0000";
		}
	};

	public static final ResultCodeTemplate INVALID_PARAMS = new ResultCodeTemplate() {

		@Override
		public String getMessage() {
			return "invalid params";
		}

		@Override
		public String getCode() {
			return "1001";
		}
	};

	public static final ResultCodeTemplate INVALID_DATE = new ResultCodeTemplate() {

		@Override
		public String getMessage() {
			return "invalid date";
		}

		@Override
		public String getCode() {
			return "1003";
		}
	};
	
	public static final ResultCodeTemplate INVALID_EMAIL = new ResultCodeTemplate() {

		@Override
		public String getMessage() {
			return "invalid email";
		}

		@Override
		public String getCode() {
			return "1004";
		}
	};
	
	public static final ResultCodeTemplate INVALID_PHONE = new ResultCodeTemplate() {

		@Override
		public String getMessage() {
			return "invalid phone";
		}

		@Override
		public String getCode() {
			return "1005";
		}
	};

	public static final ResultCodeTemplate FORBIDDEN = new ResultCodeTemplate() {

		@Override
		public String getMessage() {
			return "forbidden";
		}

		@Override
		public String getCode() {
			return "3000";
		}
	};
	
	public static final ResultCodeTemplate NOT_AUTH = new ResultCodeTemplate() {

		@Override
		public String getMessage() {
			return "not auth";
		}

		@Override
		public String getCode() {
			return "3001";
		}
	};
	
	public static final ResultCodeTemplate OBJECT_NOT_FOUND = new ResultCodeTemplate() {

		@Override
		public String getMessage() {
			return "object not found";
		}

		@Override
		public String getCode() {
			return "3002";
		}
	};
	
	public static final ResultCodeTemplate NOT_USER_AUTH = new ResultCodeTemplate() {

		@Override
		public String getMessage() {
			return "not user auth";
		}

		@Override
		public String getCode() {
			return "3003";
		}
	};
	
	public static final ResultCodeTemplate DUPLICATE_USER = new ResultCodeTemplate() {

		@Override
		public String getMessage() {
			return "duplicate user";
		}

		@Override
		public String getCode() {
			return "3004";
		}
	};

	public static final ResultCodeTemplate INVALID_REQUEST = new ResultCodeTemplate() {

		@Override
		public String getMessage() {
			return "invalid request";
		}

		@Override
		public String getCode() {
			return "4000";
		}
	};
	
	public static final ResultCodeTemplate OVER_LIMIT = new ResultCodeTemplate() {

		@Override
		public String getMessage() {
			return "over limit";
		}

		@Override
		public String getCode() {
			return "4001";
		}
	};

	public static final ResultCodeTemplate DUPLICATE_REQUEST = new ResultCodeTemplate() {
		
		@Override
		public String getMessage() {
			return "duplicate request";
		}
		
		@Override
		public String getCode() {
			return "4002";
		}
	};
	
	public static final ResultCodeTemplate FREQUENT_REQUEST = new ResultCodeTemplate() {
		
		@Override
		public String getMessage() {
			return "frequent request";
		}
		
		@Override
		public String getCode() {
			return "4003";
		}
	};
	public static final ResultCodeTemplate BAD_REQUEST = new ResultCodeTemplate() {
		
		@Override
		public String getMessage() {
			return "bad request";
		}
		
		@Override
		public String getCode() {
			return "4004";
		}
	};
	
	public static final ResultCodeTemplate ERROR = new ResultCodeTemplate() {

		@Override
		public String getMessage() {
			return "error";
		}

		@Override
		public String getCode() {
			return "5000";
		}
	};
	
	public static final ResultCodeTemplate UPLOAD_FAILED = new ResultCodeTemplate() {
		
		@Override
		public String getMessage() {
			return "upload failed";
		}
		
		@Override
		public String getCode() {
			return "5001";
		}
	};
	
	public static final ResultCodeTemplate DOWNLOAD_FAILED = new ResultCodeTemplate() {
		
		@Override
		public String getMessage() {
			return "download failed";
		}
		
		@Override
		public String getCode() {
			return "5002";
		}
	};
}

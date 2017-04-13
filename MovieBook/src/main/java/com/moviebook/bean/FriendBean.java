package com.moviebook.bean;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class FriendBean extends UserBean {

	public enum InviteStatus {
		@SerializedName("sent")
		SENT(0), @SerializedName("accepted")
		ACCEPTED(1), @SerializedName("rejected")
		REJECTED(-1);

		private final int code;
		private static Map<Integer, InviteStatus> map = new HashMap<>();

		static {
			for (InviteStatus i : InviteStatus.values()) {
				map.put(i.code(), i);
			}
		}

		private InviteStatus(int code) {
			this.code = code;
		}

		public int code() {
			return code;
		}

		public static InviteStatus valueOf(int status) {
			return map.get(status);
		}

	}

	private InviteStatus status;

	public FriendBean() {
		super();
	}

	public InviteStatus getStatus() {
		return status;
	}

	public void setStatus(InviteStatus status) {
		this.status = status;
	}

}

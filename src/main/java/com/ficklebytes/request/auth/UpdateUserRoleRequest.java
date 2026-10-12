package com.ficklebytes.request.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ficklebytes.enums.Role;
import com.ficklebytes.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRoleRequest {
	
		@JsonProperty(value = "userId", required = true)
		private int userId;
	
		@JsonProperty(value = "username", required = true)
		private String username;
	
		@JsonProperty(value = "role", required = true)
		private Role role;

		@Override
		public String toString() {
			return Utils.toString(this);
		}
}

package com.feup.client.data.local.datastore

import kotlinx.serialization.Serializable

@Serializable
data class SerializableUsersState(
    val users: List<SerializableUser> = emptyList(),
    val loggedInUserNickname: String? = null,
)
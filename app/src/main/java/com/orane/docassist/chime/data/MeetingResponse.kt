/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.orane.docassist.chime.data

import com.amazonaws.services.chime.sdk.meetings.session.Attendee
import com.amazonaws.services.chime.sdk.meetings.session.Meeting
import com.google.gson.annotations.SerializedName

data class MeetingResponse(
    @SerializedName("JoinInfo") val joinInfo: MeetingInfo
)

data class MeetingInfo(
    @SerializedName("Title") val title: String,
    @SerializedName("Meeting") val meeting: Meeting,
    @SerializedName("Attendee") val attendee: Attendee
)

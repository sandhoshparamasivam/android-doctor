/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.orane.docassist.chime.data

import com.amazonaws.services.chime.sdk.meetings.audiovideo.SignalStrength
import com.amazonaws.services.chime.sdk.meetings.audiovideo.VolumeLevel

data class RosterAttendee(
    val attendeeId: String,
    val attendeeName: String,
    val volumeLevel: VolumeLevel = VolumeLevel.NotSpeaking,
    val signalStrength: SignalStrength = SignalStrength.High
)

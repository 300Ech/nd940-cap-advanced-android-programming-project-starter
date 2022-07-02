package com.example.android.politicalpreparedness.network.models

data class VoterInfoObject(
    var electionName: String,
    val date: String,
    val votingUrl: String,
    val ballotUrl: String
)
package com.example.android.politicalpreparedness.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VoterInfoResponse (
    val election: Election,
    val pollingLocations: String? = null, //TODO: Future Use
    val contests: String? = null, //TODO: Future Use
    val state: List<State>? = null,
    val electionElectionOfficials: List<ElectionOfficial>? = null
)

fun VoterInfoResponse.toVoterInfoObject() : VoterInfoObject {
    return VoterInfoObject(
        electionName = election.name,
        date = election.electionDay.toString(),
        votingUrl = state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl ?: "",
        ballotUrl = state?.get(0)?.electionAdministrationBody?.ballotInfoUrl ?: ""
    )
}
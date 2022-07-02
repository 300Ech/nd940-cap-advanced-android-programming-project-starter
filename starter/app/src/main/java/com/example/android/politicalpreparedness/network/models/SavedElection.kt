package com.example.android.politicalpreparedness.network.models

import androidx.room.*
import com.squareup.moshi.*
import java.util.*

@Entity(tableName = "saved_election_table")
data class SavedElection(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "electionDay")val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division
)

fun SavedElection.toElectionOject() : Election {
    return Election(
            id = id,
            name = name,
            electionDay = electionDay,
            division = division
    )
}